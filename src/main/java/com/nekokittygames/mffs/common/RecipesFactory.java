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

import com.nekokittygames.mffs.common.compat.EnderIOCompat;
import ic2.api.item.IC2Items;
import ic2.api.item.IItemAPI;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.Map;

public final class RecipesFactory {

	public static Map<Object,IRecipe> Ic2Recipes=new HashMap<Object, IRecipe>();
	public static Map<Object,IRecipe> IndRecipes=new HashMap<Object, IRecipe>();
	public static Map<Object,IRecipe> EIORecipes=new HashMap<Object, IRecipe>();



	public static IRecipe GetRecipe(Object result,int forMod)
	{
		switch (forMod)
		{
			case 0:
				return IndRecipes.get(result);
			case 1:
				return Ic2Recipes.get(result);
			case 3:
				return EIORecipes.get(result);

		}
		return null;
	}
	// forMod: 0: independent, 1: IC2, 2: Thermal Expansion, 3: Ender IO
	public static boolean addRecipe(String Recipe, int count, int forMod,
			Block block, Item item) {


		/*if ((forMod >= 4 || forMod < 0) || (count < 0)
				|| (block == null && item == null)
				|| (block != null && item != null) || (Recipe.length() != 9)) {
			System.out
					.println("[ModularForceFieldSystem] Recipes generating Fail for :"
							+ block + "/" + item);
			return false;
		}

		ItemStack itemstack = null;

		Object result=null;
		if (block != null && item == null) {
			itemstack = new ItemStack(block, count);
			result=block;
		}
		if (block == null && item != null) {
			result=item;
			itemstack = new ItemStack(item, count);
		}

		String[] recipeSplit = new String[] { Recipe.substring(0, 3),
				Recipe.substring(3, 6), Recipe.substring(6, 9) };

		switch (forMod) {
		case 0: // Independent


			IndRecipes.put(result,GameRegistry.addShapedRecipe(itemstack, recipeSplit,

			'a', Items.ENDER_PEARL, 'b',
					Items.IRON_PICKAXE,
					'c',
					Items.BUCKET,
					'd',
					Items.LAVA_BUCKET,
					'e',
					Items.WATER_BUCKET,
					'f',
					Items.BONE, // Vanilla Stuff a++
					'g', Items.BLAZE_ROD, 'h', Items.ROTTEN_FLESH, 'i',
					Items.DIAMOND, 'j', Items.SPIDER_EYE, 'k', Blocks.OBSIDIAN,
					'l', Blocks.GLASS, 'm', Items.REDSTONE, 'n', Blocks.LEVER,
					'o', Items.PAPER,
					'p',Blocks.GLOWSTONE,
					'u', ModularForceFieldSystem.MFFSitemForcicium, 'v',
					ModularForceFieldSystem.MFFSitemFocusmatix, 'w',
					ModularForceFieldSystem.MFFSProjectorTypCube,
					'x',
					new ItemStack(
							ModularForceFieldSystem.MFFSitemForcePowerCrystal,
							1, -1), // MFFs Stuff z--
					'y', ModularForceFieldSystem.MFFSitemFocusmatix, 'z',
					ModularForceFieldSystem.MFFSItemIDCard

			));
			return true;

		case 1: // IndustrialCraft 2
			if (ModularForceFieldSystem.ic2Found
					&& ModularForceFieldSystem.enableIC2Recipes) {

				Ic2Recipes.put(result,GameRegistry
						.addShapedRecipe(
								itemstack,
								recipeSplit,

								'a',
								Items.ENDER_PEARL,
								'b',
								Items.IRON_PICKAXE,
								'c',
								Items.BUCKET,
								'd',
								Items.LAVA_BUCKET,
								'e',
								Items.WATER_BUCKET,
								'f',
								Items.BONE, // Vanilla Stuff a++
								'g',
								Items.BLAZE_ROD,
								'h',
								Items.ROTTEN_FLESH,
								'i',
								Items.DIAMOND,
								'j',
								Items.SPIDER_EYE,
								'k',
								Blocks.OBSIDIAN,
								'l',
								Blocks.GLASS,
								'm',
								Items.REDSTONE,
								'n',
								Blocks.LEVER,
								'o',
								Items.PAPER,
								'p',Blocks.GLOWSTONE,
								'u',
								ModularForceFieldSystem.MFFSitemForcicium,
								'v',
								ModularForceFieldSystem.MFFSitemFocusmatix,
								'w',
								ModularForceFieldSystem.MFFSProjectorTypCube,
								'x',
								new ItemStack(
										ModularForceFieldSystem.MFFSitemForcePowerCrystal,
										1, -1), // MFFs Stuff z--
								'y',
								ModularForceFieldSystem.MFFSitemFocusmatix,
								'z', ModularForceFieldSystem.MFFSItemIDCard,

								'A', IC2Items.getItem("plate","iron"),
								'B', IC2Items.getItem("upgrade","overclocker"),
								'C', IC2Items.getItem("crafting","circuit"),
								'D', IC2Items.getItem("crafting","advanced_circuit"),
								'E', IC2Items.getItem("crafting","carbon_plate"),
								'F', IC2Items.getItem("resource","advanced_machine"),
								'G', IC2Items.getItem("te","extractor"),
								'H', IC2Items.getItem("cable","type:copper,insulation:0"),
								'I', IC2Items.getItem("cable","type:copper,insulation:1"),
								'J', IC2Items.getItem("frequency_transmitter"),
								'K', IC2Items.getItem("crafting","alloy"),
								'M', IC2Items.getItem("cable","type:glass,insulation:0"),
								'N', IC2Items.getItem("te","lv_transformer"),
								'O', IC2Items.getItem("te","mv_transformer"),
								'P', IC2Items.getItem("te","hv_transformer"),
								'Q', IC2Items.getItem("te","tesla_coil"),
								'R', IC2Items.getItem("misc_resource","matter"),
								'S', IC2Items.getItem("wrench")

						));
				return true;
			}
			break;

			*//*case 2: // Thermal Expansion
				if (ModularForceFieldSystem.thermalExpansionFound
						&& ModularForceFieldSystem.enableTERecipes) {
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
									ModularForceFieldSystem.MFFSProjectorTypCube,
									'x',
									new ItemStack(
											ModularForceFieldSystem.MFFSitemForcePowerCrystal,
											1, -1),
									'y',
									ModularForceFieldSystem.MFFSitemFocusmatix,
									'z', ModularForceFieldSystem.MFFSItemIDCard,

									'A', Item.ingotIron,
									'B', ItemRegistry.getItem("gearTin", 1),
									'C', ItemRegistry.getItem("powerCoilSilver", 1),
									'D', ItemRegistry.getItem("powerCoilElectrum", 1),
									'E', ItemRegistry.getItem("ingotElectrum", 1),
									'F', ItemRegistry.getItem("machineFrame", 1),
									'G', ItemRegistry.getItem("energyCellFrameEmpty", 1),
									'H', ItemRegistry.getItem("ingotCopper", 1),
									'I', ItemRegistry.getItem("powerCoilGold", 1),
									'J', Item.enderPearl,
									'K', ItemRegistry.getItem("bucketEnder", 1),
									'L', ItemRegistry.getItem("hardenedGlass", 1),
									'M', ItemRegistry.getItem("gearCopper", 1),
									'S', ItemRegistry.getItem("wrench", 1)

							);
					return true;
				}
				*//*
            case 3:
                if (ModularForceFieldSystem.enderIoFound
                        && ModularForceFieldSystem.enableEIRecipes) {

                    EIORecipes.put(result,GameRegistry
                            .addShapedRecipe(
                                    itemstack,
                                    EnderIOCompat.getEnderIORecipes(recipeSplit)

                            ));
                    return true;
                }
                break;
		}

		//return false;*/
		return true;
	}

}
