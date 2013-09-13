package mods.mffs.common.tileentity;

import mods.mffs.api.PointXYZ;
import mods.mffs.common.Linkgrid;
import mods.mffs.common.MFFSMaschines;
import mods.mffs.common.ModularForceFieldSystem;
import mods.mffs.common.NBTTagCompoundHelper;
import mods.mffs.common.container.ContainerControlSystem;
import mods.mffs.common.item.ItemCardDataLink;
import mods.mffs.common.item.ItemCardSecurityLink;
import mods.mffs.network.server.NetworkHandlerServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;

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
	private ItemStack inventory[];

	public TileEntityControlSystem() {
		inventory = new ItemStack[40];
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
		Linkgrid.getWorldMap(worldObj).getControlSystem().remove(getDeviceID());
		super.invalidate();
	}

	@Override
	public void dropPlugins() {
		for (int a = 0; a < this.inventory.length; a++) {
			dropplugins(a, this);
		}
	}

	@Override
	public void updateEntity() {
		if (!worldObj.isRemote) {

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
		super.updateEntity();
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
		return ItemCardSecurityLink.getLinkedSecurityStation(this, 0, worldObj);
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
		return "ControlSystem";
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
					EntityPlayer player = worldObj.getPlayerEntityByName(value);
					if (player != null) {
						player.openGui(ModularForceFieldSystem.instance, 0,
								worldObj, remote.xCoord, remote.yCoord,
								remote.zCoord);
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
							.getWorldMap(worldObj)
							.getTileEntityMachines(
									ItemCardDataLink
											.getDeviceTyp(getStackInSlot(slot)),
									DeviceID);
					if (device != null)
						return device;
				}
				setInventorySlotContents(slot, new ItemStack(
						ModularForceFieldSystem.MFFSitemcardempty));
			}
		return null;
	}

	// Getter/Setter for Remote Info

	public boolean getRemoteGUIinRange() {
		return RemoteGUIinRange;
	}

	public void setRemoteGUIinRange(boolean b) {
		RemoteGUIinRange = b;
		NetworkHandlerServer.updateTileEntityField(this, "RemoteGUIinRange");
	}

	public int getRemotePowerleft() {
		return RemotePowerleft;
	}

	public void setRemotePowerleft(int i) {
		RemotePowerleft = i;
		NetworkHandlerServer.updateTileEntityField(this, "RemotePowerleft");
	}

	public boolean getRemotehasPowersource() {
		return RemotehasPowersource;
	}

	public void setRemotehasPowersource(boolean b) {
		RemotehasPowersource = b;
		NetworkHandlerServer
				.updateTileEntityField(this, "RemotehasPowersource");
	}

	public boolean getRemoteSecurityStationlink() {
		return RemoteSecurityStationlink;
	}

	public void setRemoteSecurityStationlink(boolean b) {
		RemoteSecurityStationlink = b;
		NetworkHandlerServer.updateTileEntityField(this,
				"RemoteSecurityStationlink");
	}

	public boolean getRemoteSwitchValue() {
		return RemoteSwitchValue;
	}

	public void setRemoteSwitchValue(boolean b) {
		RemoteSwitchValue = b;
		NetworkHandlerServer.updateTileEntityField(this, "RemoteSwitchValue");
	}

	public short getRemoteSwitchModi() {
		return RemoteSwitchModi;
	}

	public void setRemoteSwitchModi(short s) {
		RemoteSwitchModi = s;
		NetworkHandlerServer.updateTileEntityField(this, "RemoteSwitchModi");
	}

	public boolean getRemoteActive() {
		return RemoteActive;
	}

	public void setRemoteActive(boolean b) {
		RemoteActive = b;
		NetworkHandlerServer.updateTileEntityField(this, "RemoteActive");
	}

	public String getRemoteDeviceTyp() {
		return RemoteDeviceTyp;
	}

	public void setRemoteDeviceTyp(String s) {
		RemoteDeviceTyp = s;
		NetworkHandlerServer.updateTileEntityField(this, "RemoteDeviceTyp");
	}

	public String getRemoteDeviceName() {
		return RemoteDeviceName;
	}

	public void setRemoteDeviceName(String s) {
		RemoteDeviceName = s;
		NetworkHandlerServer.updateTileEntityField(this, "RemoteDeviceName");
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
	public String getType() { return "MFFSControlSystem"; }

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
