package com.nekokittygames.mffs.common.tileentities;

import com.nekokittygames.mffs.common.init.MFFSTileTypes;
import com.nekokittygames.mffs.common.libs.NetworkComponents;
import net.minecraft.tileentity.TileEntityType;

public class TileCapacitor extends TileNetworkComponent {
    public TileCapacitor() {
        super(MFFSTileTypes.CAPACITOR);
    }

    @Override
    public NetworkComponents getNetworkType() {
        return NetworkComponents.CAPACITOR;
    }

}
