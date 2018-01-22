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

package com.nekokittygames.mffs.common.container;

import com.nekokittygames.mffs.common.SlotHelper;
import com.nekokittygames.mffs.common.tileentity.TileEntitySecStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSecStorage extends Container {
	private EntityPlayer player;
	private TileEntitySecStorage SecStorage;

	public ContainerSecStorage(EntityPlayer player,
			TileEntitySecStorage tileentity) {
		SecStorage = tileentity;
		this.player = player;

		addSlotToContainer(new SlotHelper(SecStorage, 0, 12, 24)); // Security
																	// link

		int var3;
		int var4;

		for (var3 = 0; var3 < 6; ++var3) {
			for (var4 = 0; var4 < 9; ++var4) {
				this.addSlotToContainer(new SlotHelper(SecStorage,
						(var4 + var3 * 9) + 1, 12 + var4 * 18, 43 + var3 * 18));
			}
		}

		for (var3 = 0; var3 < 3; ++var3) {
			for (var4 = 0; var4 < 9; ++var4) {
				this.addSlotToContainer(new Slot(player.inventory, var4 + var3
						* 9 + 9, 12 + var4 * 18, 155 + var3 * 18));
			}
		}

		for (var3 = 0; var3 < 9; ++var3) {
			this.addSlotToContainer(new Slot(player.inventory, var3,
					12 + var3 * 18, 213));
		}
	}

	public EntityPlayer getPlayer() {
		return player;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return SecStorage.isUsableByPlayer(entityplayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p, int i) {
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(i);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (i < SecStorage.getSizeInventory()) {
				if (!mergeItemStack(itemstack1, SecStorage.getSizeInventory(),
						inventorySlots.size(), true)) {
					return null;
				}
			} else if (!mergeItemStack(itemstack1, 0,
					SecStorage.getSizeInventory(), false)) {
				return null;
			}
			if (itemstack1.getCount() == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
		}
		return itemstack;
	}

}
