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

import java.util.Random;

import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.modules.ModuleBase;
import com.nekokittygames.mffs.common.tileentity.TileEntityMachines;
import com.nekokittygames.mffs.common.tileentity.TileEntityProjector;
import com.nekokittygames.mffs.libs.LibBlockNames;
import com.nekokittygames.mffs.libs.LibMisc;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockProjector extends BlockMFFSBase {
	public static final PropertyInteger FIELD_TYPE= PropertyInteger.create("type",0, 8);
	public BlockProjector() {

		super(LibBlockNames.PROJECTOR);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING,EnumFacing.NORTH).withProperty(ACTIVE,false).withProperty(FIELD_TYPE,0));
	}


	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this,FACING,ACTIVE,FIELD_TYPE);
	}

		@Override
		public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
			TileEntityProjector proj= (TileEntityProjector) worldIn.getTileEntity(pos);
			int type=0;
			if(proj.get_type()!=null)
				type=proj.get_type().getForceFieldModuleType();
			//ModularForceFieldSystem.log.info("With state type: "+type);
			return super.getActualState(state, worldIn, pos).withProperty(FIELD_TYPE,type);
		}

	@Override
	public TileEntity createNewTileEntity(World world,int meta) {
		return new TileEntityProjector();
	}


	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityplayer, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntityProjector tileentity = (TileEntityProjector) world
				.getTileEntity(pos);

		if (tileentity.isBurnout()) {
			return false;
		}

		return super.onBlockActivated(world, pos, state, entityplayer, hand, facing, hitX, hitY, hitZ);
	}


	@Override
	public void randomDisplayTick(IBlockState stateIn, World world, BlockPos pos, Random rand) {
		TileEntityProjector tileentity = (TileEntityProjector) world
				.getTileEntity(pos);

		if (tileentity.isBurnout()) {
			double d = pos.getX() + Math.random();
			double d1 = pos.getY() + Math.random();
			double d2 = pos.getZ() + Math.random();

			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d, d1, d2, 0.0D, 0.0D, 0.0D);
		}

	}

}
