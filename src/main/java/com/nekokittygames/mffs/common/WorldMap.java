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

package com.nekokittygames.mffs.common;

import java.util.Hashtable;
import java.util.Map;

import com.google.common.collect.MapMaker;

import com.nekokittygames.mffs.api.PointXYZ;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public final class WorldMap {
	private static Map ForceFieldWorlds = new MapMaker().weakKeys().makeMap();

	public static class ForceFieldWorld {
		private static Map<Integer, ForceFieldBlockStack> ForceFieldStackMap = new Hashtable<Integer, ForceFieldBlockStack>();

		public ForceFieldBlockStack getorcreateFFStackMap(BlockPos pos,
				World world) {
			PointXYZ png = new PointXYZ(pos, world);
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

		public int isExistForceFieldStackMap(BlockPos pos, int counter,
				int typ, World world) {
			BlockPos newPos=new BlockPos(pos);
			switch (typ) {
			case 0:
				newPos=newPos.add(0,counter,0);
				break;
			case 1:
				newPos=newPos.add(0,-counter,0);
				break;
			case 2:
				newPos=newPos.add(0,0,counter);
				break;
			case 3:
				newPos=newPos.add(0,0,-counter);
				break;
			case 4:
				newPos=newPos.add(counter,0,0);
				break;
			case 5:
				newPos=newPos.add(-counter,0,0);
				break;
			}

			ForceFieldBlockStack Map = ForceFieldStackMap.get(new PointXYZ(newPos, world).hashCode());

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
