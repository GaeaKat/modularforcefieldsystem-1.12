package net.newgaea.mffs.common.tiles;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.newgaea.mffs.common.storage.MFFSEnergyStorage;

public class TileCapacitor extends TileNetwork{

    private boolean master;

    public TileCapacitor(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public boolean isMaster() {
        return master;
    }

    public TileCapacitor setMaster(boolean master) {
        this.master = master;
        return this;
    }
}
