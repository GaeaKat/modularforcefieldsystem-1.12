package com.minalien.mffs.recipes

import cpw.mods.fml.common.registry.GameRegistry
import com.minalien.mffs.blocks.{BlockForciciumStorage, BlockMonazitOre}
import net.minecraft.item.ItemStack
import com.minalien.mffs.items.ItemForcicium

/**
 * Central controller for recipes related to MFFS.
 */
object MFFSRecipes {
	/**
	 * Registers all mod recipes to Minecraft.
	 */
	def registerRecipes() {
		////////// Vanilla Smelting Recipes
		GameRegistry.addSmelting(BlockMonazitOre, new ItemStack(ItemForcicium, 4), 1)

		////////// Vanilla Shaped Recipes

		////////// Vanilla Shapeless Recipes
		GameRegistry.addShapelessRecipe(
			new ItemStack(BlockForciciumStorage),
			ItemForcicium, ItemForcicium, ItemForcicium,
			ItemForcicium, ItemForcicium, ItemForcicium,
			ItemForcicium, ItemForcicium, ItemForcicium
		)

		GameRegistry.addShapelessRecipe(
			new ItemStack(ItemForcicium, 9),
			BlockForciciumStorage
		)
	}
}
