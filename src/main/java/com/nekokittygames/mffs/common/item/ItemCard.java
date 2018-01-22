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
import com.nekokittygames.mffs.common.NBTTagCompoundHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nullable;
import java.util.List;

public abstract class ItemCard extends Item {

	private StringBuffer info = new StringBuffer();
	protected int Tick;

	public ItemCard() {
		super();
		setMaxStackSize(1);
		Tick = 0;
	}

	@Override
	public boolean isRepairable() {
		return false;

	}

	public static void setforArea(ItemStack itemStack, String areaname) {
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper
				.getTAGfromItemstack(itemStack);
		nbtTagCompound.setString("Areaname", areaname);
	}

	public static String getforAreaname(ItemStack itemstack) {
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper
				.getTAGfromItemstack(itemstack);
		if (nbtTagCompound != null) {
			return nbtTagCompound.getString("Areaname");
		}
		return "not set";
	}

	public boolean isvalid(ItemStack itemStack) {
		NBTTagCompound tag = NBTTagCompoundHelper
				.getTAGfromItemstack(itemStack);
		if (tag.hasKey("valid"))
			return tag.getBoolean("valid");
		return false;
	}

	public void setinvalid(ItemStack itemStack) {
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper
				.getTAGfromItemstack(itemStack);
		nbtTagCompound.setBoolean("valid", false);
	}

	@Override
	public void addInformation(ItemStack itemStack, @Nullable World worldIn, List<String> info, ITooltipFlag flagIn) {
		NBTTagCompound tag = NBTTagCompoundHelper
				.getTAGfromItemstack(itemStack);
		info.add("Links To: " + getforAreaname(itemStack));
		if (tag.hasKey("worldname"))
			info.add("World: " + tag.getString("worldname"));
		if (tag.hasKey("linkTarget"))
			info.add("Coords: "
					+ new PointXYZ(tag.getCompoundTag("linkTarget")).toString());
		if (tag.hasKey("valid"))
			info.add((tag.getBoolean("valid")) ? "\u00a72Valid"
					: "\u00a74Invalid");

	}

	public void setInformation(ItemStack itemStack, PointXYZ png, String key,
			int value) {

		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper
				.getTAGfromItemstack(itemStack);

		nbtTagCompound.setInteger(key, value);
		nbtTagCompound.setString("worldname",
				DimensionManager.getWorld(png.dimensionId).getWorldInfo()
						.getWorldName());
		nbtTagCompound.setTag("linkTarget", png.asNBT());
		nbtTagCompound.setBoolean("valid", true);

	}

	public int getValuefromKey(String key, ItemStack itemStack) {
		NBTTagCompound tag = NBTTagCompoundHelper
				.getTAGfromItemstack(itemStack);
		if (tag.hasKey(key))
			return tag.getInteger(key);
		return 0;
	}

	public PointXYZ getCardTargetPoint(ItemStack itemStack) {
		NBTTagCompound tag = NBTTagCompoundHelper
				.getTAGfromItemstack(itemStack);
		if (tag.hasKey("linkTarget")) {
			return new PointXYZ(tag.getCompoundTag("linkTarget"));
		} else {
			tag.setBoolean("valid", false);
		}

		return null;
	}

}