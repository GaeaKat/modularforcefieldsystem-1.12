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

package mods.mffs.common.item;

import java.util.List;

import mods.mffs.api.IForceEnergyItems;
import mods.mffs.api.IPowerLinkItem;
import mods.mffs.common.NBTTagCompoundHelper;
import mods.mffs.common.tileentity.TileEntityMachines;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemForcePowerCrystal extends ItemMFFSBase implements
		IPowerLinkItem, IForceEnergyItems {
	private Icon[] damagedIcons = new Icon[5];

	public ItemForcePowerCrystal(int i) {
		super(i);
		setMaxStackSize(1);
		setMaxDamage(100);

	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("mffs:ForcePowerCrystal");

		for (int _i = 0; _i < 5; _i++)
			damagedIcons[_i] = iconRegister
					.registerIcon("mffs:ForcePowerCrystal_" + _i);
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
	public Icon getIconFromDamage(int dmg) {
		if (dmg == 0)
			return itemIcon;

		return damagedIcons[((100 - dmg) / 20)];
		// return 112 + ((100-dmg)/20);
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
	@SideOnly(Side.CLIENT)
	public void getSubItems(int i, CreativeTabs tabs, List itemList) {
		ItemStack charged = new ItemStack(this, 1);
		charged.setItemDamage(1);
		setAvailablePower(charged, getMaximumPower(null));
		itemList.add(charged);

		ItemStack empty = new ItemStack(this, 1);
		empty.setItemDamage(100);
		setAvailablePower(empty, 0);
		itemList.add(empty);

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
