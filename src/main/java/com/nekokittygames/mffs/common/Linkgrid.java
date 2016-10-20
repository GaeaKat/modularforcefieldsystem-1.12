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
import java.util.Random;

import com.google.common.collect.MapMaker;

import com.nekokittygames.mffs.api.PointXYZ;
import com.nekokittygames.mffs.common.tileentity.TileEntityAdvSecurityStation;
import com.nekokittygames.mffs.common.tileentity.TileEntityAreaDefenseStation;
import com.nekokittygames.mffs.common.tileentity.TileEntityCapacitor;
import com.nekokittygames.mffs.common.tileentity.TileEntityControlSystem;
import com.nekokittygames.mffs.common.tileentity.TileEntityExtractor;
import com.nekokittygames.mffs.common.tileentity.TileEntityMachines;
import com.nekokittygames.mffs.common.tileentity.TileEntityProjector;
import com.nekokittygames.mffs.common.tileentity.TileEntitySecStorage;
import net.minecraft.world.World;

public final class Linkgrid {
	private static Map WorldpowernetMap = new MapMaker().weakKeys().makeMap();

	public static class Worldlinknet {

		private Map<Integer, TileEntityProjector> Projektor = new Hashtable<Integer, TileEntityProjector>();
		private Map<Integer, TileEntityCapacitor> Capacitors = new Hashtable<Integer, TileEntityCapacitor>();
		private Map<Integer, TileEntityAdvSecurityStation> SecStation = new Hashtable<Integer, TileEntityAdvSecurityStation>();
		private Map<Integer, TileEntityAreaDefenseStation> DefStation = new Hashtable<Integer, TileEntityAreaDefenseStation>();
		private Map<Integer, TileEntityExtractor> Extractor = new Hashtable<Integer, TileEntityExtractor>();
		private Map<Integer, TileEntityProjector> Jammer = new Hashtable<Integer, TileEntityProjector>();
		private Map<Integer, TileEntityProjector> FieldFusion = new Hashtable<Integer, TileEntityProjector>();
		private Map<Integer, TileEntitySecStorage> SecStorage = new Hashtable<Integer, TileEntitySecStorage>();
		private Map<Integer, TileEntityControlSystem> ControlSystem = new Hashtable<Integer, TileEntityControlSystem>();

		public Map<Integer, TileEntitySecStorage> getSecStorage() {
			return SecStorage;
		}

		public Map<Integer, TileEntityControlSystem> getControlSystem() {
			return ControlSystem;
		}

		public Map<Integer, TileEntityExtractor> getExtractor() {
			return Extractor;
		}

		public Map<Integer, TileEntityProjector> getProjektor() {
			return Projektor;
		}

		public Map<Integer, TileEntityCapacitor> getCapacitor() {
			return Capacitors;
		}

		public Map<Integer, TileEntityAdvSecurityStation> getSecStation() {
			return SecStation;
		}

		public Map<Integer, TileEntityAreaDefenseStation> getDefStation() {
			return DefStation;
		}

		public Map<Integer, TileEntityProjector> getJammer() {
			return Jammer;
		}

		public Map<Integer, TileEntityProjector> getFieldFusion() {
			return FieldFusion;
		}

