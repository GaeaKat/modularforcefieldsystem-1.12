package net.newgaea.mffs.common.blocks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockNetwork extends ModTileBlock {
    public BlockNetwork(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType interactWith(World worldIn, BlockPos pos, PlayerEntity player) {
        return null;
    }
}
