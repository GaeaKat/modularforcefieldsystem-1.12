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
    
    Thunderdark 
    Matchlighter

 */

package com.nekokittygames.mffs.common.block;

import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.SecurityHelper;
import com.nekokittygames.mffs.common.SecurityRight;
import com.nekokittygames.mffs.common.multitool.ItemMultitool;
import com.nekokittygames.mffs.common.tileentity.TileEntitySecStorage;
import com.nekokittygames.mffs.libs.LibBlockNames;
import com.nekokittygames.mffs.libs.LibMisc;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;


public class BlockSecurtyStorage extends BlockMFFSBase {

	public BlockSecurtyStorage() {
		super(LibBlockNames.SECURITY_STORAGE);
	}

	@Override
	public TileEntity createNewTileEntity(World world,int meta) {
		return new TileEntitySecStorage();

	}


	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityplayer, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return true;

		if (entityplayer.getActiveItemStack() != null
				&& (entityplayer.getActiveItemStack().getItem() instanceof ItemMultitool)) {
			return false;
		}

		TileEntitySecStorage tileentity = (TileEntitySecStorage) world
				.getTileEntity(pos);
		if (tileentity != null) {

			if (SecurityHelper.isAccessGranted(tileentity, entityplayer, world,
					SecurityRight.OSS)) {
				if (!world.isRemote)
					entityplayer.openGui(ModularForceFieldSystem.instance, 0,
							world, pos.getX(),pos.getY(),pos.getZ());
				return true;
			} else {
				return true;
			}

		} else {

			return true;
		}
	}

}
