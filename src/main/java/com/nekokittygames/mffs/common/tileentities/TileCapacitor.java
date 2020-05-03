package com.nekokittygames.mffs.common.tileentities;

import com.nekokittygames.mffs.common.init.MFFSRegistration;
import com.nekokittygames.mffs.common.libs.NetworkComponents;

public class TileCapacitor extends TileNetworkComponent {
    public TileCapacitor() {
        super(MFFSRegistration.TileEntity.CAPACITOR.get());
    }

    @Override
    public NetworkComponents getNetworkType() {
        return NetworkComponents.CAPACITOR;
    }

}
