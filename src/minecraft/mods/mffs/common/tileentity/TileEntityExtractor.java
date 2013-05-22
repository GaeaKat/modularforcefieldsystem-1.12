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
    
    Matchlighter
    Thunderdark 

 */

package mods.mffs.common.tileentity;

import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import mods.mffs.api.IPowerLinkItem;
import mods.mffs.common.Linkgrid;
import mods.mffs.common.ModularForceFieldSystem;
import mods.mffs.common.container.ContainerForceEnergyExtractor;
import mods.mffs.common.item.ItemCapacitorUpgradeCapacity;
import mods.mffs.common.item.ItemExtractorUpgradeBooster;
import mods.mffs.common.item.ItemForcicium;
import mods.mffs.common.item.ItemForcicumCell;
import mods.mffs.network.server.NetworkHandlerServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;

import java.util.LinkedList;
import java.util.List;

public class TileEntityExtractor extends TileEntityFEPoweredMachine implements
		IPowerReceptor, IEnergySink, IPeripheral {
	private ItemStack inventory[];

	private int workmode = 0;

	protected int WorkEnergy;
	protected int MaxWorkEnergy;
	private int ForceEnergybuffer;
	private int MaxForceEnergyBuffer;
	private int WorkCylce;
	private int workTicker;
	private int workdone;
	private int maxworkcylce;
	private int capacity;
	private IPowerProvider powerProvider;
	private boolean addedToEnergyNet;

	public TileEntityExtractor() {
		inventory = new ItemStack[5];
		WorkEnergy = 0;
		MaxWorkEnergy = 4000;
		ForceEnergybuffer = 0;
		MaxForceEnergyBuffer = 1000000;
		WorkCylce = 0;
		workTicker = 20;
		maxworkcylce = 125;
		capacity = 0;
		addedToEnergyNet = false;

		if (ModularForceFieldSystem.buildcraftFound) {
			powerProvider = PowerFramework.currentFramework
					.createPowerProvider();
			powerProvider.configure(10, 2, (int) (getMaxWorkEnergy() / 2.5),
					(int) (getMaxWorkEnergy() / 2.5),
					(int) (getMaxWorkEnergy() / 2.5));
		}

	}

	@Override
	public void setSide(int i) {
		super.setSide(i);
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int Capacity) {
		if (this.capacity != Capacity) {
			this.capacity = Capacity;
			NetworkHandlerServer.updateTileEntityField(this, "capacity");
		}
	}

	public int getMaxworkcylce() {
		return maxworkcylce;
	}

	public void setMaxworkcylce(int maxworkcylce) {
		this.maxworkcylce = maxworkcylce;
	}

	public int getWorkdone() {
		return workdone;
	}

	public void setWorkdone(int workdone) {
		if (this.workdone != workdone) {
			this.workdone = workdone;
			NetworkHandlerServer.updateTileEntityField(this, "workdone");
		}
	}

	public int getWorkTicker() {
		return workTicker;
	}

	public void setWorkTicker(int workTicker) {
		this.workTicker = workTicker;
	}

	public int getMaxForceEnergyBuffer() {
		return MaxForceEnergyBuffer;
	}

	public void setMaxForceEnergyBuffer(int maxForceEnergyBuffer) {
		MaxForceEnergyBuffer = maxForceEnergyBuffer;
	}

	public int getForceEnergybuffer() {
		return ForceEnergybuffer;
	}

	public void setForceEnergybuffer(int forceEnergybuffer) {
		ForceEnergybuffer = forceEnergybuffer;
	}

	public void setWorkCylce(int i) {
		if (this.WorkCylce != i) {
			this.WorkCylce = i;
			NetworkHandlerServer.updateTileEntityField(this, "WorkCylce");
		}
	}

	public int getWorkCylce() {
		return WorkCylce;
	}

	public int getWorkEnergy() {
		return WorkEnergy;
	}

	public void setWorkEnergy(int workEnergy) {
		WorkEnergy = workEnergy;
	}

	public int getMaxWorkEnergy() {
		return MaxWorkEnergy;
	}

	public void setMaxWorkEnergy(int maxWorkEnergy) {
		MaxWorkEnergy = maxWorkEnergy;
	}

	@Override
	public void dropPlugins() {
		for (int a = 0; a < this.inventory.length; a++) {
			dropplugins(a, this);
		}
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this) {
			return false;
		} else {
			return entityplayer.getDistance(xCoord + 0.5D, yCoord + 0.5D,
					zCoord + 0.5D) <= 64D;
		}
	}

	public void checkslots(boolean init) {

		if (getStackInSlot(2) != null) {
			if (getStackInSlot(2).getItem() == ModularForceFieldSystem.MFFSitemupgradecapcap) {
				setMaxForceEnergyBuffer(1000000 + (getStackInSlot(2).stackSize * 100000));
			} else {
				setMaxForceEnergyBuffer(1000000);
			}
		} else {
			setMaxForceEnergyBuffer(1000000);
		}

		if (getStackInSlot(3) != null) {
			if (getStackInSlot(3).getItem() == ModularForceFieldSystem.MFFSitemupgradeexctractorboost) {
				setWorkTicker(20 - getStackInSlot(3).stackSize);
			} else {
				setWorkTicker(20);
			}
		} else {
			setWorkTicker(20);
		}

		if (getStackInSlot(4) != null) {
			if (getStackInSlot(4).getItem() == ModularForceFieldSystem.MFFSitemForcicumCell) {
				workmode = 1;
				setMaxWorkEnergy(200000);
			}
		} else {
			workmode = 0;
			setMaxWorkEnergy(4000);
		}
	}

	private boolean hasPowertoConvert() {
		if (WorkEnergy >= MaxWorkEnergy - 1) {
			setWorkEnergy(0);
			return true;
		}
		return false;
	}

	private boolean hasfreeForceEnergyStorage() {
		if (this.MaxForceEnergyBuffer > this.ForceEnergybuffer)
			return true;
		return false;
	}

	private boolean hasStufftoConvert() {
		if (WorkCylce > 0) {
			return true;
		} else {
			if (ModularForceFieldSystem.adventureMapMode) {
				setMaxworkcylce(ModularForceFieldSystem.ForciciumCellWorkCycle);
				setWorkCylce(getMaxworkcylce());
				return true;
			}

			if (getStackInSlot(0) != null) {
				if (getStackInSlot(0).getItem() == ModularForceFieldSystem.MFFSitemForcicium) {
					setMaxworkcylce(ModularForceFieldSystem.ForciciumWorkCycle);
					setWorkCylce(getMaxworkcylce());
					decrStackSize(0, 1);
					return true;
				}

				if (getStackInSlot(0).getItem() == ModularForceFieldSystem.MFFSitemForcicumCell) {
					if (((ItemForcicumCell) getStackInSlot(0).getItem())
							.useForcecium(1, getStackInSlot(0))) {
						setMaxworkcylce(ModularForceFieldSystem.ForciciumCellWorkCycle);
						setWorkCylce(getMaxworkcylce());
						return true;
					}
				}
			}
		}

		return false;
	}

	public void transferForceEnergy() {
		if (this.getForceEnergybuffer() > 0) {
			if (this.hasPowerSource()) {
				int PowerTransferrate = this.getMaximumPower() / 120;
				int freeAmount = this.getMaximumPower()
						- this.getAvailablePower();

				if (this.getForceEnergybuffer() > freeAmount) {
					if (freeAmount > PowerTransferrate) {
						emitPower(PowerTransferrate, false);
						this.setForceEnergybuffer(this.getForceEnergybuffer()
								- PowerTransferrate);

					} else {
						emitPower(freeAmount, false);
						this.setForceEnergybuffer(this.getForceEnergybuffer()
								- freeAmount);
					}
				} else {
					if (freeAmount > this.getForceEnergybuffer()) {
						emitPower(getForceEnergybuffer(), false);
						this.setForceEnergybuffer(this.getForceEnergybuffer()
								- getForceEnergybuffer());
					} else {
						emitPower(freeAmount, false);
						this.setForceEnergybuffer(this.getForceEnergybuffer()
								- freeAmount);
					}
				}
			}
		}

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
	public void updateEntity() {
		if (worldObj.isRemote == false) {

			if (init) {
				checkslots(true);
			}

			if (!addedToEnergyNet && ModularForceFieldSystem.ic2Found) {
				MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
				addedToEnergyNet = true;
			}

			if (getSwitchModi() == 1)
				if (!getSwitchValue() && isRedstoneSignal())
					toggelSwitchValue();

			if (getSwitchModi() == 1)
				if (getSwitchValue() && !isRedstoneSignal())
					toggelSwitchValue();

			if (!isActive() && getSwitchValue())
				setActive(true);

			if (isActive() && !getSwitchValue())
				setActive(false);

			if (isActive()) {
				if (ModularForceFieldSystem.buildcraftFound)
					converMJtoWorkEnergy();
			}

			if (this.getTicker() >= getWorkTicker()) {
				checkslots(false);

				if (workmode == 0 && isActive()) {

					if (this.getWorkdone() != getWorkEnergy() * 100
							/ getMaxWorkEnergy())
						setWorkdone(getWorkEnergy() * 100 / getMaxWorkEnergy());

					if (getWorkdone() > 100) {
						setWorkdone(100);
					}

					if (this.getCapacity() != (getForceEnergybuffer() * 100)
							/ getMaxForceEnergyBuffer())
						setCapacity((getForceEnergybuffer() * 100)
								/ getMaxForceEnergyBuffer());

					if (this.hasfreeForceEnergyStorage()
							&& this.hasStufftoConvert()) {

						if (this.hasPowertoConvert()) {
							setWorkCylce(getWorkCylce() - 1);
							setForceEnergybuffer(getForceEnergybuffer()
									+ ModularForceFieldSystem.ExtractorPassForceEnergyGenerate);
						}
					}

					transferForceEnergy();

					this.setTicker((short) 0);
				}

				if (workmode == 1 && isActive()) {
					if (this.getWorkdone() != getWorkEnergy() * 100
							/ getMaxWorkEnergy())
						setWorkdone(getWorkEnergy() * 100 / getMaxWorkEnergy());

					if (((ItemForcicumCell) getStackInSlot(4).getItem())
							.getForceciumlevel(getStackInSlot(4)) < ((ItemForcicumCell) getStackInSlot(
							4).getItem()).getMaxForceciumlevel()) {

						if (this.hasPowertoConvert() && isActive()) {
							((ItemForcicumCell) getStackInSlot(4).getItem())
									.setForceciumlevel(
											getStackInSlot(4),
											((ItemForcicumCell) getStackInSlot(
													4).getItem())
													.getForceciumlevel(getStackInSlot(4)) + 1);
						}
					}

					this.setTicker((short) 0);
				}
			}

			this.setTicker((short) (this.getTicker() + 1));
		}
		super.updateEntity();
	}

	@Override
	public Container getContainer(InventoryPlayer inventoryplayer) {
		return new ContainerForceEnergyExtractor(inventoryplayer.player, this);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		ForceEnergybuffer = nbttagcompound.getInteger("ForceEnergybuffer");
		WorkEnergy = nbttagcompound.getInteger("WorkEnergy");
		WorkCylce = nbttagcompound.getInteger("WorkCylce");

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

		nbttagcompound.setInteger("WorkCylce", WorkCylce);
		nbttagcompound.setInteger("WorkEnergy", WorkEnergy);
		nbttagcompound.setInteger("ForceEnergybuffer", ForceEnergybuffer);

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
	public ItemStack getStackInSlot(int i) {
		return inventory[i];
	}

	@Override
	public String getInvName() {
		return "Extractor";
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		inventory[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
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
	public int getStartInventorySide(ForgeDirection side) {
		return 0;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		return 1;
	}

	@Override
	public List<String> getFieldsforUpdate() {
		List<String> NetworkedFields = new LinkedList<String>();
		NetworkedFields.clear();

		NetworkedFields.addAll(super.getFieldsforUpdate());
		NetworkedFields.add("capacity");
		NetworkedFields.add("WorkCylce");
		NetworkedFields.add("WorkEnergy");
		NetworkedFields.add("workdone");

		return NetworkedFields;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack, int Slot) {
		switch (Slot) {
		case 0:
			if ((par1ItemStack.getItem() instanceof ItemForcicium || par1ItemStack
					.getItem() instanceof ItemForcicumCell)
					&& getStackInSlot(4) == null)
				return true;
			break;

		case 1:
			if (par1ItemStack.getItem() instanceof IPowerLinkItem)
				return true;
			break;

		case 2:
			if (par1ItemStack.getItem() instanceof ItemCapacitorUpgradeCapacity)
				return true;
			break;

		case 3:
			if (par1ItemStack.getItem() instanceof ItemExtractorUpgradeBooster)
				return true;
			break;

		case 4:
			if (par1ItemStack.getItem() instanceof ItemForcicumCell
					&& getStackInSlot(0) == null)
				return true;
			break;
		}
		return false;
	}

	@Override
	public int getSlotStackLimit(int Slot) {
		switch (Slot) {
		case 0: // Forcicium
			return 64;
		case 1: // Powerlink
			return 1;
		case 2: // Cap upgrade
			return 9;
		case 3: // Boost upgrade
			return 19;
		case 4: // Forcicium cell
			return 1;
		}
		return 1;
	}

	@Override
	public int demandsEnergy() {
		if (!this.isActive())
			return 0;
		return getMaxWorkEnergy() - getWorkEnergy();
	}

	@Override
	public int injectEnergy(Direction directionFrom, int amount) {

		int freespace = getMaxWorkEnergy() - getWorkEnergy();

		if (freespace >= amount) {
			setWorkEnergy(getWorkEnergy() + amount);
			return 0;
		} else {

			setWorkEnergy(getMaxWorkEnergy());
			return amount - freespace;
		}
	}

	@Override
	public void invalidate() {
		if (addedToEnergyNet) {

			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			addedToEnergyNet = false;
		}

		Linkgrid.getWorldMap(worldObj).getExtractor().remove(getDeviceID());

		super.invalidate();
	}

	@Override
	public boolean isAddedToEnergyNet() {
		return addedToEnergyNet;
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity tileentity, Direction direction) {
		return true;
	}

	public void converMJtoWorkEnergy() {
		if (this.getWorkEnergy() < this.getMaxWorkEnergy()) {
			float use = powerProvider
					.useEnergy(1, (float) (this.getMaxWorkEnergy() - this
							.getWorkEnergy() / 2.5), true);

			if (getWorkEnergy() + (use * 2.5) > getMaxWorkEnergy()) {
				setWorkEnergy(getMaxWorkEnergy());
			} else {
				setWorkEnergy((int) (getWorkEnergy() + (use * 2.5)));
			}
		}
	}

	@Override
	public void setPowerProvider(IPowerProvider provider) {
		this.powerProvider = provider;
	}

	@Override
	public IPowerProvider getPowerProvider() {
		return powerProvider;
	}

	@Override
	public void doWork() {
	}

	@Override
	public int powerRequest(ForgeDirection from) {
		double workEnergyinMJ = getWorkEnergy() / 2.5;
		double MaxWorkEnergyinMj = getMaxWorkEnergy() / 2.5;

		return (int) Math.round(MaxWorkEnergyinMj - workEnergyinMJ);
	}

	@Override
	public ItemStack getPowerLinkStack() {
		return this.getStackInSlot(getPowerlinkSlot());
	}

	@Override
	public int getPowerlinkSlot() {
		return 1;
	}

	@Override
	public int getMaxSafeInput() {
		return 2048;
	}

	@Override
	public TileEntityAdvSecurityStation getLinkedSecurityStation() {

		TileEntityCapacitor cap = Linkgrid.getWorldMap(worldObj).getCapacitor()
				.get(getPowerSourceID());
		if (cap != null) {
			TileEntityAdvSecurityStation sec = cap.getLinkedSecurityStation();
			if (sec != null) {
				return sec;
			}

		}
		return null;
	}

	@Override
	public boolean canAttachToSide(int side) {
		return true;
	}

	@Override
	public void attach(IComputerAccess computer) {
	}

	@Override
	public void detach(IComputerAccess computer) {
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
	public String getType() { return "MFFSExtractor"; }
}
