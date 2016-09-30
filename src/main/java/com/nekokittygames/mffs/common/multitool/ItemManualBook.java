package com.nekokittygames.mffs.common.multitool;

import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.libs.LibItemNames;
import com.nekokittygames.mffs.libs.LibMisc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemManualBook extends ItemMultitool {

	public ItemManualBook() {
		super( 5);
		setUnlocalizedName(LibMisc.UNLOCALIZED_PREFIX+ LibItemNames.MULTITOOL_MANUAL);
		setRegistryName(LibItemNames.MULTITOOL_MANUAL);

	}


	@Override
	public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		return EnumActionResult.PASS;
	}


	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (playerIn.isSneaking()) {
			return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
		}

		if (worldIn.isRemote)
			playerIn.openGui(ModularForceFieldSystem.instance, 1, worldIn, 0,
					0, 0);

		return ActionResult.newResult(EnumActionResult.SUCCESS,itemStackIn);
	}


}
