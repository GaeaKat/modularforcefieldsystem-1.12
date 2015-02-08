package com.minalien.mffs.core

import java.util


import com.google.common.collect.Lists
import com.minalien.mffs.blocks.BlockForcefield
import com.minalien.mffs.blocks.machines.{BlockCapacitor, BlockExtractor, MachineBlock}
import com.minalien.mffs.items.cards.{ItemPowerLinkCard, ItemCardEmpty}
import com.minalien.mffs.items.{ItemForciumCell, ItemForcium}
import com.minalien.mffs.tiles.MFFSMachine
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.util.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.ForgeChunkManager
import net.minecraftforge.common.ForgeChunkManager.Ticket
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.common.{Mod, SidedProxy}
import org.apache.logging.log4j.Logger

/**
 * Created by Katrina on 13/12/2014.
 */
@Mod(modid = ModularForcefieldSystem.MOD_ID, name = "Modular Forcefield System", modLanguage = "scala",guiFactory = "com.minalien.mffs.client.gui.MFFSGuiFactory")
object ModularForcefieldSystem {
  /**
   * MFFS Mod ID.
   */
  final val MOD_ID = "modularforcefieldsystem"
  final var logger:Logger=null
  var netHandler: SimpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID+"|B")
  @SidedProxy(clientSide="com.minalien.mffs.client.ClientProxy", serverSide="com.minalien.mffs.core.CommonProxy")
  var proxy: CommonProxy = null


  def registerItems():Unit =
  {
    def registerItem(item:Item,name:String): Unit =
    {
      GameRegistry.registerItem(item,name)
    }
    registerItem(ItemForcium,"forcium")
    registerItem(ItemForciumCell,"forciciumCell")
    registerItem(ItemCardEmpty,"emptyCard")
    registerItem(ItemPowerLinkCard,"powerLinkCard")
  }

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
    registerBlock(BlockCapacitor,"capacitor")
    registerBlock(BlockForcefield,"forcefield")
  }

  /**
   * Loads mod configuration file and registers blocks and items.
   *
   * @param eventArgs Event arguments passed from Forge Mod Loader.
   */
  @EventHandler
  def preInit(eventArgs: FMLPreInitializationEvent) {

    registerBlocks()
    registerItems()
    logger=eventArgs.getModLog

  }
  @EventHandler
  def Init(eventArgs: FMLInitializationEvent) {
    proxy.registerInventoryBlock(BlockExtractor,"extractor")
    proxy.registerInventoryBlock(BlockForcefield,"forcefield")
    proxy.registerInventoryBlock(BlockCapacitor,"capacitor")
    proxy.registerItem(ItemForcium,"forcium")
    proxy.registerItem(ItemForciumCell,"forciciumCell")
    proxy.registerItem(ItemCardEmpty,"emptyCard")
    proxy.registerItem(ItemPowerLinkCard,"powerLinkCard")
    NetworkRegistry.INSTANCE.registerGuiHandler(this,GuiHandler)
    proxy.registerPackets()
  }

  @EventHandler
  def PostInit(eventArgs:FMLPostInitializationEvent): Unit =
  {
    object MFFSChunkLoadCallback extends ForgeChunkManager.OrderedLoadingCallback
    {
      override def ticketsLoaded(tickets: util.List[Ticket], world: World, maxTicketCount: Int): util.List[Ticket] =
      {
        val validTickets:java.util.List[Ticket]=Lists.newArrayList[Ticket]()
        for(i <- 0 until tickets.size)
        {
          val ticket:Ticket=tickets.get(i)
          val MachineX:Int=ticket.getModData.getInteger("MachineX")
          val MachineY:Int=ticket.getModData.getInteger("MachineY")
          val MachineZ:Int=ticket.getModData.getInteger("MachineZ")
          if(world.getTileEntity(new BlockPos(MachineX,MachineY,MachineZ))!=null) {
            world.getTileEntity(new BlockPos(MachineX, MachineY, MachineZ)) match {
              case s: MFFSMachine => validTickets.add(ticket)
            }
          }
        }
        validTickets
      }

      override def ticketsLoaded(tickets: util.List[Ticket], world: World): Unit =
      {
        for(i <- 0 until tickets.size)
        {
          val ticket:Ticket=tickets.get(i)
          val MachineX:Int=ticket.getModData.getInteger("MachineX")
          val MachineY:Int=ticket.getModData.getInteger("MachineY")
          val MachineZ:Int=ticket.getModData.getInteger("MachineZ")
          val machine:MFFSMachine=world.getTileEntity(new BlockPos(MachineX,MachineY,MachineZ)).asInstanceOf[MFFSMachine]
          machine.forceChunkLoading(ticket)
        }
      }
    }
    ForgeChunkManager.setForcedChunkLoadingCallback(this,MFFSChunkLoadCallback)
  }
}
