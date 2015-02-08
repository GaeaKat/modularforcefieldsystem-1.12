package com.minalien.mffs.core

import com.minalien.mffs.network.packet.name.{PacketDeleteLetter, PacketAddLetter, PacketClearName}
import com.minalien.mffs.network.packet.{PacketCapacitor, PacketType}
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.fml.relauncher.Side

/**
 * Created by Katrina on 22/12/2014.
 */
class CommonProxy {

  def registerInventoryBlock(block: Block,name: String): Unit=
  {

  }

  def registerItem(item:Item,name: String): Unit=
  {

  }


  def registerPackets(): Unit =
  {
    ModularForcefieldSystem.netHandler.registerMessage(classOf[PacketType],classOf[PacketType],200+1,Side.SERVER)
    ModularForcefieldSystem.netHandler.registerMessage(classOf[PacketCapacitor],classOf[PacketCapacitor],200+2,Side.SERVER)
    ModularForcefieldSystem.netHandler.registerMessage(classOf[PacketClearName],classOf[PacketClearName],200+3,Side.SERVER)
    ModularForcefieldSystem.netHandler.registerMessage(classOf[PacketAddLetter],classOf[PacketAddLetter],200+4,Side.SERVER)
    ModularForcefieldSystem.netHandler.registerMessage(classOf[PacketDeleteLetter],classOf[PacketDeleteLetter],200+5,Side.SERVER)
  }
}
