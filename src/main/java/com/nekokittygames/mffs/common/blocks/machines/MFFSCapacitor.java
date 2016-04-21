package com.nekokittygames.mffs.common.blocks.machines;

import com.nekokittygames.mffs.common.MFFS;
import com.nekokittygames.mffs.common.blocks.MFFSTileBlock;
import com.nekokittygames.mffs.common.libs.LibBlockNames;
import com.nekokittygames.mffs.common.tiles.TileCapacitor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by katsw on 03/04/2016.
 */
public class MFFSCapacitor extends MFFSMachineFaced {
    public MFFSCapacitor() {
        super(Material.anvil);
        setRegistryName(LibBlockNames.MFFS_CAPACITOR);
        setUnlocalizedName(LibBlockNames.MFFS_CAPACITOR);
        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileCapacitor();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity == null || playerIn.isSneaking()) {
            return false;
        }
        //code to open gui explained later
        playerIn.openGui(MFFS.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
}
