package net.newgaea.mffs.common.items.linkcards;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.newgaea.mffs.api.IPowerLinkItem;
import net.newgaea.mffs.common.items.ModItem;
import net.newgaea.mffs.common.tiles.TileNetwork;

public class ItemCreativePowerCard extends ModItem implements IPowerLinkItem {
    public ItemCreativePowerCard(Properties properties) {
        super(properties);
    }

    @Override
    public int getPercentageCapacity(ItemStack stack, TileNetwork machine, World world) {
        return 100;
    }

    @Override
    public int getAvailablePower(ItemStack stack, TileNetwork machine, World world) {
        return 10000000;
    }

    @Override
    public int getMaximumPower(ItemStack stack, TileNetwork machine, World world) {
        return 10000000;
    }

    @Override
    public boolean consumePower(ItemStack stack, int powerAmount, boolean simulation, TileNetwork machine, World world) {
        return true;
    }

    @Override
    public boolean insertPower(ItemStack stack, int powerAmount, boolean simulation, TileNetwork machine, World world) {
        return false;
    }

    @Override
    public int getPowersourceID(ItemStack stack, TileNetwork machine, World world) {
        return 0;
    }

    @Override
    public int getFreeStorageAmount(ItemStack stack, TileNetwork machine, World world) {
        return 0;
    }

    @Override
    public boolean isPowerSourceItem() {
        return true;
    }


}
