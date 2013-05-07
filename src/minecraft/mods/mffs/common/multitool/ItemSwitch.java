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

package mods.mffs.common.multitool;

import cpw.mods.fml.common.registry.LanguageRegistry;
import mods.mffs.api.ISwitchabel;
import mods.mffs.common.Functions;
import mods.mffs.common.SecurityHelper;
import mods.mffs.common.SecurityRight;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemSwitch extends ItemMultitool {

	public ItemSwitch(int id) {
		super(id, 1);

	}

	@Override
	public boolean onItemUseFirst(ItemStack itemstack,
			EntityPlayer entityplayer, World world, int x, int y, int z,
			int side, float hitX, float hitY, float hitZ) {

		if (world.isRemote)
			return false;

		TileEntity tileentity = world.getBlockTileEntity(x, y, z);

		if (tileentity instanceof ISwitchabel) {

			if (SecurityHelper.isAccessGranted(tileentity, entityplayer, world,
					SecurityRight.EB)) {

				if (((ISwitchabel) tileentity).isSwitchabel()) {
					if (this.consumePower(itemstack, 1000, true)) {
						this.consumePower(itemstack, 1000, false);
						((ISwitchabel) tileentity).toggelSwitchValue();
						return true;
					} else {

						Functions.ChattoPlayer(entityplayer, LanguageRegistry.instance().getStringLocalization
								("multitool.notEnoughFE"));
						return false;
					}
				} else {

					Functions.ChattoPlayer(entityplayer, LanguageRegistry.instance().getStringLocalization
							("multitool.notInSwitchMode"));
					return false;
				}

			}

		}

		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world,
			EntityPlayer entityplayer) {

		return super.onItemRightClick(itemstack, world, entityplayer);
	}

}
