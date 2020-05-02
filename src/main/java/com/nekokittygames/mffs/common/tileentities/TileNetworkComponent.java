package com.nekokittygames.mffs.common.tileentities;

import com.nekokittygames.mffs.MFFS;
import com.nekokittygames.mffs.common.data.ForcePowerNetwork;
import com.nekokittygames.mffs.common.data.MFFSWorldSavedData;
import com.nekokittygames.mffs.common.libs.NetworkComponents;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;

public abstract class TileNetworkComponent extends TileMFFS{

public static final String NETWORK_ID="NETWORKID";
    private int networkID=-1;

    public TileNetworkComponent(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public abstract NetworkComponents getNetworkType();

    @Override
    public void readExtra(CompoundNBT compound) {
        networkID=compound.getInt(NETWORK_ID);
    }

    @Override
    public CompoundNBT writeExtra(CompoundNBT compound) {
        compound.putInt(NETWORK_ID,networkID);
        return compound;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        ForcePowerNetwork network=getNetwork();
        MFFS.getLOGGER().debug(network.getNetworkID());
    }



    public ForcePowerNetwork getNetwork()
    {
        if(networkID!=-1) {
            ForcePowerNetwork network = MFFSWorldSavedData.get(world).getNetworks().findNetworkByID(networkID);
            if(network!=null)
                return network;
            network=MFFSWorldSavedData.get(world).getNetworks().createNetwork();
            this.networkID=network.getNetworkID();
            MFFSWorldSavedData.save(world);
            return network;
        }
        else {
            ForcePowerNetwork network = MFFSWorldSavedData.get(world).getNetworks().createNetwork();
            this.networkID=network.getNetworkID();
            MFFSWorldSavedData.save(world);
            return network;
        }
    }
}
