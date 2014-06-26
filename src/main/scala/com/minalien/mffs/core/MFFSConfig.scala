package com.minalien.mffs.core

import java.io.File

import net.minecraftforge.common.config.Configuration

/**
 * Contains a list of various configuration options for MFFS.
 */
object MFFSConfig {
	final val CATEGORY_MACHINES = "Machines"

	/**
	 * Forge Configuration file.
	 */
	var configFile: Configuration = null

	/**
	 * Stores all configuration options related to Machines added by the mod.
	 */
	object machines {
		/**
		 * Maximum number of Forcefield blocks generated every tick.
		 */
		var maxFieldBlocksGeneratedPerTick = 200
	}

	/**
	 * Loads the MFFS Configuration files.
	 *
	 * @param file Configuration file recommended by FML.
	 */
	def initialize(file: File) {
		configFile = new Configuration(file)

		val maxFieldBlocks = configFile.get(CATEGORY_MACHINES, "Max Field Blocks Generated Per Tick", machines.maxFieldBlocksGeneratedPerTick)
		maxFieldBlocks.comment = "Maximum number of Forcefield blocks generated or destroyed every tick (per Projector)."
		machines.maxFieldBlocksGeneratedPerTick = maxFieldBlocks.getInt(machines.maxFieldBlocksGeneratedPerTick)

		if(configFile.hasChanged)
			configFile.save()
	}
}
