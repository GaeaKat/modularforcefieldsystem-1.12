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

package com.nekokittygames.mffs.common;


import crazypants.enderio.EnderIO;
import ic2.api.item.IC2Items;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.lang.reflect.Method;

public class MFFSRecipes {
	public static final int MONAZIT_MACERATION_OUTPUT = 8;

	public static void AddIC2Recipes() {
		Method addMaceratorRecipe = null;
		Method addMatterAmplifier = null;

		Recipes.macerator.addRecipe(new RecipeInputItemStack(new ItemStack(
				ModularForceFieldSystem.MFFSMonazitOre, 1)),new NBTTagCompound(),false,new ItemStack(ModularForceFieldSystem.MFFSitemForcicium, MONAZIT_MACERATION_OUTPUT));

		//		));
        NBTTagCompound tag=new NBTTagCompound();
        tag.setInteger("amplification",5000);
        Recipes.matterAmplifier.addRecipe(new RecipeInputItemStack(new ItemStack(ModularForceFieldSystem.MFFSitemForcicium)),tag,false);
		//Recipes.matterAmplifier.addRecipe(new ItemStack(
		//		ModularForceFieldSystem.MFFSitemForcicium, 1), 5000);

		if (ModularForceFieldSystem.enableUUMatterForcicium)
			Recipes.advRecipes.addRecipe(
					new ItemStack(
							ModularForceFieldSystem.MFFSitemForcicium,
							8),
					new Object[] { " RR", "R  ", " R ",
							Character.valueOf('R'),
							IC2Items.getItem("misc_resource","matter") });

		RecipesFactory.addRecipe("AAAAxAADA", 1, 1, null,
				ModularForceFieldSystem.MFFSitemForcicumCell);
		RecipesFactory.addRecipe(" E EBE E ", 4, 1, null,
				ModularForceFieldSystem.MFFSitemupgradeexctractorboost);
		RecipesFactory.addRecipe(" E ExE E ", 1, 1, null,
				ModularForceFieldSystem.MFFSitemupgradecapcap);
		RecipesFactory.addRecipe("HHHEIEEDE", 1, 1, null,
				ModularForceFieldSystem.MFFSitemupgradecaprange);
		RecipesFactory.addRecipe("AlAlilAlA", 64, 1, null,
				ModularForceFieldSystem.MFFSitemFocusmatix);
		RecipesFactory.addRecipe("ooooCoooo", 1, 1, null,
				ModularForceFieldSystem.MFFSitemcardempty);
		RecipesFactory.addRecipe("mSnExEEDE", 1, 1, null,
				ModularForceFieldSystem.MFFSitemWrench);
	}

	public static void AddEIRecipes()
	{
		RecipesFactory.addRecipe("AAAAxAADA", 1, 3, null,
				ModularForceFieldSystem.MFFSitemForcicumCell);
		RecipesFactory.addRecipe(" E EBE E ", 4, 3, null,
				ModularForceFieldSystem.MFFSitemupgradeexctractorboost);
		RecipesFactory.addRecipe(" E ExE E ", 1, 3, null,
				ModularForceFieldSystem.MFFSitemupgradecapcap);
		RecipesFactory.addRecipe("HHHEIEEDE", 1, 3, null,
				ModularForceFieldSystem.MFFSitemupgradecaprange);
		RecipesFactory.addRecipe("AlAlilAlA", 64, 3, null,
				ModularForceFieldSystem.MFFSitemFocusmatix);
		RecipesFactory.addRecipe("ooooCoooo", 1, 3, null,
				ModularForceFieldSystem.MFFSitemcardempty);
		RecipesFactory.addRecipe("mSnExEEDE", 1, 3, null,
				ModularForceFieldSystem.MFFSitemWrench);
	}
	public static void AddTERecipes() {
		/*CraftingManagers.pulverizerManager.addRecipe(400, new ItemStack(
				ModularForceFieldSystem.MFFSMonazitOre, 1), new ItemStack(
				ModularForceFieldSystem.MFFSitemForcicium, MONAZIT_MACERATION_OUTPUT), false);

		RecipesFactory.addRecipe("AAAAxAADA", 1, 2, null,
				ModularForceFieldSystem.MFFSitemForcicumCell);
		RecipesFactory.addRecipe(" E EBE E ", 4, 2, null,
				ModularForceFieldSystem.MFFSitemupgradeexctractorboost);
		RecipesFactory.addRecipe(" E ExE E ", 1, 2, null,
				ModularForceFieldSystem.MFFSitemupgradecapcap);
		RecipesFactory.addRecipe("HHHEaEEDE", 1, 2, null,
				ModularForceFieldSystem.MFFSitemupgradecaprange);
		RecipesFactory.addRecipe("AlAlilAlA", 64, 2, null,
				ModularForceFieldSystem.MFFSitemFocusmatix);
		RecipesFactory.addRecipe("mSnLxLLDL", 1, 2, null,
				ModularForceFieldSystem.MFFSitemWrench);
		RecipesFactory.addRecipe("oMomMmoMo", 1, 2, null,
				ModularForceFieldSystem.MFFSitemcardempty);

		if(ModularForceFieldSystem.buildcraftFound)
			GameRegistry.addRecipe(
					new ItemStack(ModularForceFieldSystem.MFFSitemWrench, 1),
					"mSn",
					"ExE",
					"EDE",
					'm', Item.redstone,
					'S', BuildCraftCore.wrenchItem,
					'n', Block.lever,
					'E', ItemRegistry.getItem("hardenedGlass", 1),
					'x',
					new ItemStack(
							ModularForceFieldSystem.MFFSitemForcePowerCrystal,
							1, -1),
					'D', ItemRegistry.getItem("powerCoilElectrum", 1)
			);*/
	}

