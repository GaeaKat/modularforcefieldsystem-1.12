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
import com.nekokittygames.mffs.common.Linkgrid;
import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.SecurityHelper;
import com.nekokittygames.mffs.common.SecurityRight;
import com.nekokittygames.mffs.common.tileentity.TileEntityAdvSecurityStation;
import com.nekokittygames.mffs.common.tileentity.TileEntityAreaDefenseStation;
import com.nekokittygames.mffs.common.tileentity.TileEntityCapacitor;
import com.nekokittygames.mffs.common.tileentity.TileEntityControlSystem;
import com.nekokittygames.mffs.common.tileentity.TileEntityProjector;
import com.nekokittygames.mffs.common.tileentity.TileEntitySecStorage;
import com.nekokittygames.mffs.libs.LibItemNames;
import com.nekokittygames.mffs.libs.LibMisc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCardSecurityLink extends ItemCard {

	public ItemCardSecurityLink() {
		super();
		setUnlocalizedName(LibMisc.UNLOCALIZED_PREFIX+ LibItemNames.SECURITY_LINK_CARD);
		setRegistryName(LibItemNames.SECURITY_LINK_CARD);
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

		if (inventiory.getStackInSlot(slot) != ItemStack.EMPTY) {

			if (inventiory.getStackInSlot(slot).getItem() instanceof ItemCardSecurityLink) {
				ItemCardSecurityLink card = (ItemCardSecurityLink) inventiory
						.getStackInSlot(slot).getItem();
				PointXYZ png = card.getCardTargetPoint(inventiory
						.getStackInSlot(slot));
				if (png != null) {
					if (png.dimensionId != world.provider.getDimension())
						return null;

					if (world.getTileEntity(png.pos) instanceof TileEntityAdvSecurityStation) {
						TileEntityAdvSecurityStation sec = (TileEntityAdvSecurityStation) world
								.getTileEntity(png.pos);
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
					if (world.getChunkFromBlockCoords(png.pos).isLoaded())
						inventiory
								.setInventorySlotContents(
										slot,
										new ItemStack(
												ModItems.EMPTY_CARD));
				}
			}
		}

		return null;
	}


	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		TileEntity tileEntity = world.getTileEntity(pos);
		ItemStack stack=player.getHeldItem(hand);
		if (!world.isRemote) {

			if (tileEntity instanceof TileEntityControlSystem) {

				if (SecurityHelper.isAccessGranted(tileEntity, player,
						world, SecurityRight.EB)) {

					return Functions.setIteminSlot(stack, player,
							tileEntity, 0, "<Security Station Link>")?EnumActionResult.SUCCESS:EnumActionResult.FAIL;

				}
			}

			if (tileEntity instanceof TileEntityCapacitor) {

				if (SecurityHelper.isAccessGranted(tileEntity, player,
						world, SecurityRight.EB)) {

					return Functions.setIteminSlot(stack, player,
							tileEntity, 4, "<Security Station Link>")?EnumActionResult.SUCCESS:EnumActionResult.FAIL;

				}
			}

			if (tileEntity instanceof TileEntityAreaDefenseStation) {
				if (SecurityHelper.isAccessGranted(tileEntity, player,
						world, SecurityRight.EB)) {

					return Functions.setIteminSlot(stack, player,
							tileEntity, 1, "<Security Station Link>")?EnumActionResult.SUCCESS:EnumActionResult.FAIL;
				}
			}

			if (tileEntity instanceof TileEntitySecStorage) {
				if (SecurityHelper.isAccessGranted(tileEntity, player,
						world, SecurityRight.EB)) {

					return Functions.setIteminSlot(stack, player,
							tileEntity, 0, "<Security Station Link>")?EnumActionResult.SUCCESS:EnumActionResult.FAIL;
				}
			}

			if (tileEntity instanceof TileEntityProjector) {
				if (SecurityHelper.isAccessGranted(tileEntity, player,
						world, SecurityRight.EB)) {

					return Functions.setIteminSlot(stack, player,
							tileEntity, 12, "<Security Station Link>")?EnumActionResult.SUCCESS:EnumActionResult.FAIL;
				}
			}
		}

		return EnumActionResult.FAIL;
	}

}