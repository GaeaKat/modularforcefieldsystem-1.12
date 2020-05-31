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
    public static final String ACTIVE="ACTIVE";
    private int networkID=-1;
    private boolean active=false;

    public TileNetworkComponent(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public abstract NetworkComponents getNetworkType();

    @Override
    public void readExtra(CompoundNBT compound) {
        networkID=compound.getInt(NETWORK_ID);
        if(compound.hasUniqueId(ACTIVE))
            active=compound.getBoolean(ACTIVE);
    }

    @Override
    public CompoundNBT writeExtra(CompoundNBT compound) {
        compound.putInt(NETWORK_ID,networkID);
        compound.putBoolean(ACTIVE,active);
        return compound;
    }

    @Override
    public void onLoad() {
        super.onLoad();
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
