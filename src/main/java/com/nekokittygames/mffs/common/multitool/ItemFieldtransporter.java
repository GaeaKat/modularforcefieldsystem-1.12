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

package com.nekokittygames.mffs.common.multitool;

import com.nekokittygames.mffs.libs.LibItemNames;
import com.nekokittygames.mffs.libs.LibMisc;
import com.nekokittygames.mffs.api.IFieldTeleporter;
import com.nekokittygames.mffs.common.Functions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFieldtransporter extends ItemMultitool implements
		IFieldTeleporter {

	public ItemFieldtransporter() {
		super(4);
		setUnlocalizedName(LibMisc.UNLOCALIZED_PREFIX+ LibItemNames.MULTITOOL_TRANSPORTER);
		setRegistryName(LibItemNames.MULTITOOL_TRANSPORTER);

	}

	@Override
	public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		return EnumActionResult.PASS;
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
		Functions.ChattoPlayer(player, "fieldSecurity.transmissionComplete");
	}

	@Override
	public void onFieldTeleportFailed(EntityPlayer player, ItemStack stack,
			int teleportCost) {
		Functions.ChattoPlayer(player, "fieldSecurity.notEnoughFE");
	}

}
