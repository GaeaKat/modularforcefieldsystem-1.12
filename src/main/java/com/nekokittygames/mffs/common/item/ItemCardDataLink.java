package com.nekokittygames.mffs.common.item;

import com.nekokittygames.mffs.libs.LibItemNames;
import com.nekokittygames.mffs.libs.LibMisc;
import com.nekokittygames.mffs.api.PointXYZ;
import com.nekokittygames.mffs.common.Linkgrid;
import com.nekokittygames.mffs.common.MFFSMaschines;
import com.nekokittygames.mffs.common.NBTTagCompoundHelper;
import com.nekokittygames.mffs.common.tileentity.TileEntityMachines;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCardDataLink extends ItemCard {

	public ItemCardDataLink() {
		super();
		setMaxStackSize(1);
		setUnlocalizedName(LibMisc.UNLOCALIZED_PREFIX+LibItemNames.DATA_LINK_CARD);
		setRegistryName(LibItemNames.DATA_LINK_CARD);
	}


	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity,
			int par4, boolean par5) {
		super.onUpdate(itemStack, world, entity, par4, par5);

		if (Tick > 600) {

			int DeviceID = this.getValuefromKey("DeviceID", itemStack);
			if (DeviceID != 0) {
				TileEntityMachines device = Linkgrid.getWorldMap(world)
						.getTileEntityMachines(getDeviceTyp(itemStack),
								DeviceID);
				if (device != null) {

					if (!device.getDeviceName().equals(
							ItemCard.getforAreaname(itemStack))) {
						ItemCard.setforArea(itemStack, device.getDeviceName());
					}

				}
			}

			Tick = 0;
		}
		Tick++;
	}

	public void setInformation(ItemStack itemStack, PointXYZ png, String key,
			int value, TileEntity tileentity) {
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper
				.getTAGfromItemstack(itemStack);
		nbtTagCompound.setString("displayName",
				MFFSMaschines.fromTE(tileentity).displayName);
		super.setInformation(itemStack, png, key, value);

	}

	@Override
	public void addInformation(ItemStack itemStack, @Nullable World worldIn, List<String> info, ITooltipFlag flagIn) {
		NBTTagCompound tag = NBTTagCompoundHelper
				.getTAGfromItemstack(itemStack);

		info.add(I18n.format("itemInfo.deviceType") + getDeviceTyp(itemStack));
		info.add(I18n.format("itemInfo.deviceName") + getforAreaname(itemStack));

		if (tag.hasKey("worldname"))
			info.add("World: " + tag.getString("worldname"));
		if (tag.hasKey("linkTarget"))
			info.add("Coords: "
					+ new PointXYZ(tag.getCompoundTag("linkTarget")).toString());
		if (tag.hasKey("valid"))
			info.add((tag.getBoolean("valid")) ? "\u00a72Valid"
					: "\u00a74Invalid");
	}

	public static String getDeviceTyp(ItemStack itemstack) {
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper
				.getTAGfromItemstack(itemstack);
		if (nbtTagCompound != null) {
			return nbtTagCompound.getString("displayName");
		}
		return "-";
	}

}
