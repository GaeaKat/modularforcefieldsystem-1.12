package com.nekokittygames.mffs.common.item;

import com.nekokittygames.mffs.libs.LibItemNames;
import com.nekokittygames.mffs.libs.LibMisc;
import com.nekokittygames.mffs.api.IForceEnergyItems;
import com.nekokittygames.mffs.api.IPowerLinkItem;
import com.nekokittygames.mffs.common.tileentity.TileEntityMachines;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCardPower extends ItemMFFSBase implements IPowerLinkItem,
		IForceEnergyItems {

	public ItemCardPower() {
		super(LibItemNames.POWER_CARD);
		setMaxStackSize(1);

	}


	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public int getPercentageCapacity(ItemStack itemStack,
			TileEntityMachines tem, World world) {
		return 100;
	}

	@Override
	public int getAvailablePower(ItemStack itemStack, TileEntityMachines tem,
			World world) {
		return 10000000;
	}

	@Override
	public int getMaximumPower(ItemStack itemStack, TileEntityMachines tem,
			World world) {
		return 10000000;
	}

	@Override
	public boolean consumePower(ItemStack itemStack, int powerAmount,
			boolean simulation, TileEntityMachines tem, World world) {
		return true;
	}

	@Override
	public boolean insertPower(ItemStack itemStack, int powerAmount,
			boolean simulation, TileEntityMachines tem, World world) {
		return false;
	}

	@Override
	public int getPowersourceID(ItemStack itemStack, TileEntityMachines tem,
			World world) {
		return 0;
	}

	@Override
	public int getfreeStorageAmount(ItemStack itemStack,
			TileEntityMachines tem, World world) {
		return 0;
	}

	@Override
	public boolean isPowersourceItem() {
		return true;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> info, ITooltipFlag flagIn) {

		info.add(I18n.format("itemInfo.infinitePower.lineOne"));
		info.add(I18n.format("itemInfo.infinitePower.lineTwo"));
	}

	// ForceEnergyItems -> PowerLinkItem compatibility

	@Override
	public int getAvailablePower(ItemStack itemStack) {
		return getAvailablePower(itemStack, null, null);
	}

	@Override
	public int getMaximumPower(ItemStack itemStack) {
		return getMaximumPower(itemStack, null, null);
	}

	@Override
	public boolean consumePower(ItemStack itemStack, int powerAmount,
			boolean simulation) {
		return true;
	}

	@Override
	public void setAvailablePower(ItemStack itemStack, int amount) {
	}

	@Override
	public int getPowerTransferrate() {
		return 1000000;
	}

	@Override
	public int getItemDamage(ItemStack stackInSlot) {
		return 0;
	}

}
