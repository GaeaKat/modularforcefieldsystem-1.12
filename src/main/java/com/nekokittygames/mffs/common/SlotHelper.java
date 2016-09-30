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


import com.nekokittygames.mffs.common.tileentity.TileEntityMachines;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotHelper extends Slot {
	private TileEntityMachines te;
	private int Slot;

	public SlotHelper(IInventory par2IInventory, int par3, int par4, int par5) {
		super(par2IInventory, par3, par4, par5);
		this.te = (TileEntityMachines) par2IInventory;
		this.Slot = par3;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		return te.isItemValid(par1ItemStack, Slot);
	}

	@Override
	public int getSlotStackLimit() {
		return te.getSlotStackLimit(Slot);
	}
}
