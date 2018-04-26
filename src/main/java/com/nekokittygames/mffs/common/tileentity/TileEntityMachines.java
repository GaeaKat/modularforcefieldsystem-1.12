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


import com.nekokittygames.mffs.api.IMFFS_Wrench;
import com.nekokittygames.mffs.api.ISwitchabel;
import com.nekokittygames.mffs.api.PointXYZ;
import com.nekokittygames.mffs.common.IModularProjector.Slots;
import com.nekokittygames.mffs.common.Linkgrid;
import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.SecurityHelper;
import com.nekokittygames.mffs.common.SecurityRight;
import com.nekokittygames.mffs.common.block.BlockMFFSBase;
import com.nekokittygames.mffs.common.item.*;
import com.nekokittygames.mffs.network.INetworkHandlerEventListener;
import com.nekokittygames.mffs.network.INetworkHandlerListener;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class TileEntityMachines extends TileEntity implements
		INetworkHandlerListener, INetworkHandlerEventListener, ISidedInventory,
		IMFFS_Wrench, ISwitchabel,ITickable {

	private boolean Active;
	private EnumFacing curSide;
	private short ticker;
	protected boolean init;
	protected String DeviceName;
	protected int DeviceID;
	protected short SwitchModi;
	protected boolean SwitchValue;
	protected Random random = new Random();
	protected Ticket chunkTicket;
	protected NonNullList<ItemStack> inventory;

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return (oldState.getBlock()!=newSate.getBlock());
	}

	public TileEntityMachines(int slots) {
		Active = false;
		SwitchValue = false;
		init = true;
		curSide = EnumFacing.UP;
		SwitchModi = 0; // 0:OFF 1: Redstone 2:Switch 3:CC
		ticker = 0;
		DeviceID = 0;
		DeviceName = "Please set Name";
		inventory = NonNullList.withSize(slots, ItemStack.EMPTY);
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
		this.markDirty();
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
		world.markBlockRangeForRenderUpdate(pos,pos);
	}

	@Override
	public List<String> getFieldsforUpdate() {
		List<String> NetworkedFields = new LinkedList<String>();
		NetworkedFields.clear();

		NetworkedFields.add("Active");
		NetworkedFields.add("curSide");
		NetworkedFields.add("DeviceID");
		NetworkedFields.add("DeviceName");
		NetworkedFields.add("SwitchModi");
		NetworkedFields.add("SwitchValue");

		return NetworkedFields;
	}

	@Override
	public void onNetworkHandlerUpdate(String field) {

		world.markBlockRangeForRenderUpdate(pos,pos);

	}


	@Override
	public void update() {

		if (!world.isRemote)
			if (init)
				init();

		if (world.isRemote)
			if (DeviceID == 0) {
				if (this.getTicker() >= 5 + random.nextInt(20)) {
					setTicker((short) 0);
				}
				setTicker((short) (getTicker() + 1));
			}
	}

	public void init() {

		DeviceID = Linkgrid.getWorldMap(world).refreshID(this, DeviceID);
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

	}

	public boolean isRedstoneSignal() {
		// if(world.isBlockGettingPowered(xCoord,yCoord, zCoord) ||
		if (world.getStrongPower(pos) > 0
				|| world.isBlockIndirectlyGettingPowered(pos)>0)
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

	}

	public void setDeviceName(String DeviceName) {
		this.DeviceName = DeviceName;
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
		return new PointXYZ(pos, world);
	}

	public abstract void dropPlugins();

	public void dropPlugins(int slot) {

		if (world.isRemote) {
			this.setInventorySlotContents(slot, ItemStack.EMPTY);
			return;
		}

		if (!getStackInSlot(slot).isEmpty()) {
			ItemStack stack = getStackInSlot(slot);
			if (stack.getItem() instanceof ItemCardSecurityLink
					|| stack.getItem() instanceof ItemCardPowerLink
					|| stack.getItem() instanceof ItemCardPersonalID
					|| stack.getItem() instanceof ItemCardDataLink) {
				world.spawnEntity(new EntityItem(world,
						this.pos.getX(), this.pos.getY(), this.pos.getZ(), new ItemStack(
						ModItems.EMPTY_CARD, 1)));
			} else {
				world.spawnEntity(new EntityItem(world,
						this.pos.getX(), this.pos.getY(), this.pos.getZ(), stack));
			}

			setInventorySlotContents(slot, ItemStack.EMPTY);
			this.markDirty();
		}
	}


	@Override
	public void markDirty() {
		super.markDirty();
	}

	public abstract Container getContainer(InventoryPlayer inventoryplayer);

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		readExtraNBT(nbttagcompound);
	}

	public void readExtraNBT(NBTTagCompound nbttagcompound) {
		curSide = EnumFacing.values()[nbttagcompound.getInteger("side")];
		Active = nbttagcompound.getBoolean("active");
		SwitchValue = nbttagcompound.getBoolean("SwitchValue");
		DeviceID = nbttagcompound.getInteger("DeviceID");
		DeviceName = nbttagcompound.getString("DeviceName");
		SwitchModi = nbttagcompound.getShort("SwitchModi");
		
		NBTTagList itemTags = nbttagcompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		inventory = NonNullList.withSize(inventory.size(), ItemStack.EMPTY);
		for (int i = 0; i < itemTags.tagCount(); i++) {
			NBTTagCompound nbt = (NBTTagCompound) itemTags.getCompoundTagAt(i);
			byte slot = nbt.getByte("Slot");
			if (slot >= 0 && slot < inventory.size()) {
				inventory.set(slot, new ItemStack(nbt));
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		writeExtraNBT(nbttagcompound);
		return nbttagcompound;
	}

	public void writeExtraNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setShort("SwitchModi", SwitchModi);
		nbttagcompound.setInteger("side", curSide.getIndex());
		nbttagcompound.setBoolean("active", Active);
		nbttagcompound.setBoolean("SwitchValue", SwitchValue);
		nbttagcompound.setInteger("DeviceID", DeviceID);
		nbttagcompound.setString("DeviceName", DeviceName);
		
		NBTTagList itemTags = new NBTTagList();
		for (int i = 0; i < inventory.size(); i++) {
			if (!inventory.get(i).isEmpty()) {
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setByte("Slot", (byte) i);
				inventory.get(i).writeToNBT(nbt);
				itemTags.appendTag(nbt);
			}
		}
		nbttagcompound.setTag("Items", itemTags);
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

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {

		NBTTagCompound comp=new NBTTagCompound();
		writeExtraNBT(comp);
		SPacketUpdateTileEntity packetUpdateTileEntity=new SPacketUpdateTileEntity(pos,world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos)),comp);
		return packetUpdateTileEntity;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		readExtraNBT(pkt.getNbtCompound());
	}

	@Override
	public boolean wrenchCanManipulate(EntityPlayer entityPlayer, EnumFacing side) {
		if (!SecurityHelper.isAccessGranted(this, entityPlayer, world,
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
	public void setSide(EnumFacing i) {
		curSide = i;
		world.setBlockState(pos,world.getBlockState(pos).withProperty(BlockMFFSBase.FACING,i));

	}

	public boolean isActive() {
		return Active;
	}

	public void setActive(boolean flag) {
		Active = flag;
		if(world.getBlockState(pos).getBlock() instanceof BlockMFFSBase)
			world.setBlockState(pos,world.getBlockState(pos).withProperty(BlockMFFSBase.ACTIVE,flag));

	}

	@Override
	public EnumFacing getSide() {
		if(world.getBlockState(pos).getBlock() instanceof BlockMFFSBase)
			return world.getBlockState(pos).getValue(BlockMFFSBase.FACING);
		return EnumFacing.UP;
	}



	public void forceChunkLoading(Ticket ticket) {
		if (chunkTicket == null) {
			chunkTicket = ticket;
		}
		ChunkPos Chunk = new ChunkPos(pos.getX() >> 4,
				pos.getZ()>> 4);
		ForgeChunkManager.forceChunk(ticket, Chunk);
	}

	protected void registerChunkLoading() {
		if (chunkTicket == null) {
			chunkTicket = ForgeChunkManager.requestTicket(
					ModularForceFieldSystem.instance, world, Type.NORMAL);
		}
		if (chunkTicket == null) {
			System.out
					.println("[ModularForceFieldSystem]no free Chunkloaders available");
			return;
		}

		chunkTicket.getModData().setInteger("MaschineX", pos.getX());
		chunkTicket.getModData().setInteger("MaschineY", pos.getY());
		chunkTicket.getModData().setInteger("MaschineZ", pos.getZ());
		ForgeChunkManager.forceChunk(chunkTicket, new ChunkPos(
				pos.getX() >> 4, pos.getZ()>> 4));

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
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		if (world.getTileEntity(pos) != this) {
			return false;
		} else {
			return player.getDistance(pos.getX(),pos.getY(),pos.getZ()) <= 64D;
		}
	}




	@Override
	public int getSizeInventory() {
		return inventory.size();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory.get(slot);
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (!inventory.get(slot).isEmpty()) {
			if (inventory.get(slot).getCount() <= amount) {
				ItemStack stack = inventory.get(slot);
				inventory.set(slot, ItemStack.EMPTY);
				return stack;
			}
			
			return inventory.get(slot).splitStack(amount);
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int slot) {
		ItemStack stack = inventory.get(slot);
		
		inventory.set(slot, ItemStack.EMPTY);
		markDirty();
		
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inventory.set(slot, stack);
		
		if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		}
	}

	public int countItemsInSlot(Slots slt) {
		if (!getStackInSlot(slt.slot).isEmpty())
			return this.getStackInSlot(slt.slot).getCount();
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
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
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
		return false;
	}
}
