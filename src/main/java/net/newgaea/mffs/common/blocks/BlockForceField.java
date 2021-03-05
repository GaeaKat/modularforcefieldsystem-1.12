package net.newgaea.mffs.common.blocks;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.newgaea.mffs.MFFS;
import net.newgaea.mffs.common.config.MFFSConfig;
import net.newgaea.mffs.common.data.Grid;
import net.newgaea.mffs.common.forcefield.ForceFieldBlockStack;
import net.newgaea.mffs.common.forcefield.WorldMap;
import net.newgaea.mffs.common.init.MFFSBlocks;
import net.newgaea.mffs.common.init.MFFSTiles;
import net.newgaea.mffs.common.misc.EnumFieldType;
import net.newgaea.mffs.common.tiles.TileForcefield;
import net.newgaea.mffs.common.tiles.TileProjector;

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


    public VoxelShape getRayTraceShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.empty();
    }

    @OnlyIn(Dist.CLIENT)
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1.0F;
    }

    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }


    @Override
    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
        if(state.getBlock() == adjacentBlockState.getBlock())
            return true;
        return super.isSideInvisible(state, adjacentBlockState, side);
    }


    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        //super.onReplaced(state, worldIn, pos, newState, isMoving);
        if (!state.isIn(newState.getBlock())) {
            ForceFieldBlockStack ffWorldMap = WorldMap.getForceFieldWorld(worldIn).getForceFieldBlockStack(pos.hashCode());
            if(ffWorldMap!=null) {
                if(!ffWorldMap.isEmpty()) {
                    TileProjector projector = Grid.getWorldGrid(worldIn).getProjectors().get(ffWorldMap.getProjectorID());
                    if(projector!=null) {
                        if(!projector.isActive()) {
                            ffWorldMap.removeByProjector(ffWorldMap.getProjectorID());
                        } else {
                            worldIn.setBlockState(pos, MFFSBlocks.FORCEFIELD.getDefaultState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                            worldIn.markBlockRangeForRenderUpdate(pos,state,worldIn.getBlockState(pos));
                            ffWorldMap.setSync(true);
                            if(ffWorldMap.getType()== EnumFieldType.Default) {
                                projector.consumePower(MFFSConfig.FORCEFIELD_PER_TICK.get() * MFFSConfig.FIELD_CREATE_MODIFIER.get(),false);
                            } else {
                                projector.consumePower(MFFSConfig.FORCEFIELD_PER_TICK.get() * MFFSConfig.FIELD_CREATE_MODIFIER.get() * MFFSConfig.ZAPPER_MODIFIER.get(),false);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isTransparent(BlockState state) {
        return true;
    }
}
