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
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants;

import java.util.LinkedList;
import java.util.List;

public class TileEntityAdvSecurityStation extends TileEntityMachines {
	private String MainUser;
	private ItemStack inventory[];
	private boolean securityEstablished = false;

	public TileEntityAdvSecurityStation() {

		inventory = new ItemStack[40];
		MainUser = "";

	}

	@Override
	public void dropPlugins() {
		for (int a = 0; a < this.inventory.length; a++) {
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

	public void dropPlugins(int slot) {
		if (getStackInSlot(slot) != null) {
			if (getStackInSlot(slot).getItem() instanceof ItemCardSecurityLink
					|| getStackInSlot(slot).getItem() instanceof ItemCardPowerLink
					|| getStackInSlot(slot).getItem() instanceof ItemCardPersonalID) {
				world.spawnEntity(new EntityItem(world,
						this.pos.getX(), this.pos.getY(),
						this.pos.getZ(), new ItemStack(
								ModItems.EMPTY_CARD, 1)));
			} else {
				world.spawnEntity(new EntityItem(world,
						this.pos.getX(), this.pos.getY(),
						this.pos.getZ(), this.getStackInSlot(slot)));
			}

			this.setInventorySlotContents(slot, null);
			this.markDirty();
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
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
	}

	@Override
	public void readExtraNBT(NBTTagCompound nbttagcompound) {
		super.readExtraNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		inventory = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist
					.getCompoundTagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if (byte0 >= 0 && byte0 < inventory.length) {
				inventory[byte0] = new ItemStack(nbttagcompound1);
			}
		}
	}

	@Override
	public void writeExtraNBT(NBTTagCompound nbttagcompound) {
		super.writeExtraNBT(nbttagcompound);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("Items", nbttaglist);
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		readExtraNBT(tag);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound cmp= super.getUpdateTag();
		writeExtraNBT(cmp);
		return cmp;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		writeExtraNBT(nbttagcompound);
		return nbttagcompound;
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
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inventory[i];
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (inventory[i] != null) {
			if (inventory[i].getCount() <= j) {
				ItemStack itemstack = inventory[i];
				inventory[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = inventory[i].splitStack(j);
			if (inventory[i].getCount() == 0) {
				inventory[i] = null;
			}
			return itemstack1;
		} else {
			return null;
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack item=inventory[index];
		inventory[index]=null;
		this.markDirty();
		return item;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		inventory[i] = itemstack;
		if (itemstack != null && itemstack.getCount()> getInventoryStackLimit()) {
			itemstack.setCount( getInventoryStackLimit());
		}
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
			if (getStackInSlot(a) != null) {
				if (getStackInSlot(a).getItem() == ModItems.PERSONAL_ID) {
					String username_invtory = NBTTagCompoundHelper
							.getTAGfromItemstack(getStackInSlot(a)).getString(
									"name");

					ItemCardPersonalID Card = (ItemCardPersonalID) getStackInSlot(
							a).getItem();

					boolean access = ItemCardPersonalID.hasRight(
							getStackInSlot(a), right);

					if (username_invtory.equals(username)) {
						if (access) {
							return true;
						} else {
							return false;
						}
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
				if (stack != ItemStack.EMPTY) {
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

	public ItemStack[] getContents() {
		return inventory;
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

			if (getStackInSlot(1) != null) {
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
			if (getStackInSlot(1) != null) {
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
			if (getStackInSlot(1) != null) {
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
		if (getStackInSlot(1) != null) {
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


	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {

	}


	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[0];
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
	}
}
