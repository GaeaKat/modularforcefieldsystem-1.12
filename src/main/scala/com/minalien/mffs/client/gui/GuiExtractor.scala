package com.minalien.mffs.client.gui


import com.minalien.mffs.containers.ContainerForceEnergyExtractor
import com.minalien.mffs.core.{ModularForcefieldSystem, MFFSConfig}
import com.minalien.mffs.network.packet.PacketType
import com.minalien.mffs.network.packet.name.{PacketClearName, PacketAddLetter, PacketDeleteLetter}
import com.minalien.mffs.tiles.TileEntityExtractor
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

/**
 * Created by Katrina on 15/01/2015.
 */
class GuiExtractor(player:EntityPlayer,Extractor:TileEntityExtractor) extends GuiContainer(new ContainerForceEnergyExtractor(player,Extractor)) {
  xSize=176
  ySize=186
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
          ModularForcefieldSystem.netHandler.sendToServer(new PacketDeleteLetter(Extractor))
        }
        if(keyCode!=54 && keyCode!=42 && keyCode!=58 && keyCode!=14)
        {
          ModularForcefieldSystem.netHandler.sendToServer(new PacketAddLetter(Extractor,typedChar))
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
      ModularForcefieldSystem.netHandler.sendToServer(new PacketClearName(Extractor))
        editMode = true
    }
  }

  override def initGui(): Unit =
  {
    this.buttonList.asInstanceOf[java.util.List[GuiButton]].add(new GraphicButton(0,(width/2)+60,(height/2)-88,Extractor,0))
    super.initGui()
  }

  override def actionPerformed(button: GuiButton): Unit =
  {
    if(button.id==0)
    {
      ModularForcefieldSystem.netHandler.sendToServer(new PacketType(Extractor))
    }
  }

  override def drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int): Unit =
  {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    this.mc.renderEngine.bindTexture(new ResourceLocation("modularforcefieldsystem:textures/gui/GuiExtractor.png"))
    val w = (width - xSize) / 2
    val k = (height - ySize) / 2
    this.drawTexturedModalRect(w, k, 0, 0, xSize, ySize)
    val Workpowerslider: Int = (79 * Extractor.workdone/ 100)
    drawTexturedModalRect(w + 49, k + 89, 176, 0, Workpowerslider, 6)

    val WorkCycle: Int = (32 * Extractor.WorkCycle) / MFFSConfig.Machines.ForciciumWorkCycle

    drawTexturedModalRect(w + 73, k + 50, 179, 81, WorkCycle, 32)

    val ForceEnergy: Int = (24 * Extractor.ForceEnergybuffer / Extractor.MaxForceEnergyBuffer)
    drawTexturedModalRect(w + 137, k + 60, 219, 80, 32, ForceEnergy)
  }

  override def drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int): Unit =
  {
    this.fontRendererObj.drawString("Force Energy", 5, 25, 0x404040)
    this.fontRendererObj.drawString("Upgrades", 10, 50, 0x404040)
    this.fontRendererObj.drawString(Extractor.getDeviceName, 8, 9, 0x404040)
    this.fontRendererObj.drawString("Extractor", 5, 35, 0x404040)
    this.fontRendererObj.drawString(String.valueOf(Extractor.ForceEnergybuffer/ 1000).concat("k"), 140, 89, 0x404040)
    this.fontRendererObj.drawString(String.valueOf(Extractor.getWorkDone()).concat("%"), 23, 89, 0x404040)
  }
}
