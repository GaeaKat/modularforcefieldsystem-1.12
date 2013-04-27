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
import mods.mffs.common.tileentity.TileEntityAreaDefenseStation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAreaDefenseStation extends Container {
	private TileEntityAreaDefenseStation defstation;

	private int capacity;
	private int SwitchTyp;
	private int contratyp;
	private int actionmode;
	private int scanmode;
	private EntityPlayer player;

	public ContainerAreaDefenseStation(EntityPlayer player,
			TileEntityAreaDefenseStation tileentity) {
		capacity = -1;
		SwitchTyp = -1;
		contratyp = -1;
		actionmode = -1;
		scanmode = -1;

		defstation = tileentity;
		this.player = player;

		addSlotToContainer(new SlotHelper(defstation, 0, 13, 27)); // Power Link
		addSlotToContainer(new SlotHelper(defstation, 1, 97, 27)); // Security
																	// Link

		addSlotToContainer(new SlotHelper(defstation, 2, 14, 51)); // Distance
																	// mod
		addSlotToContainer(new SlotHelper(defstation, 3, 14, 88)); // Distance
																	// mod

		int var3;
		int var4;

		// illegal items 5+
		for (var3 = 0; var3 < 2; ++var3) {
			for (var4 = 0; var4 < 4; ++var4) {
				this.addSlotToContainer(new SlotHelper(defstation,
						(var4 + var3 * 4) + 5, 176 + var4 * 18, 26 + var3 * 18));
			}
		}

		// itembuffer 15+
		for (var3 = 0; var3 < 5; ++var3) {
			for (var4 = 0; var4 < 4; ++var4) {
				this.addSlotToContainer(new SlotHelper(defstation,
						(var4 + var3 * 4) + 15, 176 + var4 * 18, 98 + var3 * 18));
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
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < crafters.size(); i++) {
			ICrafting icrafting = (ICrafting) crafters.get(i);

			if (contratyp != defstation.getcontratyp()) {
				icrafting.sendProgressBarUpdate(this, 1,
						defstation.getcontratyp());
			}
			if (actionmode != defstation.getActionmode()) {
				icrafting.sendProgressBarUpdate(this, 2,
						defstation.getActionmode());
			}
			if (scanmode != defstation.getScanmode()) {
				icrafting.sendProgressBarUpdate(this, 3,
						defstation.getScanmode());
			}

			if (capacity != defstation.getPercentageCapacity()) {
				icrafting.sendProgressBarUpdate(this, 4,
						defstation.getPercentageCapacity());
			}
		}

		scanmode = defstation.getScanmode();
		actionmode = defstation.getActionmode();
		contratyp = defstation.getcontratyp();
		capacity = defstation.getPercentageCapacity();
	}

	@Override
	public void updateProgressBar(int i, int j) {
		switch (i) {

		case 1:
			defstation.setcontratyp(j);
			break;
		case 2:
			defstation.setActionmode(j);
			break;
		case 3:
			defstation.setScanmode(j);
			break;
		case 4:
			defstation.setCapacity(j);
			break;
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return defstation.isUseableByPlayer(entityplayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p, int i) {
		return null;
	}

}