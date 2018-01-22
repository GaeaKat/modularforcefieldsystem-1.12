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
    
    Thunderdark
    Matchlighter

 */

package com.nekokittygames.mffs.common;

import com.nekokittygames.mffs.common.item.ModItems;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;

public enum ProjectorOptions {

	Zapper("<touch damage>", ModItems.OPTION_TOUCH_DAMAGE,
			" K KQK K ", " C CdC C "),
	Subwater("<Sponge>", ModItems.OPTION_SPONGE,
			" K KcK K ", " C CcC C "),
	Dome("<Field Manipulator>", ModItems.OPTION_FIELD_MANIPULATOR,
			" K KCK K ", " C CEC C "),
	Cutter("<Block Breaker>", ModItems.OPTION_BLOCK_BREAKER,
			" K KbK K ", " C CbC C "),
	FieldJammer("<Force Field Jammer>", ModItems.OPTION_FIELD_JAMMER,
			" J JvJ J ", " a ava a "),
	Camouflage("<Camouflage>", ModItems.OPTION_CAMOFLAGE,
			" K KRK K ", " C CKC C "),
	FieldFusion("<Field Fusion>", ModItems.OPTION_FIELD_FUSION,
			" K KDK K ", " C CDC C "),
	MoobEx("<NPC Defense>", ModItems.OPTION_MOB_DEFENCE,
			"fgfhQhjgj", "fgfhdhjgj"),
	DefenceStation("<Defense Station>", ModItems.OPTION_DEFENSE_STATION,
			" z CQC z ", " z EdE z "),
	Light("<Light>",ModItems.OPTION_LIGHT," K KpK K "," K KpK K ");

	String displayName;
	Item item;
	String recipeic;
	String recipete;

	private ProjectorOptions(String dispNm, Item item, String recipeic,
			String recipete) {

		this.displayName = dispNm;
		this.item = item;
		this.recipeic = recipeic;
		this.recipete = recipete;

	}

	public static void initialize() {

		for (ProjectorOptions mach : ProjectorOptions.values()) {
			if(ModularForceFieldSystem.enableEIRecipes)
				RecipesFactory.addRecipe(mach.recipete, 1, 3, null, mach.item);
			if(ModularForceFieldSystem.enableIC2Recipes)
				RecipesFactory.addRecipe(mach.recipeic, 1, 1, null, mach.item);
		}
	}

}
