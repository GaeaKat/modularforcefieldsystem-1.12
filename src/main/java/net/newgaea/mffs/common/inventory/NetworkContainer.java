package net.newgaea.mffs.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.newgaea.mffs.common.misc.ModeEnum;
import net.newgaea.mffs.common.tiles.TileCapacitor;
import net.newgaea.mffs.common.tiles.TileNetwork;
import net.newgaea.mffs.transport.network.property.IPropertyManaged;
import net.newgaea.mffs.transport.network.property.Property;
import net.newgaea.mffs.transport.network.property.PropertyManager;
import net.newgaea.mffs.transport.network.property.PropertyTypes;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

public abstract class NetworkContainer extends Container implements IPropertyManaged {

    protected final PropertyManager propertyManager;
    protected final Property<ModeEnum> switchMode;
    protected final Property<BlockPos> blockPos;
    protected final Property<String> deviceName;

    public NetworkContainer(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory) {
        super(type, id);
        this.propertyManager = new PropertyManager((short) windowId);
        this.switchMode=this.propertyManager.addTrackedProperty(PropertyTypes.MODE_ENUM.create());
        this.blockPos=this.propertyManager.addTrackedProperty(PropertyTypes.BLOCK_POS.create());
        this.deviceName=this.propertyManager.addTrackedProperty(PropertyTypes.STRING.create());
    }
    public NetworkContainer(@Nullable ContainerType<?> type,int id, PlayerEntity player, TileNetwork networkMachine) {
        super(type, id);
        this.propertyManager = new PropertyManager((short) windowId);
        this.switchMode=this.propertyManager.addTrackedProperty(PropertyTypes.MODE_ENUM.create(networkMachine::getMode));
        this.blockPos=this.propertyManager.addTrackedProperty(PropertyTypes.BLOCK_POS.create(networkMachine::getPos));
        this.deviceName=this.propertyManager.addTrackedProperty(PropertyTypes.STRING.create(networkMachine::getDeviceName));
    }


    public ModeEnum getMode() { return switchMode.getOrElse(ModeEnum.Off);}
    public BlockPos getPos() { return blockPos.getOrElse(new BlockPos(0,0,0));}
    public String getDeviceName() {
        return deviceName.getOrElse("");
    }

    protected int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    protected int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
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

}
