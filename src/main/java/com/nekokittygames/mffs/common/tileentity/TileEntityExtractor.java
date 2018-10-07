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

package com.nekokittygames.mffs.common.tileentity;



import cofh.redstoneflux.api.IEnergyReceiver;
import com.nekokittygames.mffs.api.IPowerLinkItem;
import com.nekokittygames.mffs.common.Linkgrid;
import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.compat.TeslaCap;
import com.nekokittygames.mffs.common.container.ContainerForceEnergyExtractor;
import com.nekokittygames.mffs.common.item.*;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

import java.util.LinkedList;
import java.util.List;

@Optional.InterfaceList({@Optional.Interface(modid = "ic2",iface = "ic2.api.energy.tile.IEnergySink"),
							@Optional.Interface(modid = "ic2",iface = "ic2.api.energy.tile.IEnergyEmitter")})
public class TileEntityExtractor extends TileEntityFEPoweredMachine implements
		IEnergySink, IEnergyReceiver {
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
	//private IPowerEmitter powerEmitter;
	private boolean addedToEnergyNet;

	private TeslaCap cap;

	public TileEntityExtractor() {
		super(5);
		WorkEnergy = 0;
		MaxWorkEnergy = 4000;
		ForceEnergybuffer = 0;
		MaxForceEnergyBuffer = 1000000;
		WorkCylce = 0;
		workTicker = 20;
		maxworkcylce = 125;
		capacity = 0;
		addedToEnergyNet = false;
		if(Loader.isModLoaded("tesla"))
			cap=new TeslaCap(this);


	}



	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int Capacity) {
		if (this.capacity != Capacity) {
			this.capacity = Capacity;
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
		this.markDirty();
		getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 2);
		getWorld().markBlockRangeForRenderUpdate(pos,pos);
		//todo: make more packet friendly
	}

	public int getForceEnergybuffer() {
		return ForceEnergybuffer;
	}

	public void setForceEnergybuffer(int forceEnergybuffer) {
		ForceEnergybuffer = forceEnergybuffer;
		this.markDirty();
		getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 2);
		getWorld().markBlockRangeForRenderUpdate(pos,pos);
	}

	public void setWorkCylce(int i) {
		if (this.WorkCylce != i) {
			this.WorkCylce = i;
		}
		this.markDirty();
		getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 2);
		getWorld().markBlockRangeForRenderUpdate(pos,pos);
	}

	public int getWorkCylce() {
		return WorkCylce;
	}

	public int getWorkEnergy() {
		return WorkEnergy;
	}

	public void setWorkEnergy(int workEnergy) {
		WorkEnergy = workEnergy;
		this.markDirty();
		getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 2);
		getWorld().markBlockRangeForRenderUpdate(pos,pos);
	}

	public int getMaxWorkEnergy() {
		return MaxWorkEnergy;
	}

	public void setMaxWorkEnergy(int maxWorkEnergy) {
		MaxWorkEnergy = maxWorkEnergy;
		this.markDirty();
		getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 2);
		getWorld().markBlockRangeForRenderUpdate(pos,pos);
	}

	@Override
	public void dropPlugins() {
		for (int a = 0; a < this.inventory.size(); a++) {
			dropPlugins(a);
		}
	}



	public void checkslots(boolean init) {

		if (!getStackInSlot(2).isEmpty()) {
			if (getStackInSlot(2).getItem() == ModItems.UPGRADE_CAPACITY) {
				setMaxForceEnergyBuffer(1000000 + (getStackInSlot(2).getCount() * 100000));
			} else {
				setMaxForceEnergyBuffer(1000000);
			}
		} else {
			setMaxForceEnergyBuffer(1000000);
		}

		if (!getStackInSlot(3).isEmpty()) {
			if (getStackInSlot(3).getItem() == ModItems.EXTRACTOR_UPGRADE_BOOSTER) {
				setWorkTicker(20 - getStackInSlot(3).getCount());
			} else {
				setWorkTicker(20);
			}
		} else {
			setWorkTicker(20);
		}

		if (!getStackInSlot(4).isEmpty()) {
			if (getStackInSlot(4).getItem() == ModItems.FORCICIUM_CELL) {
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

			if (!getStackInSlot(0).isEmpty()) {
				if (getStackInSlot(0).getItem() == ModItems.FORCICIUM) {
					setMaxworkcylce(ModularForceFieldSystem.ForciciumWorkCycle);
					setWorkCylce(getMaxworkcylce());
					decrStackSize(0, 1);
					return true;
				}

				if (getStackInSlot(0).getItem() == ModItems.FORCICIUM_CELL) {
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
	public void update() {
		if (getWorld().isRemote == false) {

			if (init) {
				checkslots(true);
				if(addedToEnergyNet==false)
				{
					if (ModularForceFieldSystem.ic2Found)
						AddToIC2EnergyNet();
					else
						addedToEnergyNet=true;
				}

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
		super.update();
	}

	@Optional.Method(modid = "ic2")
	private void AddToIC2EnergyNet() {
		if (!getWorld().isRemote) {
			EnergyTileLoadEvent event = new EnergyTileLoadEvent(this);
			MinecraftForge.EVENT_BUS.post(event);
			addedToEnergyNet=true;
		}
	}
	
	@Optional.Method(modid = "ic2")
	private void RemoveFromIC2EnergyNet() {
		if (!getWorld().isRemote) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			addedToEnergyNet = false;
		}
	}

	@Override
	public Container getContainer(InventoryPlayer inventoryplayer) {
		return new ContainerForceEnergyExtractor(inventoryplayer.player, this);
	}

	@Override
	public void readExtraNBT(NBTTagCompound nbttagcompound) {
		super.readExtraNBT(nbttagcompound);
		ForceEnergybuffer = nbttagcompound.getInteger("ForceEnergybuffer");
		WorkEnergy = nbttagcompound.getInteger("WorkEnergy");
		WorkCylce = nbttagcompound.getInteger("WorkCylce");
	}

	@Override
	public void writeExtraNBT(NBTTagCompound nbttagcompound) {
		super.writeExtraNBT(nbttagcompound);
		nbttagcompound.setInteger("WorkCylce", WorkCylce);
		nbttagcompound.setInteger("WorkEnergy", WorkEnergy);
		nbttagcompound.setInteger("ForceEnergybuffer", ForceEnergybuffer);
	}

	@Override
	public String getName() {
		return "Extractor";
	}

	@Override
	public boolean hasCustomName() {
		return false;
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
					&& getStackInSlot(4).isEmpty())
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
					&& getStackInSlot(0).isEmpty())
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
	public void invalidate() {
		if (addedToEnergyNet && ModularForceFieldSystem.ic2Found) {
			RemoveFromIC2EnergyNet();
		}
		Linkgrid.getWorldMap(getWorld()).getExtractor().remove(getDeviceID());

		super.invalidate();
	}
	
	@Override
	public void onChunkUnload() {
		if (addedToEnergyNet && ModularForceFieldSystem.ic2Found) {
			RemoveFromIC2EnergyNet();
		}
		
		super.onChunkUnload();
	}

	public void converMJtoWorkEnergy() {
		/*if (this.getWorkEnergy() < this.getMaxWorkEnergy()) {
			float use = powerProvider
					.useEnergy(1, (float) (this.getMaxWorkEnergy() - this
							.getWorkEnergy() / 2.5), true);

			if (getWorkEnergy() + (use * 2.5) > getMaxWorkEnergy()) {
				setWorkEnergy(getMaxWorkEnergy());
			} else {
				setWorkEnergy((int) (getWorkEnergy() + (use * 2.5)));
			}
		}*/
	}

	/*@Override
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
	}*/

	@Override
	public ItemStack getPowerLinkStack() {
		return this.getStackInSlot(getPowerlinkSlot());
	}

	@Override
	public int getPowerlinkSlot() {
		return 1;
	}

	@Override
	public TileEntityAdvSecurityStation getLinkedSecurityStation() {

		TileEntityCapacitor cap = Linkgrid.getWorldMap(getWorld()).getCapacitor()
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
	@Optional.Method(modid = "redstoneflux")
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}

	@Override
	@Optional.Method(modid = "redstoneflux")
	public int getEnergyStored(EnumFacing from) {
		return 0;
	}

	@Override
	@Optional.Method(modid = "redstoneflux")
	public int getMaxEnergyStored(EnumFacing from) {
		return 9999;
	}

	@Override
	@Optional.Method(modid = "redstoneflux")
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {

		double freeSpace = (double)(getMaxWorkEnergy() - getWorkEnergy());
		if(freeSpace==0)
		{

		}
		if(freeSpace >= maxReceive) {
			if(!simulate)
				setWorkEnergy(getWorkEnergy() + (int)maxReceive);
			return maxReceive;
		}
		else {
			if(!simulate)
				setWorkEnergy(getMaxWorkEnergy());
			return (int)(freeSpace);
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(Loader.isModLoaded("tesla"))
			if (hasTeslaCapability(capability))
				return true;
		return super.hasCapability(capability, facing);
	}

	@Optional.Method(modid = "tesla")
	private boolean hasTeslaCapability(Capability<?> capability) {
		if(capability== TeslaCapabilities.CAPABILITY_CONSUMER)
			return true;
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(Loader.isModLoaded("tesla"))
			if (hasTeslaCapability(capability))
				return TeslaCapabilities.CAPABILITY_CONSUMER.cast(cap);
		return super.getCapability(capability, facing);
	}


	@Override
	@Optional.Method(modid = "ic2")
	public double getDemandedEnergy() {

		if(!this.isActive())
			return 0;
		return (double)(getMaxWorkEnergy() - getWorkEnergy());
	}

	@Override
	@Optional.Method(modid = "ic2")
	public int getSinkTier() {
		return 3;
	}

	@Override
	@Optional.Method(modid = "ic2")
	public double injectEnergy(EnumFacing enumFacing, double v, double v1) {
		double freeSpace = (double) (getMaxWorkEnergy() - getWorkEnergy());
		if(getDemandedEnergy()<=0)
			return v;
		if (freeSpace >= v) {
			setWorkEnergy(getWorkEnergy() + (int) v);
			return 0;
		} else {
			setWorkEnergy(getMaxWorkEnergy());
			return (int) (v-freeSpace);
		}

	}

	@Override
	@Optional.Method(modid = "ic2")
	public boolean acceptsEnergyFrom(IEnergyEmitter iEnergyEmitter, EnumFacing enumFacing) {
		return true;
	}
}
