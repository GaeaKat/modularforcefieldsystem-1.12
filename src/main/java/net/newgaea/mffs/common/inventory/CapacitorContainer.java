package net.newgaea.mffs.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.newgaea.mffs.common.init.MFFSContainer;
import net.newgaea.mffs.common.misc.ModeEnum;
import net.newgaea.mffs.common.tiles.TileCapacitor;
import net.newgaea.mffs.transport.network.property.IPropertyManaged;
import net.newgaea.mffs.transport.network.property.Property;
import net.newgaea.mffs.transport.network.property.PropertyManager;
import net.newgaea.mffs.transport.network.property.PropertyTypes;

import javax.annotation.Nullable;
import java.util.Collections;

public class CapacitorContainer extends NetworkContainer implements IPropertyManaged {



    private final Property<Integer> availablePower;
    private final Property<Double> transmitRange;
    private final Property<Integer> linkedDevices;
    private final Property<Integer> percentageStorageCapacity;
    private final Property<Integer> powerLinkMode;



    public int getAvailablePower() {
        return availablePower.getOrElse(1);
    }

    public Double getTransmitRange() {
        return transmitRange.getOrElse(0D);
    }
    public int getLinkedDevices() {
        return linkedDevices.getOrElse(0);
    }
    public int getPercentageStorageCapacity() {
        return percentageStorageCapacity.getOrElse(0);
    }

    public int getPowerLinkMode() {
        return powerLinkMode.getOrElse(0);
    }

    public CapacitorContainer(@Nullable ContainerType<?> type, int windowId, PlayerInventory playerInventory) {
        super(type, windowId,playerInventory);


        this.availablePower=this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER.create());
        this.transmitRange=this.propertyManager.addTrackedProperty(PropertyTypes.DOUBLE.create());
        this.linkedDevices=this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER.create());
        this.percentageStorageCapacity=this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER.create());
        this.powerLinkMode=this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create());


        addSlots(TileCapacitor.createUpgrades(null),TileCapacitor.createPowerItem(null),playerInventory);
    }
    public CapacitorContainer(int id,  PlayerEntity player,TileCapacitor capacitor) {
        super(MFFSContainer.CAPACITOR.get(), id,player,capacitor);

        this.availablePower=this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER.create(capacitor::getStorageAvailablePower));
        this.transmitRange=this.propertyManager.addTrackedProperty(PropertyTypes.DOUBLE.create(() -> (double) capacitor.getTransmitRange()));
        this.linkedDevices=this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER.create(capacitor::getLinkedDevices));
        this.percentageStorageCapacity=this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER.create(capacitor::getPercentageStorageCapacity));
        this.powerLinkMode=this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create(capacitor::getPowerlinkMode));
        addSlots(capacitor.getUpgrades(),capacitor.getPowerItem(),player.inventory);

    }

    private void addSlots(IItemHandler upgrades, IItemHandler powerItem, PlayerInventory inventory) {
        addSlot(new SlotItemHandler(upgrades,0,154,47));
        addSlot(new SlotItemHandler(upgrades,1,154,67));
        addSlot(new SlotItemHandler(powerItem, 0,87,76));
        layoutPlayerInventorySlots(8,125,new InvWrapper(inventory));
    }



    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        //TODO: ENABLE SHIFT CLICK
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

        }
        return itemstack;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }



    @Override
    public PropertyManager getPropertyManager() {
        return propertyManager;
    }


}
