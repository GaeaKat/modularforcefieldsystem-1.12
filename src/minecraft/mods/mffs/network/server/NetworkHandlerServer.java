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

package mods.mffs.network.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Field;

import mods.mffs.api.PointXYZ;
import mods.mffs.common.ForceFieldBlockStack;
import mods.mffs.common.Linkgrid;
import mods.mffs.common.WorldMap;
import mods.mffs.common.tileentity.TileEntityAdvSecurityStation;
import mods.mffs.common.tileentity.TileEntityAreaDefenseStation;
import mods.mffs.common.tileentity.TileEntityCapacitor;
import mods.mffs.common.tileentity.TileEntityControlSystem;
import mods.mffs.common.tileentity.TileEntityConverter;
import mods.mffs.common.tileentity.TileEntityExtractor;
import mods.mffs.common.tileentity.TileEntityForceField;
import mods.mffs.common.tileentity.TileEntityMachines;
import mods.mffs.common.tileentity.TileEntityProjector;
import mods.mffs.common.tileentity.TileEntitySecStorage;
import mods.mffs.network.INetworkHandlerEventListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet103SetSlot;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class NetworkHandlerServer implements IPacketHandler {

	private static final boolean DEBUG = false;

	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {

		ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
		int x = dat.readInt();
		int y = dat.readInt();
		int z = dat.readInt();
		int typ = dat.readInt();

		switch (typ) {
		case 2:

			int dimension = dat.readInt();
			String daten = dat.readUTF();
			World serverworld = DimensionManager.getWorld(dimension);
			if (serverworld != null) {
				TileEntity servertileEntity = serverworld.getBlockTileEntity(x,
						y, z);

				if (servertileEntity != null) {
					for (String varname : daten.split("/")) {
						updateTileEntityField(servertileEntity, varname);
					}
				} else {
					if (DEBUG)
						System.out.println(x + "/" + y + "/" + z
								+ ":no Tileentity found !!");
				}
			} else {
				if (DEBUG)
					System.out.println("[Error]No world found !!");
			}

			break;

		case 3:
			int dimension2 = dat.readInt();
			int key = dat.readInt();
			String value = dat.readUTF();

			World serverworld2 = DimensionManager.getWorld(dimension2);
			TileEntity servertileEntity2 = serverworld2.getBlockTileEntity(x,
					y, z);

			if (servertileEntity2 instanceof INetworkHandlerEventListener) {
				((INetworkHandlerEventListener) servertileEntity2)
						.onNetworkHandlerEvent(key, value);

			}

			break;
		case 10:

			int Dim = dat.readInt();
			String Corrdinsaten = dat.readUTF();

			World worldserver = DimensionManager.getWorld(Dim);

			if (worldserver != null) {

				for (String varname : Corrdinsaten.split("#")) {

					if (!varname.isEmpty()) {
						String[] corr = varname.split("/");
						TileEntity servertileEntity = worldserver
								.getBlockTileEntity(
										Integer.parseInt(corr[2].trim()),
										Integer.parseInt(corr[1].trim()),
										Integer.parseInt(corr[0].trim()));
						if (servertileEntity instanceof TileEntityForceField) {
							ForceFieldBlockStack ffworldmap = WorldMap
									.getForceFieldWorld(worldserver)
									.getForceFieldStackMap(
											new PointXYZ(
													servertileEntity.xCoord,
													servertileEntity.yCoord,
													servertileEntity.zCoord,
													worldserver).hashCode());

							if (ffworldmap != null) {
								if (!ffworldmap.isEmpty())

								{
									TileEntityProjector projector = Linkgrid
											.getWorldMap(worldserver)
											.getProjektor()
											.get(ffworldmap.getProjectorID());

									if (projector != null) {
										ForceFieldServerUpdatehandler
												.getWorldMap(worldserver)
												.addto(servertileEntity.xCoord,
														servertileEntity.yCoord,
														servertileEntity.zCoord,
														Dim, projector.xCoord,
														projector.yCoord,
														projector.zCoord);
									}
								}
							}

						}
					}
				}
			}

			break;

		}

	}

	public static void syncClientPlayerinventorySlot(EntityPlayer player,
			Slot slot, ItemStack itemstack) {

		Packet103SetSlot pkt = new Packet103SetSlot();
		pkt.windowId = 0;
		pkt.itemSlot = slot.slotNumber;
		pkt.myItemStack = itemstack;
		PacketDispatcher.sendPacketToPlayer(pkt, (Player) player);
	}

	public static void updateTileEntityField(TileEntity tileEntity,
			String varname) {

		// System.out.println(tileEntity+"Ubertragt daten"+varname);

		if (tileEntity != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
			DataOutputStream dos = new DataOutputStream(bos);
			int x = tileEntity.xCoord;
			int y = tileEntity.yCoord;
			int z = tileEntity.zCoord;
			int typ = 1; // Server -> Client

			try {
				dos.writeInt(x);
				dos.writeInt(y);
				dos.writeInt(z);
				dos.writeInt(typ);
				dos.writeUTF(varname);

			} catch (Exception e) {
				if (DEBUG)
					System.out.println(e.getLocalizedMessage());
			}

			if (tileEntity instanceof TileEntityMachines) {
				try {
					Field f = ReflectionHelper.findField(
							TileEntityMachines.class, varname);
					f.get(tileEntity);
					dos.writeUTF(String.valueOf(f.get(tileEntity)));
				} catch (Exception e) {
					if (DEBUG)
						System.out.println(e.getLocalizedMessage());
				}
			}

			if (tileEntity instanceof TileEntityProjector) {

				try {
					Field f = ReflectionHelper.findField(
							TileEntityProjector.class, varname);
					f.get(tileEntity);
					dos.writeUTF(String.valueOf(f.get(tileEntity)));
				} catch (Exception e) {
					if (DEBUG)
						System.out.println(e.getLocalizedMessage());
				}
			}

			if (tileEntity instanceof TileEntityCapacitor) {
				try {
					Field f = ReflectionHelper.findField(
							TileEntityCapacitor.class, varname);
					f.get(tileEntity);
					dos.writeUTF(String.valueOf(f.get(tileEntity)));
				} catch (Exception e) {
					if (DEBUG)
						System.out.println(e.getLocalizedMessage());
				}
			}

			if (tileEntity instanceof TileEntityExtractor) {
				try {
					Field f = ReflectionHelper.findField(
							TileEntityExtractor.class, varname);
					f.get(tileEntity);
					dos.writeUTF(String.valueOf(f.get(tileEntity)));
				} catch (Exception e) {
					if (DEBUG)
						System.out.println(e.getLocalizedMessage());
				}
			}

			if (tileEntity instanceof TileEntityConverter) {
				try {
					Field f = ReflectionHelper.findField(
							TileEntityConverter.class, varname);
					f.get(tileEntity);
					dos.writeUTF(String.valueOf(f.get(tileEntity)));
				} catch (Exception e) {
					if (DEBUG)
						System.out.println(e.getLocalizedMessage());
				}
			}

			if (tileEntity instanceof TileEntityAreaDefenseStation) {
				try {
					Field f = ReflectionHelper.findField(
							TileEntityAreaDefenseStation.class, varname);
					f.get(tileEntity);
					dos.writeUTF(String.valueOf(f.get(tileEntity)));
				} catch (Exception e) {
					if (DEBUG)
						System.out.println(e.getLocalizedMessage());
				}
			}

			if (tileEntity instanceof TileEntityAdvSecurityStation) {
				try {
					Field f = ReflectionHelper.findField(
							TileEntityAdvSecurityStation.class, varname);
					f.get(tileEntity);
					dos.writeUTF(String.valueOf(f.get(tileEntity)));
				} catch (Exception e) {
					if (DEBUG)
						System.out.println(e.getMessage());

				}
			}

			if (tileEntity instanceof TileEntitySecStorage) {
				try {
					Field f = ReflectionHelper.findField(
							TileEntitySecStorage.class, varname);
					f.get(tileEntity);
					dos.writeUTF(String.valueOf(f.get(tileEntity)));
				} catch (Exception e) {
					if (DEBUG)
						System.out.println(e.getMessage());

				}
			}

			if (tileEntity instanceof TileEntityControlSystem) {
				try {
					Field f = ReflectionHelper.findField(
							TileEntityControlSystem.class, varname);
					f.get(tileEntity);
					dos.writeUTF(String.valueOf(f.get(tileEntity)));
				} catch (Exception e) {
					if (DEBUG)
						System.out.println(e.getMessage());

				}
			}

			Packet250CustomPayload pkt = new Packet250CustomPayload();
			pkt.channel = "MFFS";
			pkt.data = bos.toByteArray();
			pkt.length = bos.size();
			pkt.isChunkDataPacket = true;

			PacketDispatcher.sendPacketToAllAround(x, y, z, 80,
					tileEntity.worldObj.provider.dimensionId, pkt);
		}

	}

}