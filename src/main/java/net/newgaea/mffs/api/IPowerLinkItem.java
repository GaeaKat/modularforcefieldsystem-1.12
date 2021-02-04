package net.newgaea.mffs.api;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.newgaea.mffs.common.tiles.TileNetwork;

public interface IPowerLinkItem {


        int getPercentageCapacity(ItemStack stack,TileNetwork machine,World world);

        int getAvailablePower(ItemStack stack,TileNetwork machine,World world);
        int getMaximumPower(ItemStack stack,TileNetwork machine,World world);
        boolean consumePower(ItemStack stack, int powerAmount,boolean simulation, TileNetwork machine,World world);
        boolean insertPower(ItemStack stack, int powerAmount,boolean simulation, TileNetwork machine,World world);
        int getPowersourceID(ItemStack stack, TileNetwork machine, World world);

        int getFreeStorageAmount(ItemStack stack,TileNetwork machine,World world);
        boolean isPowerSourceItem();
}
