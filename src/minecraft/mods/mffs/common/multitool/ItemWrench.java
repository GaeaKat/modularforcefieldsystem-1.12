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

import buildcraft.api.tools.IToolWrench;
import ic2.api.tile.IWrenchable;
import mods.mffs.api.IMFFS_Wrench;
import mods.mffs.common.Functions;
import mods.mffs.common.tileentity.TileEntityAdvSecurityStation;
import mods.mffs.common.tileentity.TileEntityAreaDefenseStation;
import mods.mffs.common.tileentity.TileEntityMachines;
import mods.mffs.common.tileentity.TileEntityProjector;
import mods.railcraft.api.core.items.IToolCrowbar;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemWrench extends ItemMultitool implements IToolWrench,
		IToolCrowbar {

	public ItemWrench(int id) {
		super(id, 0);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ) {

		if (world.isRemote)
			return false;

		TileEntity tileentity = world.getBlockTileEntity(x, y, z);

		if (tileentity instanceof TileEntityProjector) {
			if (((TileEntityProjector) tileentity).isBurnout()) {

				if (this.consumePower(stack, 10000, true)) {
					this.consumePower(stack, 10000, false);
					((TileEntityProjector) tileentity).setBurnedOut(false);
					Functions.ChattoPlayer(player,
							"[MultiTool] Projector repaired");
					return true;
				} else {

					Functions
							.ChattoPlayer(player,
									"[MultiTool] Fail: not enough FP please charge need min 10000");
					return false;
				}

			}
		}

		if (tileentity instanceof IWrenchable
				&& !(tileentity instanceof IMFFS_Wrench)) {
			if (this.consumePower(stack, 1000, true)) {

				if (((IWrenchable) tileentity).wrenchCanSetFacing(player, side)) {

					((IWrenchable) tileentity).setFacing((short) side);
					this.consumePower(stack, 1000, false);
					return true;

				}

			} else {

				Functions
						.ChattoPlayer(player,
								"[MultiTool] Fail: not enough FE please charge need min 1000 for change Facing");
			}

			if (this.consumePower(stack, 25000, true)) {

				if (((IWrenchable) tileentity).wrenchCanRemove(player)) {
					world.spawnEntityInWorld(new EntityItem(world, x, y, z,
							((IWrenchable) tileentity).getWrenchDrop(player)));
					world.setBlock(x, y, z, 0, 0, 2);
					this.consumePower(stack, 25000, false);

				}

			} else {

				Functions
						.ChattoPlayer(player,
								"[MultiTool] Fail: not enough FE please charge need min 25000 for remove");
			}

		}

		if (tileentity instanceof IMFFS_Wrench) {

			if (this.consumePower(stack, 1000, true)) {

				if (((IMFFS_Wrench) tileentity).wrenchCanManipulate(player,
						side)) {

					if (tileentity instanceof TileEntityMachines) {

						if (tileentity instanceof TileEntityProjector) {
							if (((TileEntityProjector) tileentity).isActive())
								return false;
						}

						if (tileentity instanceof TileEntityAdvSecurityStation) {
							if (((TileEntityAdvSecurityStation) tileentity)
									.isActive())
								return false;
						}

						if (tileentity instanceof TileEntityAreaDefenseStation) {
							if (((TileEntityAreaDefenseStation) tileentity)
									.isActive())
								return false;
						}

					}

					if (((IMFFS_Wrench) tileentity).getSide() != side) {

						((IMFFS_Wrench) tileentity).setSide(side);
						this.consumePower(stack, 1000, false);
						return true;
					} else {

						world.spawnEntityInWorld(new EntityItem(world, x, y, z,
								new ItemStack(Block.blocksList[world
										.getBlockId(tileentity.xCoord,
												tileentity.yCoord,
												tileentity.zCoord)])));
						world.setBlock(x, y, z, 0, 0, 2);
						// world.setBlockWithNotify(x, y, z, 0);
						this.consumePower(stack, 1000, false);
					}

				}
			} else {

				Functions
						.ChattoPlayer(player,
								"[MultiTool] Fail: not enough FP please charge need min 1000");
			}
		}

		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world,
			EntityPlayer entityplayer) {

		return super.onItemRightClick(itemstack, world, entityplayer);
	}

	@Override
	public boolean canWrench(EntityPlayer player, int x, int y, int z) {
		if (this.consumePower(player.inventory.getCurrentItem(), 1000, true)) {
			return true;
		}
		return false;
	}

	@Override
	public void wrenchUsed(EntityPlayer player, int x, int y, int z) {
		this.consumePower(player.inventory.getCurrentItem(), 1000, false);
	}

	@Override
	public boolean canWhack(EntityPlayer player, ItemStack crowbar, int x,
			int y, int z) {
		return true;
	}

	@Override
	public void onWhack(EntityPlayer player, ItemStack crowbar, int x, int y,
			int z) {
		consumePower(player.inventory.getCurrentItem(), 1000, false);
	}

	@Override
	public boolean canLink(EntityPlayer player, ItemStack crowbar,
			EntityMinecart cart) {
		return true;
	}

	@Override
	public void onLink(EntityPlayer player, ItemStack crowbar,
			EntityMinecart cart) {

	}

	@Override
	public boolean canBoost(EntityPlayer player, ItemStack crowbar,
			EntityMinecart cart) {
		return true;
	}

	@Override
	public void onBoost(EntityPlayer player, ItemStack crowbar,
			EntityMinecart cart) {
		// CovertJaguar tentatively does 3x damage for Boosting carts.
		consumePower(player.inventory.getCurrentItem(), 3000, false);
	}

}
