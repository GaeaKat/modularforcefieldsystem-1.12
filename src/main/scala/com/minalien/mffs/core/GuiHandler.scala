package com.minalien.mffs.core

import com.minalien.mffs.client.gui.{GuiCapacitor, GuiExtractor}
import com.minalien.mffs.containers.{ContainerCapacitor, ContainerForceEnergyExtractor}
import com.minalien.mffs.tiles.{TileEntityCapacitor, TileEntityExtractor}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler

/**
 * Created by Katrina on 15/01/2015.
 */
object GuiHandler extends IGuiHandler{
  final val EXTRACTOR_GUI:Int =1
  final val CAPACITOR_GUI:Int =2
  override def getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef =
  {
    ID match
    {
      case EXTRACTOR_GUI => new ContainerForceEnergyExtractor(player,world.getTileEntity(new BlockPos(x,y,z)).asInstanceOf[TileEntityExtractor])
      case CAPACITOR_GUI => new ContainerCapacitor(player,world.getTileEntity(new BlockPos(x,y,z)).asInstanceOf[TileEntityCapacitor])
      case _ => null
    }
  }

  override def getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef =
  {
    ID match
    {
      case EXTRACTOR_GUI => new GuiExtractor(player,world.getTileEntity(new BlockPos(x,y,z)).asInstanceOf[TileEntityExtractor])
      case CAPACITOR_GUI => new GuiCapacitor(player,world.getTileEntity(new BlockPos(x,y,z)).asInstanceOf[TileEntityCapacitor])
      case _ => null
    }
  }
}
