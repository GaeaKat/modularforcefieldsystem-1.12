package com.minalien.mffs.client.gui

import java.util

import com.minalien.mffs.containers.ContainerProjector
import com.minalien.mffs.machines.TileEntityProjector
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.util.ResourceLocation
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11

/**
 * Created by Katrina on 27/11/2014.
 */
class GuiProjector(inventoryPlayer: InventoryPlayer, tileEntity: TileEntityProjector) extends GuiContainer(new ContainerProjector(inventoryPlayer, tileEntity)) {
  xSize = 176
  ySize = 186

  override def drawGuiContainerBackgroundLayer(p_146976_1_ : Float, p_146976_2_ : Int, p_146976_3_ : Int): Unit = {

    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
    this.mc.renderEngine.bindTexture(new ResourceLocation("mffs:textures/gui/GuiProjector.png"))
    val x = (width - xSize) / 2
    val y = (height - ySize) / 2
    this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize)
  }

  override def drawGuiContainerForegroundLayer(p_146979_1_ : Int, p_146979_2_ : Int): Unit = {
    super.drawGuiContainerForegroundLayer(p_146979_1_, p_146979_2_)
    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
    this.mc.renderEngine.bindTexture(new ResourceLocation("mffs:textures/gui/GuiProjector.png"))
    val percentage: Double = (tileEntity.storage.getEnergyStored.toDouble / tileEntity.storage.getMaxEnergyStored.toDouble) * 100.toDouble
    val barWidth = 0.79 * percentage
    drawTexturedModalRect(8, 91, 176, 0, barWidth.toInt, 5)
  }

  override def handleMouseInput(): Unit = {
    super.handleMouseInput()
    val x: Int = Mouse.getEventX * this.width / this.mc.displayWidth
    val y: Int = this.height - Mouse.getEventY * this.height / this.mc.displayHeight - 1

    if (func_146978_c(8, 91, 79, 5, x, y)) {
      val lst: java.util.List[String] = new util.ArrayList[String]()
      drawCreativeTabHoveringText("Energy: " + tileEntity.storage.getEnergyStored + "/" + tileEntity.storage.getMaxEnergyStored, x, y)
    }
  }
}
