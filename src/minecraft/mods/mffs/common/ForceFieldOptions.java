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

package mods.mffs.common;

import java.util.Map;

import mods.mffs.common.tileentity.TileEntityProjector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public final class ForceFieldOptions {

	// -----------------------------------Block Protected by
	// ForceField---------------------------------------------------------------------

	public static boolean BlockProtected(World world, int x, int y, int z,
			EntityPlayer entityplayer) {

		Map<Integer, TileEntityProjector> ProjectorinrangeMap = Linkgrid
				.getWorldMap(world).getProjektor();
		for (TileEntityProjector tileentity : ProjectorinrangeMap.values()) {

			int dx = tileentity.xCoord - x;
			int dy = tileentity.yCoord - y;
			int dz = tileentity.zCoord - z;

			int dist = (int) Math.round(Math.sqrt(dx * dx + dy * dy + dz * dz));

			if (dist > 64 || !tileentity.isActive()
					|| tileentity.getProjektor_Typ() == 1
					|| tileentity.getProjektor_Typ() == 2) {
				continue;
			}

			Map<Integer, TileEntityProjector> InnerMap = null;
			InnerMap = Linkgrid.getWorldMap(world).getProjektor();

			for (TileEntityProjector tileentity2 : InnerMap.values()) {

				boolean logicswitch = tileentity2.equals(tileentity);

				if (logicswitch && tileentity2.isActive()) {

					if (entityplayer != null) {

						if (!SecurityHelper.isAccessGranted(tileentity,
								entityplayer, world, SecurityRight.RPB, true)) {
							return true;
						}

					} else {

						return true;
					}

				}

			}

		}

		return false;
	}

}
