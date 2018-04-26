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

import com.nekokittygames.mffs.libs.LibItemNames;
import com.nekokittygames.mffs.libs.LibMisc;
import com.nekokittygames.mffs.api.IForceEnergyStorageBlock;
import com.nekokittygames.mffs.api.IPowerLinkItem;
import com.nekokittygames.mffs.api.PointXYZ;
import com.nekokittygames.mffs.common.Functions;
import com.nekokittygames.mffs.common.Linkgrid;
import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.SecurityHelper;
import com.nekokittygames.mffs.common.SecurityRight;
import com.nekokittygames.mffs.common.tileentity.TileEntityAreaDefenseStation;
import com.nekokittygames.mffs.common.tileentity.TileEntityCapacitor;
import com.nekokittygames.mffs.common.tileentity.TileEntityExtractor;
import com.nekokittygames.mffs.common.tileentity.TileEntityMachines;
import com.nekokittygames.mffs.common.tileentity.TileEntityProjector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCardPowerLink extends ItemCard implements IPowerLinkItem {

	public IForceEnergyStorageBlock storage;

	public ItemCardPowerLink()
	{
		super();
		setUnlocalizedName(LibMisc.UNLOCALIZED_PREFIX+LibItemNames.POWER_LINK_CARD);
		setRegistryName(LibItemNames.POWER_LINK_CARD);
	}


	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity,
			int par4, boolean par5) {
		super.onUpdate(itemStack, world, entity, par4, par5);

		if (Tick > 600) {
			int Cap_ID = this.getValuefromKey("CapacitorID", itemStack);
			if (Cap_ID != 0) {
				TileEntityCapacitor cap = Linkgrid.getWorldMap(world)
						.getCapacitor().get(Cap_ID);
				if (cap != null) {
					if (!cap.getDeviceName().equals(
							ItemCard.getforAreaname(itemStack))) {
						ItemCard.setforArea(itemStack, cap.getDeviceName());
					}
				}
			}

			Tick = 0;
		}
		Tick++;
	}


	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {

		TileEntity tileEntity = world.getTileEntity(pos);
		ItemStack stack=player.getHeldItem(hand);
		if (!world.isRemote) {

			if (tileEntity instanceof TileEntityProjector) {
				if (SecurityHelper.isAccessGranted(tileEntity, player,
						world, SecurityRight.EB)) {

					return Functions.setIteminSlot(stack, player,
							tileEntity, 0, "<Power-Link>")?EnumActionResult.SUCCESS:EnumActionResult.FAIL;

				}
			}

			if (tileEntity instanceof TileEntityExtractor) {
				if (SecurityHelper.isAccessGranted(tileEntity, player,
						world, SecurityRight.EB)) {

					if (((TileEntityExtractor) tileEntity).getStackInSlot(1).isEmpty()) {
						((TileEntityExtractor) tileEntity).setInventorySlotContents(1, stack.copy());
						player.inventory.mainInventory.get(player.inventory.currentItem).setCount(0);
						Functions.ChattoPlayer(player, "linkCard.installed");
						return EnumActionResult.SUCCESS;
					} else {
						if (((TileEntityExtractor) tileEntity)
								.getStackInSlot(1).getItem() == ModItems.EMPTY_CARD) {
							ItemStack itemstackcopy = stack.copy();
							((TileEntityExtractor) tileEntity)
									.setInventorySlotContents(1, itemstackcopy);
							Functions.ChattoPlayer(player, "linkCard.copied");
							return EnumActionResult.SUCCESS;
						}
						Functions.ChattoPlayer(player, "linkCard.notEmpty");
						return EnumActionResult.FAIL;
					}
				}
			}

			if (tileEntity instanceof TileEntityAreaDefenseStation) {
				if (SecurityHelper.isAccessGranted(tileEntity, player,
						world, SecurityRight.EB)) {

					return Functions.setIteminSlot(stack, player,
							tileEntity, 0, "<Power-Link>")?EnumActionResult.SUCCESS:EnumActionResult.FAIL;
				}
			}

			if (tileEntity instanceof TileEntityCapacitor) {
				if (SecurityHelper.isAccessGranted(tileEntity, player,
						world, SecurityRight.EB)) {

					return Functions.setIteminSlot(stack, player,
							tileEntity, 2, "<Power-Link>")?EnumActionResult.SUCCESS:EnumActionResult.FAIL;
				}
			}

		}
		return EnumActionResult.PASS;
	}

	public IForceEnergyStorageBlock getForceEnergyStorageBlock(
			ItemStack itemStack, TileEntityMachines tem, World world) {
		if (itemStack != null && itemStack.getItem() instanceof ItemCard) {
			if (((ItemCard) itemStack.getItem()).isvalid(itemStack)) {
				PointXYZ png = this.getCardTargetPoint(itemStack);
				if (png != null) {
					if (png.dimensionId != world.provider.getDimension())
						return null;

					if (world.getTileEntity(png.pos) instanceof TileEntityCapacitor) {
						TileEntityCapacitor cap = (TileEntityCapacitor) world
								.getTileEntity(png.pos);
						if (cap != null) {

							if (cap.getPowerStorageID() == this
									.getValuefromKey("CapacitorID", itemStack)
									&& this.getValuefromKey("CapacitorID",
											itemStack) != 0) {

								if (!cap.getDeviceName().equals(
										ItemCard.getforAreaname(itemStack))) {
									ItemCard.setforArea(itemStack,
											cap.getDeviceName());
								}

								if (cap.getTransmitRange() >= PointXYZ
										.distance(tem.getMaschinePoint(),
												cap.getMaschinePoint())) {
									return cap;
								}
								return null;
							}
						}
					} else {

						int Cap_ID = this.getValuefromKey("CapacitorID",
								itemStack);
						if (Cap_ID != 0) {
							TileEntityCapacitor cap = Linkgrid
									.getWorldMap(png.getPointWorld())
									.getCapacitor().get(Cap_ID);
							if (cap != null) {

								((ItemCard) this).setInformation(itemStack,
										cap.getMaschinePoint(), "CapacitorID",
										Cap_ID);
								if (cap.getTransmitRange() >= PointXYZ
										.distance(tem.getMaschinePoint(),
												cap.getMaschinePoint())) {
									return cap;
								}
								return null;
							}
						}

					}
					if (world.getChunkFromBlockCoords(png.pos).isLoaded())
						((ItemCard) itemStack.getItem()).setinvalid(itemStack);

				}
			}
		}
		return null;

	}

	@Override
	public int getAvailablePower(ItemStack itemStack, TileEntityMachines tem,
			World world) {
		storage = getForceEnergyStorageBlock(itemStack, tem, world);
		if (storage != null)
			return storage.getStorageAvailablePower();
		return 0;
	}

	@Override
	public int getMaximumPower(ItemStack itemStack, TileEntityMachines tem,
			World world) {
		storage = getForceEnergyStorageBlock(itemStack, tem, world);
		if (storage != null)
			return storage.getStorageMaxPower();
		return 1;
	}

	@Override
	public int getPowersourceID(ItemStack itemStack, TileEntityMachines tem,
			World world) {
		storage = getForceEnergyStorageBlock(itemStack, tem, world);
		if (storage != null)
			return storage.getPowerStorageID();
		return 0;
	}

	@Override
	public int getPercentageCapacity(ItemStack itemStack,
			TileEntityMachines tem, World world) {
		storage = getForceEnergyStorageBlock(itemStack, tem, world);
		if (storage != null)
			return storage.getPercentageStorageCapacity();
		return 0;
	}

	@Override
	public boolean consumePower(ItemStack itemStack, int powerAmount,
			boolean simulation, TileEntityMachines tem, World world) {
		storage = getForceEnergyStorageBlock(itemStack, tem, world);
		if (storage != null)
			return storage.consumePowerfromStorage(powerAmount, simulation);
		return false;
	}

	@Override
	public boolean insertPower(ItemStack itemStack, int powerAmount,
			boolean simulation, TileEntityMachines tem, World world) {
		storage = getForceEnergyStorageBlock(itemStack, tem, world);
		if (storage != null)
			return storage.insertPowertoStorage(powerAmount, simulation);
		return false;
	}

	@Override
	public int getfreeStorageAmount(ItemStack itemStack,
			TileEntityMachines tem, World world) {
		storage = getForceEnergyStorageBlock(itemStack, tem, world);
		if (storage != null)
			return storage.getfreeStorageAmount();
		return 0;
	}

	@Override
	public boolean isPowersourceItem() {
		return false;
	}

}
