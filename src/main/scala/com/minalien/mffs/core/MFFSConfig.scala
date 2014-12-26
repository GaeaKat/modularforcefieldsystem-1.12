package com.minalien.mffs.core

import java.io.File

import net.minecraftforge.common.config.Configuration

/**
 * Contains a list of various configuration options for MFFS.
 */
object MFFSConfig {
	final val CATEGORY_MACHINES = "machines"

	/**
	 * Forge Configuration file.
	 */
	var configFile: Configuration = null

	/**
	 * Stores all configuration options related to Machines added by the mod.
	 */
	object Machines {
		/**
		 * Maximum number of Forcefield blocks generated every tick.
		 */
		var maxFieldBlocksGeneratedPerTick = 200
		/**
		 * Base cost in RF per block generated
		 */
		var baseCostFieldGeneration=200

		/**
		 * base cost per tick to maintain Force Field Blocks
		 */
		var baseCostFieldMaintain=20
	}

	/**
	 * Loads the MFFS Configuration files.
	 *
	 * @param file Configuration file recommended by FML.
	 */
	def initialize(file: File) {
		configFile = new Configuration(file)

		sync()
	}

	/**
	 * Synchronizes the mod's properties with the Config file data.
	 */
	def sync() {
		configFile.setCategoryComment(Configuration.CATEGORY_GENERAL, "ATTENTION: Editing this file manually is no longer necessary.\n" +
			"On the Mods list screen, select the entry for Modular Forcefield System, then click the Config button to modify these settings.")

		Machines.maxFieldBlocksGeneratedPerTick = configFile.getInt("Max Field Blocks Generated Per Tick", CATEGORY_MACHINES, Machines.maxFieldBlocksGeneratedPerTick, 0, Int.MaxValue, "Maximum number of Forcefield blocks generated or destroyed every tick, per-Projector.")
		Machines.baseCostFieldGeneration=configFile.getInt("Base Cost Per Field Block Generation",CATEGORY_MACHINES,Machines.baseCostFieldGeneration,0,Int.MaxValue,"Base cost in RF to generate a Forcefield block")
		Machines.baseCostFieldMaintain=configFile.getInt("Base Maintainence Cost Per Field Block",CATEGORY_MACHINES,Machines.baseCostFieldMaintain,0,Int.MaxValue,"Base cost in RF per tick to maintain a Forcefield block")
		if(configFile.hasChanged)
			configFile.save()
	}
}
