package net.newgaea.mffs.common.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class MFFSNetwork implements INBTSerializable<CompoundNBT> {
    public static final String NAME_KEY="name";
    /**
     * The constant NETWORK_ID.
     */
    public static final String NETWORK_ID="NETWORKID";
    private String name;
    private int networkID;


    /**
     * Gets network id.
     *
     * @return the network id
     */
    public int getNetworkID() {
        return networkID;
    }

    /**
     * Sets network id.
     *
     * @param networkID the network id
     * @return the network id
     */
    public MFFSNetwork setNetworkID(int networkID) {
        this.networkID = networkID;
        return this;
    }
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt=new CompoundNBT();
        nbt.putString(NAME_KEY,name);
        nbt.putInt(NETWORK_ID,networkID);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.name=nbt.getString(NAME_KEY);
        this.networkID=nbt.getInt(NETWORK_ID);
    }
}
