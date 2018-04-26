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

package com.nekokittygames.mffs.common.tileentity;

import com.nekokittygames.mffs.common.Linkgrid;
import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.NBTTagCompoundHelper;
import com.nekokittygames.mffs.common.SecurityRight;
import com.nekokittygames.mffs.common.container.ContainerAdvSecurityStation;
import com.nekokittygames.mffs.common.item.*;
import com.nekokittygames.mffs.common.multitool.ItemDebugger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.LinkedList;
import java.util.List;

public class TileEntityAdvSecurityStation extends TileEntityMachines {
	private String MainUser;
	private boolean securityEstablished = false;

	public TileEntityAdvSecurityStation() {
		super(40);

		MainUser = "";

	}

	@Override
	public void dropPlugins() {
		for (int a = 0; a < this.inventory.size(); a++) {
			dropPlugins(a);
		}
	}

	public String getMainUser() {
		return MainUser;
	}

	public void setMainUser(String s) {
		if (!MainUser.equals(s)) {
			this.MainUser = s;
			markDirty();
		}
	}

	@Override
	public Container getContainer(InventoryPlayer inventoryplayer) {
		return new ContainerAdvSecurityStation(inventoryplayer.player, this);
	}

	@Override
	public void invalidate() {
		Linkgrid.getWorldMap(world).getSecStation().remove(getDeviceID());
		super.invalidate();
	}

	@Override
	public void update() {
		if (world.isRemote == false) {
			if (this.getTicker() >= 10) {

				if (!getMainUser().equals("")) {
					if (isActive() != true) {
						setActive(true);
					}
				} else {
					if (isActive() != false) {
						setActive(false);
					}
				}
				checkSlots();
				this.setTicker((short) 0);
				securityEstablished = true;
			}
			this.setTicker((short) (this.getTicker() + 1));
		}

		super.update();
	}

	public boolean isSecurityEstablished() {
		return this.securityEstablished;
	}

