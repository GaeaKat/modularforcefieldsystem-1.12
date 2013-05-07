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
import mods.mffs.api.IFieldTeleporter;
import mods.mffs.common.Functions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemFieldtransporter extends ItemMultitool implements
		IFieldTeleporter {

	public ItemFieldtransporter(int id) {
		super(id, 4);

	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer entityplayer,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ) {
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world,
			EntityPlayer entityplayer) {

		return super.onItemRightClick(itemstack, world, entityplayer);

	}

	@Override
	public boolean canFieldTeleport(EntityPlayer player, ItemStack stack,
			int teleportCost) {
		return consumePower(stack, teleportCost, true);
	}

	@Override
	public void onFieldTeleportSuccess(EntityPlayer player, ItemStack stack,
			int teleportCost) {
		consumePower(stack, teleportCost, false);
		Functions.ChattoPlayer(player, LanguageRegistry.instance().getStringLocalization("fieldSecurity" +
				".transmissionComplete"));
	}

	@Override
	public void onFieldTeleportFailed(EntityPlayer player, ItemStack stack,
			int teleportCost) {
		Functions.ChattoPlayer(player, LanguageRegistry.instance().getStringLocalization("fieldSecurity.notEnoughFE"));
	}

}
