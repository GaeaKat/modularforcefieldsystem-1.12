package mods.mffs.common.event;

import cpw.mods.fml.common.registry.LanguageRegistry;
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
			event.entityPlayer.addChatMessage(LanguageRegistry.instance().getStringLocalization("fieldSecurity" +
					".failedTransmutation"));
		}
		event.setCanceled(blockprotected);
	}

}