package net.newgaea.mffs.common.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.newgaea.mffs.common.libs.LibMisc;

public class MFFSWorldData extends WorldSavedData {
    private static final String DATA_NAME = LibMisc.MOD_ID + "_WorldData";
    private static final MFFSWorldData clientData=new MFFSWorldData();
    private final MFFSWorldNetwork networks=new MFFSWorldNetwork();

    public MFFSWorldNetwork getNetworks() {
        return networks;
    }
    public MFFSWorldData() {
        super(DATA_NAME);
    }

    @Override
    public void read(CompoundNBT nbt) {
        networks.deserializeNBT(nbt.getCompound("networks"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("networks",networks.serializeNBT());
        return compound;
    }

    public static void save(World world) {
        if(!(world instanceof ServerWorld)) {
            return;
        }
        ServerWorld overworld=world.getServer().getWorld(World.OVERWORLD);
        DimensionSavedDataManager manager=overworld.getSavedData();
        manager.get(MFFSWorldData::new,DATA_NAME).markDirty();
        manager.save();
    }

    public static MFFSWorldData get(World world) {
        if(!(world instanceof ServerWorld)) {
            return clientData;
        }
        ServerWorld overworld=world.getServer().getWorld(World.OVERWORLD);
        DimensionSavedDataManager manager=overworld.getSavedData();
        return manager.getOrCreate(MFFSWorldData::new,DATA_NAME);
    }
}
