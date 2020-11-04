package net.newgaea.mffs.common.tiles;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public abstract class TileMFFS extends TileEntity {

    public TileMFFS(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        readExtra(nbt);
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
