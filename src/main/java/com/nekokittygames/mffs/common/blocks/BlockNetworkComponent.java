package com.nekokittygames.mffs.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;

public abstract class BlockNetworkComponent extends BlockTileMFFS{
    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;
    public BlockNetworkComponent(Properties properties) {
        super(properties);
        this.setDefaultState(getMainBlockState(this.stateContainer.getBaseState()).with(ENABLED, false));
    }

    protected abstract BlockState getMainBlockState(BlockState baseState);

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(ENABLED);
    }
}
