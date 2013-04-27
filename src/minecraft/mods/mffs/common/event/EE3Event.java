package mods.mffs.common.event;

import mods.mffs.common.ForceFieldOptions;
import net.minecraftforge.event.ForgeSubscribe;

import com.pahimar.ee3.event.ActionRequestEvent;

public class EE3Event {

	@ForgeSubscribe
	public void onModActionEvent(ActionRequestEvent event) {

		boolean blockprotected = ForceFieldOptions.BlockProtected(
				event.entityPlayer.worldObj, event.x, event.y, event.z,
				event.entityPlayer);
		if (blockprotected) {
			event.entityPlayer
					.addChatMessage("[Field Security] Fail: Block transmute need <Change Protected Block> SecurityRight");
		}
		event.setCanceled(blockprotected);
	}

}