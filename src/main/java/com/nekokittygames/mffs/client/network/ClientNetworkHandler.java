package com.nekokittygames.mffs.client.network;

import com.nekokittygames.mffs.common.data.ForcePowerNetworks;
import com.nekokittygames.mffs.common.data.MFFSWorldSavedData;
import com.nekokittygames.mffs.common.network.UpdateForceNetworks;
import net.minecraft.client.Minecraft;

public class ClientNetworkHandler {

    public static void HandleNetworks(UpdateForceNetworks networks){
        Minecraft.getInstance().execute(() -> MFFSWorldSavedData.getClientData().getNetworks().deserializeNBT(networks.networksNBT));
    }
}
