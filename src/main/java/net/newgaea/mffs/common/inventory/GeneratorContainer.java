package net.newgaea.mffs.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.newgaea.mffs.api.MFFSTags;
import net.newgaea.mffs.common.init.MFFSContainer;
import net.newgaea.mffs.common.tiles.TileGenerator;
import net.newgaea.mffs.transport.network.property.IPropertyManaged;
import net.newgaea.mffs.transport.network.property.Property;
import net.newgaea.mffs.transport.network.property.PropertyManager;
import net.newgaea.mffs.transport.network.property.PropertyTypes;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;


public class GeneratorContainer extends Container implements IPropertyManaged {
    protected final PropertyManager propertyManager;
    private PlayerEntity playerEntity;
    private IItemHandler playerInventory;
    private final Property<Integer> generatorEnergy;
    private final Property<Integer> generatorMaxEnergy;
    private final Property<Integer> burnTime;
    private final Property<Integer> cookTime;
    private final Property<Integer> cookTimeTotal;
    private final Property<Integer> recipesUsed;


    public GeneratorContainer(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory) {
        super(type, id);
        this.propertyManager = new PropertyManager((short) windowId);
        generatorEnergy = this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create());
        generatorMaxEnergy = this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create());
        burnTime = this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create());
        cookTime = this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create());
        cookTimeTotal = this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create());
        recipesUsed = this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create());

        addSlots(TileGenerator.createMonazitHandler(null),TileGenerator.createFuelHandler(null),playerInventory);


    }
    public GeneratorContainer(int windowId, PlayerEntity player, TileGenerator tileGenerator) {
        super(MFFSContainer.GENERATOR.get(), windowId);
        this.propertyManager = new PropertyManager((short) windowId);
        generatorEnergy = this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create(tileGenerator::getEnergy));
        generatorMaxEnergy = this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create(tileGenerator::getTotalEnergy));
        burnTime = this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create(tileGenerator::getBurnTime));
        cookTime = this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create(tileGenerator::getCookTime));
        cookTimeTotal = this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create(tileGenerator::getCookTimeTotal));
        recipesUsed = this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create(tileGenerator::getRecipesUsed));
        this.playerEntity=player;
        this.playerInventory=new InvWrapper(player.inventory);

        addSlots(tileGenerator.getMonazit(),tileGenerator.getFuelItem(),player.inventory);

    }

    private void addSlots(IItemHandler monazit, IItemHandler fuelItem, PlayerInventory inventory) {
        addSlot(new SlotItemHandler(monazit, 0, 56, 17));
        addSlot(new SlotItemHandler(fuelItem, 0, 56, 53));
        layoutPlayerInventorySlots(8, 84,new InvWrapper(inventory));
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    protected void layoutPlayerInventorySlots(int leftCol, int topRow, IItemHandler inventory) {
        // Player inventory
        addSlotBox(inventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(inventory, 0, leftCol, topRow, 9, 18);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        this.propertyManager.sendChanges(this.listeners, false);

    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        this.propertyManager.sendChanges(Collections.singletonList(listener), true);
    }

    public int getEnergy()
    {

        return generatorEnergy.getOrElse(0);
    }
    public int getMaxEnergy()
    {
        return generatorMaxEnergy.getOrElse(0);
    }

    public int getBurnTime()
    {
        return burnTime.getOrElse(0);
    }

    public int getCookTime()
    {
        return cookTime.getOrElse(0);
    }

    public int getCookTimeTotal()
    {
        return cookTimeTotal.getOrElse(0);
    }
    public int getRecipesUsed()
    {
        return recipesUsed.getOrElse(0);
    }

    @OnlyIn(Dist.CLIENT)
    public int getCookProgressionScaled() {
        int i = this.getCookTime();
        int j = this.getCookTimeTotal();
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public int getBurnLeftScaled() {
        int i = this.getRecipesUsed();
        if (i == 0) {
            i = 200;
        }

        return this.getBurnTime() * 13 / i;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean func_217061_l() {
        return this.getBurnTime() > 0;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index != 1 && index != 0) {
                if(MFFSTags.CRYSTAL_MONAZIT.contains(itemstack1.getItem())) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isFuel(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 3 && index < 30) {
                    if (!this.mergeItemStack(itemstack1, 30, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 3, 38, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    protected boolean isFuel(ItemStack p_217058_1_) {
        return AbstractFurnaceTileEntity.isFuel(p_217058_1_);
    }

    @Override
    public PropertyManager getPropertyManager() {
        return propertyManager;
    }
}
