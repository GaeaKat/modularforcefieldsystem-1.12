package net.newgaea.mffs.common.tiles;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.newgaea.mffs.common.blocks.BlockSimpleNetwork;

public class TileAdvSecurityStation extends TileNetwork {
    public TileAdvSecurityStation(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void dropPlugins() {

    }

    @Override
    public TileAdvSecurityStation getLinkedSecurityStation() {
        return this;
    }

    @Override
    public Direction getSide() {
        return this.getBlockState().get(BlockSimpleNetwork.FACING);
    }
}
