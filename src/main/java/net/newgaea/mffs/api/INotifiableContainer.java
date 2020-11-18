package net.newgaea.mffs.api;

import net.minecraft.item.ItemStack;

public interface INotifiableContainer {

    void stackChanged(ItemStack stack);
}
