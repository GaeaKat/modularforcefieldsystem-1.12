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

import mods.mffs.api.PointXYZ;
import mods.mffs.common.IModularProjector;
import mods.mffs.common.IModularProjector.Slots;
import mods.mffs.common.ModularForceFieldSystem;
import mods.mffs.common.options.*;
import mods.mffs.common.tileentity.TileEntityProjector;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

import java.util.Set;

public class ItemProjectorModuleSphere extends Module3DBase {
	public ItemProjectorModuleSphere(int i) {
		super(i);
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("mffs:modules/Sphere");
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
		int radius = projector.countItemsInSlot(Slots.Distance) + 4;

		int yDown = radius;

		if (((TileEntityProjector) projector).hasOption(
				ModularForceFieldSystem.MFFSProjectorOptionDome, true)) {
			yDown = 0;
		}

		for (int y1 = -yDown; y1 <= radius; y1++) {
			for (int x1 = -radius; x1 <= radius; x1++) {
				for (int z1 = -radius; z1 <= radius; z1++) {
					int dx = x1;
					int dy = y1;
					int dz = z1;

					int dist = (int) Math.round(Math.sqrt(dx * dx + dy * dy
							+ dz * dz));

					if (dist <= radius
							&& dist > (radius - (projector
									.countItemsInSlot(Slots.Strength) + 1))) {
						ffLocs.add(new PointXYZ(x1, y1, z1, 0));
					} else if (dist <= radius) {
						//ffInterior.add(new PointXYZ(x1, y1, z1, 0));
					}
				}
			}
		}
	}

	public static boolean supportsOption(ItemProjectorOptionBase item) {

		if (item instanceof ItemProjectorOptionCamoflage)
			return true;
		if (item instanceof ItemProjectorOptionDefenseStation)
			return true;
		if (item instanceof ItemProjectorOptionFieldFusion)
			return true;
		if (item instanceof ItemProjectorOptionFieldManipulator)
			return true;
		if (item instanceof ItemProjectorOptionForceFieldJammer)
			return true;
		if (item instanceof ItemProjectorOptionMobDefence)
			return true;
		if (item instanceof ItemProjectorOptionSponge)
			return true;
		if (item instanceof ItemProjectorOptionBlockBreaker)
			return true;

		return false;

	}

	@Override
	public boolean supportsOption(Item item) {

		if (item instanceof ItemProjectorOptionCamoflage)
			return true;
		if (item instanceof ItemProjectorOptionDefenseStation)
			return true;
		if (item instanceof ItemProjectorOptionFieldFusion)
			return true;
		if (item instanceof ItemProjectorOptionFieldManipulator)
			return true;
		if (item instanceof ItemProjectorOptionForceFieldJammer)
			return true;
		if (item instanceof ItemProjectorOptionMobDefence)
			return true;
		if (item instanceof ItemProjectorOptionSponge)
			return true;
		if (item instanceof ItemProjectorOptionBlockBreaker)
			return true;

		return false;
	}

}