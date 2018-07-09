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
    Matchlighter 
 */

package com.nekokittygames.mffs.common.multitool;

import com.nekokittygames.mffs.api.IForceEnergyItems;
import com.nekokittygames.mffs.common.ForceEnergyItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class ItemMultitool extends ForceEnergyItems implements
		IForceEnergyItems {
	private int typ;


	protected ItemMultitool( int typ, boolean addToList) {
		super();
		this.typ = typ;
		setMaxStackSize(1);
		setMaxDamage(100);
		setHasSubtypes(true);
		if (addToList)
			MTTypes.add(this);
	}

	protected ItemMultitool( int typ) {
		this(typ, true);
	}


	private static List<ItemMultitool> MTTypes = new ArrayList<ItemMultitool>();

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public boolean isDamageable() {
		return true;
	}


	@Override
	public abstract EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) ;



	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (playerIn.isSneaking()) {
			int modeNum = 0;
			for (int i = 0; i < MTTypes.size(); i++) {
				ItemMultitool MT = MTTypes.get(i);
				if (MT.getRegistryName().equals(playerIn.getHeldItem(handIn).getItem().getRegistryName())) {
					if (i + 1 < MTTypes.size()) {
						modeNum = i + 1;
					} else {
						modeNum = 0;
					}
				}
			}

			int powerleft = getAvailablePower(playerIn.getHeldItem(handIn));
			ItemStack inHand = playerIn.inventory.getCurrentItem();
			inHand = new ItemStack(MTTypes.get(modeNum), 1);
			this.chargeItem(inHand, powerleft, false);
			return ActionResult.newResult(EnumActionResult.SUCCESS,inHand);
		}
		return ActionResult.newResult(EnumActionResult.FAIL,playerIn.getHeldItem(handIn));
	}


	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World,
			Entity par3Entity, int par4, boolean par5) {
		par1ItemStack.setItemDamage(getItemDamage(par1ItemStack));
	}

	@Override
	public void addInformation(ItemStack itemStack, @Nullable World worldIn, List<String> info, ITooltipFlag flagIn) {
		String tooltip = String.format("%d FE/%d FE ",
				getAvailablePower(itemStack), getMaximumPower(itemStack));
		info.add(tooltip);
	}

	@Override
	public int getPowerTransferrate() {

		return 50000;
	}

	@Override
	public int getMaximumPower(ItemStack itemStack) {

		return 1000000;
	}

	@Override
	public int getItemDamage(ItemStack itemStack) {
		return 101 - ((getAvailablePower(itemStack) * 100) / getMaximumPower(itemStack));

	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (!isInCreativeTab(tab)) return;
		super.getSubItems(tab, items);
		ItemStack charged = new ItemStack(this, 1);
		charged.setItemDamage(1);
		setAvailablePower(charged, getMaximumPower(null));
		items.add(charged);
	}

}
