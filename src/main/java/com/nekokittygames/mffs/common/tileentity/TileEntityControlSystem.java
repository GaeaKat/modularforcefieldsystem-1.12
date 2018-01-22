package com.nekokittygames.mffs.common.tileentity;

import com.nekokittygames.mffs.api.PointXYZ;
import com.nekokittygames.mffs.common.Linkgrid;
import com.nekokittygames.mffs.common.MFFSMaschines;
import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.NBTTagCompoundHelper;
import com.nekokittygames.mffs.common.container.ContainerControlSystem;
import com.nekokittygames.mffs.common.item.ItemCardDataLink;
import com.nekokittygames.mffs.common.item.ItemCardSecurityLink;
import com.nekokittygames.mffs.common.item.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;

import java.util.LinkedList;
import java.util.List;

public class TileEntityControlSystem extends TileEntityMachines implements
		ISidedInventory {

	public static int MACHINE_RANGE = 192;

	// Var. for RemoteInfo <Maschine>

	private TileEntityMachines remote = null;
	protected String RemoteDeviceName = "";
	protected String RemoteDeviceTyp = "";
	protected boolean RemoteActive = false;
	protected boolean RemoteSwitchValue = false;
	protected short RemoteSwitchModi = 0;
	protected boolean RemoteSecurityStationlink = false;
	protected boolean RemotehasPowersource = false;
	protected boolean RemoteGUIinRange = false;
	protected int RemotePowerleft = 0;
	// ------------------------------
	private NonNullList<ItemStack> inventory=NonNullList.withSize(40,ItemStack.EMPTY);

	public TileEntityControlSystem() {

	}

	@Override
	public List<String> getFieldsforUpdate() {
		List<String> NetworkedFields = new LinkedList<String>();
		NetworkedFields.clear();

		NetworkedFields.addAll(super.getFieldsforUpdate());
		NetworkedFields.add("RemoteDeviceName");
		NetworkedFields.add("RemoteDeviceTyp");
		NetworkedFields.add("RemoteActive");
		NetworkedFields.add("RemoteSwitchModi");
		NetworkedFields.add("RemoteSwitchValue");
		NetworkedFields.add("RemoteSecurityStationlink");
		NetworkedFields.add("RemotehasPowersource");
		NetworkedFields.add("RemotePowerleft");
		NetworkedFields.add("RemoteGUIinRange");

		return NetworkedFields;
	}

	@Override
	public void invalidate() {
		Linkgrid.getWorldMap(world).getControlSystem().remove(getDeviceID());
		super.invalidate();
	}

	@Override
	public void dropPlugins() {
		for (int a = 0; a < this.inventory.size(); a++) {
			dropplugins(a, this);
		}
	}

	@Override
	public void update() {
		if (!world.isRemote) {

			if (this.getTicker() == 20) {

				if (getLinkedSecurityStation() != null && !isActive())
					setActive(true);
				if (getLinkedSecurityStation() == null && isActive())
					setActive(false);

				refreshRemoteData();

				this.setTicker((short) 0);
			}
			this.setTicker((short) (this.getTicker() + 1));

		}
		super.update();
	}

	public TileEntityMachines getRemote() {
		return remote;
	}

	@Override
	public Container getContainer(InventoryPlayer inventoryplayer) {
		return new ContainerControlSystem(inventoryplayer.player, this);
	}

	@Override
	public TileEntityAdvSecurityStation getLinkedSecurityStation() {
		return ItemCardSecurityLink.getLinkedSecurityStation(this, 0, world);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		readExtraNBT(nbttagcompound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		writeExtraNBT(nbttagcompound);
		return nbttagcompound;
	}

	@Override
	public void readExtraNBT(NBTTagCompound nbttagcompound) {
		super.readExtraNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		inventory = NonNullList.withSize(40,ItemStack.EMPTY);
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist
					.getCompoundTagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if (byte0 >= 0 && byte0 < inventory.size()) {
				inventory.set(byte0, new ItemStack(nbttagcompound1));
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
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound cmp=super.getUpdateTag();
		writeExtraNBT(cmp);
		return cmp;

	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		readExtraNBT(tag);
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
			itemstack.setCount(getInventoryStackLimit());
		}
	}

	@Override
	public String getName() {
		return "ControlSystem";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int getSlotStackLimit(int slt) {
		return 1;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack, int Slot) {

		switch (Slot) {
		case 0:
			if (par1ItemStack.getItem() instanceof ItemCardSecurityLink)
				return true;
			break;
		}

		if (par1ItemStack.getItem() instanceof ItemCardDataLink)
			return true;

		return false;
	}

	@Override
	public void onNetworkHandlerEvent(int key, String value) {

		if (key == 103) {
			if (remote != null) {
				if (getRemoteGUIinRange()) {
					EntityPlayer player = world.getPlayerEntityByName(value);
					if (player != null) {
						player.openGui(ModularForceFieldSystem.instance, 0,
								world, remote.getPos().getX(), remote.getPos().getY(),
								remote.getPos().getZ());
					}
				}
			}
		}

		if (key == 102) {
			if (remote != null)
				remote.toggelSwitchValue();
		}

		if (key == 101) {
			if (remote != null)
				remote.toogleSwitchModi();
		}

		super.onNetworkHandlerEvent(key, value);
	}

	// RemoteData info -------------------------------------------------------

	private void refreshRemoteData() {
		refreshRemoteData(1);
	}

	private void refreshRemoteData(int slot) {

		remote = getTargetMaschine(slot);

		if (remote != null) {

			if (!remote.isActive() == getRemoteActive())
				setRemoteActive(remote.isActive());

			if (!remote.getDeviceName().equalsIgnoreCase(getRemoteDeviceName()))
				setRemoteDeviceName(remote.getDeviceName());

			if (remote.getSwitchModi() != getRemoteSwitchModi())
				setRemoteSwitchModi(remote.getSwitchModi());

			if (!remote.getSwitchValue() == getRemoteSwitchValue())
				setRemoteSwitchValue(remote.getSwitchValue());

			if (remote.getLinkedSecurityStation() == null) {
				this.setRemoteSecurityStationlink(false);
			} else {
				this.setRemoteSecurityStationlink(true);
			}

			if (!remote.hasPowerSource() == this.getRemotehasPowersource())
				setRemotehasPowersource(remote.hasPowerSource());

			if (remote.getPercentageCapacity() != this.getRemotePowerleft())
				setRemotePowerleft(remote.getPercentageCapacity());

			if (!MFFSMaschines.fromTE(remote).displayName
					.equalsIgnoreCase(getRemoteDeviceTyp()))
				setRemoteDeviceTyp(MFFSMaschines.fromTE(remote).displayName);

			if (PointXYZ
					.distance(getMaschinePoint(), remote.getMaschinePoint()) > MACHINE_RANGE
					&& this.getRemoteGUIinRange()) {
				this.setRemoteGUIinRange(false);
			}

			if (PointXYZ
					.distance(getMaschinePoint(), remote.getMaschinePoint()) <= MACHINE_RANGE
					&& !this.getRemoteGUIinRange()) {
				this.setRemoteGUIinRange(true);
			}

		} else {

			if (getRemoteActive() != false)
				setRemoteActive(false);

			if (getRemoteSwitchModi() != 0)
				setRemoteSwitchModi((short) 0);

			if (!getRemoteDeviceName().equalsIgnoreCase("-"))
				setRemoteDeviceName("-");

			if (!getRemoteDeviceTyp().equalsIgnoreCase("-"))
				setRemoteDeviceTyp("-");

		}
		this.markDirty();
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
		world.markBlockRangeForRenderUpdate(pos,pos);

	}

	private TileEntityMachines getTargetMaschine(int slot) {
		if (getStackInSlot(slot) != null)
			if (getStackInSlot(slot).getItem() instanceof ItemCardDataLink) {
				int DeviceID = 0;
				NBTTagCompound tag = NBTTagCompoundHelper
						.getTAGfromItemstack(getStackInSlot(slot));
				if (tag.hasKey("DeviceID"))
					DeviceID = tag.getInteger("DeviceID");

				if (DeviceID != 0) {
					TileEntityMachines device = Linkgrid
							.getWorldMap(world)
							.getTileEntityMachines(
									ItemCardDataLink
											.getDeviceTyp(getStackInSlot(slot)),
									DeviceID);
					if (device != null)
						return device;
				}
				setInventorySlotContents(slot, new ItemStack(
						ModItems.EMPTY_CARD));
			}
		return null;
	}

	// Getter/Setter for Remote Info

	public boolean getRemoteGUIinRange() {
		return RemoteGUIinRange;
	}

	public void setRemoteGUIinRange(boolean b) {
		RemoteGUIinRange = b;

	}

	public int getRemotePowerleft() {
		return RemotePowerleft;
	}

	public void setRemotePowerleft(int i) {
		RemotePowerleft = i;

	}

	public boolean getRemotehasPowersource() {
		return RemotehasPowersource;
	}

	public void setRemotehasPowersource(boolean b) {
		RemotehasPowersource = b;

	}

	public boolean getRemoteSecurityStationlink() {
		return RemoteSecurityStationlink;
	}

	public void setRemoteSecurityStationlink(boolean b) {
		RemoteSecurityStationlink = b;

	}

	public boolean getRemoteSwitchValue() {
		return RemoteSwitchValue;
	}

	public void setRemoteSwitchValue(boolean b) {
		RemoteSwitchValue = b;

	}

	public short getRemoteSwitchModi() {
		return RemoteSwitchModi;
	}

	public void setRemoteSwitchModi(short s) {
		RemoteSwitchModi = s;

	}

	public boolean getRemoteActive() {
		return RemoteActive;
	}

	public void setRemoteActive(boolean b) {
		RemoteActive = b;

	}

	public String getRemoteDeviceTyp() {
		return RemoteDeviceTyp;
	}

	public void setRemoteDeviceTyp(String s) {
		RemoteDeviceTyp = s;

	}

	public String getRemoteDeviceName() {
		return RemoteDeviceName;
	}

	public void setRemoteDeviceName(String s) {
		RemoteDeviceName = s;

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
