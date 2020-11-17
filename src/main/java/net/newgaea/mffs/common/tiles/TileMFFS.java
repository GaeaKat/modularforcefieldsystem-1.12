package net.newgaea.mffs.common.tiles;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
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

    @Override
    public CompoundNBT getUpdateTag() {
        return writeExtra(super.getUpdateTag());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        readExtra(tag);
        //readExtra(tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(this.getBlockState(),pkt.getNbtCompound());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos,1,getUpdateTag());
    }
}
