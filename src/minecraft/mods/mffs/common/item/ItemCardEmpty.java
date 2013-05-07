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

import cpw.mods.fml.common.registry.LanguageRegistry;
import mods.mffs.api.PointXYZ;
import mods.mffs.common.Functions;
import mods.mffs.common.ModularForceFieldSystem;
import mods.mffs.common.SecurityHelper;
import mods.mffs.common.SecurityRight;
import mods.mffs.common.tileentity.TileEntityAdvSecurityStation;
import mods.mffs.common.tileentity.TileEntityCapacitor;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemCardEmpty extends ItemMFFSBase {
	public ItemCardEmpty(int i) {
		super(i);
		setMaxStackSize(16);
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("mffs:EmptyCard");
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player,
			World world, int i, int j, int k, int l, float par8, float par9,
			float par10) {

		if (world.isRemote)
			return false;

		TileEntity tileEntity = world.getBlockTileEntity(i, j, k);

		if (tileEntity instanceof TileEntityAdvSecurityStation) {

			if (((TileEntityAdvSecurityStation) tileEntity).isActive()) {

				if (SecurityHelper.isAccessGranted(tileEntity, player,
						world, SecurityRight.CSR)) {

					ItemStack newcard = new ItemStack(
							ModularForceFieldSystem.MFFSItemSecLinkCard);
					((ItemCardSecurityLink) newcard.getItem()).setInformation(
							newcard, new PointXYZ(i, j, k, world),
							"Secstation_ID",
							((TileEntityAdvSecurityStation) tileEntity)
									.getDeviceID());
					ItemCard.setforArea(
							newcard,
							((TileEntityAdvSecurityStation) tileEntity)
									.getDeviceName());

					if (--itemstack.stackSize <= 0) {
						player.inventory.setInventorySlotContents(
								player.inventory.currentItem, newcard);
					} else if (!player.inventory
							.addItemStackToInventory(newcard))
						player.dropPlayerItem(newcard);

					player.inventoryContainer.detectAndSendChanges();
					Functions.ChattoPlayer(player, LanguageRegistry.instance().getStringLocalization
							("securityStation.cardCreated"));

					return true;
				}
			} else {

				Functions.ChattoPlayer(player, LanguageRegistry.instance().getStringLocalization("securityStation" +
						".mustBeActive"));
			}

		}

		if (tileEntity instanceof TileEntityCapacitor) {

			if (SecurityHelper.isAccessGranted(tileEntity, player, world,
					SecurityRight.EB)) {

				ItemStack newcard = new ItemStack(
						ModularForceFieldSystem.MFFSitemfc);
				((ItemCardPowerLink) newcard.getItem()).setInformation(newcard,
						new PointXYZ(i, j, k, world), "CapacitorID",
						((TileEntityCapacitor) tileEntity).getPowerStorageID());
				ItemCard.setforArea(newcard,
						((TileEntityCapacitor) tileEntity).getDeviceName());

				if (--itemstack.stackSize <= 0) {
					player.inventory.setInventorySlotContents(
							player.inventory.currentItem, newcard);
				} else if (!player.inventory
						.addItemStackToInventory(newcard))
					player.dropPlayerItem(newcard);

				player.inventoryContainer.detectAndSendChanges();

				player.addChatMessage(LanguageRegistry.instance().getStringLocalization("capacitor.cardCreated"));

				return true;
			}
		}

		return false;
	}
}
