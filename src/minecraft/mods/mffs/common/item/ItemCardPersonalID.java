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
import mods.mffs.common.NBTTagCompoundHelper;
import mods.mffs.common.SecurityRight;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Keyboard;

import java.util.List;


public class ItemCardPersonalID extends Item {
	public ItemCardPersonalID(int i) {
		super(i);
		setMaxStackSize(1);
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("mffs:PersonalIDCard");
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
		NBTTagCompoundHelper.getTAGfromItemstack(itemStack).setCompoundTag(
				"rights", rightsTag);
	}

	public static void setOwner(ItemStack itemStack, String username) {
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper
				.getTAGfromItemstack(itemStack);
		nbtTagCompound.setString("name", username);
	}

	public String getUsername(ItemStack itemstack) {
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper
				.getTAGfromItemstack(itemstack);
		if (nbtTagCompound != null) {
			return nbtTagCompound.getString("name");
		}
		return "nobody";
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,
			List info, boolean b) {
		String tooltip = String.format("Owner: %s ", NBTTagCompoundHelper
				.getTAGfromItemstack(itemStack).getString("name"));
		info.add(tooltip);

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
				|| Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			info.add(LanguageRegistry.instance().getStringLocalization("itemInfo.rights"));
			for (SecurityRight sr : SecurityRight.rights.values()) {

				if (hasRight(itemStack, sr)) {
					info.add("-" + sr.name);
				}

			}
		} else {
			info.add(LanguageRegistry.instance().getStringLocalization("itemInfo.rightsHoldShift"));
		}
	}
}
