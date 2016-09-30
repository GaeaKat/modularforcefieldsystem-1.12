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

import com.nekokittygames.mffs.api.PointXYZ;
import com.nekokittygames.mffs.common.ForceFieldBlockStack;
import com.nekokittygames.mffs.common.Linkgrid;
import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.WorldMap;
import com.nekokittygames.mffs.common.block.BlockForceField;
import com.nekokittygames.mffs.network.client.ForceFieldClientUpdatehandler;
import com.nekokittygames.mffs.network.server.ForceFieldServerUpdatehandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class TileEntityForceField extends TileEntity implements ITickable{
	private final Random random = new Random();
	private final int[] texturid = { -76, -76, -76, -76, -76, -76 };
	private String texturfile;
	private int Ticker = 0;
	private Block ForcefieldCamoblock;
	private int ForcefieldCamoblockmeta;

	public int getTicker() {
		return Ticker;
	}

	public void setTicker(int ticker) {
		Ticker = ticker;
	}

	public TileEntityForceField() {
	}

	public int getForcefieldCamoblockmeta() {
		return ForcefieldCamoblockmeta;
	}

	public void setForcefieldCamoblockmeta(int forcefieldCamoblockmeta) {
		ForcefieldCamoblockmeta = forcefieldCamoblockmeta;
	}

	public Block getForcefieldCamoblock() {
		return ForcefieldCamoblock;
	}

	public void setForcefieldCamoblockid(Block forcefieldCamoblock) {
		ForcefieldCamoblock = forcefieldCamoblock;
	}

	public String getTexturfile() {
		return texturfile;
	}

	public void setTexturfile(String texturfile) {
		this.texturfile = texturfile;
	}

	public int[] getTexturid() {
		return texturid;
	}

	public int getTexturid(int l) {
		return texturid[l];
	}

	@Override
	public void update() {
		if (worldObj.isRemote == false) {
			if (this.getTicker() >= 20) {
					UpdateTextur();
				this.markDirty();

				this.setTicker((short) 0);
			}

			this.setTicker((short) (this.getTicker() + 1));
		} else {
			if (this.getTicker() >= 20 + random.nextInt(20)) {
				this.setTicker((short) 0);
			}

			this.setTicker((short) (this.getTicker() + 1));
		}
	}

	@Override
	public void validate() {
		super.validate();
		//UpdateTextur();
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound cmp=super.getUpdateTag();
		return writeExtraNBT(cmp);
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		readExtraNBT(tag);
	}

	public void UpdateTextur() // Serverside
	{
		if (worldObj.isRemote == false) {
			ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(
					worldObj).getForceFieldStackMap(
					new PointXYZ(pos,
							worldObj).hashCode());

			if (ffworldmap != null) {
				if (!ffworldmap.isEmpty())

				{
					TileEntityProjector projector = Linkgrid
							.getWorldMap(worldObj).getProjektor()
							.get(ffworldmap.getProjectorID());

					if (projector != null) {
						setForcefieldCamoblockid(projector.getForcefieldCamoblock());
						setForcefieldCamoblockmeta(projector
								.getForcefieldCamoblockMeta());
						this.markDirty();
					}
				}
			}
		}
	}

	@Override
	public void markDirty() {
		super.markDirty();

	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return (oldState.getBlock()!=newSate.getBlock()) ;
	}

	public ItemStack[] getContents() {
		return null;
	}

	public void setMaxStackSize(int arg0) {
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		readExtraNBT(compound);
	}

	private void readExtraNBT(NBTTagCompound compound) {
		if(compound.hasKey("blockName")) {
			ForcefieldCamoblock = Block.getBlockFromName(compound.getString("blockName"));
			ForcefieldCamoblockmeta=compound.getInteger("blockMeta");
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		return writeExtraNBT(compound);

	}

	private NBTTagCompound writeExtraNBT(NBTTagCompound compound) {
		if(ForcefieldCamoblock!=null) {
			compound.setString("blockName", ForcefieldCamoblock.getRegistryName().toString());
			compound.setInteger("blockMeta", ForcefieldCamoblockmeta);
		}
		return compound;
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound comp=new NBTTagCompound();
		writeExtraNBT(comp);
		return new SPacketUpdateTileEntity(pos,worldObj.getBlockState(pos).getBlock().getMetaFromState(worldObj.getBlockState(pos)),comp);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		readExtraNBT(pkt.getNbtCompound());
		worldObj.markBlockRangeForRenderUpdate(pos,pos);
	}
}