		public int refreshID(TileEntityMachines tileEntity, int remDeviceID) {

			Random random = new Random();
			int DeviceID = random.nextInt();
			if (tileEntity instanceof TileEntitySecStorage) {
				if (remDeviceID == 0) {
					while (SecStorage.get(DeviceID) != null) {
						DeviceID = random.nextInt();
					}
				} else {
					DeviceID = remDeviceID;
				}
				SecStorage.put(DeviceID, (TileEntitySecStorage) tileEntity);
				return DeviceID;
			}
			if (tileEntity instanceof TileEntityControlSystem) {
				if (remDeviceID == 0) {
					while (ControlSystem.get(DeviceID) != null) {
						DeviceID = random.nextInt();
					}
				} else {
					DeviceID = remDeviceID;
				}
				ControlSystem.put(DeviceID,
						(TileEntityControlSystem) tileEntity);
				return DeviceID;
			}
			if (tileEntity instanceof TileEntityAdvSecurityStation) {
				if (remDeviceID == 0) {
					while (SecStation.get(DeviceID) != null) {
						DeviceID = random.nextInt();
					}
				} else {
					DeviceID = remDeviceID;
				}
				SecStation.put(DeviceID,
						(TileEntityAdvSecurityStation) tileEntity);
				return DeviceID;
			}
			if (tileEntity instanceof TileEntityAreaDefenseStation) {
				if (remDeviceID == 0) {
					while (DefStation.get(DeviceID) != null) {
						DeviceID = random.nextInt();
					}
				} else {
					DeviceID = remDeviceID;
				}
				DefStation.put(DeviceID,
						(TileEntityAreaDefenseStation) tileEntity);
				return DeviceID;
			}
			if (tileEntity instanceof TileEntityCapacitor) {
				if (remDeviceID == 0) {
					while (Capacitors.get(DeviceID) != null) {
						DeviceID = random.nextInt();
					}
				} else {
					DeviceID = remDeviceID;
				}
				Capacitors.put(DeviceID, (TileEntityCapacitor) tileEntity);
				return DeviceID;
			}

			if (tileEntity instanceof TileEntityExtractor) {
				if (remDeviceID == 0) {
					while (Extractor.get(DeviceID) != null) {
						DeviceID = random.nextInt();
					}
				} else {
					DeviceID = remDeviceID;
				}
				Extractor.put(DeviceID, (TileEntityExtractor) tileEntity);
				return DeviceID;
			}
			if (tileEntity instanceof TileEntityProjector) {
				if (remDeviceID == 0) {
					while (Projektor.get(DeviceID) != null) {
						DeviceID = random.nextInt();
					}
				} else {
					DeviceID = remDeviceID;
				}
				Projektor.put(DeviceID, (TileEntityProjector) tileEntity);
				return DeviceID;
			}
			return 0;
		}

		public int connectedtoCapacitor(TileEntityCapacitor Cap, int range) {
			int counter = 0;
			for (TileEntityProjector tileentity : Projektor.values()) {
				if (tileentity.getPowerSourceID() == Cap.getPowerStorageID()) {
					if (range >= PointXYZ.distance(
							tileentity.getMaschinePoint(),
							Cap.getMaschinePoint())) {
						counter++;
					}
				}
			}

			for (TileEntityCapacitor tileentity : Capacitors.values()) {
				if (tileentity.getPowerSourceID() == Cap.getPowerStorageID()) {
					if (range >= PointXYZ.distance(
							tileentity.getMaschinePoint(),
							Cap.getMaschinePoint())) {
						counter++;
					}
				}
			}

			for (TileEntityAreaDefenseStation tileentity : DefStation.values()) {
				if (tileentity.getPowerSourceID() == Cap.getPowerStorageID()) {
					if (range >= PointXYZ.distance(
							tileentity.getMaschinePoint(),
							Cap.getMaschinePoint())) {
						counter++;
					}
				}
			}

			for (TileEntityExtractor tileentity : Extractor.values()) {
				if (tileentity.getPowerSourceID() == Cap.getPowerStorageID()) {
					if (range >= PointXYZ.distance(
							tileentity.getMaschinePoint(),
							Cap.getMaschinePoint())) {
						counter++;
					}
				}
			}



			return counter;
		}

		public TileEntityMachines getTileEntityMachines(String displayname,
				int key) {

			MFFSMaschines tem = MFFSMaschines.fromdisplayName(displayname);

			if (tem != null) {
				switch (tem.index) {
				case 1:
					return getProjektor().get(key);
				case 2:
					return getExtractor().get(key);
				case 3:
					return getCapacitor().get(key);
				case 5:
					return getDefStation().get(key);
				case 6:
					return getSecStation().get(key);
				case 7:
					return getSecStorage().get(key);
				case 8:
					return getControlSystem().get(key);
				}
			}
			return null;
		}

	}

	public static Worldlinknet getWorldMap(World world) {
		if (world != null) {
			if (!WorldpowernetMap.containsKey(world)) {
				WorldpowernetMap.put(world, new Worldlinknet());
			}
			return (Worldlinknet) WorldpowernetMap.get(world);
		}

		return null;
	}

}