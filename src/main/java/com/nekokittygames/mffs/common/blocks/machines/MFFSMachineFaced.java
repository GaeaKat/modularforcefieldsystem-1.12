package com.nekokittygames.mffs.common.blocks.machines;

import com.nekokittygames.mffs.common.blocks.MFFSTileBlock;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * Created by katsw on 03/04/2016.
 */
public abstract class MFFSMachineFaced extends MFFSTileBlock{


    public static final PropertyDirection FACING = PropertyDirection.create( "facing" );
    public static final PropertyBool ACTIVE= PropertyBool.create("active");

    public MFFSMachineFaced(Material blockMaterial) {
        super(blockMaterial);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING,EnumFacing.NORTH).withProperty(ACTIVE,false));
    }

    public MFFSMachineFaced(Material blockMaterial, MapColor blockColour) {
        super(blockMaterial, blockColour);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer )
    {
        return this.getDefaultState().withProperty( FACING, BlockPistonBase.getFacingFromEntity(pos,placer));
    }


    @Override
    public int getMetaFromState(IBlockState state) {
        Byte b0  = 0;
        int i= b0 | state.getValue(FACING).getIndex();
        i=i| ((state.getValue(ACTIVE))? 0:1) << 3;
        return i;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        int x=meta&8;
        return this.getDefaultState().withProperty(FACING,BlockPistonBase.getFacing(meta)).withProperty(ACTIVE, (x>0));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer( this, new IProperty[] {FACING,ACTIVE} );
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        //worldIn.setBlockState(pos,worldIn.getBlockState(pos).withProperty(ACTIVE,!worldIn.getBlockState(pos).getValue(ACTIVE)));
    }

}
