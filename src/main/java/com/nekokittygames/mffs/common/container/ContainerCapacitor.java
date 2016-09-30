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
import com.nekokittygames.mffs.common.tileentity.TileEntityCapacitor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCapacitor extends Container {
	private TileEntityCapacitor generatorentity;

	private int capacity;
	private int forcepower;
	private int Powerlinkmode;
	private short linketprojektor;
	private EntityPlayer player;

	public ContainerCapacitor(EntityPlayer player,
			TileEntityCapacitor tileentity) {
		forcepower = -1;
		linketprojektor = -1;
		capacity = -1;
		Powerlinkmode = -1;
		generatorentity = tileentity;
		this.player = player;

		addSlotToContainer(new SlotHelper(generatorentity, 4, 154, 88)); // Security
																			// Link
		addSlotToContainer(new SlotHelper(generatorentity, 0, 154, 47)); // Capacity
																			// upgrade
		addSlotToContainer(new SlotHelper(generatorentity, 1, 154, 67)); // Range
																			// upgrade
		addSlotToContainer(new SlotHelper(generatorentity, 2, 87, 76)); // Force
																		// Energy/ext.
																		// Powerlink

		int var3;

		for (var3 = 0; var3 < 3; ++var3) {
			for (int var4 = 0; var4 < 9; ++var4) {
				this.addSlotToContainer(new Slot(player.inventory, var4 + var3
						* 9 + 9, 8 + var4 * 18, 125 + var3 * 18));
			}
		}

		for (var3 = 0; var3 < 9; ++var3) {
			this.addSlotToContainer(new Slot(player.inventory, var3,
					8 + var3 * 18, 183));
		}
	}

	public EntityPlayer getPlayer() {
		return player;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < listeners.size(); i++) {
			IContainerListener icrafting = (IContainerListener) listeners.get(i);

			if (linketprojektor != generatorentity.getLinketProjektor()) {
				icrafting.sendProgressBarUpdate(this, 1,
						generatorentity.getLinketProjektor());
			}

			if (forcepower != generatorentity.getStorageAvailablePower()) {
				icrafting.sendProgressBarUpdate(this, 2,
						generatorentity.getStorageAvailablePower() & 0xffff);
				icrafting.sendProgressBarUpdate(this, 3,
						generatorentity.getStorageAvailablePower() >>> 16);
			}
			if (capacity != generatorentity.getPercentageStorageCapacity()) {
				icrafting.sendProgressBarUpdate(this, 4,
						generatorentity.getPercentageStorageCapacity());
			}
			if (Powerlinkmode != generatorentity.getPowerlinkmode()) {
				icrafting.sendProgressBarUpdate(this, 6,
						generatorentity.getPowerlinkmode());
			}
		}

		linketprojektor = generatorentity.getLinketProjektor();
		forcepower = generatorentity.getStorageAvailablePower();
		capacity = generatorentity.getPercentageStorageCapacity();
		Powerlinkmode = generatorentity.getPowerlinkmode();
	}

	@Override
	public void updateProgressBar(int i, int j) {
		switch (i) {
		case 1:

			generatorentity.setLinketprojektor((short) j);
			break;

		case 2:
			generatorentity.setForcePower((generatorentity
					.getStorageAvailablePower() & 0xffff0000) | j);
			break;
		case 3:
			generatorentity.setForcePower((generatorentity
					.getStorageAvailablePower() & 0xffff) | (j << 16));
			break;

		case 4:
			generatorentity.setCapacity(j);
			break;

		case 6:
			generatorentity.setPowerlinkmode(j);
			break;
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return generatorentity.isUseableByPlayer(entityplayer);
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