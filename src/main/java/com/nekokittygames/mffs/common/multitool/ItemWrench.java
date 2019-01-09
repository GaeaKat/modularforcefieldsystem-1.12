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

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

import buildcraft.api.tools.IToolWrench;

import com.nekokittygames.mffs.api.IMFFS_Wrench;
import com.nekokittygames.mffs.common.Functions;
import com.nekokittygames.mffs.common.tileentity.TileEntityAdvSecurityStation;
import com.nekokittygames.mffs.common.tileentity.TileEntityAreaDefenseStation;
import com.nekokittygames.mffs.common.tileentity.TileEntityMachines;
import com.nekokittygames.mffs.common.tileentity.TileEntityProjector;
import com.nekokittygames.mffs.libs.LibItemNames;

@Optional.InterfaceList({ @Optional.Interface(iface = "buildcraft.api.tools.IToolWrench", modid = "BuildCraftAPI|core") })
public class ItemWrench extends ItemMultitool implements IToolWrench {

	public ItemWrench() {
		super(0,LibItemNames.MULTITOOL_WRENCH);
	}






	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		if (world.isRemote)
			return EnumActionResult.PASS;
		ItemStack stack=player.getHeldItem(hand);
		TileEntity tileentity = world.getTileEntity(pos);

		if (tileentity instanceof TileEntityProjector) {
			if (((TileEntityProjector) tileentity).isBurnout()) {

				if (this.consumePower(stack, 10000, true)) {
					this.consumePower(stack, 10000, false);
					((TileEntityProjector) tileentity).setBurnedOut(false);
					Functions.ChattoPlayer(player, "multitool.projectorRepaired");
					return EnumActionResult.SUCCESS;
				} else {

					Functions.ChattoPlayer(player, "multitool.notEnoughFE");
					return EnumActionResult.FAIL;
				}

			}
		}



		if (tileentity instanceof IMFFS_Wrench) {

			if (this.consumePower(stack, 1000, true)) {

				if (((IMFFS_Wrench) tileentity).wrenchCanManipulate(player,side)) {

					if (tileentity instanceof TileEntityMachines) {

						if (tileentity instanceof TileEntityProjector) {
							if (((TileEntityProjector) tileentity).isActive())
								return EnumActionResult.FAIL;
						}

						if (tileentity instanceof TileEntityAdvSecurityStation) {
							if (((TileEntityAdvSecurityStation) tileentity)
									.isActive())
								return EnumActionResult.FAIL;
						}

						if (tileentity instanceof TileEntityAreaDefenseStation) {
							if (((TileEntityAreaDefenseStation) tileentity)
									.isActive())
								return EnumActionResult.FAIL;
						}

					}

					if (((IMFFS_Wrench) tileentity).getSide() != side) {

						((IMFFS_Wrench) tileentity).setSide(side);
						this.consumePower(stack, 1000, false);
						return EnumActionResult.SUCCESS;
					} else {
						world.spawnEntity(new EntityItem(world, pos.getX(),pos.getY(),pos.getZ(),
								new ItemStack(world.getBlockState(pos).getBlock())));
						world.setBlockState(pos, Blocks.AIR.getDefaultState(),2);
						// world.setBlockWithNotify(x, y, z, 0);
						this.consumePower(stack, 1000, false);
					}

				}
			} else {

				Functions.ChattoPlayer(player, I18n.format("multitool" +
						".notEnoughFE"));
			}
		}

		return EnumActionResult.PASS;
	}

	@Override
	@Optional.Method(modid = "BuildCraftAPI|core")
	public boolean canWrench(EntityPlayer player, EnumHand enumHand, ItemStack itemStack, RayTraceResult rayTraceResult) {
		if (this.consumePower(player.getHeldItem(enumHand), 1000, true)) {
			return true;
		}
		return false;
	}

	@Override
	@Optional.Method(modid = "BuildCraftAPI|core")
	public void wrenchUsed(EntityPlayer player, EnumHand enumHand, ItemStack itemStack, RayTraceResult rayTraceResult)
	{
		this.consumePower(player.getHeldItem(enumHand), 1000, false);
	}



	/*@Override
	public boolean canWrench(EntityPlayer player, int x, int y, int z) {
		if (this.consumePower(player.inventory.getCurrentItem(), 1000, true)) {
			return true;
		}
		return false;
	}

	@Override
	public void wrenchUsed(EntityPlayer player, int x, int y, int z) {
		this.consumePower(player.inventory.getCurrentItem(), 1000, false);
	}*/



}
