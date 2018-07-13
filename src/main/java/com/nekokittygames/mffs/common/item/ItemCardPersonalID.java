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
import com.nekokittygames.mffs.common.NBTTagCompoundHelper;
import com.nekokittygames.mffs.common.SecurityRight;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;


public class ItemCardPersonalID extends ItemMFFSBase {
	public ItemCardPersonalID() {
		super(LibItemNames.PERSONAL_ID);
		setMaxStackSize(1);

	}

	public ItemCardPersonalID(final String itemName) {
		super(itemName);
		setMaxStackSize(1);
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public boolean isDamageable() {
		return true;
	}

	public static boolean hasRight(ItemStack itemStack, SecurityRight sr) {
		NBTTagCompound itemTag = NBTTagCompoundHelper
				.getTAGfromItemstack(itemStack);
		NBTTagCompound rightsTag = itemTag.getCompoundTag("rights");

		if (itemTag.hasKey(sr.rightKey)) { // Update and delete old keys
			setRight(itemStack, sr, itemTag.getBoolean(sr.rightKey));
			itemTag.removeTag(sr.rightKey);
		}
		return rightsTag.getBoolean(sr.rightKey);
	}

	public static void setRight(ItemStack itemStack, SecurityRight sr,
			boolean value) {
		NBTTagCompound rightsTag = NBTTagCompoundHelper.getTAGfromItemstack(
				itemStack).getCompoundTag("rights");
		rightsTag.setBoolean(sr.rightKey, value);
		NBTTagCompoundHelper.getTAGfromItemstack(itemStack).setTag(
				"rights", rightsTag);
	}

	public static void setOwner(ItemStack itemStack, String uuid,String username) {
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper
				.getTAGfromItemstack(itemStack);
		nbtTagCompound.setString("name", uuid);
		nbtTagCompound.setString("username", username);
	}

	public String getUUID(ItemStack itemstack) {
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper
				.getTAGfromItemstack(itemstack);
		if (nbtTagCompound != null) {
			return nbtTagCompound.getString("name");
		}
		return "nobody";
	}
	public String getUsername(ItemStack itemstack) {
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper
				.getTAGfromItemstack(itemstack);
		if (nbtTagCompound != null) {
			return nbtTagCompound.getString("username");
		}
		return "nobody";
	}
	@Override
	public void addInformation(ItemStack itemStack, @Nullable World worldIn, List<String> info, ITooltipFlag flagIn) {
		String tooltip = String.format("Owner: %s ", NBTTagCompoundHelper
				.getTAGfromItemstack(itemStack).getString("username"));
		info.add(tooltip);

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
				|| Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			info.add(I18n.format("itemInfo.rights"));
			for (SecurityRight sr : SecurityRight.rights.values()) {

				if (hasRight(itemStack, sr)) {
					info.add("-" + sr.name);
				}

			}
		} else {
			info.add(I18n.format("itemInfo.rightsHoldShift"));
		}
	}
}
