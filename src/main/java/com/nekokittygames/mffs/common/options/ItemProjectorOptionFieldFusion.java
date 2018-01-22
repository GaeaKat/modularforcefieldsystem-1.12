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

package com.nekokittygames.mffs.common.options;

import java.util.Map;

import com.nekokittygames.mffs.api.PointXYZ;
import com.nekokittygames.mffs.common.ForceFieldBlockStack;
import com.nekokittygames.mffs.common.Linkgrid;
import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.WorldMap;
import com.nekokittygames.mffs.common.block.ModBlocks;
import com.nekokittygames.mffs.common.item.ModItems;
import com.nekokittygames.mffs.common.tileentity.TileEntityProjector;
import com.nekokittygames.mffs.libs.LibItemNames;
import com.nekokittygames.mffs.libs.LibMisc;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class ItemProjectorOptionFieldFusion extends ItemProjectorOptionBase
		implements IInteriorCheck {
	public ItemProjectorOptionFieldFusion() {
		super();
		setUnlocalizedName(LibMisc.UNLOCALIZED_PREFIX+ LibItemNames.OPTION_FIELD_FUSION);
		setRegistryName(LibItemNames.OPTION_FIELD_FUSION);
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
					if (tpng.pos.equals(png.pos))
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
				.getorcreateFFStackMap(png.pos, world);

		if (!ffworldmap.isEmpty()) {
			if (ffworldmap.getGenratorID() == Proj.getPowerSourceID()) {
				TileEntityProjector Projector = Linkgrid.getWorldMap(world)
						.getProjektor().get(ffworldmap.getProjectorID());

				if (Projector != null) {
					if (Projector
							.hasOption(
									ModItems.OPTION_FIELD_FUSION,
									true)) {
						Projector.getfield_queue().remove(png);
						ffworldmap.removebyProjector(Projector.getDeviceID());

						PointXYZ ffpng = ffworldmap.getPoint();

						if (world.getBlockState(ffpng.pos).getBlock() == ModBlocks.FORCE_FIELD) {
							world.removeTileEntity(ffpng.pos);
							world.setBlockState(ffpng.pos, Blocks.AIR.getDefaultState(),2);
						}
					}
				}
			}
		}

	}

}