package com.nekokittygames.mffs.common.tileentities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public abstract class TileMFFS extends TileEntity {
    public TileMFFS(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }


    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        readExtra(compound);
    }

    public abstract void readExtra(CompoundNBT compound);


    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT nbt= super.write(compound);
        nbt=writeExtra(nbt);
        return nbt;

    }
    public abstract CompoundNBT writeExtra(CompoundNBT compound);
}
