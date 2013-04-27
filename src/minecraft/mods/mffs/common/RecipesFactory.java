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

import cpw.mods.fml.common.registry.GameRegistry;
import ic2.api.item.Items;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class RecipesFactory {

	// forMod: 0: independent, 1: IC2, 2: BC3, 3: Thermal Expansion
	public static boolean addRecipe(String Recipe, int count, int forMod,
			Block block, Item item) {

		if ((forMod >= 2 || forMod < 0) || (count < 0)
				|| (block == null && item == null)
				|| (block != null && item != null) || (Recipe.length() != 9)) {
			System.out
					.println("[ModularForceFieldSystem] Recipes generating Fail for :"
							+ block + "/" + item);
			return false;
		}

		ItemStack itemstack = null;

		if (block != null && item == null)
			itemstack = new ItemStack(block, count);
		if (block == null && item != null)
			itemstack = new ItemStack(item, count);

		String[] recipeSplit = new String[] { Recipe.substring(0, 3),
				Recipe.substring(3, 6), Recipe.substring(6, 9) };

		switch (forMod) {
		case 0: // Independent

			GameRegistry.addRecipe(itemstack, recipeSplit,

			'a', Item.enderPearl, 'b',
					Item.pickaxeIron,
					'c',
					Item.bucketEmpty,
					'd',
					Item.bucketLava,
					'e',
					Item.bucketWater,
					'f',
					Item.bone, // Vanilla Stuff a++
					'g', Item.blazeRod, 'h', Item.rottenFlesh, 'i',
					Item.diamond, 'j', Item.spiderEye, 'k', Block.obsidian,
					'l', Block.glass, 'm', Item.redstone, 'n', Block.lever,
					'o', Item.paper,

					'u', ModularForceFieldSystem.MFFSitemForcicium, 'v',
					ModularForceFieldSystem.MFFSitemFocusmatix, 'w',
					ModularForceFieldSystem.MFFSProjectorTypkube,
					'x',
					new ItemStack(
							ModularForceFieldSystem.MFFSitemForcePowerCrystal,
							1, -1), // MFFs Stuff z--
					'y', ModularForceFieldSystem.MFFSitemFocusmatix, 'z',
					ModularForceFieldSystem.MFFSItemIDCard

			);
			return true;

		case 1: // IndustrialCraft 2
			if (ModularForceFieldSystem.ic2Found
					&& ModularForceFieldSystem.enableIC2Recipes) {

				GameRegistry
						.addRecipe(
								itemstack,
								recipeSplit,

								'a',
								Item.enderPearl,
								'b',
								Item.pickaxeIron,
								'c',
								Item.bucketEmpty,
								'd',
								Item.bucketLava,
								'e',
								Item.bucketWater,
								'f',
								Item.bone, // Vanilla Stuff a++
								'g',
								Item.blazeRod,
								'h',
								Item.rottenFlesh,
								'i',
								Item.diamond,
								'j',
								Item.spiderEye,
								'k',
								Block.obsidian,
								'l',
								Block.glass,
								'm',
								Item.redstone,
								'n',
								Block.lever,
								'o',
								Item.paper,

								'u',
								ModularForceFieldSystem.MFFSitemForcicium,
								'v',
								ModularForceFieldSystem.MFFSitemFocusmatix,
								'w',
								ModularForceFieldSystem.MFFSProjectorTypkube,
								'x',
								new ItemStack(
										ModularForceFieldSystem.MFFSitemForcePowerCrystal,
										1, -1), // MFFs Stuff z--
								'y',
								ModularForceFieldSystem.MFFSitemFocusmatix,
								'z', ModularForceFieldSystem.MFFSItemIDCard,

								'A', Items.getItem("refinedIronIngot"), 'B',
								Items.getItem("overclockerUpgrade"), 'C', Items
										.getItem("electronicCircuit"), 'D',
								Items.getItem("advancedCircuit"), 'E', Items
										.getItem("carbonPlate"),
								'F',
								Items.getItem("advancedMachine"),
								'G',
								Items.getItem("extractor"),
								'H',
								Items.getItem("copperCableItem"), // Ic2 Stuff
																	// A++
								'I', Items.getItem("insulatedCopperCableItem"),
								'J', Items.getItem("frequencyTransmitter"),
								'K', Items.getItem("advancedAlloy"), 'M', Items
										.getItem("glassFiberCableItem"), 'N',
								Items.getItem("lvTransformer"), 'O', Items
										.getItem("mvTransformer"), 'P', Items
										.getItem("hvTransformer"), 'Q', Items
										.getItem("teslaCoil"), 'R', Items
										.getItem("matter"), 'S', Items
										.getItem("wrench")

						);
				return true;
			}
			break;
		}

		return false;
	}

}
