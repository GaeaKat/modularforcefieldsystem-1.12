/*  
    Copyright (C) 2012 Thunderdark

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    Contributors:
    Thunderdark - initial implementation
 */

package com.nekokittygames.mffs.common.item;

import java.util.List;

import com.nekokittygames.mffs.api.IForceEnergyItems;
import com.nekokittygames.mffs.api.IPowerLinkItem;
import com.nekokittygames.mffs.common.NBTTagCompoundHelper;
import com.nekokittygames.mffs.common.tileentity.TileEntityMachines;
import com.nekokittygames.mffs.libs.LibItemNames;
import com.nekokittygames.mffs.libs.LibMisc;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemForcePowerCrystal extends ItemMFFSBase implements
		IPowerLinkItem, IForceEnergyItems {

	public ItemForcePowerCrystal() {
		super();
		setMaxStackSize(1);
		setMaxDamage(100);
		setUnlocalizedName(LibMisc.UNLOCALIZED_PREFIX+ LibItemNames.FORCE_POWER_CRYSTAL);
		setRegistryName(LibItemNames.FORCE_POWER_CRYSTAL);
		setHasSubtypes(true);

	}



	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public boolean isDamageable() {
		return true;
	}

	@Override
	public int getPowerTransferrate() {
		return 100000;
	}



	@Override
	public int getItemDamage(ItemStack itemStack) {

		return 101 - ((getAvailablePower(itemStack, null, null) * 100) / getMaximumPower(itemStack));

	}

	@Override
	public int getMaximumPower(ItemStack itemStack) {
		return 5000000;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,
			List info, boolean b) {
		String tooltip = String.format("%d FE/%d FE ",
				getAvailablePower(itemStack, null, null),
				getMaximumPower(itemStack));
		info.add(tooltip);
	}


	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		ItemStack charged = new ItemStack(this, 1);
		charged.setItemDamage(1);
		setAvailablePower(charged, getMaximumPower(null));
		subItems.add(charged);

		ItemStack empty = new ItemStack(this, 1);
		empty.setItemDamage(100);
		setAvailablePower(empty, 0);
		subItems.add(empty);
	}


	@Override
	public boolean isPowersourceItem() {
		return true;
	}

	@Override
	public int getAvailablePower(ItemStack itemStack, TileEntityMachines tem,
			World world) {
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper
				.getTAGfromItemstack(itemStack);
		if (nbtTagCompound != null) {
			return nbtTagCompound.getInteger("ForceEnergy");
		}
		return 0;
	}

	@Override
	public int getMaximumPower(ItemStack itemStack, TileEntityMachines tem,
			World world) {
		return getMaximumPower(itemStack);
	}

	@Override
	public boolean consumePower(ItemStack itemStack, int powerAmount,
			boolean simulation, TileEntityMachines tem, World world) {

		if (getAvailablePower(itemStack, null, null) >= powerAmount) {
			if (!simulation) {
				setAvailablePower(itemStack,
						getAvailablePower(itemStack, null, null) - powerAmount);
			}
			return true;
		}
		return false;

	}

	@Override
	public int getPowersourceID(ItemStack itemStack, TileEntityMachines tem,
			World world) {

		return -1;
	}

	@Override
	public int getPercentageCapacity(ItemStack itemStack,
			TileEntityMachines tem, World world) {
		return ((getAvailablePower(itemStack, null, null) / 1000) * 100)
				/ (getMaximumPower(itemStack) / 1000);

	}

	@Override
	public boolean insertPower(ItemStack itemStack, int powerAmount,
			boolean simulation, TileEntityMachines tem, World world) {

		if (getAvailablePower(itemStack) + powerAmount <= getMaximumPower(itemStack)) {
			if (!simulation) {
				setAvailablePower(itemStack,
						getAvailablePower(itemStack, null, null) + powerAmount);
			}
			return true;
		}

		return false;
	}

	@Override
	public int getfreeStorageAmount(ItemStack itemStack,
			TileEntityMachines tem, World world) {
		return getMaximumPower(itemStack)
				- getAvailablePower(itemStack, null, null);
	}

	@Override
	public void setAvailablePower(ItemStack itemStack, int ForceEnergy) {
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper
				.getTAGfromItemstack(itemStack);
		nbtTagCompound.setInteger("ForceEnergy", ForceEnergy);

		itemStack.setItemDamage(getItemDamage(itemStack));

	}

	@Override
	public int getAvailablePower(ItemStack itemStack) {
		return getAvailablePower(itemStack, null, null);
	}

	@Override
	public boolean consumePower(ItemStack itemStack, int powerAmount,
			boolean simulation) {
		return consumePower(itemStack, powerAmount, simulation, null, null);
	}

}
