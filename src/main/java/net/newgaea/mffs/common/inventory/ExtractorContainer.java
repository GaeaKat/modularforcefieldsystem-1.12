package net.newgaea.mffs.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.newgaea.mffs.common.init.MFFSContainer;
import net.newgaea.mffs.common.tiles.TileExtractor;
import net.newgaea.mffs.common.tiles.TileFENetwork;
import net.newgaea.mffs.common.tiles.TileNetwork;
import net.newgaea.mffs.transport.network.property.Property;
import net.newgaea.mffs.transport.network.property.PropertyManager;
import net.newgaea.mffs.transport.network.property.PropertyTypes;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;

public class ExtractorContainer  extends NetworkContainer{

    private final Property<Integer> workDone;
    private final Property<Integer> workCycle;
    private final Property<Integer> forceEnergyBuffer;
    private final Property<Integer> maxForceEnergyBuffer;

    public int getWorkDone() {
        return workDone.getOrElse(0);
    }
    public int getWorkCycle() {
        return workCycle.getOrElse(0);
    }
    public int getForceEnergyBuffer() {
        return forceEnergyBuffer.getOrElse(0);
    }
    public int getMaxForceEnergyBuffer() {
        return maxForceEnergyBuffer.getOrElse(1);
    }
    public ExtractorContainer(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory) {
        super(type, id, playerInventory);
        workDone=this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create());
        workCycle=this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create());
        forceEnergyBuffer=this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create());
        maxForceEnergyBuffer=this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create());
        addSlots(TileExtractor.createUpgrades(null), TileExtractor.createMonazitCell(null),TileExtractor.createMonazit(null), TileFENetwork.createLinkHandler(null),playerInventory);
    }

    public ExtractorContainer(int id, PlayerEntity player, TileExtractor networkMachine) {
        super(MFFSContainer.EXTRACTOR.get(), id, player, networkMachine);
        workDone=this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create(networkMachine::getWorkDone));
        workCycle=this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create(networkMachine::getWorkCycle));
        forceEnergyBuffer=this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create(networkMachine::getForceEnergyBuffer));
        maxForceEnergyBuffer=this.getPropertyManager().addTrackedProperty(PropertyTypes.INTEGER.create(networkMachine::getMaxForceEnergyBuffer));
        addSlots(networkMachine.getUpgrades(),networkMachine.getMonazitCell(),networkMachine.getMonazit(), networkMachine.getLink(), player.inventory);
    }

    private void addSlots(IItemHandler upgrades,IItemHandler monazitCell,IItemHandler monazit,IItemHandler powerLink,PlayerInventory inventory) {
        addSlot(new SlotItemHandler(upgrades,0,20,66));
        addSlot(new SlotItemHandler(upgrades,1,39,66));
        addSlot(new SlotItemHandler(monazitCell,0,112,26));
        addSlot(new SlotItemHandler(monazit,0,82,26));
        addSlot(new SlotItemHandler(powerLink,0,145,40));
        layoutPlayerInventorySlots(8,104,new InvWrapper(inventory));
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
