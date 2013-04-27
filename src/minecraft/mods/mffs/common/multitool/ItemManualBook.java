package mods.mffs.common.multitool;

import mods.mffs.common.ModularForceFieldSystem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemManualBook extends ItemMultitool {

	public ItemManualBook(int par1) {
		super(par1, 5);

	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ) {
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world,
			EntityPlayer entityplayer) {
		if (entityplayer.isSneaking()) {
			return super.onItemRightClick(itemstack, world, entityplayer);
		}

		if (world.isRemote)
			entityplayer.openGui(ModularForceFieldSystem.instance, 1, world, 0,
					0, 0);

		return itemstack;
	}

}
