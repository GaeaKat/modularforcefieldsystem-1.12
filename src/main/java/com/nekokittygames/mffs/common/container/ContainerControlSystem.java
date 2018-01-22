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
import com.nekokittygames.mffs.common.tileentity.TileEntityControlSystem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerControlSystem extends Container {
	private EntityPlayer player;
	private TileEntityControlSystem Control;

	public ContainerControlSystem(EntityPlayer player,
			TileEntityControlSystem tileentity) {
		Control = tileentity;
		this.player = player;

		addSlotToContainer(new SlotHelper(Control, 0, 236, 4)); // Security Link
		addSlotToContainer(new SlotHelper(Control, 1, 203, 30)); // Reader

		int var3;
		int var4;

		for (var3 = 0; var3 < 7; ++var3) {
			for (var4 = 0; var4 < 4; ++var4) {
				this.addSlotToContainer(new SlotHelper(Control,
						(var4 + var3 * 4) + 4, 176 + var4 * 18, 80 + var3 * 18));
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
		return Control.isUsableByPlayer(entityplayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p, int i) {
		return null;
	}

}
