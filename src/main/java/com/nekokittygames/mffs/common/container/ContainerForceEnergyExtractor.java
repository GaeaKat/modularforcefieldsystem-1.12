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
import com.nekokittygames.mffs.common.tileentity.TileEntityExtractor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerForceEnergyExtractor extends Container {
	private TileEntityExtractor ForceEnergyExtractor;
	private EntityPlayer player;
	private int WorkCylce;
	private int workdone;
	private int ForceEnergybuffer;

	public ContainerForceEnergyExtractor(EntityPlayer player,
			TileEntityExtractor tileentity) {
		ForceEnergyExtractor = tileentity;
		this.player = player;
		WorkCylce = -1;
		workdone = -1;
		ForceEnergybuffer = -1;

		addSlotToContainer(new SlotHelper(ForceEnergyExtractor, 0, 82, 26)); // Forcicum
		addSlotToContainer(new SlotHelper(ForceEnergyExtractor, 1, 145, 40)); // Powerlink
		addSlotToContainer(new SlotHelper(ForceEnergyExtractor, 2, 20, 66)); // Cap
																				// upgrade
		addSlotToContainer(new SlotHelper(ForceEnergyExtractor, 3, 39, 66)); // Boost
																				// upgrade
		addSlotToContainer(new SlotHelper(ForceEnergyExtractor, 4, 112, 26)); // Forcicum
																				// cell

		int var3;

		for (var3 = 0; var3 < 3; ++var3) {
			for (int var4 = 0; var4 < 9; ++var4) {
				this.addSlotToContainer(new Slot(player.inventory, var4 + var3
						* 9 + 9, 8 + var4 * 18, 104 + var3 * 18));
			}
		}

		for (var3 = 0; var3 < 9; ++var3) {
			this.addSlotToContainer(new Slot(player.inventory, var3,
					8 + var3 * 18, 162));
		}
	}

	public EntityPlayer getPlayer() {
		return player;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return ForceEnergyExtractor.isUseableByPlayer(entityplayer);
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

	@Override
	public void updateProgressBar(int i, int j) {
		switch (i) {
		case 0:
			ForceEnergyExtractor.setWorkdone(j);
			break;

		case 1:
			ForceEnergyExtractor.setWorkCylce(j);
			break;

		case 2:
			ForceEnergyExtractor.setForceEnergybuffer((ForceEnergyExtractor
					.getForceEnergybuffer() & 0xffff0000) | j);
			break;
		case 3:
			ForceEnergyExtractor.setForceEnergybuffer((ForceEnergyExtractor
					.getForceEnergybuffer() & 0xffff) | (j << 16));
			break;
		}
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < listeners.size(); i++) {
			IContainerListener icrafting = (IContainerListener) listeners.get(i);

			if (workdone != ForceEnergyExtractor.getWorkdone()) {
				icrafting.sendProgressBarUpdate(this, 0,
						ForceEnergyExtractor.getWorkdone());
			}
			if (WorkCylce != ForceEnergyExtractor.getWorkCylce()) {
				icrafting.sendProgressBarUpdate(this, 1,
						ForceEnergyExtractor.getWorkCylce());
			}

			if (ForceEnergybuffer != ForceEnergyExtractor
					.getForceEnergybuffer()) {
				icrafting.sendProgressBarUpdate(this, 2,
						ForceEnergyExtractor.getForceEnergybuffer() & 0xffff);
				icrafting.sendProgressBarUpdate(this, 3,
						ForceEnergyExtractor.getForceEnergybuffer() >>> 16);
			}
		}

		workdone = ForceEnergyExtractor.getWorkdone();
		WorkCylce = ForceEnergyExtractor.getWorkCylce();
		ForceEnergybuffer = ForceEnergyExtractor.getForceEnergybuffer();
	}
}