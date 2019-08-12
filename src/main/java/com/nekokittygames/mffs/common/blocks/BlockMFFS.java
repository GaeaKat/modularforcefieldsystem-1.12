package com.nekokittygames.mffs.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public abstract class BlockMFFS extends Block {

    public BlockMFFS(Properties properties) {
        super(properties);
    }
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            this.interactWith(worldIn, pos, player);
        }

        return true;
    }

    public abstract void interactWith(World worldIn, BlockPos pos, PlayerEntity player);

}
