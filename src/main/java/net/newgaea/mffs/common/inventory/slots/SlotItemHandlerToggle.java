package net.newgaea.mffs.common.inventory.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotItemHandlerToggle extends SlotItemHandler {
    private boolean enabled;
    public SlotItemHandlerToggle(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        enabled=true;
    }


    @Override
    public boolean isItemValid(ItemStack stack) {
        return enabled && super.isItemValid(stack);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled=enabled;
    }
}
