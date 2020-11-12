package net.newgaea.mffs.common.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MFFSWorldNetwork implements INBTSerializable<CompoundNBT> {
    public static final String NETWORKS="NETWORKS";
    private int nextID=0;
    Map<Integer,MFFSNetwork> networks;

    public MFFSWorldNetwork() {
        networks=new HashMap<>();
    }

    private int FindNextID() {
        return nextID++;
    }

    public MFFSNetwork createNetwork() {
        MFFSNetwork  network=new MFFSNetwork();
        network.setNetworkID(FindNextID());
        networks.put(network.getNetworkID(),network);
        return network;
    }
    public MFFSNetwork findNetworkByID(int id)
    {
        return networks.getOrDefault(id, null);
    }
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt=new CompoundNBT();
        ListNBT listNBT=networks.values().stream().map(MFFSNetwork::serializeNBT).collect(Collectors.toCollection(ListNBT::new));
        nbt.put(NETWORKS,listNBT);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ListNBT list=nbt.getList(NETWORKS, Constants.NBT.TAG_COMPOUND);
        for(int i=0;i<list.size();i++) {
            CompoundNBT compoundNBT=list.getCompound(i);
            MFFSNetwork network=new MFFSNetwork();
            network.deserializeNBT(compoundNBT);
            if(network.getNetworkID()>=nextID)
                nextID++;
            networks.put(network.getNetworkID(),network);
        }
    }
}
