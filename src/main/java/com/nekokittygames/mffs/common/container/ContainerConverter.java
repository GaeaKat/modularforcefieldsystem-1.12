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
import com.nekokittygames.mffs.common.tileentity.TileEntityConverter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerConverter extends Container {
	private int linkPower;
	private int capacity;
	private EntityPlayer player;
	private TileEntityConverter Convertor;

	private int RF_Output;

	public ContainerConverter(EntityPlayer player,
			TileEntityConverter tileentity) {
		Convertor = tileentity;
		this.player = player;
		linkPower = -1;
		capacity = -1;
		RF_Output = -1;

		addSlotToContainer(new SlotHelper(Convertor, 0, 44, 28)); // Power link

		int var3;

		for (var3 = 0; var3 < 3; ++var3) {
			for (int var4 = 0; var4 < 9; ++var4) {
				this.addSlotToContainer(new Slot(player.inventory, var4 + var3
						* 9 + 9, 50 + var4 * 18, 133 + var3 * 18));
			}
		}

		for (var3 = 0; var3 < 9; ++var3) {
			this.addSlotToContainer(new Slot(player.inventory, var3,
					50 + var3 * 18, 191));
		}
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < listeners.size(); i++) {
			IContainerListener icrafting = (IContainerListener) listeners.get(i);

			if (linkPower != Convertor.getLinkPower()) {
				icrafting.sendProgressBarUpdate(this, 0,
						Convertor.getLinkPower() & 0xffff);
				icrafting.sendProgressBarUpdate(this, 1,
						Convertor.getLinkPower() >>> 16);
			}

			if (capacity != Convertor.getPercentageCapacity())
				icrafting.sendProgressBarUpdate(this, 3,
						Convertor.getPercentageCapacity());

			if (RF_Output != Convertor.getRF_Output())
				icrafting.sendProgressBarUpdate(this, 6,
						Convertor.getRF_Output());
		}

		linkPower = Convertor.getLinkPower();
		capacity = Convertor.getPercentageCapacity();
		RF_Output = Convertor.getRF_Output();
	}

	@Override
	public void updateProgressBar(int i, int j) {
		switch (i) {
		case 0:
			Convertor.setLinkPower(Convertor.getLinkPower() & 0xffff0000 | j);
			break;
		case 1:
			Convertor.setLinkPower(Convertor.getLinkPower() & 0xffff | j << 16);
			break;
		case 3:
			Convertor.setCapacity(j);
			break;

		case 6:
			Convertor.setRF_Output(j);
			break;
		}
	}

	public EntityPlayer getPlayer() {
		return player;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return Convertor.isUseableByPlayer(entityplayer);
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
