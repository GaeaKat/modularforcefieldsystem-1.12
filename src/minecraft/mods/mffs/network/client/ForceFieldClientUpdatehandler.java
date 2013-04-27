package mods.mffs.network.client;

import java.util.EnumSet;
import java.util.Stack;

import mods.mffs.common.ModularForceFieldSystem;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

public final class ForceFieldClientUpdatehandler implements
		IScheduledTickHandler {

	protected static Stack<Integer> queue = new Stack<Integer>();

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
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
			NetworkHandlerClient
					.requestForceFieldInitialData(
							ModularForceFieldSystem.proxy.getClientWorld().provider.dimensionId,
							str.toString());
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
		return "ForceField Client Ticker";
	}

	@Override
	public int nextTickSpacing() {
		return 1;
	}

	public static void addto(int x, int y, int z) {
		queue.push(x);
		queue.push(y);
		queue.push(z);

	}

}
