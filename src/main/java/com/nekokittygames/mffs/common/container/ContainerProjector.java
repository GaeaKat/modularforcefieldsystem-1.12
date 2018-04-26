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
import com.nekokittygames.mffs.common.tileentity.TileEntityProjector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;

public class ContainerProjector extends ContainerMachine {
	private TileEntityProjector projectorentity;
	private int linkPower;
	private int maxlinkPower;
	private int accesstyp;
	private int capacity;

	public ContainerProjector(EntityPlayer player,
			TileEntityProjector tileentity) {
		super(player, tileentity);
		projectorentity = tileentity;
		linkPower = -1;
		maxlinkPower = -1;
		accesstyp = -1;
		capacity = -1;

		addSlotToContainer(new SlotHelper(projectorentity, 0, 11, 61)); // Linkcard
		addSlotToContainer(new SlotHelper(projectorentity, 1, 11, 38)); // Typ
																		// Slot

		addSlotToContainer(new SlotHelper(projectorentity, 2, 120, 82)); // OptionSlot
		addSlotToContainer(new SlotHelper(projectorentity, 3, 138, 82)); // OptionSlot
		addSlotToContainer(new SlotHelper(projectorentity, 4, 156, 82)); // OptionSlot

		addSlotToContainer(new SlotHelper(projectorentity, 6, 155, 64)); // StreghtSlot
		addSlotToContainer(new SlotHelper(projectorentity, 5, 119, 64)); // DistancetSlot

		addSlotToContainer(new SlotHelper(projectorentity, 7, 137, 28)); // Focus
																			// up
		addSlotToContainer(new SlotHelper(projectorentity, 8, 137, 62)); // Focus
																			// down
		addSlotToContainer(new SlotHelper(projectorentity, 9, 154, 45)); // Focus
																			// right
		addSlotToContainer(new SlotHelper(projectorentity, 10, 120, 45)); // Focus
																			// left

		addSlotToContainer(new SlotHelper(projectorentity, 11, 137, 45)); // Centerslot

		addSlotToContainer(new SlotHelper(projectorentity, 12, 92, 38)); // SecCard

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

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < listeners.size(); i++) {
			IContainerListener icrafting = (IContainerListener) listeners.get(i);

			if (linkPower != projectorentity.getLinkPower()) {
				icrafting.sendWindowProperty(this, 0,
						projectorentity.getLinkPower() & 0xffff);
				icrafting.sendWindowProperty(this, 1,
						projectorentity.getLinkPower() >>> 16);
			}
			if (capacity != projectorentity.getPercentageCapacity()) {
				icrafting.sendWindowProperty(this, 2,
						projectorentity.getPercentageCapacity());
			}

			if (accesstyp != projectorentity.getaccesstyp()) {
				icrafting.sendWindowProperty(this, 4,
						projectorentity.getaccesstyp());
			}

		}

		linkPower = projectorentity.getLinkPower();
		accesstyp = projectorentity.getaccesstyp();
		capacity = projectorentity.getPercentageCapacity();
	}

	@Override
	public void updateProgressBar(int i, int j) {
		switch (i) {
		case 0:
			projectorentity
					.setLinkPower((projectorentity.getLinkPower() & 0xffff0000)
							| j);
			break;
		case 1:
			projectorentity
					.setLinkPower((projectorentity.getLinkPower() & 0xffff)
							| (j << 16));
			break;
		case 2:
			projectorentity.setCapacity(j);
			break;

		case 4:
			projectorentity.setaccesstyp(j);
			break;
		}
	}
}