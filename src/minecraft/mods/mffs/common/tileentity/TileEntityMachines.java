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

import ic2.api.tile.IWrenchable;
import mods.mffs.api.IMFFS_Wrench;
import mods.mffs.api.ISwitchabel;
import mods.mffs.api.PointXYZ;
import mods.mffs.common.IModularProjector.Slots;
import mods.mffs.common.Linkgrid;
import mods.mffs.common.ModularForceFieldSystem;
import mods.mffs.common.SecurityHelper;
import mods.mffs.common.SecurityRight;
import mods.mffs.common.item.ItemCardDataLink;
import mods.mffs.common.item.ItemCardPersonalID;
import mods.mffs.common.item.ItemCardPowerLink;
import mods.mffs.common.item.ItemCardSecurityLink;
import mods.mffs.network.INetworkHandlerEventListener;
import mods.mffs.network.INetworkHandlerListener;
import mods.mffs.network.client.NetworkHandlerClient;
import mods.mffs.network.server.NetworkHandlerServer;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.common.ISidedInventory;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class TileEntityMachines extends TileEntity implements
		INetworkHandlerListener, INetworkHandlerEventListener, ISidedInventory,
		IMFFS_Wrench, IWrenchable, ISwitchabel {

	private boolean Active;
	private int Side;
	private short ticker;
	protected boolean init;
	protected String DeviceName;
	protected int DeviceID;
	protected short SwitchModi;
	protected boolean SwitchValue;
	protected Random random = new Random();
	protected Ticket chunkTicket;

	public TileEntityMachines() {
		Active = false;
		SwitchValue = false;
		init = true;
		Side = -1;
		SwitchModi = 0; // 0:OFF 1: Redstone 2:Switch 3:CC
		ticker = 0;
		DeviceID = 0;
		DeviceName = "Please set Name";
	}

	public int getPercentageCapacity() {
		return 0;
	}

	public boolean hasPowerSource() {
		return false;
	}

	public abstract TileEntityAdvSecurityStation getLinkedSecurityStation();

	@Override
	public void onNetworkHandlerEvent(int key, String value) {

		switch (key) {
		case 0: // GUIchangeSwitchModi id: 0
			toogleSwitchModi();
			break;
		// DeviceName tipping Events id :10-12
		case 10:
			setDeviceName("");
			break;
		case 11:
			if (getDeviceName().length() <= 20)
				setDeviceName(getDeviceName() + value);
			break;
		case 12:
			if (getDeviceName().length() >= 1)
				setDeviceName(getDeviceName().substring(0,
						getDeviceName().length() - 1));
			break;

		}
	}

	@Override
	public List<String> getFieldsforUpdate() {
		List<String> NetworkedFields = new LinkedList<String>();
		NetworkedFields.clear();

		NetworkedFields.add("Active");
		NetworkedFields.add("Side");
		NetworkedFields.add("DeviceID");
		NetworkedFields.add("DeviceName");
		NetworkedFields.add("SwitchModi");
		NetworkedFields.add("SwitchValue");

		return NetworkedFields;
	}

	@Override
	public void onNetworkHandlerUpdate(String field) {
		worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public void updateEntity() {

		if (!worldObj.isRemote)
			if (init)
				init();

		if (worldObj.isRemote)
			if (DeviceID == 0) {
				if (this.getTicker() >= 5 + random.nextInt(20)) {
					NetworkHandlerClient.requestInitialData(this, true);
					setTicker((short) 0);
				}
				setTicker((short) (getTicker() + 1));
			}
	}

	public void init() {

		DeviceID = Linkgrid.getWorldMap(worldObj).refreshID(this, DeviceID);
		if (ModularForceFieldSystem.enableChunkLoader)
			registerChunkLoading();
		init = false;
	}

	public short getmaxSwitchModi() {
		return 0;
	}

	public short getminSwitchModi() {
		return 0;
	}

	public void toogleSwitchModi() {

		if (getSwitchModi() == getmaxSwitchModi()) {
			SwitchModi = getminSwitchModi();
		} else {
			SwitchModi++;
		}
		NetworkHandlerServer.updateTileEntityField(this, "SwitchModi");
	}

	public boolean isRedstoneSignal() {
		// if(worldObj.isBlockGettingPowered(xCoord,yCoord, zCoord) ||
		if (worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) > 0
				|| worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord,
						zCoord))
			return true;
		return false;
	}

	public short getSwitchModi() {
		if (SwitchModi < getminSwitchModi())
			SwitchModi = getminSwitchModi();
		return SwitchModi;
	}

	public boolean getSwitchValue() {
		return SwitchValue;
	}

	@Override
	public boolean isSwitchabel() {
		if (getSwitchModi() == 2)
			return true;
		return false;
	}

	@Override
	public void toggelSwitchValue() {
		SwitchValue = !SwitchValue;
		NetworkHandlerServer.updateTileEntityField(this, "SwitchValue");
	}

	public void setDeviceName(String DeviceName) {
		this.DeviceName = DeviceName;
		NetworkHandlerServer.updateTileEntityField(this, "DeviceName");
	}

	public int getDeviceID() {
		return DeviceID;
	}

	public void setDeviceID(int i) {
		this.DeviceID = i;
	}

	public String getDeviceName() {
		return DeviceName;
	}

	public PointXYZ getMaschinePoint() {
		return new PointXYZ(this.xCoord, this.yCoord, this.zCoord, worldObj);
	}

	public abstract void dropPlugins();

	public void dropplugins(int slot, IInventory inventory) {

		if (worldObj.isRemote) {
			this.setInventorySlotContents(slot, null);
			return;
		}

		if (inventory.getStackInSlot(slot) != null) {
			if (inventory.getStackInSlot(slot).getItem() instanceof ItemCardSecurityLink
					|| inventory.getStackInSlot(slot).getItem() instanceof ItemCardPowerLink
					|| inventory.getStackInSlot(slot).getItem() instanceof ItemCardPersonalID
					|| inventory.getStackInSlot(slot).getItem() instanceof ItemCardDataLink) {
				worldObj.spawnEntityInWorld(new EntityItem(worldObj,
						this.xCoord, this.yCoord, this.zCoord, new ItemStack(
								ModularForceFieldSystem.MFFSitemcardempty, 1)));
			} else {
				worldObj.spawnEntityInWorld(new EntityItem(worldObj,
						this.xCoord, this.yCoord, this.zCoord, inventory
								.getStackInSlot(slot)));
			}

			inventory.setInventorySlotContents(slot, null);
			this.onInventoryChanged();
		}
	}

	public abstract Container getContainer(InventoryPlayer inventoryplayer);

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		Side = nbttagcompound.getInteger("side");
		Active = nbttagcompound.getBoolean("active");
		SwitchValue = nbttagcompound.getBoolean("SwitchValue");
		DeviceID = nbttagcompound.getInteger("DeviceID");
		DeviceName = nbttagcompound.getString("DeviceName");
		SwitchModi = nbttagcompound.getShort("SwitchModi");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setShort("SwitchModi", SwitchModi);
		nbttagcompound.setInteger("side", Side);
		nbttagcompound.setBoolean("active", Active);
		nbttagcompound.setBoolean("SwitchValue", SwitchValue);
		nbttagcompound.setInteger("DeviceID", DeviceID);
		nbttagcompound.setString("DeviceName", DeviceName);
	}

	@Override
	public boolean wrenchCanManipulate(EntityPlayer entityPlayer, int side) {
		if (!SecurityHelper.isAccessGranted(this, entityPlayer, worldObj,
				SecurityRight.EB)) {
			return false;
		}

		return true;
	}

	public short getTicker() {
		return ticker;
	}

	public void setTicker(short ticker) {
		this.ticker = ticker;
	}

	@Override
	public void setSide(int i) {
		Side = i;
		NetworkHandlerServer.updateTileEntityField(this, "Side");
	}

	public boolean isActive() {
		return Active;
	}

	public void setActive(boolean flag) {
		Active = flag;
		NetworkHandlerServer.updateTileEntityField(this, "Active");
	}

	@Override
	public int getSide() {
		return Side;
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
		if (side == getFacing()) {
			return false;
		}
		if (this instanceof TileEntitySecStorage) {
			return false;
		}
		if (this instanceof TileEntityAdvSecurityStation) {
			return false;
		}
		if (this.Active) {
			return false;
		}

		return wrenchCanManipulate(entityPlayer, side);
	}

	@Override
	public short getFacing() {
		return (short) getSide();
	}

	@Override
	public void setFacing(short facing) {
		setSide(facing);
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		if (this.Active) {
			return false;
		}
		return wrenchCanManipulate(entityPlayer, 0);
	}

	@Override
	public float getWrenchDropRate() {
		return 1;
	}

	public void forceChunkLoading(Ticket ticket) {
		if (chunkTicket == null) {
			chunkTicket = ticket;
		}
		ChunkCoordIntPair Chunk = new ChunkCoordIntPair(xCoord >> 4,
				zCoord >> 4);
		ForgeChunkManager.forceChunk(ticket, Chunk);
	}

	protected void registerChunkLoading() {
		if (chunkTicket == null) {
			chunkTicket = ForgeChunkManager.requestTicket(
					ModularForceFieldSystem.instance, worldObj, Type.NORMAL);
		}
		if (chunkTicket == null) {
			System.out
					.println("[ModularForceFieldSystem]no free Chunkloaders available");
			return;
		}

		chunkTicket.getModData().setInteger("MaschineX", xCoord);
		chunkTicket.getModData().setInteger("MaschineY", yCoord);
		chunkTicket.getModData().setInteger("MaschineZ", zCoord);
		ForgeChunkManager.forceChunk(chunkTicket, new ChunkCoordIntPair(
				xCoord >> 4, zCoord >> 4));

		forceChunkLoading(chunkTicket);
	}

	@Override
	public void invalidate() {
		ForgeChunkManager.releaseTicket(chunkTicket);
		super.invalidate();
	}

	public abstract boolean isItemValid(ItemStack par1ItemStack, int Slot);

	public abstract int getSlotStackLimit(int slt);

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
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

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {

		return new ItemStack(Block.blocksList[worldObj.getBlockId(xCoord,
				yCoord, zCoord)]);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return null;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	public int countItemsInSlot(Slots slt) {
		if (this.getStackInSlot(slt.slot) != null)
			return this.getStackInSlot(slt.slot).stackSize;
		return 0;
	}

}
