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

package com.nekokittygames.mffs.common.item;

import com.nekokittygames.mffs.api.PointXYZ;
import com.nekokittygames.mffs.common.Functions;
import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.SecurityHelper;
import com.nekokittygames.mffs.common.SecurityRight;
import com.nekokittygames.mffs.common.tileentity.TileEntityAdvSecurityStation;
import com.nekokittygames.mffs.common.tileentity.TileEntityCapacitor;
import com.nekokittygames.mffs.libs.LibItemNames;
import com.nekokittygames.mffs.libs.LibMisc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemCardEmpty extends ItemMFFSBase {
	public ItemCardEmpty() {
		super();
		setMaxStackSize(16);
		setUnlocalizedName(LibMisc.UNLOCALIZED_PREFIX+ LibItemNames.EMPTY_CARD);
		setRegistryName(LibItemNames.EMPTY_CARD);

	}


	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return EnumActionResult.PASS;

		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TileEntityAdvSecurityStation) {

			if (((TileEntityAdvSecurityStation) tileEntity).isActive()) {

				if (SecurityHelper.isAccessGranted(tileEntity, player,
						world, SecurityRight.CSR)) {

					ItemStack newcard = new ItemStack(
							ModularForceFieldSystem.MFFSItemSecLinkCard);
					((ItemCardSecurityLink) newcard.getItem()).setInformation(
							newcard, new PointXYZ(pos, world),
							"Secstation_ID",
							((TileEntityAdvSecurityStation) tileEntity)
									.getDeviceID());
					ItemCard.setforArea(
							newcard,
							((TileEntityAdvSecurityStation) tileEntity)
									.getDeviceName());

					if (--stack.stackSize <= 0) {
						player.inventory.setInventorySlotContents(
								player.inventory.currentItem, newcard);
					} else if (!player.inventory
							.addItemStackToInventory(newcard))
						player.dropItem(newcard,false);

					player.inventoryContainer.detectAndSendChanges();
					Functions.ChattoPlayer(player, "securityStation.cardCreated");

					return EnumActionResult.SUCCESS;
				}
			} else {

				Functions.ChattoPlayer(player, "securityStation.mustBeActive");
			}

		}

		if (tileEntity instanceof TileEntityCapacitor) {

			if (SecurityHelper.isAccessGranted(tileEntity, player, world,
					SecurityRight.EB)) {

				ItemStack newcard = new ItemStack(
						ModularForceFieldSystem.MFFSitemfc);
				((ItemCardPowerLink) newcard.getItem()).setInformation(newcard,
						new PointXYZ(pos, world), "CapacitorID",
						((TileEntityCapacitor) tileEntity).getPowerStorageID());
				ItemCard.setforArea(newcard,
						((TileEntityCapacitor) tileEntity).getDeviceName());

				if (--stack.stackSize <= 0) {
					player.inventory.setInventorySlotContents(
							player.inventory.currentItem, newcard);
				} else if (!player.inventory
						.addItemStackToInventory(newcard))
					player.dropItem(newcard,false);

				player.inventoryContainer.detectAndSendChanges();

				player.addChatMessage(new TextComponentTranslation("capacitor.cardCreated"));

				return EnumActionResult.SUCCESS;
			}
		}

		return EnumActionResult.PASS;
	}

}
