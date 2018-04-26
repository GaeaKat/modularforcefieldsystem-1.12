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
import com.nekokittygames.mffs.common.item.ModItems;

import gnu.trove.map.TCharObjectMap;
import gnu.trove.map.hash.TCharObjectHashMap;
import ic2.api.item.IC2Items;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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


		if ((forMod >= 4 || forMod < 0) || (count < 0)
				|| (block == null && item == null)
				|| (block != null && item != null) || (Recipe.length() != 9)) {
			System.out
					.println("[ModularForceFieldSystem] Recipes generating Fail for :"
							+ block + "/" + item);
			Thread.dumpStack(); //Try find what's given us a bad recipe request
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

		//Replace all the colons in the path because Forge splits on the last instead of the first to get the domain
		//(Having already turned it into a domain:path String via ResourceLocation#toString)
		//Which will then make it get confused and shout in the logs
		ResourceLocation key = new ResourceLocation(ModularForceFieldSystem.MODID,
						(Arrays.toString(recipeSplit) + " => "+itemstack).replace(':', '.'));
		switch (forMod) {
		case 0: // Independent
			GameRegistry.addShapedRecipe(key, null, itemstack, extractParams(recipeSplit,
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
					'u', ModItems.FORCICIUM, 'v',
					ModItems.PROJECTOR_FOCUS_MATRIX, 'w',
					ModItems.MODULE_CUBE,
					'x',
					new ItemStack(
							ModItems.FORCEPOWER_CRYSTAL,
							1, OreDictionary.WILDCARD_VALUE), // MFFs Stuff z--
					'y', ModItems.PROJECTOR_FOCUS_MATRIX, 'z',
					ModItems.PERSONAL_ID

			));
			IndRecipes.put(result, ForgeRegistries.RECIPES.getValue(key));
			return true;

		case 1: // IndustrialCraft 2
			if (ModularForceFieldSystem.ic2Found
					&& ModularForceFieldSystem.enableIC2Recipes) {
				GameRegistry.addShapedRecipe(
								key,
								null,
								itemstack,
								extractParams(
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
								ModItems.FORCICIUM,
								'v',
								ModItems.PROJECTOR_FOCUS_MATRIX,
								'w',
								ModItems.MODULE_CUBE,
								'x',
								new ItemStack(
										ModItems.FORCEPOWER_CRYSTAL,
										1, OreDictionary.WILDCARD_VALUE), // MFFs Stuff z--
								'y',
								ModItems.PROJECTOR_FOCUS_MATRIX,
								'z', ModItems.PERSONAL_ID,

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
					Ic2Recipes.put(result, ForgeRegistries.RECIPES.getValue(key));
				return true;
			}
			break;

			/*case 2: // Thermal Expansion
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
            */
		}

		return false;
	}

	public static void addShapelessRecipe(Item output, Object... objects) {
		GameRegistry.addShapelessRecipe(new ResourceLocation(ModularForceFieldSystem.MODID,
				(Arrays.toString(objects)+" => "+output.getUnlocalizedName()).replace(':', '.')),
				null, new ItemStack(output),
				//Turn all the objects into ingredients using Forge's helper method
				Arrays.stream(objects).map(CraftingHelper::getIngredient).toArray(Ingredient[]::new));
	}

	/**
	 * Returns only the options that are used so {@link GameRegistry} doesn't crash
	 * 
	 * @param split The recipe to extract characters from
	 * @param options The various pairs of character - object options
	 * 
	 * @return An array of the split and the used pairs
	 */
	private static Object[] extractParams(String[] split, Object... options) {
		TCharObjectMap<Tuple<Character, Object>> choices = new TCharObjectHashMap<>();
		
		char last = 0;
		for (Object option : options) {
			if (option instanceof Character) {
				if (last != 0) {//We expect Character - Object pairs, a second Character is therefore bad
					throw new IllegalArgumentException("Duplicate character definitions! "+option+" and "+last);
				}

				last = (Character) option;
			} else {
				choices.put(last, new Tuple<>(last, option));
				last = 0;
			}
		}
		
		List<Object> used = new ArrayList<>(12);
		for (String part : split) used.add(part);
		
		for (String part : split) {
			for (char c : part.toCharArray()) {
				if (c == ' ') continue; //Skip over checking empty spaces
				
				Tuple<Character, Object> pair = choices.get(c); //There shouldn't be any misses
				if (pair == null) throw new IllegalStateException("Unable to find "+c+" in "+choices);
				
				used.add(pair.getFirst()); //Add the character
				used.add(pair.getSecond()); //Then the associated input
			}
		}
		
		return used.toArray();
	}
}
