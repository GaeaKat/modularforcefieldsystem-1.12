package com.minalien.mffs.core

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPreInitializationEvent}
import net.minecraft.item.Item
import cpw.mods.fml.common.registry.GameRegistry
import com.minalien.mffs.items.ItemForcicium
import net.minecraft.block.Block
import com.minalien.mffs.blocks.{BlockForciciumStorage, BlockMonazitOre}
import com.minalien.mffs.world.MonazitOreWorldGenerator
import com.minalien.mffs.recipes.MFFSRecipes

/**
 * Modular Forcefield System 3.0 - Minecraft Mod
 *
 * @author Cassy 'Minalien' Murray
 */
@Mod(modid = ModularForcefieldSystem.MOD_ID, name = "Modular Forcefield System", modLanguage = "scala")
object ModularForcefieldSystem {
	/**
	 * MFFS Mod ID.
	 */
	final val MOD_ID = "ModularForcefieldSystem"

	/**
	 * Loads mod configuration file and registers blocks and items.
	 *
	 * @param eventArgs Event arguments passed from Forge Mod Loader.
	 */
	@EventHandler
	def preInit(eventArgs: FMLPreInitializationEvent) {
		registerItems()
		registerBlocks()
	}

	/**
	 * Registers any relevant World Generators & Mod recipes.
	 *
	 * @param eventArgs Event arguments passed from Forge Mod Loader.
	 */
	@EventHandler
	def init(eventArgs: FMLInitializationEvent) {
		GameRegistry.registerWorldGenerator(MonazitOreWorldGenerator, 0)

		MFFSRecipes.registerRecipes()
	}

	/**
	 * Registers all items added by the mod.
	 */
	def registerItems() {
		def registerItem(item: Item) {
			GameRegistry.registerItem(item, item.getUnlocalizedName, MOD_ID)
		}

		// Basic Items
		registerItem(ItemForcicium)
	}

	/**
	 * Registers all blocks added by the mod.
	 */
	def registerBlocks() {
		def registerBlock(block: Block) {
			GameRegistry.registerBlock(block, block.getUnlocalizedName)
		}

		// Basic Blocks
		registerBlock(BlockMonazitOre)
		registerBlock(BlockForciciumStorage)
	}
}
