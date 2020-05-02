package com.nekokittygames.mffs.common.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.*;

/**
 * The type Force power network.
 */
public class ForcePowerNetwork implements INBTSerializable<CompoundNBT> {
    /**
     * The constant NETWORK_ID.
     */
    public static final String NETWORK_ID="NETWORKID";
    /**
     * The constant NETWORK_CAPACITOR.
     */
    public static final String NETWORK_CAPACITOR="NETWORKCAPACITOR";
    /**
     * The constant NETWORK_RANGE.
     */
    public static final String NETWORK_RANGE = "NETWORKRANGE";
    /**
     * The constant NETWORK_OBJECTS.
     */
    public static final String NETWORK_OBJECTS = "NETWORKOBJECTS";

    private int networkID;
    private BlockPos capacitorPosition;
    private int networkRange;
    private final HashMap<String, List<BlockPos>> networkObjects=new HashMap<>();

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
    public ForcePowerNetwork setNetworkID(int networkID) {
        this.networkID = networkID;
        return this;
    }

    /**
     * Gets capacitor position.
     *
     * @return the capacitor position
     */
    public BlockPos getCapacitorPosition() {
        return capacitorPosition;
    }

    /**
     * Sets capacitor position.
     *
     * @param capacitorPosition the capacitor position
     * @return the capacitor position
     */
    public ForcePowerNetwork setCapacitorPosition(BlockPos capacitorPosition) {
        this.capacitorPosition = capacitorPosition;
        return this;
    }

    /**
     * Gets network range.
     *
     * @return the network range
     */
    public int getNetworkRange() {
        return networkRange;
    }

    /**
     * Sets network range.
     *
     * @param networkRange the network range
     * @return the network range
     */
    public ForcePowerNetwork setNetworkRange(int networkRange) {
        this.networkRange = networkRange;
        return this;
    }

    /**
     * Gets network objects.
     *
     * @return the network objects
     */
    public HashMap<String, List<BlockPos>> getNetworkObjects() {
        return networkObjects;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt=new CompoundNBT();
        nbt.putInt(NETWORK_ID,networkID);
        nbt.put(NETWORK_CAPACITOR,NBTUtil.writeBlockPos(capacitorPosition));
        nbt.putInt(NETWORK_RANGE,networkRange);
        ListNBT objects=new ListNBT();
        networkObjects.forEach((key, value) -> {
            CompoundNBT entryNBT = new CompoundNBT();
            entryNBT.putString("KEY", key);
            ListNBT list=new ListNBT();
            for(BlockPos pos:value)
            {
                list.add(NBTUtil.writeBlockPos(pos));
            }
            entryNBT.put("VALUE", list);
            objects.add(entryNBT);
        });
        nbt.put(NETWORK_OBJECTS,objects);
        //networkObjects.forEach((key, value) -> objects.put(key, NBTUtil.writeBlockPos(value)));
        //nbt.put(NETWORK_OBJECTS,objects);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        networkID=nbt.getInt(NETWORK_ID);
        capacitorPosition=NBTUtil.readBlockPos(nbt.getCompound(NETWORK_CAPACITOR));
        networkRange=nbt.getInt(NETWORK_RANGE);
        networkObjects.clear();
        ListNBT objects=nbt.getList(NETWORK_OBJECTS, Constants.NBT.TAG_COMPOUND);
        objects.stream()
                .filter(inbt -> inbt.getId() == 10)
                .map(inbt -> (CompoundNBT) inbt)
                .forEach(cNBT -> {
                    ListNBT list=cNBT.getList("VALUE",10);
                    ArrayList<BlockPos> posses=new ArrayList<>();
                    for(int i=0;i<list.size();i++)
                        posses.add(NBTUtil.readBlockPos(list.getCompound(i)));
                    networkObjects.put(cNBT.getString("KEY"), posses);
                });

    }
}
