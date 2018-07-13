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

package com.nekokittygames.mffs.common.modules;

import com.nekokittygames.mffs.api.PointXYZ;
import com.nekokittygames.mffs.common.IModularProjector;
import com.nekokittygames.mffs.common.IModularProjector.Slots;
import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.item.ModItems;
import com.nekokittygames.mffs.common.options.*;
import com.nekokittygames.mffs.common.tileentity.TileEntityProjector;
import com.nekokittygames.mffs.libs.LibItemNames;
import com.nekokittygames.mffs.libs.LibMisc;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.Set;

public class ItemProjectorModuleTube extends Module3DBase {
	public ItemProjectorModuleTube() {
		super(LibItemNames.MODULE_TUBE);
		this.setForceFieldModuleType(2);
	}

	@Override
	public boolean supportsDistance() {
		return true;
	}

	@Override
	public boolean supportsStrength() {
		return true;
	}

	@Override
	public boolean supportsMatrix() {
		return false;
	}

	@Override
	public void calculateField(IModularProjector projector,
			Set<PointXYZ> ffLocs, Set<PointXYZ> ffInterior) {

		int tpx = 0;
		int tpy = 0;
		int tpz = 0;
		int x_offset_s = 0;
		int y_offset_s = 0;
		int z_offset_s = 0;
		int x_offset_e = 0;
		int y_offset_e = 0;
		int z_offset_e = 0;

		int distance = projector.countItemsInSlot(Slots.Distance) + 2;
		int Strength = projector.countItemsInSlot(Slots.Strength);

		if (projector.getSide() == EnumFacing.UP || projector.getSide() == EnumFacing.DOWN) {
			tpy = Strength;
			tpx = distance;
			tpz = distance;

			y_offset_s = Strength - Strength;
			if (((TileEntityProjector) projector).hasOption(
					ModItems.OPTION_FIELD_MANIPULATOR, true)) {
				if (projector.getSide() == EnumFacing.UP) {
					y_offset_e = Strength;
				}
				if (projector.getSide() == EnumFacing.DOWN) {
					y_offset_s = Strength;
				}
			}
		}

		if (projector.getSide() == EnumFacing.NORTH || projector.getSide() == EnumFacing.SOUTH) {
			tpy = distance;
			tpz = Strength;
			tpx = distance;

			z_offset_s = Strength - Strength;
			if (((TileEntityProjector) projector).hasOption(
					ModItems.OPTION_FIELD_MANIPULATOR, true)) {
				if (projector.getSide() == EnumFacing.NORTH) {
					z_offset_e = Strength;
				}
				if (projector.getSide() == EnumFacing.SOUTH) {
					z_offset_s = Strength;
				}
			}
		}
		if (projector.getSide() == EnumFacing.WEST || projector.getSide() == EnumFacing.EAST) {
			tpy = distance;
			tpz = distance;
			tpx = Strength;

			x_offset_s = Strength - Strength;
			if (((TileEntityProjector) projector).hasOption(
					ModItems.OPTION_FIELD_MANIPULATOR, true)) {
				if (projector.getSide() == EnumFacing.WEST) {
					x_offset_e = Strength;
				}
				if (projector.getSide() == EnumFacing.EAST) {
					x_offset_s = Strength;
				}
			}
		}

		for (int z1 = 0 - tpz + z_offset_s; z1 <= tpz - z_offset_e; z1++) {
			for (int x1 = 0 - tpx + x_offset_s; x1 <= tpx - x_offset_e; x1++) {
				for (int y1 = 0 - tpy + y_offset_s; y1 <= tpy - y_offset_e; y1++) {
					int tpx_temp = tpx;
					int tpy_temp = tpy;
					int tpz_temp = tpz;

					if (tpx == Strength
							&& (projector.getSide() == EnumFacing.WEST || projector.getSide() == EnumFacing.EAST)) {
						tpx_temp += 1;
					}
					if (tpy == Strength
							&& (projector.getSide() == EnumFacing.UP || projector.getSide() == EnumFacing.DOWN)) {
						tpy_temp += 1;
					}
					if (tpz == Strength
							&& (projector.getSide() == EnumFacing.NORTH || projector.getSide() == EnumFacing.SOUTH)) {
						tpz_temp += 1;
					}

					if ((x1 == 0 - tpx_temp || x1 == tpx_temp
							|| y1 == 0 - tpy_temp || y1 == tpy_temp
							|| z1 == 0 - tpz_temp || z1 == tpz_temp)
							&& ((((TileEntityProjector) projector).getPos().getY()+ y1) >= 0)) {
						ffLocs.add(new PointXYZ(new BlockPos(x1, y1, z1), 0));
					} else {
						ffInterior.add(new PointXYZ(x1, y1, z1, 0));
					}
				}
			}
		}

	}

	public static boolean supportsOption(ItemProjectorOptionBase item) {

		if (item instanceof ItemProjectorOptionCamoflage)
			return true;
		if (item instanceof ItemProjectorOptionFieldFusion)
			return true;
		if (item instanceof ItemProjectorOptionFieldManipulator)
			return true;
		if (item instanceof ItemProjectorOptionForceFieldJammer)
			return true;
		if (item instanceof ItemProjectorOptionSponge)
			return true;
		if (item instanceof ItemProjectorOptionBlockBreaker)
			return true;
		if (item instanceof ItemProjectorOptionTouchDamage)
			return true;
		if(item instanceof ItemProjectorOptionLight)
			return true;
		return false;

	}

	@Override
	public boolean supportsOption(Item item) {

		if (item instanceof ItemProjectorOptionCamoflage)
			return true;
		if (item instanceof ItemProjectorOptionFieldFusion)
			return true;
		if (item instanceof ItemProjectorOptionFieldManipulator)
			return true;
		if (item instanceof ItemProjectorOptionForceFieldJammer)
			return true;
		if (item instanceof ItemProjectorOptionSponge)
			return true;
		if (item instanceof ItemProjectorOptionBlockBreaker)
			return true;
		if (item instanceof ItemProjectorOptionTouchDamage)
			return true;
		if(item instanceof ItemProjectorOptionLight)
			return true;
		return false;
	}

}