package net.newgaea.mffs.common.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import java.util.Random;

import static net.minecraft.inventory.InventoryHelper.spawnItemStack;

public class MFFSInventoryHelper {
    private static final Random RANDOM = new Random();

    public static void dropInventoryItems(World worldIn, BlockPos pos, IItemHandler inventory) {
        dropInventoryItems(worldIn, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), inventory);
    }

    public static void dropInventoryItems(World worldIn, Entity entityAt, IItemHandler inventory) {
        dropInventoryItems(worldIn, entityAt.getPosX(), entityAt.getPosY(), entityAt.getPosZ(), inventory);
    }

    private static void dropInventoryItems(World worldIn, double x, double y, double z, IItemHandler inventory) {
        for(int i = 0; i < inventory.getSlots(); ++i) {
            spawnItemStack(worldIn, x, y, z, inventory.getStackInSlot(i));
        }

    }


}
