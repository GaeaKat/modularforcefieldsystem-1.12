package com.minalien.mffs.network.packet

import com.minalien.mffs.client.ClientHelper
import com.minalien.mffs.core.MiscHelper
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.server.MinecraftServer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.simpleimpl.{MessageContext, IMessage}

/**
 * Created by Katrina on 22/01/2015.
 */
@SerialVersionUID(-1447633008013055477L)
abstract class PacketTile[T<: TileEntity](t:T) extends IMessage {

  protected var dim: Int = 0
  protected var pos: BlockPos= null
  protected var tile:T=t
  if(tile!=null)
  {
    this.pos=tile.getPos
    this.dim=tile.getWorld.provider.getDimensionId
  }
  @transient
  protected var player: EntityPlayer = null


  def this()=
  {
    this(null.asInstanceOf[T])

  }

  override def fromBytes(buf: ByteBuf): Unit =
  {
    this.pos=new BlockPos(buf.readInt(),buf.readInt(),buf.readInt())
    this.dim=buf.readInt()
  }

  override def toBytes(buf: ByteBuf): Unit =
  {
    buf.writeInt(pos.getX)
    buf.writeInt(pos.getY)
    buf.writeInt(pos.getZ)
    buf.writeInt(dim)
  }


  def onMessag(message:PacketTile[T],ctx:MessageContext):IMessage=
  {
    val server:MinecraftServer=MiscHelper.server
    if(ctx.side.isClient)
      message.player=ClientHelper.getPlayer
    else
      message.player=ctx.getServerHandler.playerEntity

    if(server!=null)
    {
      val world:World=server.worldServerForDimension(message.dim)
      if(world==null)
      {
        MiscHelper.printCurrentStackTrace("No world found for dimension " + message.dim + "!")
        return null
      }

      val tile:TileEntity=world.getTileEntity(message.pos);
      if(tile!=null)
        message.tile=tile.asInstanceOf[T]

    }
    null
  }
}
