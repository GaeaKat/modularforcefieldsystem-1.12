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
package mods.mffs.common.block;

import mods.mffs.common.ModularForceFieldSystem;
import mods.mffs.common.tileentity.TileEntityCapacitor;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCapacitor extends BlockMFFSBase {
	public BlockCapacitor(int i) {
		super(i);
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		icons[0] = iconRegister.registerIcon("mffs:Capacitor/SideInactive"
				+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32" : ""));
		icons[1] = iconRegister.registerIcon("mffs:Capacitor/FaceInactive"
				+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32" : ""));
		icons[2] = iconRegister.registerIcon("mffs:Capacitor/SideActive"
				+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32" : ""));
		icons[3] = iconRegister.registerIcon("mffs:Capacitor/FaceActive"
				+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32" : ""));

		blockIcon = icons[0];
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityCapacitor();
	}

}
