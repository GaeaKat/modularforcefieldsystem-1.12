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

import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class ItemMFFSBase extends Item {

	public String baseName;
	public ItemMFFSBase(final String itemName) {
		super();
		baseName=itemName;
		setItemName(this,itemName);
		if(isInCreativeTab())
			setCreativeTab(ModularForceFieldSystem.MFFSTab);
	}
	public static void setItemName(ItemMFFSBase item, String itemName) {
		item.setRegistryName(ModularForceFieldSystem.MODID,itemName);
		final ResourceLocation regName= Objects.requireNonNull(item.getRegistryName());
		item.setUnlocalizedName(regName.toString());
	}

	private boolean isInCreativeTab() {
		return true;
	}

}
