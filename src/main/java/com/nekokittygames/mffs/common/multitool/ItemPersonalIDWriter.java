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
import com.nekokittygames.mffs.api.PointXYZ;
import com.nekokittygames.mffs.common.Functions;
import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.SecurityHelper;
import com.nekokittygames.mffs.common.SecurityRight;
import com.nekokittygames.mffs.common.item.ItemCard;
import com.nekokittygames.mffs.common.item.ItemCardDataLink;
import com.nekokittygames.mffs.common.item.ItemCardPersonalID;
import com.nekokittygames.mffs.common.tileentity.TileEntityMachines;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ItemPersonalIDWriter extends ItemMultitool {
	public ItemPersonalIDWriter() {
		super( 2);
		setUnlocalizedName(LibMisc.UNLOCALIZED_PREFIX+LibItemNames.MULTITOOL_ID_WRITER);
		setRegistryName(LibItemNames.MULTITOOL_ID_WRITER);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack itemstack,
			EntityPlayer entityplayer, Entity entity) {
		if (entity instanceof EntityPlayer) {
			List<Slot> slots = entityplayer.inventoryContainer.inventorySlots;
			for (Slot slot : slots) {
				ItemStack stack = slot.getStack();
				if (stack != null) {
					if (stack.getItem() == ModularForceFieldSystem.MFFSitemcardempty) {
						if (this.consumePower(itemstack, 1000, true)) {
							this.consumePower(itemstack, 1000, false);
							ItemStack IDCard = new ItemStack(
									ModularForceFieldSystem.MFFSItemIDCard, 1);
							ItemCardPersonalID.setOwner(IDCard,
									((EntityPlayer) entity).getUniqueID().toString(),((EntityPlayer)entity).getName());

							if (--stack.stackSize <= 0) {
								slot.putStack(IDCard);
							} else if (!entityplayer.inventory
									.addItemStackToInventory(IDCard))
								entityplayer.dropItem(IDCard,false);

							Functions.ChattoPlayer(entityplayer, I18n.format("multitool.idCardCreated"));
							return true;
						} else {
							Functions.ChattoPlayer(entityplayer, I18n.format("multitool.notEnoughFE"));
							return true;
						}
					}
				}
			}

			Functions.ChattoPlayer(entityplayer, I18n.format("multitool" +
					".needBlankCard"));
			return true;
		}
		return false;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (playerIn.isSneaking()) {
			return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
		}

		List<Slot> slots = playerIn.inventoryContainer.inventorySlots;
		for (Slot slot : slots) {
			ItemStack stack = slot.getStack();
			if (stack != null) {
				if (stack.getItem() == ModularForceFieldSystem.MFFSitemcardempty) {
					if (this.consumePower(itemStackIn, 1000, true)) {
						this.consumePower(itemStackIn, 1000, false);
						ItemStack IDCard = new ItemStack(
								ModularForceFieldSystem.MFFSItemIDCard, 1);
						ItemCardPersonalID.setOwner(IDCard,
								playerIn.getUniqueID().toString(),playerIn.getName());

						if (--stack.stackSize <= 0) {
							slot.putStack(IDCard);
						} else if (!playerIn.inventory
								.addItemStackToInventory(IDCard))
							playerIn.dropItem(IDCard,false);
						if (worldIn.isRemote)
							Functions.ChattoPlayer(playerIn, I18n.format("multitool.idCardCreated"));

						return ActionResult.newResult(EnumActionResult.SUCCESS,itemStackIn);
					} else {
						if (worldIn.isRemote)
							Functions
									.ChattoPlayer(playerIn, I18n.format("multitool.notEnoughFE"));
						return ActionResult.newResult(EnumActionResult.FAIL,itemStackIn);
					}
				}
			}
		}
		if (worldIn.isRemote)
			Functions.ChattoPlayer(playerIn, I18n.format("multitool" +
					".needBlankCard"));

		return ActionResult.newResult(EnumActionResult.FAIL,itemStackIn);
	}


	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)
			return EnumActionResult.PASS;

		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof TileEntityMachines) {
			if (SecurityHelper.isAccessGranted(tileEntity, playerIn, worldIn,
					SecurityRight.UCS)) {
				List<Slot> slots = playerIn.inventoryContainer.inventorySlots;
				for (Slot slot : slots) {
					ItemStack playerstack = slot.getStack();
					if (playerstack != null) {
						if (playerstack.getItem() == ModularForceFieldSystem.MFFSitemcardempty) {
							if (this.consumePower(stack, 1000, true)) {
								this.consumePower(stack, 1000, false);
								ItemStack IDCard = new ItemStack(
										ModularForceFieldSystem.MFFSitemDataLinkCard);

								ItemCard
										.setforArea(
												IDCard,
												((TileEntityMachines) tileEntity)
														.getDeviceName());
								((ItemCardDataLink) IDCard.getItem())
										.setInformation(
												IDCard,
												new PointXYZ(new BlockPos(pos), worldIn),
												"DeviceID",
												((TileEntityMachines) tileEntity)
														.getDeviceID(),
												tileEntity);

								if (--playerstack.stackSize <= 0) {
									slot.putStack(IDCard);
								} else if (!playerIn.inventory
										.addItemStackToInventory(IDCard))
									playerIn.dropItem(IDCard,false);

								playerIn.inventoryContainer
										.detectAndSendChanges();
								Functions.ChattoPlayer(playerIn, I18n.format("multitool.dataLinkCreated"));

								return EnumActionResult.SUCCESS;
							} else {

								Functions.ChattoPlayer(playerIn, I18n.format("multitool.notEnoughFE"));
								return EnumActionResult.FAIL;
							}
						}
					}
				}
			}
		}
		return EnumActionResult.FAIL;

	}

	@Override
	public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		return EnumActionResult.PASS;
	}


}
