package com.minalien.mffs.core

import com.minalien.mffs.blocks.machines.{BlockExtractor, MachineBlock}
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraftforge.fml.common.{SidedProxy, Mod}
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPreInitializationEvent}
import net.minecraftforge.fml.common.registry.GameRegistry

/**
 * Created by Katrina on 13/12/2014.
 */
@Mod(modid = ModularForcefieldSystem.MOD_ID, name = "Modular Forcefield System", modLanguage = "scala")
object ModularForcefieldSystem {
  /**
   * MFFS Mod ID.
   */
  final val MOD_ID = "modularforcefieldsystem"


  @SidedProxy(clientSide="com.minalien.mffs.client.ClientProxy", serverSide="com.minalien.mffs.core.CommonProxy")
  var proxy: CommonProxy = null
  def registerBlocks(): Unit =
  {
    def registerBlock(block: Block,name: String) {
      GameRegistry.registerBlock(block, name)

      block match {
        case machineBlock: MachineBlock =>
          GameRegistry.registerTileEntity(machineBlock.getTileEntityClass, name)

        case _ =>
      }
    }
    registerBlock(BlockExtractor,"extractor")
  }

  /**
   * Loads mod configuration file and registers blocks and items.
   *
   * @param eventArgs Event arguments passed from Forge Mod Loader.
   */
  @EventHandler
  def preInit(eventArgs: FMLPreInitializationEvent) {
    System.out.println("Scala rules")
    registerBlocks()

  }
  @EventHandler
  def Init(eventArgs: FMLInitializationEvent) {
    proxy.registerInventoryBlock(BlockExtractor,"extractor")


  }
}
