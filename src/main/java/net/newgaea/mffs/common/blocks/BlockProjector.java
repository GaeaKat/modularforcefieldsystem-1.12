package net.newgaea.mffs.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.newgaea.mffs.api.EnumProjectorModule;
import net.newgaea.mffs.common.init.MFFSTiles;
import net.newgaea.mffs.common.tiles.TileProjector;

public class BlockProjector extends BlockOmniNetwork {

    public static EnumProperty<EnumProjectorModule> TYPE=EnumProperty.create("type", EnumProjectorModule.class);
    public BlockProjector(Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(TYPE, EnumProjectorModule.Empty));

    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(TYPE);
    }

    @Override
    public ActionResultType interactWith(World world, BlockPos pos, PlayerEntity player) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof INamedContainerProvider) {
            NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
        } else {
            throw new IllegalStateException("Our named container provider is missing!");
        }
        return ActionResultType.SUCCESS;
    }
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return MFFSTiles.PROJECTOR.create();
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.isIn(newState.getBlock())) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileProjector) {
                worldIn.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }
}
