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

package mods.mffs.common.block;

import mods.mffs.common.ModularForceFieldSystem;
import mods.mffs.common.SecurityHelper;
import mods.mffs.common.SecurityRight;
import mods.mffs.common.multitool.ItemMultitool;
import mods.mffs.common.tileentity.TileEntitySecStorage;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSecurtyStorage extends BlockMFFSBase {

	public BlockSecurtyStorage(int i) {
		super(i);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntitySecStorage();

	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		icons[0] = icons[1] = iconRegister
				.registerIcon("mffs:SecStorage/Inactive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		icons[2] = icons[3] = iconRegister
				.registerIcon("mffs:SecStorage/Active"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));

		blockIcon = icons[0];
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k,
			EntityPlayer entityplayer, int par6, float par7, float par8,
			float par9) {

		if (world.isRemote)
			return true;

		if (entityplayer.getCurrentEquippedItem() != null
				&& (entityplayer.getCurrentEquippedItem().getItem() instanceof ItemMultitool)) {
			return false;
		}

		TileEntitySecStorage tileentity = (TileEntitySecStorage) world
				.getBlockTileEntity(i, j, k);
		if (tileentity != null) {

			if (SecurityHelper.isAccessGranted(tileentity, entityplayer, world,
					SecurityRight.OSS)) {
				if (!world.isRemote)
					entityplayer.openGui(ModularForceFieldSystem.instance, 0,
							world, i, j, k);
				return true;
			} else {
				return true;
			}

		} else {

			return true;
		}
	}
}
