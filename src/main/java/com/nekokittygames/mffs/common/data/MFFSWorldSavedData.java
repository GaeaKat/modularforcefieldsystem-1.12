package com.nekokittygames.mffs.common.data;

import com.nekokittygames.mffs.common.libs.LibMisc;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

public class MFFSWorldSavedData extends WorldSavedData {
    private static final String DATA_NAME = LibMisc.MOD_ID + "_WorldData";
    private static MFFSWorldSavedData clientData=new MFFSWorldSavedData();


    public static MFFSWorldSavedData getClientData() {
        return clientData;
    }
    private ForcePowerNetworks networks=new ForcePowerNetworks();

    public ForcePowerNetworks getNetworks() {
        return networks;
    }

    public MFFSWorldSavedData(String s)
    {
        super(s);
    }

    public MFFSWorldSavedData() {
        super(DATA_NAME);
    }

    @Override
    public void read(CompoundNBT nbt) {
        networks.deserializeNBT(nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT nbt=compound;
        nbt.put("networks",networks.serializeNBT());
        return compound;
    }

    public static void save(World world) {
        if(! (world instanceof ServerWorld))
        {
            return;
        }
        ServerWorld overworld=world.getServer().getWorld(DimensionType.OVERWORLD);
        DimensionSavedDataManager storage=overworld.getSavedData();
        storage.save();

    }
    public static MFFSWorldSavedData get(World world) {
        if(! (world instanceof ServerWorld))
        {
            return clientData;
        }
        ServerWorld overworld=world.getServer().getWorld(DimensionType.OVERWORLD);

        DimensionSavedDataManager storage = overworld.getSavedData();
        return storage.getOrCreate(MFFSWorldSavedData::new,DATA_NAME);
    }
}
