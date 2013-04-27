package mods.mffs.network.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.EnumSet;
import java.util.Map;
import java.util.Stack;

import com.google.common.collect.MapMaker;

import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;

public class ForceFieldServerUpdatehandler implements IScheduledTickHandler {

	private static Map WorldForcedield = new MapMaker().weakKeys().makeMap();

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {

		for (World world : DimensionManager.getWorlds()) {
			StringBuilder str = new StringBuilder();

			while (!ForceFieldServerUpdatehandler.getWorldMap(world).queue.isEmpty()) {

				str.append(ForceFieldServerUpdatehandler.getWorldMap(world).queue.pop());
				str.append("/");
				str.append(ForceFieldServerUpdatehandler.getWorldMap(world).queue.pop());
				str.append("/");
				str.append(ForceFieldServerUpdatehandler.getWorldMap(world).queue.pop());
				str.append("!");
				str.append(ForceFieldServerUpdatehandler.getWorldMap(world).queue.pop());
				str.append("<");
				str.append(ForceFieldServerUpdatehandler.getWorldMap(world).queue.pop());
				str.append("/");
				str.append(ForceFieldServerUpdatehandler.getWorldMap(world).queue.pop());
				str.append("/");
				str.append(ForceFieldServerUpdatehandler.getWorldMap(world).queue.pop());
				str.append(">");

				if (str.length() > 7500)
					break;
			}

			if (str.length() > 0) {
				try {
					ByteArrayOutputStream bos = new ByteArrayOutputStream(63000);
					DataOutputStream dos = new DataOutputStream(bos);
					int typ = 100;

					dos.writeInt(0);
					dos.writeInt(0);
					dos.writeInt(0);
					dos.writeInt(typ);
					dos.writeUTF(str.toString());

					Packet250CustomPayload pkt = new Packet250CustomPayload();
					pkt.channel = "MFFS";
					pkt.data = bos.toByteArray();
					pkt.length = bos.size();
					pkt.isChunkDataPacket = true;

					PacketDispatcher.sendPacketToAllInDimension(pkt,
							world.provider.dimensionId);

				} catch (Exception e) {
					if (true)
						System.out.println(e.getLocalizedMessage());
				}

			}
			str.setLength(0);

		}

	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {

	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER);
	}

	@Override
	public String getLabel() {
		return "ForceField Server Ticker";
	}

	@Override
	public int nextTickSpacing() {
		return 1;
	}

	public static ForceFieldpacket getWorldMap(World world) {
		if (world != null) {
			if (!WorldForcedield.containsKey(world)) {
				WorldForcedield.put(world, new ForceFieldpacket());
			}
			return (ForceFieldpacket) WorldForcedield.get(world);
		}

		return null;
	}

	public static class ForceFieldpacket {

		protected Stack<Integer> queue = new Stack<Integer>();

		public void addto(int x, int y, int z, int dimensionId, int px, int py,
				int pz) {

			queue.push(z);
			queue.push(y);
			queue.push(x);
			queue.push(dimensionId);
			queue.push(px);
			queue.push(py);
			queue.push(pz);
		}

	}

}
