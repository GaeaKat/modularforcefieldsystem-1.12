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

import com.nekokittygames.mffs.api.IPowerLinkItem;
import com.nekokittygames.mffs.api.PointXYZ;
import com.nekokittygames.mffs.common.*;
import com.nekokittygames.mffs.common.block.BlockForceField;
import com.nekokittygames.mffs.common.block.BlockProjector;
import com.nekokittygames.mffs.common.container.ContainerProjector;
import com.nekokittygames.mffs.common.item.ItemCardSecurityLink;
import com.nekokittygames.mffs.common.item.ItemProjectorFieldModulatorDistance;
import com.nekokittygames.mffs.common.item.ItemProjectorFieldModulatorStrength;
import com.nekokittygames.mffs.common.item.ItemProjectorFocusMatrix;
import com.nekokittygames.mffs.common.modules.Module3DBase;
import com.nekokittygames.mffs.common.modules.ModuleBase;
import com.nekokittygames.mffs.common.options.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

public class TileEntityProjector extends TileEntityFEPoweredMachine implements
		IModularProjector {
	private ItemStack ProjektorItemStacks[];

	private final int[] focusmatrix = { 0, 0, 0, 0 }; // Up 7,Down 8,Right
														// 9,Left 10
	private String ForceFieldTexturids = "-76/-76/-76/-76/-76/-76";
	private String ForceFieldTexturfile = "/terrain.png";
	private Block ForcefieldCamoblock;
	private int ForcefieldCamoblockMeta;
	private int switchdelay;
	private ForceFieldTyps forcefieldblock_meta;
	private int ProjektorTyp;
	private int linkPower;
	private int blockcounter;
	private boolean burnout;
	private int accesstyp;
	private int capacity;

	protected Stack<Integer> field_queue = new Stack<Integer>();
	protected Set<PointXYZ> field_interior = new HashSet<PointXYZ>();
	protected Set<PointXYZ> field_def = new HashSet<PointXYZ>();

	public TileEntityProjector() {
		Random random = new Random();

		ProjektorItemStacks = new ItemStack[13];
		linkPower = 0;
		forcefieldblock_meta = ForceFieldTyps.Default;
		ProjektorTyp = 0;
		switchdelay = 0;
		burnout = false;
		accesstyp = 0;
		capacity = 0;
	}

	// Start Getter AND Setter

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int Capacity) {
		this.capacity = Capacity;
	}

	public int getaccesstyp() {
		return accesstyp;
	}

	public void setaccesstyp(int accesstyp) {
		this.accesstyp = accesstyp;
	}


	public int getForcefieldCamoblockMeta() {
		return ForcefieldCamoblockMeta;
	}

	public void setForcefieldCamoblockMeta(int meta) {
		ForcefieldCamoblockMeta=meta;
	}
	public Block getForcefieldCamoblock() {
		return ForcefieldCamoblock;
	}

	public void setForcefieldCamoblock(Block forcefieldCamoblock) {
		ForcefieldCamoblock = forcefieldCamoblock;

	}

	public String getForceFieldTexturfile() {
		return ForceFieldTexturfile;
	}

	public void setForceFieldTexturfile(String forceFieldTexturfile) {
		ForceFieldTexturfile = forceFieldTexturfile;

	}

	public int getProjektor_Typ() {
		return ProjektorTyp;
	}

	public void setProjektor_Typ(int ProjektorTyp) {
		this.ProjektorTyp = ProjektorTyp;
		worldObj.setBlockState(pos,worldObj.getBlockState(pos).withProperty(BlockProjector.FIELD_TYPE,ProjektorTyp));
		worldObj.markBlockRangeForRenderUpdate(pos,pos);


	}

	public int getBlockcounter() {
		return blockcounter;
	}

	public ForceFieldTyps getforcefieldblock_meta() {
		return forcefieldblock_meta;
	}

	public void setforcefieldblock_meta(ForceFieldTyps ffmeta) {
		this.forcefieldblock_meta = ffmeta;
	}

	public int getLinkPower() {
		return linkPower;
	}

	public void setLinkPower(int linkPower) {
		this.linkPower = linkPower;
	}

	public void ProjektorBurnout() {
		this.setBurnedOut(true);
		dropPlugins();
	}

	public boolean isBurnout() {
		return burnout;
	}

	@Override
	public void setBurnedOut(boolean b) {
		burnout = b;
	}

	// End Getter AND Setter

	// Start NBT Read/ Save

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
	}

	@Override
	public void readExtraNBT(NBTTagCompound nbttagcompound) {
		super.readExtraNBT(nbttagcompound);
		accesstyp = nbttagcompound.getInteger("accesstyp");
		burnout = nbttagcompound.getBoolean("burnout");
		ProjektorTyp = nbttagcompound.getInteger("Projektor_Typ");
		forcefieldblock_meta = ForceFieldTyps.values()[nbttagcompound.getShort("forcefieldblockmeta")];

		NBTTagList nbttaglist = nbttagcompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		ProjektorItemStacks = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist
					.getCompoundTagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if (byte0 >= 0 && byte0 < ProjektorItemStacks.length) {
				ProjektorItemStacks[byte0] = ItemStack
						.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public void writeExtraNBT(NBTTagCompound nbttagcompound) {
		super.writeExtraNBT(nbttagcompound);
		nbttagcompound.setInteger("accesstyp", accesstyp);
		nbttagcompound.setBoolean("burnout", burnout);
		nbttagcompound.setInteger("Projektor_Typ", ProjektorTyp);
		nbttagcompound.setShort("forcefieldblockmeta", (short) forcefieldblock_meta.ordinal());

		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < ProjektorItemStacks.length; i++) {
			if (ProjektorItemStacks[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				ProjektorItemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("Items", nbttaglist);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);


		return nbttagcompound;
	}

	// Stop NBT Read/ Save

	// Start Slot / Upgrades System

	@Override
	public void dropPlugins() {
		for (int a = 0; a < this.ProjektorItemStacks.length; a++) {
			dropplugins(a, this);
		}
	}

	@Override
	public void markDirty() {
		super.markDirty();
		getLinkedSecurityStation();
		checkslots();
	}


	public void checkslots() {

		if (hasValidTypeMod()) {

			if (getProjektor_Typ() != ProjectorTyp.TypfromItem(get_type()).ProTyp)
				setProjektor_Typ(ProjectorTyp.TypfromItem(get_type()).ProTyp);

			if (getforcefieldblock_meta() != get_type().getForceFieldTyps())
				setforcefieldblock_meta(get_type().getForceFieldTyps());

			worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 2);
			worldObj.markBlockRangeForRenderUpdate(pos,pos);

		} else {
			if (getProjektor_Typ() != 0) {
				setProjektor_Typ(0);
			}
			worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 2);
		}

		// Focus function

		if (hasValidTypeMod()) {
			for (int place = 7; place < 11; place++) {
				if (getStackInSlot(place) != null) {
					if (getStackInSlot(place).getItem() == ModularForceFieldSystem.MFFSitemFocusmatix) {
						switch (ProjectorTyp.TypfromItem(get_type()).ProTyp) {
							case 6:
								focusmatrix[place - 7] = getStackInSlot(place).stackSize + 1;
								break;
							case 7:
								focusmatrix[place - 7] = getStackInSlot(place).stackSize + 2;
								break;
							default:
								focusmatrix[place - 7] = getStackInSlot(place).stackSize;
								break;
						}
					} else {
						dropplugins(place, this);
					}
				} else {
					switch (ProjectorTyp.TypfromItem(get_type()).ProTyp) {
						case 6:
							focusmatrix[place - 7] = 1;
							break;
						case 7:
							focusmatrix[place - 7] = 2;
							break;
						default:
							focusmatrix[place - 7] = 0;
							break;
					}
				}
			}
		}

		if (getStackInSlot(11) != null) {




					this.setForcefieldCamoblock(((ItemBlock)(getStackInSlot(11).getItem())).getBlock());
				this.setForcefieldCamoblockMeta(getStackInSlot(11).getItemDamage());
					UpdateForcefieldTexttur();

		} else {
			if (!ForceFieldTexturids
					.equalsIgnoreCase("-76/-76/-76/-76/-76/-76")
					|| this.getForcefieldCamoblock() != null) {
				this.setForcefieldCamoblock(null);
				UpdateForcefieldTexttur();
			}
		}

		if (hasOption(ModularForceFieldSystem.MFFSProjectorOptionCamouflage,
				true))
			if (getforcefieldblock_meta() != ForceFieldTyps.Camouflage) {
				setforcefieldblock_meta(ForceFieldTyps.Camouflage);
			}

		if (hasOption(ModularForceFieldSystem.MFFSProjectorOptionZapper, true))
			if (getforcefieldblock_meta() != ForceFieldTyps.Zapper) {
				setforcefieldblock_meta(ForceFieldTyps.Zapper);
			}

		if (hasOption(ModularForceFieldSystem.MFFSProjectorOptionFieldFusion,
				true)) {
			if (!Linkgrid.getWorldMap(worldObj).getFieldFusion()
					.containsKey(getDeviceID()))
				Linkgrid.getWorldMap(worldObj).getFieldFusion()
						.put(getDeviceID(), this);
		} else {
			if (Linkgrid.getWorldMap(worldObj).getFieldFusion()
					.containsKey(getDeviceID()))
				Linkgrid.getWorldMap(worldObj).getFieldFusion()
						.remove(getDeviceID());
		}

		if (hasOption(
				ModularForceFieldSystem.MFFSProjectorOptionForceFieldJammer,
				false)) {
			if (!Linkgrid.getWorldMap(worldObj).getJammer()
					.containsKey(getDeviceID()))
				Linkgrid.getWorldMap(worldObj).getJammer()
						.put(getDeviceID(), this);
		} else {
			if (Linkgrid.getWorldMap(worldObj).getJammer()
					.containsKey(getDeviceID()))
				Linkgrid.getWorldMap(worldObj).getJammer()
						.remove(getDeviceID());
		}

		if (hasValidTypeMod()) {
			ModuleBase modTyp = get_type();

			if (!modTyp.supportsStrength())
				dropplugins(6, this);
			if (!modTyp.supportsDistance())
				dropplugins(5, this);

			if (!modTyp.supportsMatrix()) {
				dropplugins(7, this);
				dropplugins(8, this);
				dropplugins(9, this);
				dropplugins(10, this);
			}

			for (int spot = 2; spot <= 4; spot++) {
				if (getStackInSlot(spot) != null) {
					if (!modTyp.supportsOption(getStackInSlot(spot).getItem())) {
						dropplugins(spot, this);
					}
				}
				if (getStackInSlot(spot) != null) {
					if (getStackInSlot(spot).getItem() instanceof ItemProjectorOptionForceFieldJammer
							&& isPowersourceItem()) {
						dropplugins(spot, this);
					}
				}

				if (getStackInSlot(spot) != null) {
					if (getStackInSlot(spot).getItem() instanceof ItemProjectorOptionFieldFusion
							&& isPowersourceItem()) {
						dropplugins(spot, this);
					}
				}

				if (getStackInSlot(spot) != null) {
					if (getStackInSlot(spot).getItem() instanceof ItemProjectorOptionDefenseStation
							&& isPowersourceItem()) {
						dropplugins(spot, this);
					}
				}

			}

			if (getStackInSlot(12) != null) {
				if (getStackInSlot(12).getItem() instanceof ItemCardSecurityLink
						&& isPowersourceItem()) {
					dropplugins(12, this);
				}
			}

			if (!this
					.hasOption(
							ModularForceFieldSystem.MFFSProjectorOptionCamouflage,
							true))
				dropplugins(11, this);

		} else {
			for (int spot = 2; spot <= 10; spot++) {
				dropplugins(spot, this);
			}
		}
	}

	private void UpdateForcefieldTexttur() {
		if (this.isActive()
				&& this.hasOption(
						ModularForceFieldSystem.MFFSProjectorOptionCamouflage,
						true)) {
			for (PointXYZ png : field_def) {

				if (worldObj.getChunkFromBlockCoords(png.pos).isLoaded()) {

					TileEntity tileEntity = worldObj.getTileEntity(png.pos);

					if (tileEntity != null
							&& tileEntity instanceof TileEntityForceField) {
						((TileEntityForceField) tileEntity).UpdateTextur();
					}

				}
			}
		}
	}

	// Stop Slot / Upgrades System

	@Override
	public void update() {
		if (worldObj.isRemote == false) {

			if (init) {
				checkslots();
				if (this.isActive()) {
					calculateField(true);
				}
			}

			if (hasPowerSource()) {
				setLinkPower(getAvailablePower());

				if (isPowersourceItem() && this.getaccesstyp() != 0)
					this.setaccesstyp(0);

			} else {

				setLinkPower(0);
			}

			if (getSwitchModi() == 1)
				if (!getSwitchValue() && isRedstoneSignal())
					toggelSwitchValue();

			if (getSwitchModi() == 1)
				if (getSwitchValue() && !isRedstoneSignal())
					toggelSwitchValue();

			if ((getSwitchValue() && (switchdelay >= 40)) && hasValidTypeMod()
					&& hasPowerSource()
					&& this.getLinkPower() > Forcepowerneed(5)) {
				if (isActive() != true) {
					setActive(true);
					switchdelay = 0;
					try {
						if (calculateField(true)) {
							FieldGenerate(true);
						}
					} catch (ArrayIndexOutOfBoundsException ex) {
						System.out.println("Found.");
					}
					worldObj.notifyBlockUpdate(pos,worldObj.getBlockState(pos),worldObj.getBlockState(pos),3);
				}
			}
			if ((!getSwitchValue() && switchdelay >= 40) || !hasValidTypeMod()
					|| !hasPowerSource() || burnout
					|| this.getLinkPower() <= Forcepowerneed(1)) {
				if (isActive() != false) {
					setActive(false);
					switchdelay = 0;
					destroyField();
					worldObj.notifyBlockUpdate(pos,worldObj.getBlockState(pos),worldObj.getBlockState(pos),3);
				}
			}

			if (this.getTicker() == 20) {

				if (isActive()) {
					FieldGenerate(false);

					if (hasOption(
							ModularForceFieldSystem.MFFSProjectorOptionMoobEx,
							true))
						ItemProjectorOptionMobDefence.ProjectorNPCDefence(this,
								worldObj);

					if (hasOption(
							ModularForceFieldSystem.MFFSProjectorOptionDefenceStation,
							true))
						ItemProjectorOptionDefenseStation
								.ProjectorPlayerDefence(this, worldObj);

				}

				this.setTicker((short) 0);
			}

			this.setTicker((short) (this.getTicker() + 1));
		}
		switchdelay++;

		super.update();
	}

	private boolean calculateField(boolean addtoMap) { // Should only be called
														// when being turned on
														// after setting changes
														// or on first on.
		long time = System.currentTimeMillis();

		field_def.clear();
		field_interior.clear();
		if (hasValidTypeMod()) {
			Set<PointXYZ> tField = new HashSet<PointXYZ>();
			Set<PointXYZ> tFieldInt = new HashSet<PointXYZ>();

			if (get_type() instanceof Module3DBase) {
				((Module3DBase) get_type()).calculateField(this, tField,
						tFieldInt);
			} else {
				get_type().calculateField(this, tField);
			}

			for (PointXYZ pnt : tField) {

				if (pnt.pos.getY() + this.pos.getY()< 255) {
					PointXYZ tp = new PointXYZ(pnt.pos.add(this.pos), worldObj);

					if (Forcefielddefine(tp, addtoMap)) {
						field_def.add(tp);
					} else {
						return false;
					}
				}
			}
			for (PointXYZ pnt : tFieldInt) {

				if (pnt.pos.getY() + this.pos.getY()< 255) {
					PointXYZ tp = new PointXYZ(this.pos.add(pnt.pos), worldObj);

					if (calculateBlock(tp)) {
						field_interior.add(tp);
					} else {
						return false;
					}
				}
			}

			return true;
		}
		return false;
	}

	public boolean calculateBlock(PointXYZ pnt) {

		for (ItemProjectorOptionBase opt : getOptions(true)) {
			if (opt instanceof IInteriorCheck)
				((IInteriorCheck) opt).checkInteriorBlock(pnt, worldObj, this);

		}
		return true;
	}

	public boolean Forcefielddefine(PointXYZ png, boolean addtoMap) {

		for (ItemProjectorOptionBase opt : getOptions(true)) {

			if (opt instanceof ItemProjectorOptionForceFieldJammer) {
				if (((ItemProjectorOptionForceFieldJammer) opt)
						.CheckJammerinfluence(png, worldObj, this))
					return false;
			}

			if (opt instanceof ItemProjectorOptionFieldFusion) {
				if (((ItemProjectorOptionFieldFusion) opt)
						.checkFieldFusioninfluence(png, worldObj, this))
					return true;
			}

		}

		ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(worldObj)
				.getorcreateFFStackMap(png.pos, worldObj);

		if (!ffworldmap.isEmpty()) {
			if (ffworldmap.getProjectorID() != getDeviceID()) {
				ffworldmap.removebyProjector(getDeviceID());
				ffworldmap.add(getPowerSourceID(), getDeviceID(),
						getforcefieldblock_meta());
			}
		} else {
			ffworldmap.add(getPowerSourceID(), getDeviceID(),
					getforcefieldblock_meta());
			ffworldmap.setSync(false);
		}

		field_queue.push(png.hashCode());

		return true;
	}

	public void FieldGenerate(boolean init) {

		int cost = 0;

		if (init) {
			cost = ModularForceFieldSystem.forceFieldBlockCost
					* ModularForceFieldSystem.forceFieldBlockCreateModifier;
		} else {
			cost = ModularForceFieldSystem.forceFieldBlockCost;
		}

		if (getforcefieldblock_meta() == ForceFieldTyps.Zapper) {
			cost *= ModularForceFieldSystem.forceFieldBlockZapperModifier;
		}

		consumePower(cost * field_def.size(), false);

		blockcounter = 0;

		for (PointXYZ pnt : field_def) {

			if (blockcounter >= ModularForceFieldSystem.forceFieldMaxBlocksPerTick) {
				break;
			}
			ForceFieldBlockStack ffb = WorldMap.getForceFieldWorld(worldObj)
					.getForceFieldStackMap(pnt.hashCode());

			if (ffb != null) {

				if (ffb.isSync())
					continue;

				PointXYZ png = ffb.getPoint();

				if (worldObj.getChunkFromBlockCoords(png.pos).isLoaded()) {
					if (!ffb.isEmpty()) {
						if (ffb.getProjectorID() == getDeviceID()) {
							if (hasOption(
									ModularForceFieldSystem.MFFSProjectorOptionCutter,
									true)) {
								IBlockState blockState = worldObj.getBlockState(png.pos);
								TileEntity entity = worldObj
										.getTileEntity(png.pos);

								if (blockState.getBlock() != ModularForceFieldSystem.MFFSFieldblock
										&& blockState.getBlock() != Blocks.AIR
										&& blockState.getBlock() != Blocks.BEDROCK
										&& entity == null)

								{
									List<ItemStack> stacks = Functions
											.getItemStackFromBlock(worldObj,
													png.pos);
									worldObj.setBlockToAir(png.pos);

									if (ProjectorTyp.TypfromItem(get_type()).Blockdropper
											&& stacks != null) {
										IInventory inventory = InventoryHelper
												.findAttachedInventory(
														worldObj, pos);
										if (inventory != null) {
											if (inventory.getSizeInventory() > 0) {
												InventoryHelper
														.addStacksToInventory(
																inventory,
																stacks);
											}
										}
									}
								}
							}

							if (worldObj.getBlockState(png.pos).getMaterial()
									.isLiquid()
									|| worldObj.isAirBlock(png.pos)
									|| worldObj.getBlockState(png.pos).getBlock() == ModularForceFieldSystem.MFFSFieldblock) {
								if (worldObj.getBlockState(png.pos).getBlock() != ModularForceFieldSystem.MFFSFieldblock) {
									worldObj.setBlockState(
											png.pos,
											ModularForceFieldSystem.MFFSFieldblock.getDefaultState().withProperty(BlockForceField.FORCEFIELD_TYPE,ffb.getTyp()), 3);
									if(ffb.getTyp()==ForceFieldTyps.Camouflage)
									{
										((TileEntityForceField)worldObj.getTileEntity(png.pos)).setForcefieldCamoblockid(getForcefieldCamoblock());
										((TileEntityForceField)worldObj.getTileEntity(png.pos)).setForcefieldCamoblockmeta(getForcefieldCamoblockMeta());
                                        worldObj.getTileEntity(png.pos).markDirty();


									}
									blockcounter++; // Count create
													// blocks...
								}
								ffb.setSync(true);
							}
						}
					}
				}
			}
		}
	}

	public void destroyField() {
		while (!field_queue.isEmpty()) {
			ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(
					worldObj).getForceFieldStackMap(field_queue.pop());

			if (!ffworldmap.isEmpty()) {
				if (ffworldmap.getProjectorID() == getDeviceID()) {
					ffworldmap.removebyProjector(getDeviceID());

					if (ffworldmap.isSync()) {
						PointXYZ png = ffworldmap.getPoint();
						worldObj.removeTileEntity(png.pos);
						worldObj.setBlockToAir(png.pos);
						// worldObj.setBlockWithNotify(png.X,png.Y,png.Z, 0);
					}

					ffworldmap.setSync(false);
				} else {
					ffworldmap.removebyProjector(getDeviceID());
				}
			}
		}

		Map<Integer, TileEntityProjector> FieldFusion = Linkgrid.getWorldMap(
				worldObj).getFieldFusion();
		for (TileEntityProjector tileentity : FieldFusion.values()) {

			if (tileentity.getPowerSourceID() == this.getPowerSourceID()) {
				if (tileentity.isActive()) {
					tileentity.calculateField(false);
				}
			}
		}
	}

	@Override
	public void invalidate() {
		Linkgrid.getWorldMap(worldObj).getProjektor().remove(getDeviceID());
		destroyField();
		super.invalidate();
	}

	public int Forcepowerneed(int factor) {
		if (!field_def.isEmpty()) {
			return field_def.size()
					* ModularForceFieldSystem.forceFieldBlockCost;
		}
		int forcepower = 0;
		int blocks = 0;

		int tmplength = 1;

		if (this.countItemsInSlot(Slots.Strength) != 0) {
			tmplength = this.countItemsInSlot(Slots.Strength);
		}

		switch (this.getProjektor_Typ()) {
		case 1:
			blocks = ((this.countItemsInSlot(Slots.FocusDown)
					+ this.countItemsInSlot(Slots.FocusLeft)
					+ this.countItemsInSlot(Slots.FocusRight) + this
						.countItemsInSlot(Slots.FocusUp)) + 1) * tmplength;
			break;
		case 2:
			blocks = (this.countItemsInSlot(Slots.FocusDown)
					+ this.countItemsInSlot(Slots.FocusUp) + 1)
					* (this.countItemsInSlot(Slots.FocusLeft)
							+ this.countItemsInSlot(Slots.FocusRight) + 1);
			break;
		case 3:
			blocks = (((this.countItemsInSlot(Slots.Distance) + 2
					+ this.countItemsInSlot(Slots.Distance) + 2) * 4) + 4)
					* (this.countItemsInSlot(Slots.Strength) + 1);
			break;
		case 4:
		case 5:
			blocks = (this.countItemsInSlot(Slots.Distance) * this
					.countItemsInSlot(Slots.Distance)) * 6;
			break;
		}

		forcepower = blocks * ModularForceFieldSystem.forceFieldBlockCost;
		if (factor != 1) {
			forcepower = (forcepower * ModularForceFieldSystem.forceFieldBlockCreateModifier)
					+ (forcepower * 5);
		}
		return forcepower;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (ProjektorItemStacks[i] != null) {
			if (ProjektorItemStacks[i].stackSize <= j) {
				ItemStack itemstack = ProjektorItemStacks[i];
				ProjektorItemStacks[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = ProjektorItemStacks[i].splitStack(j);
			if (ProjektorItemStacks[i].stackSize == 0) {
				ProjektorItemStacks[i] = null;
			}
			return itemstack1;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		ProjektorItemStacks[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return ProjektorItemStacks[i];
	}

	@Override
	public String getName() {
		return "Projektor";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return ProjektorItemStacks.length;
	}

	@Override
	public Container getContainer(InventoryPlayer inventoryplayer) {
		return new ContainerProjector(inventoryplayer.player, this);
	}

	@Override
	public void onNetworkHandlerEvent(int key, String value) {

		if (!this.isActive()) {

			switch (key) {

			case 1:

				if (!isPowersourceItem()) {
					if (getaccesstyp() != 3) {
						if (getaccesstyp() == 2) {
							setaccesstyp(0);
						} else {
							setaccesstyp(getaccesstyp() + 1);
						}
					}
				}

				break;

			}
		}
		super.onNetworkHandlerEvent(key, value);
	}

	@Override
	public List<String> getFieldsforUpdate() {
		List<String> NetworkedFields = new LinkedList<String>();
		NetworkedFields.clear();

		NetworkedFields.addAll(super.getFieldsforUpdate());

		NetworkedFields.add("ProjektorTyp");
		NetworkedFields.add("burnout");
		NetworkedFields.add("camoflage");
		NetworkedFields.add("ForceFieldTexturfile");
		NetworkedFields.add("ForceFieldTexturids");
		NetworkedFields.add("ForcefieldCamoblock");
		NetworkedFields.add("ForcefieldCamoblockmeta");

		return NetworkedFields;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack, int Slot) {

		if (Slot == 1 && par1ItemStack.getItem() instanceof ModuleBase)
			return true;
		if (Slot == 0 && par1ItemStack.getItem() instanceof IPowerLinkItem)
			return true;
		if (Slot == 11
				&& this.hasOption(
						ModularForceFieldSystem.MFFSProjectorOptionCamouflage,
						true))
			return par1ItemStack.getItem() instanceof ItemBlock;

		if (hasValidTypeMod()) {
			ModuleBase modTyp = get_type();

			switch (Slot) {
			case 12:
				if (par1ItemStack.getItem() instanceof ItemProjectorOptionDefenseStation
						&& isPowersourceItem())
					return false;

				if (par1ItemStack.getItem() instanceof ItemCardSecurityLink
						&& isPowersourceItem())
					return false;

				if (par1ItemStack.getItem() instanceof ItemCardSecurityLink)
					return true;

				break;
			case 5:
				if (par1ItemStack.getItem() instanceof ItemProjectorFieldModulatorDistance)
					return modTyp.supportsDistance();
				break;
			case 6:
				if (par1ItemStack.getItem() instanceof ItemProjectorFieldModulatorStrength)
					return modTyp.supportsStrength();
				break;

			case 7:
			case 8:
			case 9:
			case 10:
				if (par1ItemStack.getItem() instanceof ItemProjectorFocusMatrix)
					return modTyp.supportsMatrix();

				break;

			case 2:
			case 3:
			case 4:

				if (isActive())
					return false;

				if (par1ItemStack.getItem() instanceof ItemProjectorOptionTouchDamage) {
					for (int spot = 2; spot <= 4; spot++) {
						if (getStackInSlot(spot) != null) {
							if (getStackInSlot(spot).getItem() instanceof ItemProjectorOptionCamoflage)
								return false;
						}
					}
				}

				if (par1ItemStack.getItem() instanceof ItemProjectorOptionCamoflage) {
					for (int spot = 2; spot <= 4; spot++) {
						if (getStackInSlot(spot) != null) {
							if (getStackInSlot(spot).getItem() instanceof ItemProjectorOptionTouchDamage)
								return false;
						}
					}
				}

				if (!this.hasPowerSource())
					return false;

				if (par1ItemStack.getItem() instanceof ItemProjectorOptionDefenseStation
						&& isPowersourceItem())
					return false;

				if (par1ItemStack.getItem() instanceof ItemProjectorOptionFieldFusion
						&& isPowersourceItem())
					return false;

				if (par1ItemStack.getItem() instanceof ItemProjectorOptionForceFieldJammer
						&& isPowersourceItem())
					return false;

				if (par1ItemStack.getItem() instanceof ItemProjectorOptionBase)
					return modTyp.supportsOption(par1ItemStack.getItem());

				break;
			}

		}

		return false;
	}

	@Override
	public int getSlotStackLimit(int Slot) {
		switch (Slot) {
		case 5: // Distance Slot
		case 6: // Strength Slot
			return 64;

		case 7: // Focus Up
		case 8: // Focus Down
		case 9: // Focus right
		case 10: // Focus left
			return 64;
		}

		return 1;
	}

	public boolean hasValidTypeMod() {

		if (this.getStackInSlot(1) != null
				&& getStackInSlot(1).getItem() instanceof ModuleBase)
			return true;
		return false;
	}

	public ModuleBase get_type() {
		if (hasValidTypeMod())
			return (ModuleBase) this.getStackInSlot(1).getItem();

		return null;
	}

	@Override
	public Set<PointXYZ> getInteriorPoints() {
		return field_interior;
	}

	public Set<PointXYZ> getfield_queue() {
		return field_def;
	}

	@Override
	public TileEntityAdvSecurityStation getLinkedSecurityStation() {
		TileEntityAdvSecurityStation sec = ItemCardSecurityLink
				.getLinkedSecurityStation(this, 12, worldObj);
		if (sec != null) {
			if (this.getaccesstyp() != 3 && !isPowersourceItem())
				this.setaccesstyp(3);
			return sec;
		}

		if (this.getaccesstyp() == 3)
			this.setaccesstyp(0);
		return null;
	}

	public int getSecStation_ID() {
		TileEntityAdvSecurityStation sec = getLinkedSecurityStation();
		if (sec != null)
			return sec.getDeviceID();
		return 0;
	}

	public boolean hasOption(Item item, boolean includecheckall) {

		for (ItemProjectorOptionBase opt : getOptions(includecheckall)) {
			if (opt == item)
				return true;
		}
		return false;
	}

	public List<ItemProjectorOptionBase> getOptions(boolean includecheckall) {
		List<ItemProjectorOptionBase> ret = new ArrayList<ItemProjectorOptionBase>();
		for (int place = 2; place < 5; place++) {
			if (getStackInSlot(place) != null) {
				if (getStackInSlot(place).getItem() instanceof ItemProjectorOptionBase)
					ret.add((ItemProjectorOptionBase) (getStackInSlot(place)
							.getItem()));
			}

			if (includecheckall) {
				for (ItemProjectorOptionBase opt : ItemProjectorOptionBase
						.get_instances()) {
					if (opt instanceof IChecksOnAll && !ret.contains(opt))
						ret.add(opt);
				}
			}

		}

		return ret;
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
	public ItemStack getPowerLinkStack() {
		return this.getStackInSlot(getPowerlinkSlot());
	}

	@Override
	public int getPowerlinkSlot() {
		return 0;
	}


	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

	@Override
	public World getWorldObj() {
		return worldObj;
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
		return null;
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
}
