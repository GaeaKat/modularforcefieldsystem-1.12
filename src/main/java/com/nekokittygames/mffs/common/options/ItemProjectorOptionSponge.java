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

package com.nekokittygames.mffs.common.options;

import com.nekokittygames.mffs.api.PointXYZ;
import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.tileentity.TileEntityProjector;
import com.nekokittygames.mffs.libs.LibItemNames;
import com.nekokittygames.mffs.libs.LibMisc;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class ItemProjectorOptionSponge extends ItemProjectorOptionBase
		implements IInteriorCheck {
	public ItemProjectorOptionSponge() {
		super(LibItemNames.OPTION_SPONGE);
	}

	@Override
	public void checkInteriorBlock(PointXYZ png, World world,
			TileEntityProjector Projector) {
		if (world.getBlockState(png.pos).getMaterial().isLiquid()) {
			if (!ModularForceFieldSystem.forceFieldRemoveOnlyWaterAndLava) {
				world.setBlockState(png.pos, Blocks.AIR.getDefaultState(), 2);
			} else if (world.getBlockState(png.pos).getBlock() == Blocks.LAVA
					|| world.getBlockState(png.pos).getBlock() == Blocks.FLOWING_LAVA
					|| world.getBlockState(png.pos).getBlock() == Blocks.WATER
					|| world.getBlockState(png.pos).getBlock() == Blocks.FLOWING_WATER

			)

			{
				world.setBlockState(png.pos, Blocks.AIR.getDefaultState(), 2);
			}
		}
	}

}