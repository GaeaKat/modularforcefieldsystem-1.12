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
import com.nekokittygames.mffs.common.Linkgrid;
import com.nekokittygames.mffs.common.tileentity.TileEntityProjector;
import com.nekokittygames.mffs.libs.LibItemNames;
import com.nekokittygames.mffs.libs.LibMisc;
import net.minecraft.world.World;

public class ItemProjectorOptionForceFieldJammer extends
		ItemProjectorOptionBase implements IChecksOnAll {
	public ItemProjectorOptionForceFieldJammer() {
		super();
		setUnlocalizedName(LibMisc.UNLOCALIZED_PREFIX+ LibItemNames.OPTION_FIELD_JAMMER);
		setRegistryName(LibItemNames.OPTION_FIELD_JAMMER);
	}


	public boolean CheckJammerinfluence(PointXYZ png, World world,
			TileEntityProjector Projector) {

		Map<Integer, TileEntityProjector> InnerMap = null;
		InnerMap = Linkgrid.getWorldMap(world).getJammer();

		for (TileEntityProjector tileentity : InnerMap.values()) {
			boolean logicswitch = false;

			logicswitch = tileentity.getPowerSourceID() != Projector
					.getPowerSourceID();

			if (logicswitch && tileentity.isActive()) {

				for (PointXYZ tpng : tileentity.getInteriorPoints()) {

					if (tpng.pos.equals(png.pos)) {
						Projector.ProjektorBurnout();
						return true;
					}
				}
			}
		}

		return false;
	}
}