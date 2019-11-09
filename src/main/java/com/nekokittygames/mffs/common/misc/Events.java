package com.nekokittygames.mffs.common.misc;

import com.nekokittygames.mffs.MFFS;
import com.nekokittygames.mffs.common.data.MFFSWorldSavedData;
import com.nekokittygames.mffs.common.libs.LibMisc;
import com.nekokittygames.mffs.common.network.UpdateForceNetworks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDispatcher;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Events {

    @SubscribeEvent
    public static final void HandlePLayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        UpdateForceNetworks forceNetworks=new UpdateForceNetworks(MFFSWorldSavedData.get(event.getEntity().world).getNetworks());
        MFFS.channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()),forceNetworks);
    }
}
