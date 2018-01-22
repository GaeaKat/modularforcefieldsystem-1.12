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
    
    Matchlighter
    Thunderdark 

 */

package com.nekokittygames.mffs.common.item;

import com.nekokittygames.mffs.libs.LibItemNames;
import com.nekokittygames.mffs.libs.LibMisc;
import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.NBTTagCompoundHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemForcicumCell extends ItemMFFSBase {

	private boolean aktiv = false;

	public ItemForcicumCell() {
		super();
		setMaxStackSize(1);
		setMaxDamage(100);
		setHasSubtypes(true);
		setUnlocalizedName(LibMisc.UNLOCALIZED_PREFIX+ LibItemNames.FORCICIUM_CELL);
		setRegistryName(LibItemNames.FORCICIUM_CELL);
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public boolean isDamageable() {
		return true;
	}



	public int getItemDamage(ItemStack itemStack) {
		return 101 - ((getForceciumlevel(itemStack) * 100) / getMaxForceciumlevel());

	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity,
			int par4, boolean par5) {

		if (world.isRemote == false) {

			if (aktiv) {

				if (getForceciumlevel(itemStack) < getMaxForceciumlevel()) {
					if (entity instanceof EntityPlayer) {

						List<Slot> slots = ((EntityPlayer) entity).inventoryContainer.inventorySlots;
						for (Slot slot : slots) {
							if (slot.getStack() != null) {
								if (slot.getStack().getItem() == ModItems.FORCICIUM) {

									setForceciumlevel(itemStack,
											getForceciumlevel(itemStack) + 1);

									if (slot.getStack().getCount() > 1) {
										ItemStack forcecium = new ItemStack(
												ModItems.FORCICIUM,
												slot.getStack().getCount() - 1);
										slot.putStack(forcecium);
									} else {

										slot.putStack(null);

									}
									break;

								}

							}
						}
					}

				}

				itemStack.setItemDamage(getItemDamage(itemStack));
			}
		}
	}


	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		String tool = String.format("%d / %d  Forcicium",
				getForceciumlevel(stack), getMaxForceciumlevel());
		tooltip.add(tool);
	}

	public boolean useForcecium(int count, ItemStack itemstack) {
		if (count > getForceciumlevel(itemstack)) {
			return false;
		} else {

			setForceciumlevel(itemstack, getForceciumlevel(itemstack) - count);
			return true;
		}

	}

	public int getMaxForceciumlevel() {

		return 1000;
	}

	public void setForceciumlevel(ItemStack itemStack, int Forceciumlevel) {

		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper
				.getTAGfromItemstack(itemStack);
		nbtTagCompound.setInteger("Forceciumlevel", Forceciumlevel);

	}

	public int getForceciumlevel(ItemStack itemstack) {

		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper
				.getTAGfromItemstack(itemstack);
		if (nbtTagCompound != null) {
			return nbtTagCompound.getInteger("Forceciumlevel");
		}
		return 0;
	}


	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer playerIn, EnumHand handIn) {
		if (world.isRemote == false) {
			if (!aktiv) {
				aktiv = true;
				playerIn.sendMessage(new TextComponentTranslation("itemInfo" +
						".forciciumCellActive"));
			} else {
				aktiv = false;
				playerIn.sendMessage(new TextComponentTranslation("itemInfo" +
						".forciciumCellInactive"));
			}

		}
		return ActionResult.newResult(EnumActionResult.SUCCESS,playerIn.getHeldItem(handIn));
	}



	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		ItemStack charged = new ItemStack(this, 1);
		charged.setItemDamage(1);
		setForceciumlevel(charged, getMaxForceciumlevel());
		subItems.add(charged);

		ItemStack empty = new ItemStack(this, 1);
		empty.setItemDamage(100);
		setForceciumlevel(empty, 0);
		subItems.add(empty);
	}


}
