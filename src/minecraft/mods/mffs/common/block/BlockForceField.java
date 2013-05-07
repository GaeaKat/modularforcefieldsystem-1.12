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
package mods.mffs.common.block;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.mffs.api.IFieldTeleporter;
import mods.mffs.api.IForceFieldBlock;
import mods.mffs.api.PointXYZ;
import mods.mffs.common.*;
import mods.mffs.common.WorldMap.ForceFieldWorld;
import mods.mffs.common.item.ItemCardPowerLink;
import mods.mffs.common.multitool.ItemDebugger;
import mods.mffs.common.tileentity.TileEntityCapacitor;
import mods.mffs.common.tileentity.TileEntityForceField;
import mods.mffs.common.tileentity.TileEntityProjector;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockForceField extends BlockContainer implements IForceFieldBlock {
	public static int renderer;
	public int posx;
	public int posy;
	public int posz;

	private final Icon[] icons = new Icon[4];

	public BlockForceField(int i) {
		super(i, Material.glass);
		setBlockUnbreakable();
		setResistance(999F);
		setTickRandomly(true);
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		icons[0] = iconRegister.registerIcon("mffs:Field/Default"
				+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32" : ""));
		icons[1] = iconRegister.registerIcon("mffs:Field/Zapper"
				+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32" : ""));
		icons[2] = iconRegister.registerIcon("mffs:Field/Area"
				+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32" : ""));
		icons[3] = iconRegister.registerIcon("mffs:Field/Containment"
				+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32" : ""));

		blockIcon = icons[0];
	}

	@Override
	public void onBlockAdded(World world, int i, int j, int k) {
		this.posx = i;
		this.posy = j;
		this.posz = k;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderBlockPass() {

		if (ModularForceFieldSystem.proxy.getClientWorld().getBlockMetadata(
				posx, posy, posz) == ForceFieldTyps.Camouflage.ordinal()) {
			TileEntityForceField ForceField = (TileEntityForceField) ModularForceFieldSystem.proxy
					.getClientWorld().getBlockTileEntity(posx, posy, posz);

			if (ForceField != null) {

				if (ForceField.getTexturid(1) == 67
						|| ForceField.getTexturid(1) == 205) {
					return 1;
				} else {
					return 0;
				}

			}
		}
		return 0;
	}

	@Override
	public int getRenderType() {
		return ModularForceFieldSystem.MFFSRENDER_ID;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	protected boolean canSilkHarvest() {
		return false;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z,
			int blockid) {
		if (blockid != ModularForceFieldSystem.MFFSFieldblock.blockID) {
			for (int x1 = -1; x1 <= 1; x1++) {
				for (int y1 = -1; y1 <= 1; y1++) {
					for (int z1 = -1; z1 <= 1; z1++) {
						if (world.getBlockId(x + x1, y + y1, z + z1) != ModularForceFieldSystem.MFFSFieldblock.blockID) {
							if (world.getBlockId(x + x1, y + y1, z + z1) == 0) {
								breakBlock(world, x + x1, y + y1, z + z1, 0, 0);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, int a, int b) {
		ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(world)
				.getForceFieldStackMap(new PointXYZ(i, j, k, world).hashCode());

		if (ffworldmap != null) {
			if (!ffworldmap.isEmpty()) {
				TileEntityProjector Projector = Linkgrid.getWorldMap(world)
						.getProjektor().get(ffworldmap.getProjectorID());
				if (Projector != null) {
					if (!Projector.isActive()) {
						ffworldmap.removebyProjector(ffworldmap
								.getProjectorID());
					} else {
						world.setBlock(i, j, k,
								ModularForceFieldSystem.MFFSFieldblock.blockID);
						world.setBlockMetadataWithNotify(i, j, k,
								ffworldmap.getTyp(), 2);
						world.markBlockForUpdate(i, j, k);
						ffworldmap.setSync(true);

						if (ffworldmap.getTyp() == 1) {
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
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {

		if (world.isRemote)
			return false;

		ItemStack stack = player.getCurrentEquippedItem();
		if (stack == null)
			return false;

		Item item = stack.getItem();
		if (!(item instanceof IFieldTeleporter))
			return false;

		ForceFieldWorld wff = WorldMap.getForceFieldWorld(world);

		ForceFieldBlockStack ffworldmap = wff
				.getForceFieldStackMap(new PointXYZ(x, y, z, world));
		if (ffworldmap != null) {
			int Sec_Gen_ID = -1;
			int First_Gen_ID = ffworldmap.getGenratorID();
			int First_Pro_ID = ffworldmap.getProjectorID();

			TileEntityCapacitor generator = Linkgrid.getWorldMap(world)
					.getCapacitor().get(First_Gen_ID);
			TileEntityProjector projector = Linkgrid.getWorldMap(world)
					.getProjektor().get(First_Pro_ID);

			if (projector != null && generator != null) {

				if (projector.isActive()) {
					boolean passtrue = false;

					switch (projector.getaccesstyp()) {
					case 0:
						passtrue = false;

						String[] ops = ModularForceFieldSystem.Admin.split(";");
						for (int i = 0; i <= ops.length - 1; i++) {
							if (player.username.equalsIgnoreCase(ops[i]))
								passtrue = true;
						}

						List<Slot> slots = player.inventoryContainer.inventorySlots;
						for (Slot slot : slots) {
							ItemStack playerstack = slot.getStack();
							if (playerstack != null) {
								if (playerstack.getItem() instanceof ItemDebugger) {
									passtrue = true;
									break;
								}
							}
						}

						break;
					case 1:
						passtrue = true;
						break;
					case 2:
						passtrue = SecurityHelper.isAccessGranted(generator,
								player, world, SecurityRight.FFB);
						break;
					case 3:
						passtrue = SecurityHelper.isAccessGranted(projector,
								player, world, SecurityRight.FFB);
						break;

					}

					if (passtrue) {
						int typ = 99;
						int ymodi = 0;

						int lm = MathHelper
								.floor_double((player.rotationYaw * 4F) / 360F + 0.5D) & 3;
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
							Sec_Gen_ID = wff.isExistForceFieldStackMap(x, y, z,
									counter, typ, world);
							if (Sec_Gen_ID != 0) {
								counter++;
							}
						}

						if (First_Gen_ID != wff.isExistForceFieldStackMap(x, y,
								z, counter - 1, typ, world)) {

							Functions.ChattoPlayer(player, LanguageRegistry.instance().getStringLocalization
									("fieldSecurity.accessDenied"));
							return false;
						}

						switch (typ) {
						case 0:
							y += counter;
							ymodi = -1;
							break;
						case 1:
							y -= counter;
							ymodi = 1;
							break;
						case 2:
							z += counter;
							break;
						case 3:
							z -= counter;
							break;
						case 4:
							x += counter;
							break;
						case 5:
							x -= counter;
							break;
						}

						Functions.ChattoPlayer(player, LanguageRegistry.instance().getStringLocalization
								("fieldSecurity.accessGranted"));

						if (counter >= 0 && counter <= 5) {

							if ((world.getBlockMaterial(x, y, z).isLiquid() || world
									.isAirBlock(x, y, z))
									&& (world.getBlockMaterial(x, y - ymodi, z)
											.isLiquid() || world.isAirBlock(x,
											y - ymodi, z))) {

								if (y - ymodi <= 0) {
									Functions.ChattoPlayer(player, LanguageRegistry.instance().getStringLocalization
											("fieldSecurity.voidNotAllowed"));
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
										player.setPositionAndUpdate(x + 0.5, y
												- ymodi, z + 0.5);
									} else {
										teleporter
												.onFieldTeleportFailed(
														player,
														stack,
														ModularForceFieldSystem.forceFieldTransportCost);
									}
								}
							} else {

								Functions.ChattoPlayer(player, LanguageRegistry.instance().getStringLocalization
										("fieldSecurity.detectedObstacle"));
							}
						} else {

							Functions.ChattoPlayer(player, LanguageRegistry.instance().getStringLocalization
									("fieldSecurity.fieldTooStrong"));
						}
					} else {
						{
							Functions.ChattoPlayer(player, LanguageRegistry.instance().getStringLocalization
									("fieldSecurity.accessDenied"));
						}
					}
				}

			} else {
				if (projector != null)
					if (projector.getStackInSlot(projector.getPowerlinkSlot()) != null)
						if (!(projector.getStackInSlot(
								projector.getPowerlinkSlot()).getItem() instanceof ItemCardPowerLink))
							Functions.ChattoPlayer(player, LanguageRegistry.instance().getStringLocalization
									("fieldSecurity.invalidItemInPowerLink"));
			}
		}

		return false;
	}

	@Override
	public void onBlockClicked(World world, int x, int y, int z,
			EntityPlayer player) {

		if (world.isRemote)
			return;

		ForceFieldBlockStack ffworldmap = WorldMap
				.getForceFieldWorld(world).getForceFieldStackMap(
						new PointXYZ(x, y, z, world).hashCode());

		if (ffworldmap != null && !ModularForceFieldSystem.adventureMapMode) {

			TileEntityProjector projector = Linkgrid.getWorldMap(world)
					.getProjektor().get(ffworldmap.getProjectorID());
			if (projector != null) {
				switch (projector.getaccesstyp()) {

				case 0:
					player.attackEntityFrom(
							MFFSDamageSource.fieldShock, 10);
					Functions.ChattoPlayer(player, LanguageRegistry.instance().getStringLocalization("field" +
							".highEnergyField"));
					break;

				case 2:
				case 3:
					if (!SecurityHelper.isAccessGranted(projector,
							player, world, SecurityRight.SR)) {
						player.attackEntityFrom(
								MFFSDamageSource.fieldShock, 10);
						Functions.ChattoPlayer(player, LanguageRegistry.instance().getStringLocalization("field" +
								".highEnergyField"));
					}
					break;
				}
			}

			if (!SecurityHelper.isAccessGranted(projector, player,
					world, SecurityRight.SR)) {
				player.attackEntityFrom(MFFSDamageSource.fieldShock,
						10);
				Functions.ChattoPlayer(player, LanguageRegistry.instance().getStringLocalization("field" +
						".highEnergyField"));
			}
		}

		Random random = null;
		updateTick(world, x, y, z, random);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i,
			int j, int k) {
		if (world.getBlockMetadata(i, j, k) == ForceFieldTyps.Zapper.ordinal()) {
			float f = 0.0625F;
			return AxisAlignedBB.getBoundingBox(i + f, j + f, k + f, i + 1 - f,
					j + 1 - f, k + 1 - f);
		}

		return AxisAlignedBB.getBoundingBox(i, j, k, i + 1, j + 1, k + 1);

	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i,
			int j, int k) {
		return AxisAlignedBB.getBoundingBox(i, j, k, i + 0, j + 0, k + 0);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k,
			Entity entity) {

		if (world.getBlockMetadata(i, j, k) == ForceFieldTyps.Zapper.ordinal()) {
			if (entity instanceof EntityLiving) {
				entity.attackEntityFrom(MFFSDamageSource.fieldShock, 10);
			}
		} else {
			if (entity instanceof EntityPlayer) {
				ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(
						world).getorcreateFFStackMap(i, j, k, world);

				if (ffworldmap != null) {

					TileEntityProjector projector = Linkgrid.getWorldMap(world)
							.getProjektor().get(ffworldmap.getProjectorID());

					if (projector != null) {

						boolean passtrue = false;

						switch (projector.getaccesstyp()) {
						case 0:
							passtrue = false;
							if (ModularForceFieldSystem.Admin
									.equals(((EntityPlayer) entity).username))
								passtrue = true;
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
						Functions.ChattoPlayer((EntityPlayer) entity, LanguageRegistry.instance()
								.getStringLocalization("field.highEnergyField"));
					}
				}
			}

		}

	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int x,
			int y, int z, int side) {
		int xCord = x;
		int yCord = y;
		int zCord = z;

		switch (side) {
		case 0:
			yCord++;
			break;
		case 1:
			yCord--;
			break;
		case 2:
			zCord++;
			break;
		case 3:
			zCord--;
			break;
		case 4:
			xCord++;
			break;
		case 5:
			xCord--;
			break;
		}

		if (blockID == iblockaccess.getBlockId(x, y, z)
				&& iblockaccess.getBlockMetadata(x, y, z) == iblockaccess
						.getBlockMetadata(xCord, yCord, zCord))
			return false;

		return super.shouldSideBeRendered(iblockaccess, x, y, z, side);

	}

	@Override
	public Icon getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k,
			int l) {
		int metadata = iblockaccess.getBlockMetadata(i, j, k);

		if (metadata == ForceFieldTyps.Default.ordinal())
			return icons[0];
		if (metadata == ForceFieldTyps.Zapper.ordinal())
			return icons[1];
		if (metadata == ForceFieldTyps.Area.ordinal())
			return icons[2];
		if (metadata == ForceFieldTyps.Containment.ordinal())
			return icons[3];

		return icons[0];
	}

	@Override
	public float getExplosionResistance(Entity entity, World world, int i,
			int j, int k, double d, double d1, double d2) {
		ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(world)
				.getForceFieldStackMap(new PointXYZ(i, j, k, world).hashCode());
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
	public void randomDisplayTick(World world, int i, int j, int k,
			Random random) {
		if (ModularForceFieldSystem.showZapperParticles
				&& world.getBlockMetadata(i, j, k) == ForceFieldTyps.Zapper
						.ordinal()) {
			double d = i + Math.random() + 0.2D;
			double d1 = j + Math.random() + 0.2D;
			double d2 = k + Math.random() + 0.2D;

			world.spawnParticle("townaura", d, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public boolean canConnectRedstone(IBlockAccess iba, int i, int j, int k,
			int dir) {
		return false;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(world)
				.getForceFieldStackMap(new PointXYZ(x, y, z, world).hashCode());

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
			world.removeBlockTileEntity(x, y, z);
			world.setBlock(x, y, z, 0, 0, 2);
		}
	}

	@Override
	public TileEntity createTileEntity(World world, int meta) {
		if (meta == ForceFieldTyps.Camouflage.ordinal()) {

			return new TileEntityForceField();

		}

		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return createTileEntity(world, 0);
	}

	@Override
	public void weakenForceField(World world, int x, int y, int z) {
		if (ModularForceFieldSystem.influencedByOtherMods) {
			world.setBlock(x, y, z, 0, 0, 2);
		}
	}
}
