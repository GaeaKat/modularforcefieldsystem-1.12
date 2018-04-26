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
package com.nekokittygames.mffs.common.block;

import java.util.Objects;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.nekokittygames.mffs.api.IFieldTeleporter;
import com.nekokittygames.mffs.api.IForceFieldBlock;
import com.nekokittygames.mffs.api.PointXYZ;
import com.nekokittygames.mffs.common.ForceFieldBlockStack;
import com.nekokittygames.mffs.common.ForceFieldTyps;
import com.nekokittygames.mffs.common.Functions;
import com.nekokittygames.mffs.common.Linkgrid;
import com.nekokittygames.mffs.common.MFFSDamageSource;
import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.SecurityHelper;
import com.nekokittygames.mffs.common.SecurityRight;
import com.nekokittygames.mffs.common.WorldMap;
import com.nekokittygames.mffs.common.WorldMap.ForceFieldWorld;
import com.nekokittygames.mffs.common.item.ItemCardPowerLink;
import com.nekokittygames.mffs.common.multitool.ItemDebugger;
import com.nekokittygames.mffs.common.tileentity.TileEntityCapacitor;
import com.nekokittygames.mffs.common.tileentity.TileEntityForceField;
import com.nekokittygames.mffs.common.tileentity.TileEntityProjector;
import com.nekokittygames.mffs.libs.LibBlockNames;
import com.nekokittygames.mffs.libs.LibMisc;

