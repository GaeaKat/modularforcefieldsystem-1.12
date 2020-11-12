package net.newgaea.mffs.api;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IPowerLinkItem {

        EnumPowerLink getLinkType(ItemStack stack, World world, Entity entity);
}
