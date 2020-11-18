package net.newgaea.mffs.common.inventory.slots;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.newgaea.mffs.api.INotifiableContainer;

public class SlotitemHandlerNotify extends SlotItemHandlerToggle{
    INotifiableContainer container;
    public SlotitemHandlerNotify(IItemHandler itemHandler, INotifiableContainer container,int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.container=container;
    }

    @Override
    public void onSlotChange(ItemStack oldStackIn, ItemStack newStackIn) {
        super.onSlotChange(oldStackIn, newStackIn);
        container.stackChanged(newStackIn);
    }

    @Override
    public void onSlotChanged() {
        super.onSlotChanged();
        container.stackChanged(getStack());
    }
}
