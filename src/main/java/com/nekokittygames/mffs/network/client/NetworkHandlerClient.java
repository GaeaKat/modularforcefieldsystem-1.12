package com.nekokittygames.mffs.network.client;

import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.tileentity.TileEntityConverter;
import com.nekokittygames.mffs.common.tileentity.TileEntityMachines;
import com.nekokittygames.mffs.network.PacketTileHandler;

/**
 * Created by katsw on 11/09/2016.
 */
public class NetworkHandlerClient {
    public static void fireTileEntityEvent(TileEntityMachines machine, int i, String s) {
        PacketTileHandler handler=new PacketTileHandler(machine,i,s);
        ModularForceFieldSystem.networkWrapper.sendToServer(handler);
    }
}
