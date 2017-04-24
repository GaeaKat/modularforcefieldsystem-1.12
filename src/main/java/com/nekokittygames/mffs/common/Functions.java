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

package com.nekokittygames.mffs.common;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class Functions {
	public static Block getBlock(BlockPos pos, World world) {
		return world.getBlockState(pos).getBlock();
	}

	public static List<ItemStack> getItemStackFromBlock(World world,
														BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();

		if (block == null)
			return null;

		return block.getDrops(world, pos, world.getBlockState(pos), 0);
	}

	public static void ChattoPlayer(EntityPlayer player, String Message, Object... args) {
		player.addChatMessage(new TextComponentTranslation(Message, args));
	}

	public static boolean setIteminSlot(ItemStack itemstack,
			EntityPlayer entityplayer, TileEntity tileEntity, int Slot,
			String Cardname) {
		if (((IInventory) tileEntity).getStackInSlot(Slot) == null) {
			((IInventory) tileEntity).setInventorySlotContents(Slot, itemstack);
			entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = null;
			Functions.ChattoPlayer(entityplayer, "generic.card.installed", Cardname);
			((IInventory) tileEntity).markDirty();
			return true;
		} else {
			if (((IInventory) tileEntity).getStackInSlot(Slot).getItem() == ModularForceFieldSystem.MFFSitemcardempty) {
				ItemStack itemstackcopy = itemstack.copy();
				((IInventory) tileEntity).setInventorySlotContents(Slot,
						itemstackcopy);
				Functions.ChattoPlayer(entityplayer, "generic.card.copied", Cardname);
				((IInventory) tileEntity).markDirty();
				return true;
			}
			Functions.ChattoPlayer(entityplayer, "generic.card.fail");
			return false;
		}
	}

}
