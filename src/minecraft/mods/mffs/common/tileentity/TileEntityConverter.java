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

import cpw.mods.fml.common.FMLLog;
import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileEvent;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySource;

import mods.mffs.api.IPowerLinkItem;
import mods.mffs.common.Linkgrid;
import mods.mffs.common.ModularForceFieldSystem;
import mods.mffs.common.container.ContainerConverter;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;

public class TileEntityConverter extends TileEntityFEPoweredMachine implements
		IEnergySource {
	private ItemStack inventory[];
	private int IC_Outputpacketsize;
	private int IC_Outputpacketamount;
	private int IC_Output = 0;
	private int UE_Outputvoltage;
	private int UE_Outputamp;
	private int UE_Output = 0;

	private boolean addedToEnergyNet;
	private boolean Industriecraftfound = true;
	private int linkPower;
	private int capacity;

	public TileEntityConverter() {
		inventory = new ItemStack[4];
		capacity = 0;
		IC_Outputpacketsize = 1;
		IC_Outputpacketamount = 1;
		UE_Outputvoltage = 120;
		UE_Outputamp = 10;
		addedToEnergyNet = false;
		linkPower = 0;
	}

	public int getUE_Outputvoltage() {
		return UE_Outputvoltage;
	}

	public void setUE_Outputvoltage(int uE_Outputvoltage) {
		UE_Outputvoltage = uE_Outputvoltage;
	}

	public int getUE_Outputamp() {
		return UE_Outputamp;
	}

	public void setUE_Outputamp(int uE_Outputamp) {
		UE_Outputamp = uE_Outputamp;
	}

	public int getIC_Output() {
		return IC_Output;
	}

	public void setIC_Output(int iC_Output) {
		IC_Output = iC_Output;
	}

	public int getIC_Outputpacketsize() {
		return IC_Outputpacketsize;
	}

	public void setIC_Outputpacketsize(int iC_Outputpacketsize) {
		IC_Outputpacketsize = iC_Outputpacketsize;
	}

	public int getIC_Outputpacketamount() {
		return IC_Outputpacketamount;
	}

	public void setIC_Outputpacketamount(int iC_Outputpacketamount) {
		IC_Outputpacketamount = iC_Outputpacketamount;
	}

	@Override
	public void setSide(int i) {
		super.setSide(i);
		setUEwireConnection();
	}

	public int getLinkPower() {
		return linkPower;
	}

	public void setLinkPower(int linkPower) {
		this.linkPower = linkPower;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int Capacity) {
		this.capacity = Capacity;
	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote == false) {
			if (!addedToEnergyNet && Industriecraftfound) {
				try {
					MinecraftForge.EVENT_BUS
							.post(new EnergyTileLoadEvent(this));
					addedToEnergyNet = true;
				} catch (Exception ex) {
					Industriecraftfound = false;
				}
			}

			if (init) {
				setUEwireConnection();
			}

			if (hasPowerSource()) {
				setLinkPower(getAvailablePower());
			} else {

				setLinkPower(0);
			}

			if (getSwitchModi() == 1)
				if (!getSwitchValue() && isRedstoneSignal())
					toggelSwitchValue();

			if (getSwitchModi() == 1)
				if (getSwitchValue() && !isRedstoneSignal())
					toggelSwitchValue();

			if (getSwitchValue() && hasPowerSource() && !isActive())
				setActive(true);

			if ((!getSwitchValue() || !hasPowerSource()) && isActive())
				setActive(false);

			/*if (isActive())
				if (getIC_Output() == 1)
					EmitICpower(getIC_Outputpacketsize(),
							getIC_Outputpacketamount());*/
		}
		super.updateEntity();
	}

	@Override
	public void dropPlugins() {
		for (int a = 0; a < this.inventory.length; a++) {
			dropplugins(a, this);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		try {
			IC_Outputpacketamount = nbttagcompound
					.getShort("ICOutputpacketamount");
		} catch (Exception e) {
			IC_Outputpacketamount = nbttagcompound
					.getInteger("ICOutputpacketamount");
		}
		IC_Output = nbttagcompound.getInteger("ICOutput");
		IC_Outputpacketsize = nbttagcompound.getInteger("ICOutputpacketsize");
		UE_Output = nbttagcompound.getInteger("UEOutput");
		UE_Outputvoltage = nbttagcompound.getInteger("UEOutputvoltage");
		UE_Outputamp = nbttagcompound.getInteger("UEOutputamp");

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

		nbttagcompound.setInteger("ICOutput", IC_Output);
		nbttagcompound.setInteger("ICOutputpacketsize", IC_Outputpacketsize);
		nbttagcompound
				.setInteger("ICOutputpacketamount", IC_Outputpacketamount);

		nbttagcompound.setInteger("UEOutput", UE_Output);
		nbttagcompound.setInteger("UEOutputvoltage", UE_Outputvoltage);
		nbttagcompound.setInteger("UEOutputamp", UE_Outputamp);

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
	public ItemStack getStackInSlotOnClosing(int var1) {
		return null;
	}

	@Override
	public void onNetworkHandlerUpdate(String field) {
		worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public void onNetworkHandlerEvent(int key, String value) {

		if (key == 100) {
			if (this.getIC_Output() == 0) {
				this.setIC_Output(1);
			} else {
				this.setIC_Output(0);
			}
		}

		if (getIC_Output() == 0) {
			if (key == 200) {
				if (value.equalsIgnoreCase("+")) {
					switch (IC_Outputpacketsize) {
					case 1:
						IC_Outputpacketsize = 32;
						break;
					case 32:
						IC_Outputpacketsize = 128;
						break;
					case 128:
						IC_Outputpacketsize = 512;
						break;
					case 512:
						IC_Outputpacketsize = 2048;
						break;
					case 2048:
						IC_Outputpacketsize = 1;
						break;
					}
				}
				if (value.equalsIgnoreCase("-")) {
					switch (IC_Outputpacketsize) {
					case 1:
						IC_Outputpacketsize = 2048;
						break;
					case 32:
						IC_Outputpacketsize = 1;
						break;
					case 128:
						IC_Outputpacketsize = 32;
						break;
					case 512:
						IC_Outputpacketsize = 128;
						break;
					case 2048:
						IC_Outputpacketsize = 512;
						break;
					}
				}
			}
			if (key == 201) {
				if (value.equalsIgnoreCase("+")) {
					if (IC_Outputpacketamount == 9) {
						IC_Outputpacketamount = 1;
					} else {
						IC_Outputpacketamount++;
					}
				}
				if (value.equalsIgnoreCase("-")) {
					if (IC_Outputpacketamount == 1) {
						IC_Outputpacketamount = 9;
					} else {
						IC_Outputpacketamount--;
					}
				}
			}
		}

		super.onNetworkHandlerEvent(key, value);
	}

	@Override
	public void invalidate() {
		if (addedToEnergyNet) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			addedToEnergyNet = false;
		}
		Linkgrid.getWorldMap(worldObj).getConverter().remove(getDeviceID());
		super.invalidate();
	}

	@Override
	public double getOfferedEnergy() {
		if(!this.isActive() || !(getIC_Output() == 1))
			return 0;

		int amount = getIC_Outputpacketsize();

		if(!consumePower((ModularForceFieldSystem.ExtractorPassForceEnergyGenerate / 4000) * amount, true))
			return 0;

		return amount;
	}

	@Override
	public void drawEnergy(double amount) {
		consumePower((ModularForceFieldSystem.ExtractorPassForceEnergyGenerate / 4000) * (int)amount, false);
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
		return (receiver instanceof IEnergyAcceptor);
	}

	@Override
	public Container getContainer(InventoryPlayer inventoryplayer) {
		return new ContainerConverter(inventoryplayer.player, this);
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack, int Slot) {
		switch (Slot) {
		case 0:
			if (!(par1ItemStack.getItem() instanceof IPowerLinkItem))
				return false;
			break;
		}

		return true;
	}

	@Override
	public int getSlotStackLimit(int Slot) {
		return 1;
	}

	@Override
	public ItemStack getPowerLinkStack() {
		return this.getStackInSlot(getPowerlinkSlot());
	}

	@Override
	public int getPowerlinkSlot() {
		return 0;
	}

	@Override
	public short getmaxSwitchModi() {
		return 3;
	}

	@Override
	public short getminSwitchModi() {
		return 1;
	}

	public void setUEwireConnection() {
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
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

	@Override
	public String getType() { return "MFFSConverter"; }

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return new int[0];
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		return false;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return false;
	}
}
