package com.minalien.mffs.core

import java.io.File

import net.minecraftforge.common.config.Configuration

import scala.collection.mutable.ArrayBuffer
import scala.collection.JavaConversions._

/**
 * Contains a list of various configuration options for MFFS.
 */
object MFFSConfig {
	final val CATEGORY_MACHINES = Configuration.CATEGORY_GENERAL

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
		configFile.load()

		configFile.setCategoryComment(Configuration.CATEGORY_GENERAL, "ATTENTION: Editing this file manually is no longer necessary.\n" +
			"On the Mods list screen, select the entry for Modular Forcefield System, then click the Config button to modify these settings.")

		val orderedKeys = new ArrayBuffer[String]

		Machines.maxFieldBlocksGeneratedPerTick = configFile.getInt("Max Field Blocks Generated Per Tick", CATEGORY_MACHINES, Machines.maxFieldBlocksGeneratedPerTick, 0, Int.MaxValue, "Maximum number of Forcefield blocks generated or destroyed every tick, per-Projector.")
		orderedKeys += "Max Field Blocks Generated Per Tick"

		configFile.setCategoryPropertyOrder(CATEGORY_MACHINES, orderedKeys.toList)

		if(configFile.hasChanged)
			configFile.save()
	}
}
