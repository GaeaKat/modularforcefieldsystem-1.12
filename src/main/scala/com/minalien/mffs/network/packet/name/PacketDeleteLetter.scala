package com.minalien.mffs.network.packet.name

import com.minalien.mffs.network.packet.PacketTile
import com.minalien.mffs.tiles.MFFSMachine
import net.minecraft.server.MinecraftServer
import net.minecraftforge.fml.common.network.simpleimpl.{MessageContext, IMessage, IMessageHandler}

/**
 * Created by Katrina on 25/01/2015.
 */
class PacketDeleteLetter(tile:MFFSMachine) extends PacketTile[MFFSMachine](tile) with IMessageHandler[PacketDeleteLetter,IMessage]{




  def this()=
  {
    this(null.asInstanceOf[MFFSMachine])

  }


  override def onMessage(message: PacketDeleteLetter, ctx: MessageContext): IMessage =
  {
    super.onMessag(message,ctx)
    if(!ctx.side.isServer)
      throw new IllegalStateException("received PacketType "+message+"on client side!")
    MinecraftServer.getServer.addScheduledTask(new Runnable {

      override def run(): Unit =
      {
        if(message.tile.getDeviceName.length>=1)
          message.tile.setDeviceName(message.tile.getDeviceName.substring(0,message.tile.getDeviceName.length-1))
      }
    })
    null
  }
}
