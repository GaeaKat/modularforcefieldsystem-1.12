package com.nekokittygames.mffs.network.client;

import java.util.EnumSet;
import java.util.Stack;

import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent;


public final class ForceFieldClientUpdatehandler  {

	protected static Stack<Integer> queue = new Stack<Integer>();

	public void tickEnd(TickEvent.ClientTickEvent event)
	{
		if(event.phase== TickEvent.Phase.END)
		{
			StringBuilder str = new StringBuilder();

			while (!ForceFieldClientUpdatehandler.queue.isEmpty()) {

				str.append(ForceFieldClientUpdatehandler.queue.pop());
				str.append("/");
				str.append(ForceFieldClientUpdatehandler.queue.pop());
				str.append("/");
				str.append(ForceFieldClientUpdatehandler.queue.pop());
				str.append("#");

				if (str.length() > 7500)
					break;
			}

			if (str.length() > 0) {
				str.setLength(0);
			}
		}
	}



	public static void addto(BlockPos pos) {
		queue.push(pos.getX());
		queue.push(pos.getY());
		queue.push(pos.getZ());

	}

}
