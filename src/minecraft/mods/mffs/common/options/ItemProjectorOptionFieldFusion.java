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

package mods.mffs.common.options;

import java.util.Map;

import mods.mffs.api.PointXYZ;
import mods.mffs.common.ForceFieldBlockStack;
import mods.mffs.common.Linkgrid;
import mods.mffs.common.ModularForceFieldSystem;
import mods.mffs.common.WorldMap;
import mods.mffs.common.tileentity.TileEntityProjector;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.world.World;

public class ItemProjectorOptionFieldFusion extends ItemProjectorOptionBase
		implements IInteriorCheck {
	public ItemProjectorOptionFieldFusion(int i) {
		super(i);
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("mffs:options/FieldFusion");
	}

	public boolean checkFieldFusioninfluence(PointXYZ png, World world,
			TileEntityProjector Proj) {

		Map<Integer, TileEntityProjector> InnerMap = null;
		InnerMap = Linkgrid.getWorldMap(world).getFieldFusion();
		for (TileEntityProjector tileentity : InnerMap.values()) {

			boolean logicswitch = false;
			if (!Proj.isPowersourceItem())
				logicswitch = tileentity.getPowerSourceID() == Proj
						.getPowerSourceID()
						&& tileentity.getDeviceID() != Proj.getDeviceID();

			if (logicswitch && tileentity.isActive()) {
				for (PointXYZ tpng : tileentity.getInteriorPoints()) {
					if (tpng.X == png.X && tpng.Y == png.Y && tpng.Z == png.Z)
						return true;
				}
			}
		}
		return false;
	}

	@Override
	public void checkInteriorBlock(PointXYZ png, World world,
			TileEntityProjector Proj) {

		ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(world)
				.getorcreateFFStackMap(png.X, png.Y, png.Z, world);

		if (!ffworldmap.isEmpty()) {
			if (ffworldmap.getGenratorID() == Proj.getPowerSourceID()) {
				TileEntityProjector Projector = Linkgrid.getWorldMap(world)
						.getProjektor().get(ffworldmap.getProjectorID());

				if (Projector != null) {
					if (Projector
							.hasOption(
									ModularForceFieldSystem.MFFSProjectorOptionFieldFusion,
									true)) {
						Projector.getfield_queue().remove(png);
						ffworldmap.removebyProjector(Projector.getDeviceID());

						PointXYZ ffpng = ffworldmap.getPoint();

						if (world.getBlockId(ffpng.X, ffpng.Y, ffpng.Z) == ModularForceFieldSystem.MFFSFieldblock.blockID) {
							world.removeBlockTileEntity(ffpng.X, ffpng.Y,
									ffpng.Z);
							world.setBlock(ffpng.X, ffpng.Y, ffpng.Z, 0, 0, 2);
						}
					}
				}
			}
		}

	}

}