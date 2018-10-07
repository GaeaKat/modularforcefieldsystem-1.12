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
    
    Thunderdark
    Matchlighter

 */

package com.nekokittygames.mffs.common;

import com.nekokittygames.mffs.api.IForceEnergyItems;
import com.nekokittygames.mffs.common.item.ItemMFFSBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class ForceEnergyItems extends ItemMFFSBase implements
		IForceEnergyItems {

	public ForceEnergyItems(final String itemName) {
		super(itemName);
	}

	@Override
	public boolean consumePower(ItemStack itemStack, int amount,
			boolean simulation) {
		if (itemStack.getItem() instanceof IForceEnergyItems) {

			if (getAvailablePower(itemStack) >= amount) {
				if (!simulation) {
					setAvailablePower(itemStack, getAvailablePower(itemStack)
							- amount);
				}
				return true;
			}
		}
		return false;
	}

	public boolean chargeItem(ItemStack itemStack, int amount,
			boolean simulation) {

		if (itemStack.getItem() instanceof IForceEnergyItems) {

			if (getAvailablePower(itemStack) + amount <= getMaximumPower(itemStack)) {
				if (!simulation) {
					setAvailablePower(itemStack, getAvailablePower(itemStack)
							+ amount);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void setAvailablePower(ItemStack itemStack, int ForceEnergy) {
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper
				.getTAGfromItemstack(itemStack);
		nbtTagCompound.setInteger("ForceEnergy", ForceEnergy);
	}

	@Override
	public int getAvailablePower(ItemStack itemstack) {

		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper
				.getTAGfromItemstack(itemstack);
		if (nbtTagCompound != null) {
			return nbtTagCompound.getInteger("ForceEnergy");
		}
		return 0;
	}

}
