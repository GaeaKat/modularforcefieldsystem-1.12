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

package mods.mffs.common.options;

import mods.mffs.api.PointXYZ;
import mods.mffs.common.ModularForceFieldSystem;
import mods.mffs.common.tileentity.TileEntityProjector;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.world.World;

public class ItemProjectorOptionSponge extends ItemProjectorOptionBase
		implements IInteriorCheck {
	public ItemProjectorOptionSponge(int i) {
		super(i);
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("mffs:options/Sponge");
	}

	@Override
	public void checkInteriorBlock(PointXYZ png, World world,
			TileEntityProjector Projector) {
		if (world.getBlockMaterial(png.X, png.Y, png.Z).isLiquid()) {
			if (!ModularForceFieldSystem.forceFieldRemoveOnlyWaterAndLava) {
				world.setBlock(png.X, png.Y, png.Z, 0, 0, 2);
			} else if (world.getBlockId(png.X, png.Y, png.Z) == 8
					|| world.getBlockId(png.X, png.Y, png.Z) == 9
					|| world.getBlockId(png.X, png.Y, png.Z) == 10
					|| world.getBlockId(png.X, png.Y, png.Z) == 11

			)

			{
				world.setBlock(png.X, png.Y, png.Z, 0, 0, 2);
			}
		}
	}

}