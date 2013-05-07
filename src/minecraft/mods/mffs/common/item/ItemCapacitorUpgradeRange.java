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
import mods.mffs.common.MFFSMaschines;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

import java.util.List;


public class ItemCapacitorUpgradeRange extends ItemMFFSBase {
	public ItemCapacitorUpgradeRange(int i) {
		super(i);
		setMaxStackSize(9);
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("mffs:CapacitorUpgradeRange");
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,
			List info, boolean b) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
				|| Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			info.add(LanguageRegistry.instance().getStringLocalization("itemInfo.compatibleWith"));
			info.add("MFFS " + MFFSMaschines.Capacitor.displayName);

		} else {
			info.add(LanguageRegistry.instance().getStringLocalization("itemInfo.compatibleWithHoldShift"));
		}
	}
}