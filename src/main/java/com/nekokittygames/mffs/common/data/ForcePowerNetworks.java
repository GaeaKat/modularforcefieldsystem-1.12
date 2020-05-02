package com.nekokittygames.mffs.common.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.stream.Collectors;

public class ForcePowerNetworks implements INBTSerializable<CompoundNBT> {
public static final String NETWORKS="NETWORKS";
private int nextID=0;
    private final HashMap<Integer,ForcePowerNetwork> networks=new HashMap<>();



    public ForcePowerNetwork findNetworkByID(int id)
    {
        return networks.getOrDefault(id, null);
    }

    public ForcePowerNetwork createNetwork() {
            ForcePowerNetwork network=new ForcePowerNetwork();
            network.setNetworkID(FindNextID());
            networks.put(network.getNetworkID(),network);
            return network;
    }

    private int FindNextID() {
        return nextID++;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt=new CompoundNBT();
        ListNBT list= networks.values().stream().map(ForcePowerNetwork::serializeNBT).collect(Collectors.toCollection(ListNBT::new));
        nbt.put(NETWORKS,list);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ListNBT list=nbt.getList(NETWORKS,10);
        for(int i=0;i<list.size();i++)
        {
            CompoundNBT compoundNBT=list.getCompound(i);
            ForcePowerNetwork network=new ForcePowerNetwork();
            network.deserializeNBT(compoundNBT);
            if(network.getNetworkID()>=nextID)
                nextID++;
            networks.put(network.getNetworkID(),network);
        }
    }
}
