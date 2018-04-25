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

	public TileEntityControlSystem() {
		super(40);
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
	public int getInventoryStackLimit() {
		return 1;
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
}
