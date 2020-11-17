package net.newgaea.mffs.common.blocks;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.newgaea.mffs.common.init.MFFSTiles;
import net.newgaea.mffs.common.tiles.TileForcefield;

public class BlockForceField extends ModTileBlock{
    public BlockForceField(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType interactWith(World worldIn, BlockPos pos, PlayerEntity player) {
        return null;
    }
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return MFFSTiles.FORCEFIELD.create();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        ItemStack item = player.getHeldItem(hand);
        if (!item.isEmpty() && item.getItem() instanceof BlockItem) {
            if (!world.isRemote) {
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof TileForcefield) {
                    BlockState mimicState = ((BlockItem) item.getItem()).getBlock().getDefaultState();
                    ((TileForcefield) te).setMimicState(mimicState);
                }
            }
            return ActionResultType.SUCCESS;
        }
        return super.onBlockActivated(state, world, pos, player, hand, trace);
    }


}
