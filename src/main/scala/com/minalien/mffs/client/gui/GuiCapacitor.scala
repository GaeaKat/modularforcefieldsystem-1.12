package com.minalien.mffs.client.gui

import com.minalien.mffs.containers.ContainerCapacitor
import com.minalien.mffs.core.ModularForcefieldSystem
import com.minalien.mffs.network.packet.PacketType
import com.minalien.mffs.network.packet.name.{PacketClearName, PacketAddLetter, PacketDeleteLetter}
import com.minalien.mffs.tiles.TileEntityCapacitor
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

/**
 * Created by Katrina on 25/01/2015.
 */
class GuiCapacitor(player:EntityPlayer,Core:TileEntityCapacitor)  extends GuiContainer(new ContainerCapacitor(player,Core)) {
  this.xSize=176
  this.ySize=207
  var editMode: Boolean = false

  override def keyTyped(typedChar: Char, keyCode: Int): Unit =
  {
    if(keyCode!=1 && editMode)
    {
      if(typedChar==13)
      {
        editMode=false
        return
      }
      if(keyCode==14)
      {
        ModularForcefieldSystem.netHandler.sendToServer(new PacketDeleteLetter(Core))
      }
      if(keyCode!=54 && keyCode!=42 && keyCode!=58 && keyCode!=14)
      {
        ModularForcefieldSystem.netHandler.sendToServer(new PacketAddLetter(Core,typedChar))
      }
    }
    else {
      super.keyTyped(typedChar,keyCode)
    }
  }


  override def mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int): Unit =
  {
    super.mouseClicked(mouseX, mouseY, mouseButton)
    val xMin: Int = (width - xSize) / 2
    val yMin: Int = (height - ySize) / 2

    val x: Int = mouseX - xMin
    val y: Int = mouseY - yMin

    if (editMode) {
      editMode = false;
    } else if (x >= 10 && y >= 5 && x <= 141 && y <= 19) {
      ModularForcefieldSystem.netHandler.sendToServer(new PacketClearName(Core))
      editMode = true
    }
  }



  override def drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int): Unit =
  {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    this.mc.renderEngine.bindTexture(new ResourceLocation("modularforcefieldsystem:textures/gui/GuiCapacitor.png"))
    val w = (width - xSize) / 2
    val k = (height - ySize) / 2
    this.drawTexturedModalRect(w, k, 0, 0, xSize, ySize)
    val i1: Int = (79 * Core.getPercentageStorageCapacity/100)
    drawTexturedModalRect(w + 8, k + 112, 176, 0, i1 + 1, 79)
  }

  protected override def drawGuiContainerForegroundLayer(par1: Int, par2: Int) {
    fontRendererObj.drawString("Force Energy Capacitor", 8, 25, 0x404040)
    fontRendererObj.drawString(Core.getDeviceName, 8, 8, 0x404040)
    fontRendererObj.drawString("FE: " + String.valueOf(Core.getStorageAvailablePower), 8, 100, 0x404040)
    fontRendererObj.drawString("Power Uplink: ", 8, 80, 0x404040)
    fontRendererObj.drawString("transmit range:", 8, 60, 0x404040)
    fontRendererObj.drawString((new StringBuilder).append(" ").append(Core.getTransmitRange).toString, 90, 60, 0x404040)
    fontRendererObj.drawString("linked device:", 8, 43, 0x404040)
    fontRendererObj.drawString((new StringBuilder).append(" ").append(Core.linkedprojector).toString, 90, 45, 0x404040)
  }

  override def initGui {
    this.buttonList.asInstanceOf[java.util.List[GuiButton]].add(new GraphicButton(0, (width / 2) + 65, (height / 2) - 100, Core, 0))
    this.buttonList.asInstanceOf[java.util.List[GuiButton]].add(new GraphicButton(1, (width / 2) + 20, (height / 2) - 28, Core, 1))
    super.initGui
  }

  override def actionPerformed(button: GuiButton): Unit =
  {
    // TODO: packet code, ICK
    if(button.id==0)
    {
      ModularForcefieldSystem.netHandler.sendToServer(new PacketType(Core))
    }
  }
}
