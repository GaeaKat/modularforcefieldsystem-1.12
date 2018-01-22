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


import com.nekokittygames.mffs.api.IForceEnergyItems;
import com.nekokittygames.mffs.api.IForceEnergyStorageBlock;
import com.nekokittygames.mffs.api.IPowerLinkItem;
import com.nekokittygames.mffs.common.Linkgrid;
import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.container.ContainerCapacitor;
import com.nekokittygames.mffs.common.item.ItemCapacitorUpgradeCapacity;
import com.nekokittygames.mffs.common.item.ItemCapacitorUpgradeRange;
import com.nekokittygames.mffs.common.item.ItemCardSecurityLink;
import com.nekokittygames.mffs.common.item.ModItems;
import com.nekokittygames.mffs.network.INetworkHandlerEventListener;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants;

import java.util.LinkedList;
import java.util.List;

public class TileEntityCapacitor extends TileEntityFEPoweredMachine implements
		INetworkHandlerEventListener, IForceEnergyStorageBlock {
	private ItemStack inventory[];
	private int forcePower;
	private short linketprojektor;
	private int capacity;
	private int Powerlinkmode;
	private int TransmitRange;

	public TileEntityCapacitor() {
		inventory = new ItemStack[5];
		forcePower = 0;
		linketprojektor = 0;
		TransmitRange = 8;
		capacity = 0;
		Powerlinkmode = 0;
	}

	@Override
	public int getPowerStorageID() {

		return getDeviceID();
	}

	public void setTransmitRange(int transmitRange) {
		TransmitRange = transmitRange;

	}

	public int getTransmitRange() {

		return TransmitRange;
	}

	public int getPowerlinkmode() {
		return Powerlinkmode;
	}

	public void setPowerlinkmode(int powerlinkmode) {
		Powerlinkmode = powerlinkmode;
	}

	@Override
	public int getPercentageStorageCapacity() {
		return capacity;
	}

	public void setCapacity(int Capacity) {
		if (getPercentageStorageCapacity() != Capacity) {
			this.capacity = Capacity;

		}
	}

	@Override
	public Container getContainer(InventoryPlayer inventoryplayer) {
		return new ContainerCapacitor(inventoryplayer.player, this);
	}

	public Short getLinketProjektor() {
		return linketprojektor;
	}

	public void setLinketprojektor(Short linketprojektor) {
		if (this.linketprojektor != linketprojektor) {
			this.linketprojektor = linketprojektor;

		}
	}

	@Override
	public int getStorageAvailablePower() {
		return forcePower;
	}

	public void setForcePower(int f) {
		forcePower = f;
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public TileEntityAdvSecurityStation getLinkedSecurityStation() {
		return ItemCardSecurityLink.getLinkedSecurityStation(this, 4, getWorld());
	}

	public int getSecStation_ID() {
		TileEntityAdvSecurityStation sec = getLinkedSecurityStation();
		if (sec != null)
			return sec.getDeviceID();
		return 0;
	}

	@Override
	public int getStorageMaxPower() {

		if (getStackInSlot(0) != null) {
			if (getStackInSlot(0).getItem() == ModItems.UPGRADE_CAPACITY) {

				if (this.forcePower > 10000000 + (2000000 * getStackInSlot(0).getCount()))
					setForcePower(10000000 + (2000000 * getStackInSlot(0).getCount()));

				return 10000000 + (2000000 * getStackInSlot(0).getCount());
			}
		}
		if (this.forcePower > 10000000)
			setForcePower(10000000);
		return 10000000;
	}

	private void checkslots(boolean init) {

		if (getStackInSlot(1) != null) {
			if (getStackInSlot(1).getItem() == ModItems.UPGRADE_RANGE) {

				setTransmitRange(8 * (getStackInSlot(1).getCount() + 1));

			}
		} else {
			setTransmitRange(8);
		}

		if (getStackInSlot(2) != null) {
			if (getStackInSlot(2).getItem() instanceof IForceEnergyItems) {
				if (this.getPowerlinkmode() != 3
						&& this.getPowerlinkmode() != 4)
					this.setPowerlinkmode(3);
				IForceEnergyItems ForceEnergyItem = (IForceEnergyItems) getStackInSlot(
						2).getItem();

				switch (this.getPowerlinkmode()) {
				case 3:

					if (ForceEnergyItem.getAvailablePower(getStackInSlot(2)) < ForceEnergyItem
							.getMaximumPower(null)) {
						int maxtransfer = ForceEnergyItem
								.getPowerTransferrate();
						int freeeamount = ForceEnergyItem.getMaximumPower(null)
								- ForceEnergyItem
										.getAvailablePower(getStackInSlot(2));

						if (this.getStorageAvailablePower() > 0) {
							if (this.getStorageAvailablePower() > maxtransfer) {
								if (freeeamount > maxtransfer) {
									ForceEnergyItem
											.setAvailablePower(
													getStackInSlot(2),
													ForceEnergyItem
															.getAvailablePower(getStackInSlot(2))
															+ maxtransfer);
									this.setForcePower(this
											.getStorageAvailablePower()
											- maxtransfer);
								} else {
									ForceEnergyItem
											.setAvailablePower(
													getStackInSlot(2),
													ForceEnergyItem
															.getAvailablePower(getStackInSlot(2))
															+ freeeamount);
									this.setForcePower(this
											.getStorageAvailablePower()
											- freeeamount);
								}
							} else {
								if (freeeamount > this
										.getStorageAvailablePower()) {
									ForceEnergyItem
											.setAvailablePower(
													getStackInSlot(2),
													ForceEnergyItem
															.getAvailablePower(getStackInSlot(2))
															+ this.getStorageAvailablePower());
									this.setForcePower(this
											.getStorageAvailablePower()
											- this.getStorageAvailablePower());
								} else {
									ForceEnergyItem
											.setAvailablePower(
													getStackInSlot(2),
													ForceEnergyItem
															.getAvailablePower(getStackInSlot(2))
															+ freeeamount);
									this.setForcePower(this
											.getStorageAvailablePower()
											- freeeamount);
								}
							}

							getStackInSlot(2).setItemDamage(
									ForceEnergyItem
											.getItemDamage(getStackInSlot(2)));
						}
					}

					break;
				case 4:

					if (ForceEnergyItem.getAvailablePower(getStackInSlot(2)) > 0) {

						int maxtransfer = ForceEnergyItem
								.getPowerTransferrate();
						int freeeamount = this.getStorageMaxPower()
								- this.getStorageAvailablePower();
						int amountleft = ForceEnergyItem
								.getAvailablePower(getStackInSlot(2));

						if (freeeamount >= amountleft) {
							if (amountleft >= maxtransfer) {
								ForceEnergyItem
										.setAvailablePower(
												getStackInSlot(2),
												ForceEnergyItem
														.getAvailablePower(getStackInSlot(2))
														- maxtransfer);
								this.setForcePower(this
										.getStorageAvailablePower()
										+ maxtransfer);
							} else {

								ForceEnergyItem
										.setAvailablePower(
												getStackInSlot(2),
												ForceEnergyItem
														.getAvailablePower(getStackInSlot(2))
														- amountleft);
								this.setForcePower(this
										.getStorageAvailablePower()
										+ amountleft);
							}

						} else {

							ForceEnergyItem
									.setAvailablePower(
											getStackInSlot(2),
											ForceEnergyItem
													.getAvailablePower(getStackInSlot(2))
													- freeeamount);
							this.setForcePower(this.getStorageAvailablePower()
									+ freeeamount);

						}

						getStackInSlot(2).setItemDamage(
								ForceEnergyItem
										.getItemDamage(getStackInSlot(2)));

					}

					break;
				}

			}

			if (getStackInSlot(2).getItem() == ModItems.POWER_CARD) {

				if (this.getPowerlinkmode() != 0
						&& this.getPowerlinkmode() != 1
						&& this.getPowerlinkmode() != 2)
					this.setPowerlinkmode(0);

			}
		}

	}

	@Override
	public void dropPlugins() {
		for (int a = 0; a < this.inventory.length; a++) {
			dropplugins(a, this);
		}
	}

	@Override
	public void invalidate() {
		Linkgrid.getWorldMap(getWorld()).getCapacitor().remove(getDeviceID());
		super.invalidate();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		readExtraNBT(nbttagcompound);
	}

	@Override
	public void readExtraNBT(NBTTagCompound nbttagcompound) {
		super.readExtraNBT(nbttagcompound);
		forcePower = nbttagcompound.getInteger("forcepower");
		Powerlinkmode = nbttagcompound.getInteger("Powerlinkmode");

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
		nbttagcompound.setInteger("forcepower", forcePower);
		nbttagcompound.setInteger("Powerlinkmode", Powerlinkmode);

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
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		writeExtraNBT(nbttagcompound);
		return nbttagcompound;
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound cmp= super.getUpdateTag();
		writeExtraNBT(cmp);
		return cmp;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		readExtraNBT(tag);
	}

	@Override
	public void update() {
		if (getWorld().isRemote == false) {

			if (init) {
				checkslots(true);
			}

			if (getSwitchModi() == 1)
				if (!getSwitchValue() && isRedstoneSignal())
					toggelSwitchValue();

			if (getSwitchModi() == 1)
				if (getSwitchValue() && !isRedstoneSignal())
					toggelSwitchValue();

			if (getSwitchValue()) {
				if (isActive() != true) {
					setActive(true);
				}
			} else {
				if (isActive() != false) {
					setActive(false);
				}
			}

			if (this.getTicker() == 10) {

				if (this.getLinketProjektor() != (short) Linkgrid.getWorldMap(
						getWorld())
						.connectedtoCapacitor(this, getTransmitRange()))
					setLinketprojektor((short) Linkgrid.getWorldMap(getWorld())
							.connectedtoCapacitor(this, getTransmitRange()));

				if (this.getPercentageStorageCapacity() != ((getStorageAvailablePower() / 1000) * 100)
						/ (getStorageMaxPower() / 1000))
					setCapacity(((getStorageAvailablePower() / 1000) * 100)
							/ (getStorageMaxPower() / 1000));

				checkslots(false);
				if (isActive()) {
					powertransfer();
				}
				this.setTicker((short) 0);
			}
			this.setTicker((short) (this.getTicker() + 1));
		}
		super.update();
	}

	private void powertransfer() {
		if (hasPowerSource()) {
			int PowerTransferrate = this.getMaximumPower() / 120;
			int freeStorageAmount = this.getMaximumPower()
					- this.getAvailablePower();
			int balancelevel = this.getStorageAvailablePower()
					- getAvailablePower();

			switch (this.getPowerlinkmode()) {
			case 0:
				if (getPercentageStorageCapacity() >= 95
						&& getPercentageCapacity() != 100) {
					if (freeStorageAmount > PowerTransferrate) {
						emitPower(PowerTransferrate, false);
						consumePowerfromStorage(PowerTransferrate, false);

					} else {
						emitPower(freeStorageAmount, false);
						consumePowerfromStorage(freeStorageAmount, false);
					}
				}
				break;
			case 1:
				if (getPercentageCapacity() < this
						.getPercentageStorageCapacity()) {

					if (balancelevel > PowerTransferrate) {
						emitPower(PowerTransferrate, false);
						consumePowerfromStorage(PowerTransferrate, false);
					} else {
						emitPower(balancelevel, false);
						consumePowerfromStorage(balancelevel, false);
					}
				}
				break;

			case 2:
				if (getStorageAvailablePower() > 0
						&& getPercentageCapacity() != 100) {
					if (getStorageAvailablePower() > PowerTransferrate) {
						if (freeStorageAmount > PowerTransferrate) {
							emitPower(PowerTransferrate, false);
							consumePowerfromStorage(PowerTransferrate, false);
						} else {
							emitPower(freeStorageAmount, false);
							consumePowerfromStorage(freeStorageAmount, false);
						}
					} else {
						if (freeStorageAmount > getStorageAvailablePower()) {
							emitPower(getStorageAvailablePower(), false);
							consumePowerfromStorage(getStorageAvailablePower(),
									false);
						} else {
							emitPower(freeStorageAmount, false);
							consumePowerfromStorage(freeStorageAmount, false);
						}
					}
				}
				break;

			}
		}

	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inventory[i];
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
		if (itemstack != null && itemstack.getCount() > getInventoryStackLimit()) {
			itemstack.setCount(getInventoryStackLimit());
		}
	}

	@Override
	public String getName() {
		return "Generator";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public void onNetworkHandlerEvent(int key, String value) {

		switch (key) {
		case 1:
			if (getStackInSlot(2) != null) {

				if (getStackInSlot(2).getItem() instanceof IForceEnergyItems) {
					if (this.getPowerlinkmode() == 4) {
						this.setPowerlinkmode(3);
					} else {
						this.setPowerlinkmode(4);
					}

					return;
				}
				if (getStackInSlot(2).getItem() == ModItems.POWER_CARD) {

					if (this.getPowerlinkmode() < 2) {
						this.setPowerlinkmode(this.getPowerlinkmode() + 1);
					} else {
						this.setPowerlinkmode(0);
					}

					return;
				}
			}

			if (this.getPowerlinkmode() != 4) {
				this.setPowerlinkmode(this.getPowerlinkmode() + 1);
			} else {
				this.setPowerlinkmode(0);
			}
			break;
		}
		super.onNetworkHandlerEvent(key, value);
	}

	@Override
	public List<String> getFieldsforUpdate() {

		List<String> NetworkedFields = new LinkedList<String>();
		NetworkedFields.clear();

		NetworkedFields.addAll(super.getFieldsforUpdate());

		NetworkedFields.add("linketprojektor");
		NetworkedFields.add("capacity");
		NetworkedFields.add("TransmitRange");

		return NetworkedFields;
	}

	@Override
	public int getfreeStorageAmount() {

		return this.getStorageMaxPower() - this.getStorageAvailablePower();
	}

	@Override
	public boolean insertPowertoStorage(int powerAmount, boolean simulation) {

		if (simulation) {
			if (getStorageAvailablePower() + powerAmount <= getStorageMaxPower())
				return true;
			return false;
		} else {
			setForcePower(getStorageAvailablePower() + powerAmount);
			return true;
		}
	}

	@Override
	public boolean consumePowerfromStorage(int powerAmount, boolean simulation) {

		if (simulation) {
			if (getStorageAvailablePower() >= powerAmount)
				return true;
			return false;
		} else {
			if (getStorageAvailablePower() - powerAmount >= 0) {
				setForcePower(getStorageAvailablePower() - powerAmount);
			} else {
				setForcePower(0);
			}
			return true;
		}
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack, int Slot) {
		switch (Slot) {
		case 0:
			if (par1ItemStack.getItem() instanceof ItemCapacitorUpgradeCapacity)
				return true;
			break;
		case 1:
			if (par1ItemStack.getItem() instanceof ItemCapacitorUpgradeRange)
				return true;
			break;
		case 2:
			if (par1ItemStack.getItem() instanceof IForceEnergyItems
					|| par1ItemStack.getItem() instanceof IPowerLinkItem)
				return true;
			break;
		case 4:
			if (par1ItemStack.getItem() instanceof ItemCardSecurityLink)
				return true;
			break;
		}

		return false;
	}

	@Override
	public int getSlotStackLimit(int Slot) {
		switch (Slot) {
		case 0: // Range upgrade
			return 9;
		case 1: // Cap upgrade
			return 9;
		case 2: // Link/power item
			return 64;
		}
		return 1;
	}

	@Override
	public short getmaxSwitchModi() {
		return 3;
	}

	@Override
	public short getminSwitchModi() {
		return 1;
	}

	@Override
	public ItemStack getPowerLinkStack() {
		return this.getStackInSlot(getPowerlinkSlot());
	}

	@Override
	public int getPowerlinkSlot() {
		return 2;
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
