package com.minalien.mffs.network.packet

import com.minalien.mffs.api.IForceEnergyItems
import com.minalien.mffs.items.ItemForciumCell
import com.minalien.mffs.tiles.TileEntityCapacitor
import net.minecraft.server.MinecraftServer
import net.minecraftforge.fml.common.network.simpleimpl.{IMessage, IMessageHandler, MessageContext}

/**
 * Created by Katrina on 25/01/2015.
 */
class PacketCapacitor(tile:TileEntityCapacitor) extends PacketTile[TileEntityCapacitor](tile) with IMessageHandler[PacketCapacitor,IMessage]{




  def this()=
  {
    this(null.asInstanceOf[TileEntityCapacitor])

  }


  override def onMessage(message: PacketCapacitor, ctx: MessageContext): IMessage =
  {
    super.onMessag(message,ctx)
    if(!ctx.side.isServer)
      throw new IllegalStateException("received PacketType "+message+"on client side!")
    MinecraftServer.getServer.addScheduledTask(new Runnable {

      override def run(): Unit =
      {
        var tile:TileEntityCapacitor=message.tile
        if(tile.getStackInSlot(2)!=null)
        {
          tile.getStackInSlot(2).getItem match
          {
            case s:IForceEnergyItems =>
            {
              if(tile.getPowerLinkMode==4)
                tile.setPowerLinkMode(3)
              else
                tile.setPowerLinkMode(4)
            }
            case ItemForciumCell =>
            {
              if(tile.getPowerLinkMode<2)
                tile.setPowerLinkMode(tile.getPowerLinkMode+1)
              else
                tile.setPowerLinkMode(0)
            }
          }

        }
        else
        {
          if(tile.getPowerLinkMode!=4)
            tile.setPowerLinkMode(tile.getPowerLinkMode+1)
          else
            tile.setPowerLinkMode(0)
        }
      }
    })
    null
  }
}