	public static void AddAERecipes() {
		/*if(Util.getGrinderRecipeManage() == null)
		{
			System.out.println("[ModularForceFieldSystem] Error: AE Grind Stone Recipe Manager is null!");
			return;
		}

		Util.getGrinderRecipeManage().addRecipe(new ItemStack(ModularForceFieldSystem.MFFSMonazitOre, 1),
				new ItemStack(ModularForceFieldSystem.MFFSitemForcicium, ModularForceFieldSystem.grindRecipeOutput),
				ModularForceFieldSystem.grindRecipeCost);*/
	}

	public static void init() {

		RecipesFactory.addRecipe("uuuuiuuuu", 1, 0, null,
				ModularForceFieldSystem.MFFSitemForcePowerCrystal);
		RecipesFactory.addRecipe("vvvvvvvvv", 1, 0, null,
				ModularForceFieldSystem.MFFSProjectorFFStrength);
		RecipesFactory.addRecipe("vvv   vvv", 1, 0, null,
				ModularForceFieldSystem.MFFSProjectorFFDistance);

		CraftingManager
				.getInstance()
				.addShapelessRecipe(
						new ItemStack(ModularForceFieldSystem.MFFSitemcardempty),
						new Object[] { new ItemStack(
								ModularForceFieldSystem.MFFSitemfc) });
		CraftingManager.getInstance().addShapelessRecipe(
				new ItemStack(ModularForceFieldSystem.MFFSitemcardempty),
				new Object[] { new ItemStack(
						ModularForceFieldSystem.MFFSItemIDCard) });
		CraftingManager.getInstance().addShapelessRecipe(
				new ItemStack(ModularForceFieldSystem.MFFSitemcardempty),
				new Object[] { new ItemStack(
						ModularForceFieldSystem.MFFSItemSecLinkCard) });
		CraftingManager.getInstance().addShapelessRecipe(
				new ItemStack(ModularForceFieldSystem.MFFSitemcardempty),
				new Object[] { new ItemStack(
						ModularForceFieldSystem.MFFSAccessCard) });
		CraftingManager.getInstance().addShapelessRecipe(
				new ItemStack(ModularForceFieldSystem.MFFSitemcardempty),
				new Object[] { new ItemStack(
						ModularForceFieldSystem.MFFSitemDataLinkCard) });

		GameRegistry.addSmelting(
				ModularForceFieldSystem.MFFSMonazitOre, new ItemStack(
						ModularForceFieldSystem.MFFSitemForcicium, 4), 0.5F);

		if (ModularForceFieldSystem.ic2Found && ModularForceFieldSystem.enableIC2Recipes)
			AddIC2Recipes();

		if (ModularForceFieldSystem.thermalExpansionFound && ModularForceFieldSystem.enableTERecipes)
			AddTERecipes();

		if(ModularForceFieldSystem.appliedEnergisticsFound && ModularForceFieldSystem.enableAEGrindStoneRecipe)
			AddAERecipes();
		if(ModularForceFieldSystem.enderIoFound && ModularForceFieldSystem.enableEIRecipes)
			AddEIRecipes();
	}

}
