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

import mods.mffs.common.Linkgrid;
import mods.mffs.common.container.ContainerSecStorage;
import mods.mffs.common.item.ItemCardSecurityLink;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileEntitySecStorage extends TileEntityMachines implements
		ISidedInventory, IInventory {

	private ItemStack inventory[];

	public TileEntitySecStorage() {
		inventory = new ItemStack[60];
	}

	@Override
	public void dropPlugins() {
		for (int a = 0; a < this.inventory.length; a++) {
			dropplugins(a, this);
		}
	}

	@Override
	public TileEntityAdvSecurityStation getLinkedSecurityStation() {
		return ItemCardSecurityLink.getLinkedSecurityStation(this, 0, worldObj);
	}

	@Override
	public void invalidate() {
		Linkgrid.getWorldMap(worldObj).getSecStorage().remove(getDeviceID());
		super.invalidate();
	}

	public int getSecStation_ID() {
		TileEntityAdvSecurityStation sec = getLinkedSecurityStation();
		if (sec != null)
			return sec.getDeviceID();
		return 0;
	}

	@Override
	public short getmaxSwitchModi() {
		return 3;
	}

	@Override
	public short getminSwitchModi() {
		return 2;
	}

	public int getfreeslotcount() {
		int count = 0;

		for (int a = 1; a < this.inventory.length; a++) {
			if (getStackInSlot(a) == null)
				count++;
		}

		return count;
	}

	@Override
	public void updateEntity() {

		if (!worldObj.isRemote) {
			if (getLinkedSecurityStation() != null && !isActive()
					&& getSwitchValue())
				setActive(true);
			if ((getLinkedSecurityStation() == null || !getSwitchValue())
					&& isActive())
				setActive(false);
		}
		super.updateEntity();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
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
		return "SecStation";
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
		this.inventory[par1] = par2ItemStack;

		if (par2ItemStack != null
				&& par2ItemStack.stackSize > this.getInventoryStackLimit()) {
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}

		this.onInventoryChanged();
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
		if (isActive())
			return 0;
		return 1;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		if (isActive())
			return 0;
		return 54;
	}

	@Override
	public Container getContainer(InventoryPlayer inventoryplayer) {
		return new ContainerSecStorage(inventoryplayer.player, this);
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack, int Slot) {
		switch (Slot) {
		case 0:
			if (!(par1ItemStack.getItem() instanceof ItemCardSecurityLink))
				return false;
			break;
		}

		return true;
	}

	@Override
	public int getSlotStackLimit(int slt) {
		if (slt == 0)
			return 1;
		return 64;
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
	public String getType() { return "MFFSSecuredStorage"; }
}
