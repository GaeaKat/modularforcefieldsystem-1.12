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

package mods.mffs.common;

import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.LanguageRegistry;

public enum ProjectorOptions {

	Zapper("<touch damage>", ModularForceFieldSystem.MFFSProjectorOptionZapper,
			" K KQK K ", " C CdC C "), Subwater("<Sponge>",
			ModularForceFieldSystem.MFFSProjectorOptionSubwater, " K KcK K ",
			" C CcC C "), Dome("<Field Manipulator>",
			ModularForceFieldSystem.MFFSProjectorOptionDome, " K KCK K ",
			" C CEC C "), Cutter("<Block Breaker>",
			ModularForceFieldSystem.MFFSProjectorOptionCutter, " K KbK K ",
			" C CbC C "), FieldJammer("<Force Field Jammer>",
			ModularForceFieldSystem.MFFSProjectorOptionForceFieldJammer,
			" J JvJ J ", " a ava a "), Camouflage("<Camouflage>",
			ModularForceFieldSystem.MFFSProjectorOptionCamouflage, " K KRK K ",
			" C CGC C "), FieldFusion("<Field Fusion>",
			ModularForceFieldSystem.MFFSProjectorOptionFieldFusion,
			" K KDK K ", " C CFC C "), MoobEx("<NPC Defense>",
			ModularForceFieldSystem.MFFSProjectorOptionMoobEx, "fgfhQhjgj",
			"fgfhdhjgj"), DefenceStation("<Defense Station>",
			ModularForceFieldSystem.MFFSProjectorOptionDefenceStation,
			" z CQC z ", " z EdE z ");

	String displayName;
	Item item;
	String recipeic;
	String recipeue;

	private ProjectorOptions(String dispNm, Item item, String recipeic,
			String recipeue) {

		this.displayName = dispNm;
		this.item = item;
		this.recipeic = recipeic;
		this.recipeue = recipeue;

	}

	public static void initialize() {

		for (ProjectorOptions mach : ProjectorOptions.values()) {

			LanguageRegistry.instance().addNameForObject(mach.item, "en_US",
					" MFFS Projector Upgrade " + mach.displayName);

			if (ModularForceFieldSystem.ic2Found)
				RecipesFactory.addRecipe(mach.recipeic, 1, 1, null, mach.item);

		}
	}

}
