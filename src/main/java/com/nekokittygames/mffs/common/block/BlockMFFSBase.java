/*  
    Copyright (C) 2012 Thunderdark

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    Contributors:
    Thunderdark - initial implementation
 */

package com.nekokittygames.mffs.common.block;

//import buildcraft.api.tools.IToolWrench;

import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.SecurityHelper;
import com.nekokittygames.mffs.common.SecurityRight;
import com.nekokittygames.mffs.common.item.ItemCardEmpty;
import com.nekokittygames.mffs.common.item.ItemCardPowerLink;
import com.nekokittygames.mffs.common.item.ItemCardSecurityLink;
import com.nekokittygames.mffs.common.modules.ModuleBase;
import com.nekokittygames.mffs.common.multitool.ItemMultitool;
import com.nekokittygames.mffs.common.tileentity.TileEntityAdvSecurityStation;
import com.nekokittygames.mffs.common.tileentity.TileEntityControlSystem;
import com.nekokittygames.mffs.common.tileentity.TileEntityMachines;
import com.nekokittygames.mffs.common.tileentity.TileEntityProjector;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

public abstract class BlockMFFSBase extends Block implements ITileEntityProvider {

	public static final PropertyDirection FACING = PropertyDirection.create( "facing" );
	public static final PropertyBool ACTIVE= PropertyBool.create("active");
	public BlockMFFSBase(String blockName) {
		super(Material.IRON);
		setBlockName(this,blockName);
		setBlockUnbreakable();
		setResistance(100F);
		setSoundType(SoundType.METAL);
		setCreativeTab(ModularForceFieldSystem.MFFSTab);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING,EnumFacing.NORTH).withProperty(ACTIVE,false));
	}
	private static void setBlockName(final Block block, final String blockName) {
		block.setRegistryName(ModularForceFieldSystem.MODID, blockName);
		final ResourceLocation registryName = Objects.requireNonNull(block.getRegistryName());
		block.setUnlocalizedName(registryName.toString());
	}
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this,FACING,ACTIVE);
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
		return this.getDefaultState().withProperty(FACING, BlockPistonBase.getFacing(meta)).withProperty(ACTIVE, (x>0));
	}
	@Override
	public abstract TileEntity createNewTileEntity(World worldIn, int meta);



	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityplayer, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (entityplayer.isSneaking()) {
			return false;
		}

		if (world.isRemote)
			return true;

		TileEntityMachines tileentity = (TileEntityMachines) world.getTileEntity(pos);

		Item equipped = (entityplayer.getHeldItemMainhand() != null ? entityplayer.getHeldItemMainhand().getItem
				() : null);
		if(equipped != null) {
			// These items will handle their code.
			if(equipped instanceof ItemMultitool || equipped instanceof ItemCardEmpty || equipped instanceof
					ModuleBase || equipped instanceof ItemCardPowerLink || equipped instanceof ItemCardSecurityLink)
				return false;

			// Levers may be placed on MFFS Machines directly
			if(equipped == Item.getItemFromBlock(Blocks.LEVER))
				return false;

			// Make sure we actually have a tile entity...
			if(tileentity == null)
				return false;

			/*if(equipped instanceof IToolWrench && ((IToolWrench)equipped).canWrench(entityplayer, x, y, z)) {
				if(!tileentity.wrenchCanManipulate(entityplayer, side))
					return false;

				tileentity.setSide(side);

				((IToolWrench)equipped).wrenchUsed(entityplayer, x, y, z);

				return true;
			}*/
		}

		if (tileentity instanceof TileEntityAdvSecurityStation) {
			if (tileentity.isActive()) {
				if (!SecurityHelper.isAccessGranted(tileentity, entityplayer,
						world, SecurityRight.CSR)) {
					return true;
				}
			}
		}

		if (tileentity instanceof TileEntityControlSystem) {

			if (!SecurityHelper.isAccessGranted(tileentity, entityplayer,
					world, SecurityRight.UCS)) {
				return true;
			}
		}

		if (!SecurityHelper.isAccessGranted(tileentity, entityplayer, world,
				SecurityRight.EB)) {
			return true;
		}

		entityplayer.openGui(ModularForceFieldSystem.instance, 0, world, pos.getX(),pos.getY(),pos.getZ());

		return true;
	}


	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof TileEntityMachines)
			((TileEntityMachines) tileEntity).dropPlugins();
		world.removeTileEntity(pos);
	}


//	public int idDropped(int i, Random random) {
//		return blockID;
//	}

	public static boolean isActive(IBlockAccess iblockaccess, BlockPos pos) {
		TileEntity tileentity = iblockaccess.getTileEntity(pos);
		if (tileentity instanceof TileEntityMachines) {
			return ((TileEntityMachines) tileentity).isActive();
		} else {
			return false;
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof TileEntityMachines) {


		}
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty( FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
	}


	@Override
	public float getExplosionResistance(Entity exploder) {
		return super.getExplosionResistance(exploder);
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
		if (world.getTileEntity(pos) instanceof TileEntityMachines) {
			TileEntity tileentity = world.getTileEntity(pos);
			if (((TileEntityMachines) tileentity).isActive()) {
				return 999F;
			} else {
				return 100F;
			}
		}
		return 100F;
	}

}
