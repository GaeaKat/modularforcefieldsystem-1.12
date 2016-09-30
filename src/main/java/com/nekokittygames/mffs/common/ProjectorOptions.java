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

import net.minecraft.item.Item;

public enum ProjectorOptions {

	Zapper("<touch damage>", ModularForceFieldSystem.MFFSProjectorOptionZapper,
			" K KQK K ", " C CdC C "),
	Subwater("<Sponge>", ModularForceFieldSystem.MFFSProjectorOptionSubwater,
			" K KcK K ", " C CcC C "),
	Dome("<Field Manipulator>", ModularForceFieldSystem.MFFSProjectorOptionDome,
			" K KCK K ", " C CEC C "),
	Cutter("<Block Breaker>", ModularForceFieldSystem.MFFSProjectorOptionCutter,
			" K KbK K ", " C CbC C "),
	FieldJammer("<Force Field Jammer>", ModularForceFieldSystem.MFFSProjectorOptionForceFieldJammer,
			" J JvJ J ", " a ava a "),
	Camouflage("<Camouflage>", ModularForceFieldSystem.MFFSProjectorOptionCamouflage,
			" K KRK K ", " C CKC C "),
	FieldFusion("<Field Fusion>", ModularForceFieldSystem.MFFSProjectorOptionFieldFusion,
			" K KDK K ", " C CDC C "),
	MoobEx("<NPC Defense>", ModularForceFieldSystem.MFFSProjectorOptionMoobEx,
			"fgfhQhjgj", "fgfhdhjgj"),
	DefenceStation("<Defense Station>", ModularForceFieldSystem.MFFSProjectorOptionDefenceStation,
			" z CQC z ", " z EdE z ");

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
				RecipesFactory.addRecipe(mach.recipete, 1, 2, null, mach.item);
		}
	}

}
