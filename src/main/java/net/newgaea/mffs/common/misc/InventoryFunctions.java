package net.newgaea.mffs.common.misc;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InventoryFunctions {
    public static List<ItemStack> getItemStacksFromBlock(World world, BlockPos png) {
        BlockState state=world.getBlockState(png);
        LootContext.Builder builder=(new LootContext.Builder((ServerWorld) world)).withRandom(new Random()).withParameter(LootParameters.TOOL,ItemStack.EMPTY).withNullableParameter(LootParameters.BLOCK_ENTITY,world.getTileEntity(png));
        return state.getDrops(builder);
    }

    public static IItemHandler findAttachedInventory(World world, BlockPos pos) {



        IItemHandler inv=null;

        for (Direction dir:
             Direction.values()) {
            TileEntity te=world.getTileEntity(pos.offset(dir));
            if(te!=null) {
                LazyOptional<IItemHandler> caps=te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,dir.getOpposite());
                if(caps.isPresent()) {
                    IItemHandler handler= (IItemHandler) caps.cast();
                    if(inv==null)
                        inv=handler;
                    if(inv.getSlots() < handler.getSlots())
                        inv=handler;
                }
            }
        }
        return inv;
    }
}
