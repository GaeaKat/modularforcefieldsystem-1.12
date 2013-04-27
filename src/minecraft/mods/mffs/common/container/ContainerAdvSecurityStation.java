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

package mods.mffs.common.container;

import mods.mffs.common.SlotHelper;
import mods.mffs.common.tileentity.TileEntityAdvSecurityStation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAdvSecurityStation extends Container {
	private TileEntityAdvSecurityStation SecStation;
	private EntityPlayer player;

	public ContainerAdvSecurityStation(EntityPlayer player,
			TileEntityAdvSecurityStation tileentity) {
		SecStation = tileentity;
		this.player = player;

		addSlotToContainer(new SlotHelper(SecStation, 0, 177, 33)); // MasterCard
		addSlotToContainer(new SlotHelper(SecStation, 1, 15, 30)); // Coder

		addSlotToContainer(new SlotHelper(SecStation, 39, 88, 102)); // Copymaster
		addSlotToContainer(new SlotHelper(SecStation, 38, 146, 102)); // Copytarget

		int var3;
		int var4;

		for (var3 = 0; var3 < 8; ++var3) {
			for (var4 = 0; var4 < 4; ++var4) {
				this.addSlotToContainer(new SlotHelper(SecStation,
						(var4 + var3 * 4) + 2, 176 + var4 * 18, 62 + var3 * 18));
			}
		}

		for (var3 = 0; var3 < 3; ++var3) {
			for (var4 = 0; var4 < 9; ++var4) {
				this.addSlotToContainer(new Slot(player.inventory, var4 + var3
						* 9 + 9, 8 + var4 * 18, 134 + var3 * 18));
			}
		}

		for (var3 = 0; var3 < 9; ++var3) {
			this.addSlotToContainer(new Slot(player.inventory, var3,
					8 + var3 * 18, 192));
		}
	}

	public EntityPlayer getPlayer() {
		return player;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return SecStation.isUseableByPlayer(entityplayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p, int i) {
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(i);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
			if (itemstack1.stackSize != itemstack.stackSize) {
				slot.onSlotChanged();
			} else {
				return null;
			}
		}
		return itemstack;
	}

}