	public void checkSlots() {
		if (getStackInSlot(0) != null) {
			if (getStackInSlot(0).getItem() == ModItems.PERSONAL_ID) {
				ItemCardPersonalID Card = (ItemCardPersonalID) getStackInSlot(0)
						.getItem();

				String name = Card.getUUID(getStackInSlot(0));

				if (!getMainUser().equals(name)) {
					setMainUser(name);
				}

				if (ItemCardPersonalID.hasRight(getStackInSlot(0), SecurityRight.CSR) != true) {
					ItemCardPersonalID.setRight(getStackInSlot(0), SecurityRight.CSR, true);
				}
			} else {

				setMainUser("");
			}
		} else {

			setMainUser("");

		}

		if (getStackInSlot(39) != null && getStackInSlot(38) != null) {
			if (getStackInSlot(38).getItem() instanceof ItemCardEmpty
					&& getStackInSlot(39).getItem() instanceof ItemCardPersonalID) {

				this.setInventorySlotContents(38,
						getStackInSlot(39).copy());

			}
		}

	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public String getName() {
		return "Secstation";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	public boolean RemoteInventory(String username, SecurityRight right) {

		for (int a = 35; a >= 1; a--) {
			ItemStack stack = getStackInSlot(a);
			
			if (!stack.isEmpty() && stack.getItem() == ModItems.PERSONAL_ID) {
				String username_invtory = NBTTagCompoundHelper
						.getTAGfromItemstack(stack).getString("name");

				ItemCardPersonalID Card = (ItemCardPersonalID) stack.getItem();

				boolean access = ItemCardPersonalID.hasRight(stack, right);

				if (username_invtory.equals(username)) {
					if (access) {
						return true;
					} else {
						return false;
					}
				}
			}
		}

		return false;
	}

	public boolean RemotePlayerInventory(String username, SecurityRight right) {
		EntityPlayer player = world.getPlayerEntityByName(username);
		if (player != null) {
			List<Slot> slots = player.inventoryContainer.inventorySlots;
			for (Slot slot : slots) {
				ItemStack stack = slot.getStack();
				if (!stack.isEmpty()) {
					if (stack.getItem() instanceof ItemAccessCard) {
						if (ItemAccessCard
								.getvalidity(stack) > 0) {
							if (ItemAccessCard
									.getlinkID(stack) == getDeviceID()) {
								if (ItemAccessCard
										.hasRight(stack, right)) {

									if (!ItemAccessCard
											.getforAreaname(stack).equals(
													getDeviceName()))
										ItemAccessCard
												.setforArea(stack, this);

									return true;
								}
							}
						} else {
							/*player.sendChatToPlayer(LanguageRegistry.instance().getStringLocalization
									("securityStation.expiredValidity"));*/
							ItemStack Card = new ItemStack(
									ModItems.EMPTY_CARD,
									1);
							slot.putStack(Card);

						}

					}
					if (stack.getItem() instanceof ItemDebugger) {
						return true;
					}

				}
			}
		}
		return false;
	}

	public boolean isAccessGranted(String username, SecurityRight sr) {

		if (!isActive())
			return true;

		String[] ops = ModularForceFieldSystem.Admin.split(";");

		EntityPlayer player = world.getPlayerEntityByName(username);
		if(player != null && player.capabilities.isCreativeMode) {
			return true;
		}

		for (int i = 0; i <= ops.length - 1; i++) {
			if (username.equalsIgnoreCase(ops[i]))
				return true;
		}

		if (this.MainUser.equals(username))
			return true;

		if (RemoteInventory(username, sr))
			return true;

		if (RemotePlayerInventory(username, sr))
			return true;

		return false;
	}

	@Override
	public List<String> getFieldsforUpdate() {

		List<String> NetworkedFields = new LinkedList<String>();
		NetworkedFields.clear();

		NetworkedFields.addAll(super.getFieldsforUpdate());
		NetworkedFields.add("MainUser");

		return NetworkedFields;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack, int Slot) {

		switch (Slot) {
		case 38:
			if (!(par1ItemStack.getItem() instanceof ItemCardEmpty))
				return false;
			break;
		case 39:
			if (par1ItemStack.getItem() instanceof ItemAccessCard
					|| !(par1ItemStack.getItem() instanceof ItemCardPersonalID))
				return false;
			break;

		}

		if (par1ItemStack.getItem() instanceof ItemCardPersonalID
				|| par1ItemStack.getItem() instanceof ItemCardEmpty)
			return true;

		return false;
	}

	@Override
	public void onNetworkHandlerEvent(int key, String value) {

		ModularForceFieldSystem.log.info("Key: "+Integer.toString(key)+". Value: "+value);
		switch (key) {
		case 100:

			if (!getStackInSlot(1).isEmpty()) {
				SecurityRight sr = SecurityRight.rights.get(value);
				if (sr != null
						&& getStackInSlot(1).getItem() instanceof ItemCardPersonalID) {
					ItemCardPersonalID
							.setRight(getStackInSlot(1), sr,
									!ItemCardPersonalID.hasRight(
											getStackInSlot(1), sr));
				}
				}
			break;
		case 101:
			if (!getStackInSlot(1).isEmpty()) {
				if (getStackInSlot(1).getItem() instanceof ItemAccessCard) {
					if (ItemAccessCard.getvalidity(getStackInSlot(1)) <= 5) {
						this.setInventorySlotContents(1, new ItemStack(
								ModItems.EMPTY_CARD, 1));
					} else {
						ItemAccessCard
								.setvalidity(getStackInSlot(1), ItemAccessCard
										.getvalidity(getStackInSlot(1)) - 5);
					}
				}
			}
			break;
		case 102:
			if (!getStackInSlot(1).isEmpty()) {
				if (getStackInSlot(1).getItem() instanceof ItemCardEmpty) {
					setInventorySlotContents(1, new ItemStack(
							ModItems.ACCESS_CARD, 1));
					if (getStackInSlot(1).getItem() instanceof ItemAccessCard) {
						ItemAccessCard.setforArea(getStackInSlot(1), this);
						ItemAccessCard.setvalidity(getStackInSlot(1), 5);
						ItemAccessCard.setlinkID(getStackInSlot(1), this);
					}
					break;
				}
				if (getStackInSlot(1).getItem() instanceof ItemAccessCard) {

					ItemAccessCard.setvalidity(getStackInSlot(1),
							ItemAccessCard.getvalidity(getStackInSlot(1)) + 5);
				}
			}
			break;

		}
		super.onNetworkHandlerEvent(key, value);
	}

	public ItemStack getModCardStack() {
		if (!getStackInSlot(1).isEmpty()) {
			return getStackInSlot(1);
		}
		return null;
	}

	@Override
	public int getSlotStackLimit(int slt) {
		return 1;
	}

	@Override
	public TileEntityAdvSecurityStation getLinkedSecurityStation() {
		return this;
	}
}
