package net.newgaea.mffs.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.newgaea.mffs.MFFS;
import net.newgaea.mffs.common.items.ItemDebugger;
import net.newgaea.mffs.common.tiles.TileFENetwork;
import org.jetbrains.annotations.Nullable;

public abstract class BlockNetwork extends ModTileBlock {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public BlockNetwork(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(ACTIVE,false));
    }
    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return true;
    }
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(player.getHeldItemMainhand().getItem()  instanceof ItemDebugger) {
            MFFS.getLog().info("Used");
            String prefix=world.isRemote?"client":"server";
            TileEntity te=world.getTileEntity(pos);
            if(te instanceof TileFENetwork) {
                TileFENetwork feNetwork= (TileFENetwork) te;

                player.sendMessage(new StringTextComponent(prefix+" deviceName: "+feNetwork.getDeviceName()), Util.DUMMY_UUID);
                player.sendMessage(new StringTextComponent(prefix+" NetworkID: "+feNetwork.getDeviceID()), Util.DUMMY_UUID);
                player.sendMessage(new StringTextComponent(prefix+" powerSourceID: "+feNetwork.getPowerSourceID()), Util.DUMMY_UUID);
                return ActionResultType.SUCCESS;
            }
            return ActionResultType.SUCCESS;
        }

        return super.onBlockActivated(state, world, pos, player, handIn, hit);
    }
}
