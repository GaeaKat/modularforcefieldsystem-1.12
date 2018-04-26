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
    
    Matchlighter
    Thunderdark 

 */

package com.nekokittygames.mffs.common.tileentity;

import com.nekokittygames.mffs.common.Linkgrid;
import com.nekokittygames.mffs.common.container.ContainerSecStorage;
import com.nekokittygames.mffs.common.item.ItemCardSecurityLink;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

public class TileEntitySecStorage extends TileEntityMachines implements
		ISidedInventory, IInventory {

	public TileEntitySecStorage() {
		super(60);
	}

	@Override
	public void dropPlugins() {
		for (int a = 0; a < this.inventory.size(); a++) {
			dropPlugins(a);
		}
	}

	@Override
	public TileEntityAdvSecurityStation getLinkedSecurityStation() {
		return ItemCardSecurityLink.getLinkedSecurityStation(this, 0, world);
	}

	@Override
	public void invalidate() {
		Linkgrid.getWorldMap(world).getSecStorage().remove(getDeviceID());
		super.invalidate();
	}

	public int getSecStation_ID() {
		TileEntityAdvSecurityStation sec = getLinkedSecurityStation();
		if (sec != null)
			return sec.getDeviceID();
		return 0;
	}

	@Override
	public short getmaxSwitchModi() {
		return 3;
	}

	@Override
	public short getminSwitchModi() {
		return 2;
	}

	public int getfreeslotcount() {
		int count = 0;

		for (int a = 1; a < this.inventory.size(); a++) {
			if (getStackInSlot(a).isEmpty())
				count++;
		}

		return count;
	}

	@Override
	public void update() {

		if (!world.isRemote) {
			if (getLinkedSecurityStation() != null && !isActive()
					&& getSwitchValue())
				setActive(true);
			if ((getLinkedSecurityStation() == null || !getSwitchValue())
					&& isActive())
				setActive(false);
		}
		super.update();
	}


	@Override
	public String getName() {
		return "SecStation";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public Container getContainer(InventoryPlayer inventoryplayer) {
		return new ContainerSecStorage(inventoryplayer.player, this);
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack, int Slot) {
		switch (Slot) {
		case 0:
			if (!(par1ItemStack.getItem() instanceof ItemCardSecurityLink))
				return false;
			break;
		}

		return true;
	}

	@Override
	public int getSlotStackLimit(int slt) {
		if (slt == 0)
			return 1;
		return 64;
	}
}
