package com.nekokittygames.mffs.network.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.EnumSet;
import java.util.Map;
import java.util.Stack;

import com.google.common.collect.MapMaker;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class ForceFieldServerUpdatehandler  {

	private static Map WorldForcedield = new MapMaker().weakKeys().makeMap();


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
