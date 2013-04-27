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

package mods.mffs.common.modules;

import java.util.Set;

import mods.mffs.api.PointXYZ;
import mods.mffs.common.IModularProjector;
import mods.mffs.common.ModularForceFieldSystem;
import mods.mffs.common.IModularProjector.Slots;
import mods.mffs.common.options.ItemProjectorOptionBase;
import mods.mffs.common.options.ItemProjectorOptionBlockBreaker;
import mods.mffs.common.options.ItemProjectorOptionCamoflage;
import mods.mffs.common.options.ItemProjectorOptionFieldFusion;
import mods.mffs.common.options.ItemProjectorOptionFieldManipulator;
import mods.mffs.common.options.ItemProjectorOptionForceFieldJammer;
import mods.mffs.common.options.ItemProjectorOptionSponge;
import mods.mffs.common.options.ItemProjectorOptionTouchDamage;
import mods.mffs.common.tileentity.TileEntityProjector;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

public class ItemProjectorModuleTube extends Module3DBase {
	public ItemProjectorModuleTube(int i) {
		super(i);
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("mffs:modules/Tube");
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

		if (projector.getSide() == 0 || projector.getSide() == 1) {
			tpy = Strength;
			tpx = distance;
			tpz = distance;

			y_offset_s = Strength - Strength;
			if (((TileEntityProjector) projector).hasOption(
					ModularForceFieldSystem.MFFSProjectorOptionDome, true)) {
				if (projector.getSide() == 0) {
					y_offset_e = Strength;
				}
				if (projector.getSide() == 1) {
					y_offset_s = Strength;
				}
			}
		}

		if (projector.getSide() == 2 || projector.getSide() == 3) {
			tpy = distance;
			tpz = Strength;
			tpx = distance;

			z_offset_s = Strength - Strength;
			if (((TileEntityProjector) projector).hasOption(
					ModularForceFieldSystem.MFFSProjectorOptionDome, true)) {
				if (projector.getSide() == 2) {
					z_offset_e = Strength;
				}
				if (projector.getSide() == 3) {
					z_offset_s = Strength;
				}
			}
		}
		if (projector.getSide() == 4 || projector.getSide() == 5) {
			tpy = distance;
			tpz = distance;
			tpx = Strength;

			x_offset_s = Strength - Strength;
			if (((TileEntityProjector) projector).hasOption(
					ModularForceFieldSystem.MFFSProjectorOptionDome, true)) {
				if (projector.getSide() == 4) {
					x_offset_e = Strength;
				}
				if (projector.getSide() == 5) {
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
							&& (projector.getSide() == 4 || projector.getSide() == 5)) {
						tpx_temp += 1;
					}
					if (tpy == Strength
							&& (projector.getSide() == 0 || projector.getSide() == 1)) {
						tpy_temp += 1;
					}
					if (tpz == Strength
							&& (projector.getSide() == 2 || projector.getSide() == 3)) {
						tpz_temp += 1;
					}

					if ((x1 == 0 - tpx_temp || x1 == tpx_temp
							|| y1 == 0 - tpy_temp || y1 == tpy_temp
							|| z1 == 0 - tpz_temp || z1 == tpz_temp)
							&& ((((TileEntityProjector) projector).yCoord + y1) >= 0)) {
						ffLocs.add(new PointXYZ(x1, y1, z1, 0));
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

		return false;
	}

}