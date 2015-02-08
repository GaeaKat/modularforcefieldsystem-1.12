package com.minalien.mffs.network.packet

import com.minalien.mffs.tiles.MFFSMachine
import net.minecraft.server.MinecraftServer
import net.minecraftforge.fml.common.network.simpleimpl.{IMessage, IMessageHandler, MessageContext}

/**
 * Created by Katrina on 22/01/2015.
 */
class PacketType(tile:MFFSMachine) extends PacketTile[MFFSMachine](tile) with IMessageHandler[PacketType,IMessage]{




  def this()=
  {
    this(null.asInstanceOf[MFFSMachine])

  }


  override def onMessage(message: PacketType, ctx: MessageContext): IMessage =
  {
    super.onMessag(message,ctx)
    if(!ctx.side.isServer)
      throw new IllegalStateException("received PacketType "+message+"on client side!")
    MinecraftServer.getServer.addScheduledTask(new Runnable {

      override def run(): Unit = message.tile.toogleSwitchMode
    })
    null
  }
}
