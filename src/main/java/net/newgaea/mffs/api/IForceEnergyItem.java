package net.newgaea.mffs.api;

import net.minecraft.item.ItemStack;

public interface IForceEnergyItem {
    public int getAvailablePower(ItemStack itemStack);

    public int getMaximumPower(ItemStack itemStack);

    public boolean consumePower(ItemStack itemStack, int powerAmount,
                                boolean simulation);

    public void setAvailablePower(ItemStack itemStack, int amount);

    public int getPowerTransferRate();

    public int getItemDamage(ItemStack stackInSlot);
}
