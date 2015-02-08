package com.minalien.mffs.network

import io.netty.buffer.ByteBuf
import net.minecraftforge.fml.common.network.simpleimpl.IMessage

/**
 * Created by Katrina on 22/01/2015.
 */
abstract class AbstractPacket  extends IMessage{
  override def fromBytes(buf: ByteBuf)

  override def toBytes(buf: ByteBuf)
}
