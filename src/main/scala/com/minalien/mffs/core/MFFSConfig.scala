package com.minalien.mffs.core

import java.io.File

import net.minecraftforge.common.config.Configuration

/**
 * Created by Katrina on 14/01/2015.
 */
object MFFSConfig {

  final val CATEGORY_MACHINES = "machines"
  final val CATEGORY_WORLDGEN = "worldgen"

  var configFile:Configuration=null
  object General
  {
    var adventureMode=false
  }
  object Machines
  {
    var chunkLoad=true
    var ForciciumCellWorkCycle=230
    var ForciciumWorkCycle=250
    var ExtractorPassForceEnergyGenerate=12000
    //def getExtractorPassForeEnergyGenerate=(ExtractorPassForceEnergyGenerate / 4000) * 4000;
  }

  def init(file:File): Unit =
  {
    configFile=new Configuration(file)
    sync()
  }

  def sync() =
  {
    configFile.setCategoryComment(Configuration.CATEGORY_GENERAL, "ATTENTION: Editing this file manually is no longer necessary.\n" +
      "On the Mods list screen, select the entry for Modular Forcefield System, then click the Config button to modify these settings.")
    Machines.chunkLoad=configFile.getBoolean("Chunk Loading",CATEGORY_MACHINES,Machines.chunkLoad,"Set this to false to turn off the MFFS Chuncloader ability")
    Machines.ForciciumCellWorkCycle=configFile.getInt("Forcium Cell Work Cycle",CATEGORY_MACHINES,Machines.ForciciumCellWorkCycle,0,500,"WorkCycle amount of Forcecium Cell inside a Extractor")
    Machines.ForciciumWorkCycle=configFile.getInt("Forcium  Work Cycle",CATEGORY_MACHINES,Machines.ForciciumWorkCycle,0,500,"WorkCycle amount of Forcecium inside a Extractor")
    Machines.ExtractorPassForceEnergyGenerate=configFile.getInt("Extractor Pass ForceEnergy Generate",CATEGORY_MACHINES,Machines.ExtractorPassForceEnergyGenerate,0,Int.MaxValue,"How many ForceEnergy generate per WorkCycle")

    General.adventureMode=configFile.getBoolean("Adventure Mode",Configuration.CATEGORY_GENERAL,General.adventureMode,"Set MFFS to AdventureMap Mode Extractor need no Forcecium and ForceField have no click damage")

  }
}
