package net.newgaea.mffs.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockNetwork extends ModTileBlock {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public BlockNetwork(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(ACTIVE,false));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }
}