public class BlockForceField extends Block implements IForceFieldBlock ,ITileEntityProvider {
	public static PropertyEnum<ForceFieldTyps> FORCEFIELD_TYPE=PropertyEnum.create("type",ForceFieldTyps.class);
	public static int renderer;
	public BlockPos blockPos;



	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this,FORCEFIELD_TYPE);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FORCEFIELD_TYPE).ordinal();
	}

	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int eventID, int eventParam)
	{
		super.eventReceived(state, worldIn, pos, eventID, eventParam);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity != null && tileentity.receiveClientEvent(eventID, eventParam);
	}
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FORCEFIELD_TYPE,ForceFieldTyps.values()[meta]);
	}

	public BlockForceField() {
		super(Material.GLASS);
		setBlockUnbreakable();
		setResistance(999F);
		setTickRandomly(true);

		this.setDefaultState(blockState.getBaseState().withProperty(FORCEFIELD_TYPE,ForceFieldTyps.Default));
		this.setRegistryName(ModularForceFieldSystem.MODID, LibBlockNames.FORCE_FIELD);
		final ResourceLocation registryName = Objects.requireNonNull(this.getRegistryName());
		this.setUnlocalizedName(registryName.toString());
	}


	@Override
	public int tickRate(World worldIn) {
		return 5;
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		this.blockPos=pos;
	}





	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return false;
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		IBlockState bs=world.getBlockState(neighbor);
		if (bs.getBlock()!= ModBlocks.FORCE_FIELD) {
			for (int x1 = -1; x1 <= 1; x1++) {
				for (int y1 = -1; y1 <= 1; y1++) {
					for (int z1 = -1; z1 <= 1; z1++) {
						if (world.getBlockState(pos.add(x1, y1, z1)) != ModBlocks.FORCE_FIELD) {
							if (world.getBlockState(pos.add(x1, y1, z1)) == Blocks.AIR) {
								breakBlock((World)world,pos.add(x1, y1, z1),bs);
							}
						}
					}
				}
			}
		}
	}


	@Override
	public int getLightValue(IBlockState state) {
		if(state.getValue(FORCEFIELD_TYPE)==ForceFieldTyps.Light)
				return 255;
			else
				return super.getLightValue(state);
	}

	public void breakBlock(World world, BlockPos pos, IBlockState block) {
		ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(world)
				.getForceFieldStackMap(new PointXYZ(pos, world).hashCode());

		if (ffworldmap != null) {
			if (!ffworldmap.isEmpty()) {
				TileEntityProjector Projector = Linkgrid.getWorldMap(world)
						.getProjektor().get(ffworldmap.getProjectorID());
				if (Projector != null) {
					if (!Projector.isActive()) {
						ffworldmap.removebyProjector(ffworldmap
								.getProjectorID());
					} else {
						world.setBlockState(pos,ModBlocks.FORCE_FIELD.getDefaultState().withProperty(FORCEFIELD_TYPE,ffworldmap.getTyp()));
						world.markAndNotifyBlock(pos,world.getChunkFromBlockCoords(pos),world.getBlockState(pos),ModBlocks.FORCE_FIELD.getDefaultState().withProperty(FORCEFIELD_TYPE,ffworldmap.getTyp()),3);
						ffworldmap.setSync(true);

						if (ffworldmap.getTyp() == ForceFieldTyps.Default) {
							Projector
									.consumePower(
											ModularForceFieldSystem.forceFieldBlockCost
													* ModularForceFieldSystem.forceFieldBlockCreateModifier,
											false);
						} else {
							Projector
									.consumePower(
											ModularForceFieldSystem.forceFieldBlockCost
													* ModularForceFieldSystem.forceFieldBlockCreateModifier
													* ModularForceFieldSystem.forceFieldBlockZapperModifier,
											false);
						}
					}
				}
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return false;

		ItemStack stack = player.getHeldItem(hand);
		if (stack == null)
			return false;


		Item item = stack.getItem();
		boolean debugger = (item instanceof ItemDebugger);
		if (!(item instanceof IFieldTeleporter) && !debugger)
			return false;

		ForceFieldWorld wff = WorldMap.getForceFieldWorld(world);

		ForceFieldBlockStack ffworldmap = wff
				.getForceFieldStackMap(new PointXYZ(pos, world));
		if (ffworldmap != null) {
			int Sec_Gen_ID = -1;
			int First_Gen_ID = ffworldmap.getGenratorID();
			int First_Pro_ID = ffworldmap.getProjectorID();

			TileEntityCapacitor generator = Linkgrid.getWorldMap(world)
					.getCapacitor().get(First_Gen_ID);
			TileEntityProjector projector = Linkgrid.getWorldMap(world)
					.getProjektor().get(First_Pro_ID);

			if (projector != null && generator != null) {
				// Debugger can be spawned in and used to kill force fields.
				if(debugger) {
					projector.setBurnedOut(true);
					player.sendMessage(new TextComponentTranslation("mffs.burntOut",projector.getPos().toString()));
					return true;
				}

				if (projector.isActive()) {
					boolean passThrough = false;

					switch (projector.getaccesstyp()) {
						case 0:
							passThrough = false;

							String[] ops = ModularForceFieldSystem.Admin.split(";");
							for (int i = 0; i <= ops.length - 1; i++) {
								if (player.getUniqueID().toString().equalsIgnoreCase(ops[i]))
									passThrough = true;
							}

							break;
						case 1:
							passThrough = true;
							break;
						case 2:
							passThrough = SecurityHelper.isAccessGranted(generator,
									player, world, SecurityRight.FFB);
							break;
						case 3:
							passThrough = SecurityHelper.isAccessGranted(projector,
									player, world, SecurityRight.FFB);
							break;

					}

					if (passThrough) {
						int typ = 99;
						int ymodi = 0;

						int lm = MathHelper
								.floor((player.rotationYaw * 4F) / 360F + 0.5D) & 3;
						int i1 = Math.round(player.rotationPitch);

						if (i1 >= 65) { // Facing 1
							typ = 1;
						} else if (i1 <= -65) { // Facing 0
							typ = 0;
						} else if (lm == 0) { // Facing 2
							typ = 2;
						} else if (lm == 1) { // Facing 5
							typ = 5;
						} else if (lm == 2) { // Facing 3
							typ = 3;
						} else if (lm == 3) { // Facing 4
							typ = 4;
						}

						int counter = 0;
						while (Sec_Gen_ID != 0) {
							Sec_Gen_ID = wff.isExistForceFieldStackMap(pos,
									counter, typ, world);
							if (Sec_Gen_ID != 0) {
								counter++;
							}
						}

						if (First_Gen_ID != wff.isExistForceFieldStackMap(pos, counter - 1, typ, world)) {

							Functions.ChattoPlayer(player, "fieldSecurity.accessDenied");
							return false;
						}
						BlockPos newPos=new BlockPos(pos);
						switch (typ) {
							case 0:
								newPos=newPos.add(0,counter,0);
								ymodi = -1;
								break;
							case 1:
								newPos=newPos.add(0,-counter,0);
								ymodi = 1;
								break;
							case 2:
								newPos=newPos.add(0,0,counter);
								break;
							case 3:
								newPos=newPos.add(0,0,-counter);
								break;
							case 4:
								newPos=newPos.add(counter,0,0);
								break;
							case 5:
								newPos=newPos.add(-counter,0,0);
								break;
						}

						Functions.ChattoPlayer(player, "fieldSecurity.accessGranted");

						if (counter >= 0 && counter <= 5) {

							if ((world.getBlockState(newPos).getMaterial().isLiquid() || world
									.isAirBlock(newPos))
									&& (world.getBlockState(newPos.add(0,-ymodi,0)).getMaterial()
									.isLiquid() || world.isAirBlock(newPos.add(0,-ymodi,0)))) {

								if (newPos.getY() - ymodi <= 0) {
									Functions.ChattoPlayer(player, "fieldSecurity.voidNotAllowed");
								} else {
									IFieldTeleporter teleporter = (IFieldTeleporter) item;
									if (teleporter
											.canFieldTeleport(
													player,
													stack,
													ModularForceFieldSystem.forceFieldTransportCost)) {
										teleporter
												.onFieldTeleportSuccess(
														player,
														stack,
														ModularForceFieldSystem.forceFieldTransportCost);
										player.setPositionAndUpdate(newPos.getX() + 0.5, newPos.getY()
												- ymodi, newPos.getZ() + 0.5);
									} else {
										teleporter
												.onFieldTeleportFailed(
														player,
														stack,
														ModularForceFieldSystem.forceFieldTransportCost);
									}
								}
							} else {

								Functions.ChattoPlayer(player, "fieldSecurity.detectedObstacle");
							}
						} else {

							Functions.ChattoPlayer(player, "fieldSecurity.fieldTooStrong");
						}
					} else {
						{
							Functions.ChattoPlayer(player, "fieldSecurity.accessDenied");
						}
					}
				}

			} else {
				if (projector != null)
					if (projector.getStackInSlot(projector.getPowerlinkSlot()) != null)
						if (!(projector.getStackInSlot(
								projector.getPowerlinkSlot()).getItem() instanceof ItemCardPowerLink))
							Functions.ChattoPlayer(player, "fieldSecurity.invalidItemInPowerLink");
			}
		}

		return false;
	}


	@Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
		if (world.isRemote)
			return;

		ForceFieldBlockStack ffworldmap = WorldMap
				.getForceFieldWorld(world).getForceFieldStackMap(
						new PointXYZ(pos, world).hashCode());

		if (ffworldmap != null && !ModularForceFieldSystem.adventureMapMode) {

			TileEntityProjector projector = Linkgrid.getWorldMap(world)
					.getProjektor().get(ffworldmap.getProjectorID());
			if (projector != null) {
				switch (projector.getaccesstyp()) {

					case 0:
						player.attackEntityFrom(
								MFFSDamageSource.fieldShock, 10);
						Functions.ChattoPlayer(player, "field.highEnergyField");
						break;

					case 2:
					case 3:
						if (!SecurityHelper.isAccessGranted(projector,
								player, world, SecurityRight.SR)) {
							player.attackEntityFrom(
									MFFSDamageSource.fieldShock, 10);
							Functions.ChattoPlayer(player, "field.highEnergyField");
						}
						break;
				}
			}

			if (!SecurityHelper.isAccessGranted(projector, player,
					world, SecurityRight.SR)) {
				player.attackEntityFrom(MFFSDamageSource.fieldShock,
						10);
				Functions.ChattoPlayer(player, "field.highEnergyField");
			}
		}

		Random random = null;
		updateTick(world, pos,world.getBlockState(pos), random);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TileEntity te=worldIn.getTileEntity(pos);
		if(te!=null && te instanceof TileEntityForceField) {
			TileEntityForceField tef= (TileEntityForceField) te;
			Block block = tef.getForcefieldCamoblock();
			if (block != null)
				return block.getActualState(block.getStateFromMeta(tef.getForcefieldCamoblockmeta()), worldIn, pos);
			else
				return super.getActualState(state, worldIn, pos);
		}
		else
			return super.getActualState(state, worldIn, pos);
	}


	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos pos) {
		if (blockState.getValue(FORCEFIELD_TYPE) == ForceFieldTyps.Zapper) {
			float f = 0.0625F;
			return new AxisAlignedBB(pos.getX()+f,pos.getY()+f,pos.getZ()+f,pos.getX()+1-f,pos.getY()+1-f,pos.getZ()+1-f);
		}

		return super.getCollisionBoundingBox(blockState,world,pos);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		return getBoundingBox(state,worldIn,pos);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		if (state.getValue(FORCEFIELD_TYPE) == ForceFieldTyps.Zapper) {
			if (entity instanceof EntityLiving) {
				entity.attackEntityFrom(MFFSDamageSource.fieldShock, 10);
			}
		} else {
			if (entity instanceof EntityPlayer) {
				ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(
						world).getorcreateFFStackMap(pos, world);

				if (ffworldmap != null) {

					TileEntityProjector projector = Linkgrid.getWorldMap(world)
							.getProjektor().get(ffworldmap.getProjectorID());

					if (projector != null) {

						boolean passtrue = false;

						switch (projector.getaccesstyp()) {
							case 0:
								passtrue = false;
								for (String op : ModularForceFieldSystem.Admin.split(";")) {
									if (entity.getUniqueID().toString().equalsIgnoreCase(op)) {
										passtrue = true;
										break;
									}
								}
								break;
							case 1:
								passtrue = true;
								break;
							case 2:
								TileEntityCapacitor generator = Linkgrid
										.getWorldMap(world).getCapacitor()
										.get(ffworldmap.getGenratorID());
								passtrue = SecurityHelper.isAccessGranted(
										generator, ((EntityPlayer) entity), world,
										SecurityRight.FFB);
								break;
							case 3:
								passtrue = SecurityHelper.isAccessGranted(
										projector, ((EntityPlayer) entity), world,
										SecurityRight.FFB);
								break;
						}

						if (!passtrue) {
							((EntityPlayer) entity).attackEntityFrom(
									MFFSDamageSource.fieldShock, 20);

						} else {
							((EntityPlayer) entity).attackEntityFrom(
									MFFSDamageSource.fieldShock, 1);
						}
						Functions.ChattoPlayer((EntityPlayer) entity, "field.highEnergyField");
					}
				}
			}

		}
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();

			if (blockState != iblockstate)
			{
				return true;
			}

			if (block == this)
			{
				return false;
			}
		return !false && block == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
	{
		return layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.CUTOUT;
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
		ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(world)
				.getForceFieldStackMap(new PointXYZ(pos, world).hashCode());
		if (ffworldmap != null && !ffworldmap.isEmpty()) {

			TileEntity tileEntity = Linkgrid.getWorldMap(world).getProjektor()
					.get(ffworldmap.getProjectorID());

			if (tileEntity instanceof TileEntityProjector && tileEntity != null) {
				((TileEntityProjector) tileEntity)
						.consumePower(
								ModularForceFieldSystem.forceFieldBlockCost
										* ModularForceFieldSystem.forceFieldBlockCreateModifier,
								false);
			}
		}

		return 999F;
	}


	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (ModularForceFieldSystem.showZapperParticles
				&& stateIn.getValue(FORCEFIELD_TYPE) == ForceFieldTyps.Zapper) {
			double d = pos.getX() + Math.random() + 0.2D;
			double d1 = pos.getY() + Math.random() + 0.2D;
			double d2 = pos.getZ() + Math.random() + 0.2D;

			worldIn.spawnParticle(EnumParticleTypes.TOWN_AURA,d, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}


	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(world)
				.getForceFieldStackMap(new PointXYZ(pos, world).hashCode());

		if (ffworldmap != null) {
			if (!ffworldmap.isEmpty()) {
				TileEntityProjector Projector = Linkgrid.getWorldMap(world)
						.getProjektor().get(ffworldmap.getProjectorID());
				if (Projector != null) {
					if (!Projector.isActive()) {
						ffworldmap.removebyProjector(ffworldmap
								.getProjectorID());
					}
				}
			}
		}

		if (ffworldmap == null || ffworldmap.isEmpty()) {
			world.removeTileEntity(pos);
			world.setBlockState(pos,Blocks.AIR.getDefaultState(),2);
		}
		world.markBlockRangeForRenderUpdate(pos,pos);
	}






	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		if (meta == ForceFieldTyps.Camouflage.ordinal()) {

			return new TileEntityForceField();

		}

		return null;
	}



	@Override
	public void weakenForceField(World world, BlockPos pos) {
		if (ModularForceFieldSystem.influencedByOtherMods) {
			world.setBlockState(pos,Blocks.AIR.getDefaultState(),2);
		}
	}
}
