package net.newgaea.mffs.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.newgaea.mffs.api.MFFSTags;
import net.newgaea.mffs.common.init.MFFSBlocks;
import net.newgaea.mffs.common.init.MFFSContainer;
import net.newgaea.mffs.common.init.MFFSItems;
import net.newgaea.mffs.common.storage.MFFSEnergyStorage;
import net.newgaea.mffs.common.tiles.TileGenerator;


public class GeneratorContainer extends Container {
    private PlayerEntity playerEntity;
    private IItemHandler playerInventory;
    private final IIntArray generatorData;


    public GeneratorContainer(ContainerType<?> type, int windowId, PlayerEntity player, IItemHandler burn, IItemHandler fuel, IIntArray array) {
        super(type, windowId);
        this.playerEntity=player;
        this.playerInventory=new InvWrapper(player.inventory);
        generatorData=array;
        addSlot(new SlotItemHandler(burn, 0, 56, 17));
        addSlot(new SlotItemHandler(fuel, 0, 56, 53));
        layoutPlayerInventorySlots(8, 84);
        this.trackIntArray(generatorData);

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

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }


    public int getEnergy()
    {
        return generatorData.get(4);
    }
    public int getMaxEnergy()
    {
        return generatorData.get(5);
    }

    //case 0:
//        return AbstractFurnaceTileEntity.this.burnTime; === getBurnTime()
//         case 1:
//                 return AbstractFurnaceTileEntity.this.recipesUsed; == 200;
//         case 2:
//                 return AbstractFurnaceTileEntity.this.cookTime; ======= getCounter()
//         case 3:
//                 return AbstractFurnaceTileEntity.this.cookTimeTotal;  ======= getBurnTIme
    public int getBurnTime()
    {
        return generatorData.get(0);
    }

    public int getCookTime()
    {
        return generatorData.get(2);
    }

    public int getCookTimeTotal()
    {
        return generatorData.get(3);
    }
    public int getRecipesUsed()
    {
        return generatorData.get(1);
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
}
