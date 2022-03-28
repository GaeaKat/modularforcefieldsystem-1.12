package dev.katcodes.mffs.common.blocks;

import dev.katcodes.mffs.common.init.MFFSBlocks;
import dev.katcodes.mffs.common.init.MFFSTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CapacitorBlock extends SimpleNetworkBlock {
    public CapacitorBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return MFFSTiles.CAPACITOR.create(pos, state);
    }
}
