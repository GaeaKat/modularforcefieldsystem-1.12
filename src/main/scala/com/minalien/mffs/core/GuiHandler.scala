package com.minalien.mffs.core

import com.minalien.mffs.client.gui.GuiProjector
import com.minalien.mffs.containers.ContainerProjector
import com.minalien.mffs.machines.TileEntityProjector
import cpw.mods.fml.common.network.IGuiHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

/**
 * Created by Katrina on 01/12/2014.
 */
object GuiHandler extends IGuiHandler {
  final val PROJECTOR_GUI: Int = 1

  override def getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = {
    if (ID == PROJECTOR_GUI)
      new ContainerProjector(player.inventory, world.getTileEntity(x, y, z).asInstanceOf[TileEntityProjector])
    else
      null
  }

  override def getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = {
    if (ID == PROJECTOR_GUI)
      new GuiProjector(player.inventory, world.getTileEntity(x, y, z).asInstanceOf[TileEntityProjector])
    else
      null
  }
}
