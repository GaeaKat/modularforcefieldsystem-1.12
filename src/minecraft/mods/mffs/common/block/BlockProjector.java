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

import java.util.Random;

import mods.mffs.common.ModularForceFieldSystem;
import mods.mffs.common.tileentity.TileEntityMachines;
import mods.mffs.common.tileentity.TileEntityProjector;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockProjector extends BlockMFFSBase {
	public BlockProjector(int i) {
		super(i);
	}

	public Icon[] activeFaceIcons = new Icon[9];
	public Icon[] activeSideIcons = new Icon[9];
	public Icon[] inactiveFaceIcons = new Icon[9];
	public Icon[] inactiveSideIcons = new Icon[9];

	@Override
	public void registerIcons(IconRegister iconRegister) {
		activeFaceIcons[0] = iconRegister
				.registerIcon("mffs:Projector/Empty/FaceActive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		activeSideIcons[0] = iconRegister
				.registerIcon("mffs:Projector/Empty/SideActive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		inactiveFaceIcons[0] = iconRegister
				.registerIcon("mffs:Projector/Empty/FaceInactive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		inactiveSideIcons[0] = iconRegister
				.registerIcon("mffs:Projector/Empty/SideInactive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));

		activeFaceIcons[1] = iconRegister
				.registerIcon("mffs:Projector/Wall/FaceActive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		activeSideIcons[1] = iconRegister
				.registerIcon("mffs:Projector/Wall/SideActive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		inactiveFaceIcons[1] = iconRegister
				.registerIcon("mffs:Projector/Wall/FaceInactive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		inactiveSideIcons[1] = iconRegister
				.registerIcon("mffs:Projector/Wall/SideInactive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));

		activeFaceIcons[2] = iconRegister
				.registerIcon("mffs:Projector/Deflector/FaceActive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		activeSideIcons[2] = iconRegister
				.registerIcon("mffs:Projector/Deflector/SideActive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		inactiveFaceIcons[2] = iconRegister
				.registerIcon("mffs:Projector/Deflector/FaceInactive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		inactiveSideIcons[2] = iconRegister
				.registerIcon("mffs:Projector/Deflector/SideInactive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));

		activeFaceIcons[3] = iconRegister
				.registerIcon("mffs:Projector/Tube/FaceActive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		activeSideIcons[3] = iconRegister
				.registerIcon("mffs:Projector/Tube/SideActive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		inactiveFaceIcons[3] = iconRegister
				.registerIcon("mffs:Projector/Tube/FaceInactive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		inactiveSideIcons[3] = iconRegister
				.registerIcon("mffs:Projector/Tube/SideInactive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));

		activeFaceIcons[4] = iconRegister
				.registerIcon("mffs:Projector/Cube/FaceActive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		activeSideIcons[4] = iconRegister
				.registerIcon("mffs:Projector/Cube/SideActive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		inactiveFaceIcons[4] = iconRegister
				.registerIcon("mffs:Projector/Cube/FaceInactive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		inactiveSideIcons[4] = iconRegister
				.registerIcon("mffs:Projector/Cube/SideInactive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));

		activeFaceIcons[5] = iconRegister
				.registerIcon("mffs:Projector/Sphere/FaceActive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		activeSideIcons[5] = iconRegister
				.registerIcon("mffs:Projector/Sphere/SideActive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		inactiveFaceIcons[5] = iconRegister
				.registerIcon("mffs:Projector/Sphere/FaceInactive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		inactiveSideIcons[5] = iconRegister
				.registerIcon("mffs:Projector/Sphere/SideInactive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));

		activeFaceIcons[6] = iconRegister
				.registerIcon("mffs:Projector/Containment/FaceActive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		activeSideIcons[6] = iconRegister
				.registerIcon("mffs:Projector/Containment/SideActive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		inactiveFaceIcons[6] = iconRegister
				.registerIcon("mffs:Projector/Containment/FaceInactive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		inactiveSideIcons[6] = iconRegister
				.registerIcon("mffs:Projector/Containment/SideInactive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));

		activeFaceIcons[7] = iconRegister
				.registerIcon("mffs:Projector/AdvCube/FaceActive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		activeSideIcons[7] = iconRegister
				.registerIcon("mffs:Projector/AdvCube/SideActive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		inactiveFaceIcons[7] = iconRegister
				.registerIcon("mffs:Projector/AdvCube/FaceInactive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		inactiveSideIcons[7] = iconRegister
				.registerIcon("mffs:Projector/AdvCube/SideInactive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));

		activeFaceIcons[8] = iconRegister
				.registerIcon("mffs:Projector/DiagWall/FaceActive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		activeSideIcons[8] = iconRegister
				.registerIcon("mffs:Projector/DiagWall/SideActive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		inactiveFaceIcons[8] = iconRegister
				.registerIcon("mffs:Projector/DiagWall/FaceInactive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		inactiveSideIcons[8] = iconRegister
				.registerIcon("mffs:Projector/DiagWall/SideInactive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));

		blockIcon = inactiveFaceIcons[0];
	}

	@Override
	public Icon getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k,
			int l) {

		int typ = 0;

		TileEntity tileentity = iblockaccess.getBlockTileEntity(i, j, k);

		int facing = (tileentity instanceof TileEntityMachines) ? ((TileEntityMachines) tileentity)
				.getSide() : 1;

		ForgeDirection blockfacing = ForgeDirection.getOrientation(l);
		ForgeDirection TileEntityfacing = ForgeDirection.getOrientation(facing);

		if (tileentity instanceof TileEntityProjector)
			typ = ((TileEntityProjector) tileentity).getProjektor_Typ();

		if (isActive(iblockaccess, i, j, k)) {
			if (blockfacing.equals(TileEntityfacing))
				return activeFaceIcons[typ];

			return activeSideIcons[typ];

		} else {

			if (blockfacing.equals(TileEntityfacing))
				return inactiveFaceIcons[typ];

			return inactiveSideIcons[typ];
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityProjector();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer entityplayer, int side, float par7, float par8,
			float par9) {

		TileEntityProjector tileentity = (TileEntityProjector) world
				.getBlockTileEntity(x, y, z);

		if (tileentity.isBurnout()) {
			return false;
		}

		return super.onBlockActivated(world, x, y, z, entityplayer, side, par7,
				par8, par9);

	}

	@Override
	public void randomDisplayTick(World world, int i, int j, int k,
			Random random) {
		TileEntityProjector tileentity = (TileEntityProjector) world
				.getBlockTileEntity(i, j, k);

		if (tileentity.isBurnout()) {
			double d = i + Math.random();
			double d1 = j + Math.random();
			double d2 = k + Math.random();

			world.spawnParticle("smoke", d, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}
}
