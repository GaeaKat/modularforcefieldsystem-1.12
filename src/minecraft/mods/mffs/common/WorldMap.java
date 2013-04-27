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

import java.util.Hashtable;
import java.util.Map;

import com.google.common.collect.MapMaker;

import mods.mffs.api.PointXYZ;
import net.minecraft.world.World;

public final class WorldMap {
	private static Map ForceFieldWorlds = new MapMaker().weakKeys().makeMap();

	public static class ForceFieldWorld {
		private static Map<Integer, ForceFieldBlockStack> ForceFieldStackMap = new Hashtable<Integer, ForceFieldBlockStack>();

		public ForceFieldBlockStack getorcreateFFStackMap(int x, int y, int z,
				World world) {
			PointXYZ png = new PointXYZ(x, y, z, world);
			if (ForceFieldStackMap.get(png.hashCode()) == null) {
				ForceFieldStackMap.put(png.hashCode(),
						new ForceFieldBlockStack(png));
			}
			return ForceFieldStackMap.get(png.hashCode());
		}

		public ForceFieldBlockStack getForceFieldStackMap(Integer hasher) {
			return ForceFieldStackMap.get(hasher);
		}

		public ForceFieldBlockStack getForceFieldStackMap(PointXYZ png) {
			return ForceFieldStackMap.get(png.hashCode());
		}

		public int isExistForceFieldStackMap(int x, int y, int z, int counter,
				int typ, World world) {
			switch (typ) {
			case 0:
				y += counter;
				break;
			case 1:
				y -= counter;
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

			ForceFieldBlockStack Map = ForceFieldStackMap.get(new PointXYZ(x,
					y, z, world).hashCode());

			if (Map == null) {
				return 0;
			} else {
				if (Map.isEmpty()) {
					return 0;
				}

				return Map.getGenratorID();
			}
		}
	}

	public static ForceFieldWorld getForceFieldWorld(World world) {
		if (world != null) {
			if (!ForceFieldWorlds.containsKey(world)) {
				ForceFieldWorlds.put(world, new ForceFieldWorld());
			}
			return (ForceFieldWorld) ForceFieldWorlds.get(world);
		}

		return null;
	}

}
