package net.newgaea.mffs.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.newgaea.mffs.api.INotifiableContainer;
import net.newgaea.mffs.api.IProjectorModule;
import net.newgaea.mffs.common.inventory.slots.SlotItemHandlerToggle;
import net.newgaea.mffs.common.inventory.slots.SlotitemHandlerNotify;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectorContainer extends Container implements INotifiableContainer {
    private PlayerEntity playerEntity;
    private IItemHandler playerInventory;
    public Slot moduleSlot;
    public Map<Direction, SlotItemHandlerToggle> fociSlots=new HashMap<>();

    public ProjectorContainer(ContainerType<?> type, int id, PlayerEntity player,IItemHandler module,IItemHandler foci) {
        super(type, id);
        this.playerEntity=player;
        this.playerInventory=new InvWrapper(player.inventory);
        moduleSlot=new SlotitemHandlerNotify(module,this,0,11,38);
        addSlot(moduleSlot);
        fociSlots.put(Direction.NORTH,new SlotItemHandlerToggle(foci,0,137,28));
        addSlot(fociSlots.get(Direction.NORTH));
        fociSlots.put(Direction.EAST,new SlotItemHandlerToggle(foci,1,154,45));
        addSlot(fociSlots.get(Direction.EAST));
        fociSlots.put(Direction.SOUTH,new SlotItemHandlerToggle(foci,2,137,62));
        addSlot(fociSlots.get(Direction.SOUTH));
        fociSlots.put(Direction.WEST,new SlotItemHandlerToggle(foci,3,120,45));
        addSlot(fociSlots.get(Direction.WEST));

        layoutPlayerInventorySlots(8,104);
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
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public void detectAndSendChanges() {
        if(moduleSlot.getHasStack())
        {
            IProjectorModule module= (IProjectorModule) moduleSlot.getStack().getItem();
            if(module.enabledFoci())
                enableFociSlots();
            else
                disableFociSlots();
        }
        else
        {
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

    @Override
    public void stackChanged(ItemStack stack) {
        if(stack!=ItemStack.EMPTY)
        {
            IProjectorModule module= (IProjectorModule) stack.getItem();
            if(module.enabledFoci())
                enableFociSlots();
            else
                disableFociSlots();
        }
        else
        {
            disableFociSlots();
        }
    }
}
