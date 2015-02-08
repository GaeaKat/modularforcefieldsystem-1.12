package com.minalien.mffs.network.packet.name

import com.minalien.mffs.network.packet.PacketTile
import com.minalien.mffs.tiles.MFFSMachine
import net.minecraft.server.MinecraftServer
import net.minecraftforge.fml.common.network.simpleimpl.{MessageContext, IMessage, IMessageHandler}

/**
 * Created by Katrina on 25/01/2015.
 */
class PacketClearName (tile:MFFSMachine) extends PacketTile[MFFSMachine](tile) with IMessageHandler[PacketClearName,IMessage]{




  def this()=
  {
    this(null.asInstanceOf[MFFSMachine])

  }


  override def onMessage(message: PacketClearName, ctx: MessageContext): IMessage =
  {
    super.onMessag(message,ctx)
    if(!ctx.side.isServer)
      throw new IllegalStateException("received PacketType "+message+"on client side!")
    MinecraftServer.getServer.addScheduledTask(new Runnable {

      override def run(): Unit = message.tile.setDeviceName("")
    })
    null
  }
}
