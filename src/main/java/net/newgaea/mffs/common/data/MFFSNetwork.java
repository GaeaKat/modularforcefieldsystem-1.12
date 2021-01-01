package net.newgaea.mffs.common.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;
import net.newgaea.mffs.api.IModularProjector;
import net.newgaea.mffs.common.tiles.TileProjector;

import java.util.ArrayList;
import java.util.List;

public class MFFSNetwork implements INBTSerializable<CompoundNBT> {
    public static final String NAME_KEY="name";
    /**
     * The constant NETWORK_ID.
     */
    public static final String NETWORK_ID="NETWORKID";
    private static final String CAPACITOR_POS = "CAPACITORPOS";
    private String name;
    private int networkID;
    private BlockPos capacitorPos;


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
        if(capacitorPos!=null)
        nbt.put(CAPACITOR_POS, NBTUtil.writeBlockPos(capacitorPos));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.name=nbt.getString(NAME_KEY);
        this.networkID=nbt.getInt(NETWORK_ID);
        if(nbt.contains(CAPACITOR_POS))
            capacitorPos=NBTUtil.readBlockPos(nbt.getCompound(CAPACITOR_POS));
    }

    public BlockPos getCapacitorPos() {
        return capacitorPos;
    }

    public MFFSNetwork setCapacitorPos(BlockPos capacitorPos) {
        this.capacitorPos = capacitorPos;
        return this;
    }

    public List<IModularProjector> getProjectors() {
        List<IModularProjector> projectors=new ArrayList<>();

        return projectors;
    }
}
