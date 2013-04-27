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

package mods.mffs.common.item;

import mods.mffs.api.PointXYZ;
import mods.mffs.common.Functions;
import mods.mffs.common.Linkgrid;
import mods.mffs.common.ModularForceFieldSystem;
import mods.mffs.common.SecurityHelper;
import mods.mffs.common.SecurityRight;
import mods.mffs.common.tileentity.TileEntityAdvSecurityStation;
import mods.mffs.common.tileentity.TileEntityAreaDefenseStation;
import mods.mffs.common.tileentity.TileEntityCapacitor;
import mods.mffs.common.tileentity.TileEntityControlSystem;
import mods.mffs.common.tileentity.TileEntityProjector;
import mods.mffs.common.tileentity.TileEntitySecStorage;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ISidedInventory;

public class ItemCardSecurityLink extends ItemCard {

	public ItemCardSecurityLink(int i) {
		super(i);
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("mffs:SecurityLinkCard");
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity,
			int par4, boolean par5) {
		super.onUpdate(itemStack, world, entity, par4, par5);

		if (Tick > 600) {
			int Sec_ID = this.getValuefromKey("Secstation_ID", itemStack);
			if (Sec_ID != 0) {
				TileEntityAdvSecurityStation sec = Linkgrid.getWorldMap(world)
						.getSecStation().get(Sec_ID);
				if (sec != null) {
					if (!sec.getDeviceName().equals(
							ItemCard.getforAreaname(itemStack))) {
						ItemCard.setforArea(itemStack, sec.getDeviceName());
					}
				}
			}

			Tick = 0;
		}
		Tick++;
	}

	public static TileEntityAdvSecurityStation getLinkedSecurityStation(
			ISidedInventory inventiory, int slot, World world) {

		if (inventiory.getStackInSlot(slot) != null) {

			if (inventiory.getStackInSlot(slot).getItem() instanceof ItemCardSecurityLink) {
				ItemCardSecurityLink card = (ItemCardSecurityLink) inventiory
						.getStackInSlot(slot).getItem();
				PointXYZ png = card.getCardTargetPoint(inventiory
						.getStackInSlot(slot));
				if (png != null) {
					if (png.dimensionId != world.provider.dimensionId)
						return null;

					if (world.getBlockTileEntity(png.X, png.Y, png.Z) instanceof TileEntityAdvSecurityStation) {
						TileEntityAdvSecurityStation sec = (TileEntityAdvSecurityStation) world
								.getBlockTileEntity(png.X, png.Y, png.Z);
						if (sec != null) {

							if (sec.getDeviceID() == card.getValuefromKey(
									"Secstation_ID",
									inventiory.getStackInSlot(slot))
									&& card.getValuefromKey("Secstation_ID",
											inventiory.getStackInSlot(slot)) != 0) {

								if (!sec.getDeviceName().equals(
										ItemCard.getforAreaname(inventiory
												.getStackInSlot(slot)))) {
									ItemCard.setforArea(
											inventiory.getStackInSlot(slot),
											sec.getDeviceName());
								}
								return sec;
							}
						}
					} else {

						int Sec_ID = card.getValuefromKey("Secstation_ID",
								inventiory.getStackInSlot(slot));
						if (Sec_ID != 0) {
							TileEntityAdvSecurityStation sec = Linkgrid
									.getWorldMap(world).getSecStation()
									.get(Sec_ID);
							if (sec != null) {

								((ItemCard) card).setInformation(
										inventiory.getStackInSlot(slot),
										sec.getMaschinePoint(),
										"Secstation_ID", Sec_ID);
								return sec;
							}
						}

					}
					if (world.getChunkFromBlockCoords(png.X, png.Z).isChunkLoaded)
						inventiory
								.setInventorySlotContents(
										slot,
										new ItemStack(
												ModularForceFieldSystem.MFFSitemcardempty));
				}
			}
		}

		return null;
	}

	@Override
	public boolean onItemUseFirst(ItemStack itemstack,
			EntityPlayer entityplayer, World world, int i, int j, int k,
			int side, float hitX, float hitY, float hitZ) {
		TileEntity tileEntity = world.getBlockTileEntity(i, j, k);

		if (!world.isRemote) {

			if (tileEntity instanceof TileEntityControlSystem) {

				if (SecurityHelper.isAccessGranted(tileEntity, entityplayer,
						world, SecurityRight.EB)) {

					return Functions.setIteminSlot(itemstack, entityplayer,
							tileEntity, 0, "<Security Station Link>");

				}
			}

			if (tileEntity instanceof TileEntityCapacitor) {

				if (SecurityHelper.isAccessGranted(tileEntity, entityplayer,
						world, SecurityRight.EB)) {

					return Functions.setIteminSlot(itemstack, entityplayer,
							tileEntity, 4, "<Security Station Link>");

				}
			}

			if (tileEntity instanceof TileEntityAreaDefenseStation) {
				if (SecurityHelper.isAccessGranted(tileEntity, entityplayer,
						world, SecurityRight.EB)) {

					return Functions.setIteminSlot(itemstack, entityplayer,
							tileEntity, 1, "<Security Station Link>");
				}
			}

			if (tileEntity instanceof TileEntitySecStorage) {
				if (SecurityHelper.isAccessGranted(tileEntity, entityplayer,
						world, SecurityRight.EB)) {

					return Functions.setIteminSlot(itemstack, entityplayer,
							tileEntity, 0, "<Security Station Link>");
				}
			}

			if (tileEntity instanceof TileEntityProjector) {
				if (SecurityHelper.isAccessGranted(tileEntity, entityplayer,
						world, SecurityRight.EB)) {

					return Functions.setIteminSlot(itemstack, entityplayer,
							tileEntity, 12, "<Security Station Link>");
				}
			}
		}

		return false;
	}

}