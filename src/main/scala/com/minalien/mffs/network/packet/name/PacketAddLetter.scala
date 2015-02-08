package com.minalien.mffs.network.packet.name

import com.minalien.mffs.network.packet.PacketTile
import com.minalien.mffs.tiles.MFFSMachine
import io.netty.buffer.ByteBuf
import net.minecraft.server.MinecraftServer
import net.minecraftforge.fml.common.network.simpleimpl.{MessageContext, IMessage, IMessageHandler}

/**
 * Created by Katrina on 25/01/2015.
 */
class PacketAddLetter(tile:MFFSMachine,var letter:Char) extends PacketTile[MFFSMachine](tile) with IMessageHandler[PacketAddLetter,IMessage]{




  def this()=
  {
    this(null.asInstanceOf[MFFSMachine],' ')

  }


  override def fromBytes(buf: ByteBuf): Unit =
  {
    super.fromBytes(buf)
    letter=buf.readChar()
  }

  override def toBytes(buf: ByteBuf): Unit =
  {
    super.toBytes(buf)
    buf.writeChar(letter)
  }

  override def onMessage(message: PacketAddLetter, ctx: MessageContext): IMessage =
  {
    super.onMessag(message,ctx)
    if(!ctx.side.isServer)
      throw new IllegalStateException("received PacketType "+message+"on client side!")
    MinecraftServer.getServer.addScheduledTask(new Runnable {

      override def run(): Unit =
      {
        if (message.tile.getDeviceName.length <= 20)
          message.tile.setDeviceName(message.tile.getDeviceName+message.letter)
      }
    })
    null
  }
}
