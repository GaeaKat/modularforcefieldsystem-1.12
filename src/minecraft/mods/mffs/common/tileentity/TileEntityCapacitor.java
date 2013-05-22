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

package mods.mffs.common.tileentity;

import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import mods.mffs.api.IForceEnergyItems;
import mods.mffs.api.IForceEnergyStorageBlock;
import mods.mffs.api.IPowerLinkItem;
import mods.mffs.common.Linkgrid;
import mods.mffs.common.ModularForceFieldSystem;
import mods.mffs.common.container.ContainerCapacitor;
import mods.mffs.common.item.ItemCapacitorUpgradeCapacity;
import mods.mffs.common.item.ItemCapacitorUpgradeRange;
import mods.mffs.common.item.ItemCardSecurityLink;
import mods.mffs.network.INetworkHandlerEventListener;
import mods.mffs.network.server.NetworkHandlerServer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;

import java.util.LinkedList;
import java.util.List;

public class TileEntityCapacitor extends TileEntityFEPoweredMachine implements
		INetworkHandlerEventListener, IForceEnergyStorageBlock, IPeripheral {
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
		NetworkHandlerServer.updateTileEntityField(this, "TransmitRange");
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
			NetworkHandlerServer.updateTileEntityField(this, "capacity");
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
			NetworkHandlerServer.updateTileEntityField(this, "linketprojektor");
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
		return ItemCardSecurityLink.getLinkedSecurityStation(this, 4, worldObj);
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
			if (getStackInSlot(0).getItem() == ModularForceFieldSystem.MFFSitemupgradecapcap) {

				if (this.forcePower > 10000000 + (2000000 * getStackInSlot(0).stackSize))
					setForcePower(10000000 + (2000000 * getStackInSlot(0).stackSize));

				return 10000000 + (2000000 * getStackInSlot(0).stackSize);
			}
		}
		if (this.forcePower > 10000000)
			setForcePower(10000000);
		return 10000000;
	}

	private void checkslots(boolean init) {

		if (getStackInSlot(1) != null) {
			if (getStackInSlot(1).getItem() == ModularForceFieldSystem.MFFSitemupgradecaprange) {

				setTransmitRange(8 * (getStackInSlot(1).stackSize + 1));

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

			if (getStackInSlot(2).getItem() == ModularForceFieldSystem.MFFSitemfc) {

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
		Linkgrid.getWorldMap(worldObj).getCapacitor().remove(getDeviceID());
		super.invalidate();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		forcePower = nbttagcompound.getInteger("forcepower");
		Powerlinkmode = nbttagcompound.getInteger("Powerlinkmode");

		NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
		inventory = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist
					.tagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if (byte0 >= 0 && byte0 < inventory.length) {
				inventory[byte0] = ItemStack
						.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

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
	public void updateEntity() {
		if (worldObj.isRemote == false) {

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
						worldObj)
						.connectedtoCapacitor(this, getTransmitRange()))
					setLinketprojektor((short) Linkgrid.getWorldMap(worldObj)
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
		super.updateEntity();
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
			if (inventory[i].stackSize <= j) {
				ItemStack itemstack = inventory[i];
				inventory[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = inventory[i].splitStack(j);
			if (inventory[i].stackSize == 0) {
				inventory[i] = null;
			}
			return itemstack1;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		inventory[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getInvName() {
		return "Generator";
	}

	@Override
	public int getStartInventorySide(ForgeDirection side) {
		return 0;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		return 0;
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
				if (getStackInSlot(2).getItem() == ModularForceFieldSystem.MFFSitemfc) {

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
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

	@Override
	public String getType() { return "MFFSCapacitor"; }

	@Override
	public String[] getMethodNames() {
		return new String[] {
			"isActive", "setActive",

			"getTransmitRange", "getStoredForceEnergy", "chargeFromForceEnergy",
				"setPowerLinkMode"
		};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch(method) {
			case 2: // getTransmitRange
				return new Object[] { this.getTransmitRange() };

			case 3: // getStoredForceEnergy
				return new Object[] { this.getStorageAvailablePower(), this.getfreeStorageAmount() };

			case 4: // chargeFromForceEnergy
				return new Object[] { false };

			case 5: // setPowerLinkMode
				if(arguments.length < 1)
					return new Object[] { false };

				int mode = (Integer)arguments[0];

				if(mode < 0 || mode > 4)
					return new Object[] { false };

				setPowerlinkmode(mode);

				return new Object[] { true };

			default:
				return super.callMethod(computer, method, arguments);
		}
	}
}
