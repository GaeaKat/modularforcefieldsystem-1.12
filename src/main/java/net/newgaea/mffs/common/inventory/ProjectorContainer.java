package net.newgaea.mffs.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.newgaea.mffs.api.INotifiableContainer;
import net.newgaea.mffs.api.IProjectorModule;
import net.newgaea.mffs.common.init.MFFSContainer;
import net.newgaea.mffs.common.inventory.slots.SlotItemHandlerToggle;
import net.newgaea.mffs.common.inventory.slots.SlotitemHandlerNotify;
import net.newgaea.mffs.common.tiles.TileFENetwork;
import net.newgaea.mffs.common.tiles.TileProjector;
import net.newgaea.mffs.transport.network.property.PropertyManager;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectorContainer extends NetworkContainer implements INotifiableContainer {
    private PlayerEntity playerEntity;
    private IItemHandler playerInventory;
    public Slot moduleSlot;
    public Map<Direction, SlotItemHandlerToggle> fociSlots = new HashMap<>();

    public List<SlotItemHandlerToggle> optionSlots = new ArrayList<>();
    public SlotItemHandlerToggle distanceSlot;
    public SlotItemHandlerToggle strengthSlot;

    public ProjectorContainer(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory) {
        super(type, id, playerInventory);
        addSlots(TileProjector.createModuleInv(null),TileProjector.createFocusInv(null),TileProjector.createDistanceInv(null),TileProjector.createStrengthInv(null),TileProjector.createOptionsInv(null),TileFENetwork.createLinkHandler(null),playerInventory);
    }

    public ProjectorContainer(int id, PlayerEntity player, TileProjector networkMachine) {
        super(MFFSContainer.PROJECTOR.get(), id, player, networkMachine);

        addSlots(networkMachine.getModule(),networkMachine.getFoci(),networkMachine.getDistanceModifiers(), networkMachine .getStrengthModifiers(),networkMachine.getOptions(),networkMachine.getLink(), player.inventory);
    }

    private void addSlots(IItemHandler module, IItemHandler foci, IItemHandler distance, IItemHandler strength,IItemHandler options,IItemHandler link,PlayerInventory inventory) {
        moduleSlot = new SlotitemHandlerNotify(module, this, 0, 11, 38);
        addSlot(moduleSlot);
        fociSlots.put(Direction.NORTH, new SlotItemHandlerToggle(foci, 0, 137, 28));
        addSlot(fociSlots.get(Direction.NORTH));
        fociSlots.put(Direction.EAST, new SlotItemHandlerToggle(foci, 1, 154, 45));
        addSlot(fociSlots.get(Direction.EAST));
        fociSlots.put(Direction.SOUTH, new SlotItemHandlerToggle(foci, 2, 137, 62));
        addSlot(fociSlots.get(Direction.SOUTH));
        fociSlots.put(Direction.WEST, new SlotItemHandlerToggle(foci, 3, 120, 45));
        addSlot(fociSlots.get(Direction.WEST));
        distanceSlot = new SlotitemHandlerNotify(distance, this, 0, 119, 63);
        addSlot(distanceSlot);
        strengthSlot = new SlotitemHandlerNotify(strength, this, 0, 155, 63);
        addSlot(strengthSlot);

        for(int i=0;i<3;i++) {
            optionSlots.add(i,new SlotItemHandlerToggle(options,i,120+i*18,82));
            addSlot(optionSlots.get(i));
        }
        layoutPlayerInventorySlots(8, 104,new InvWrapper(inventory));
    }




    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public void detectAndSendChanges() {
        if (moduleSlot.getHasStack()) {
            IProjectorModule module = (IProjectorModule) moduleSlot.getStack().getItem();
            if (module.enabledFoci())
                enableFociSlots();
            else
                disableFociSlots();
        } else {
            disableFociSlots();
        }
        super.detectAndSendChanges();
    }

    @Override
    public void setAll(List<ItemStack> p_190896_1_) {
        super.setAll(p_190896_1_);

    }

    private void enableFociSlots() {
        fociSlots.forEach(((direction, slot) -> slot.setEnabled(true)));
    }

    private void disableFociSlots() {
        fociSlots.forEach(((direction, slot) -> slot.setEnabled(false)));
    }

    private void disableAllSlots() {
        fociSlots.forEach(((direction, slot) -> slot.setEnabled(false)));
        strengthSlot.setEnabled(false);
        distanceSlot.setEnabled(false);
    }

    @Override
    public void stackChanged(ItemStack stack) {
        if (stack.getItem() instanceof IProjectorModule) {
            if (stack != ItemStack.EMPTY) {

                IProjectorModule module = (IProjectorModule) stack.getItem();
                if (module.enabledFoci())
                    enableFociSlots();
                else
                    disableFociSlots();
                distanceSlot.setEnabled(module.enabledDistance());
                strengthSlot.setEnabled(module.enabledStrength());
            } else {
                disableAllSlots();
            }

        }
    }

    @Override
    public PropertyManager getPropertyManager() {
        return propertyManager;
    }
}
