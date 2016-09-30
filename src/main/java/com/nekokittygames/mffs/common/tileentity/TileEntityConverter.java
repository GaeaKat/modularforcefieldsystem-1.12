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



import cofh.api.energy.IEnergyProvider;
import com.nekokittygames.mffs.api.IPowerLinkItem;
import com.nekokittygames.mffs.common.Linkgrid;
import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.container.ContainerConverter;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;

public class TileEntityConverter extends TileEntityFEPoweredMachine implements
		IEnergyProvider {
	private ItemStack inventory[];
	private int RF_Output=0;

	private int linkPower;
	private int capacity;

	public TileEntityConverter() {
		inventory = new ItemStack[4];
		capacity = 0;
		RF_Output = 120;
		linkPower = 0;
	}


	public int getRF_Output() {
		return RF_Output;
	}

	public void setRF_Output(int RF_Output) {
		this.RF_Output = RF_Output;
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
	public void update() {
		if (worldObj.isRemote == false) {


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

		}
		super.update();
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

		RF_Output = nbttagcompound.getInteger("RFOutput");


		NBTTagList nbttaglist = nbttagcompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		inventory = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist
					.getCompoundTagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if (byte0 >= 0 && byte0 < inventory.length) {
				inventory[byte0] = ItemStack
						.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setInteger("RFOutput", RF_Output);


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
		return nbttagcompound;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inventory[i];
	}

	@Override
	public String getName() {
		return "Converter";
	}

	@Override
	public boolean hasCustomName() {
		return false;
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
	public void onNetworkHandlerUpdate(String field) {
		worldObj.markBlockRangeForRenderUpdate(pos,pos);
	}

	@Override
	public void onNetworkHandlerEvent(int key, String value) {
			if (key == 400) {
				if (value.equalsIgnoreCase("+")) {
					switch (RF_Output) {
					case 1:
						RF_Output= 32;
						break;
					case 32:
						RF_Output= 128;
						break;
					case 128:
						RF_Output= 512;
						break;
					case 512:
						RF_Output= 2048;
						break;
					case 2048:
						RF_Output= 1;
						break;
					}
				}
				if (value.equalsIgnoreCase("-")) {
					switch (RF_Output) {
					case 1:
						RF_Output = 2048;
						break;
					case 32:
						RF_Output= 1;
						break;
					case 128:
						RF_Output= 32;
						break;
					case 512:
						RF_Output= 128;
						break;
					case 2048:
						RF_Output= 512;
						break;
					}
				}
			}
		super.onNetworkHandlerEvent(key, value);
	}

	@Override
	public void invalidate() {
		Linkgrid.getWorldMap(worldObj).getConverter().remove(getDeviceID());
		super.invalidate();
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}

	@Override
	public int getEnergyStored(EnumFacing from) {
		int rfunits=this.getAvailablePower();
		int cc=(ModularForceFieldSystem.ExtractorPassForceEnergyGenerate / 4000);
		return rfunits/cc;
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return this.getMaximumPower();
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		maxExtract=Math.max(maxExtract,getRF_Output());
		if(!this.consumePower((ModularForceFieldSystem.ExtractorPassForceEnergyGenerate / 4000) * (int)maxExtract,simulate))
			return 0;
		return maxExtract;
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
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
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

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack item=inventory[index];
		inventory[index]=null;
		this.markDirty();
		return item;
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
}
