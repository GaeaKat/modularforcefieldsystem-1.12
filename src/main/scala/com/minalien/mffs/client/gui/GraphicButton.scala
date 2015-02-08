package com.minalien.mffs.client.gui

import com.minalien.mffs.tiles.{TileEntityCapacitor, MFFSMachine}
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.renderer.{WorldRenderer, Tessellator}
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation

/**
 * Created by Katrina on 22/01/2015.
 */
class GraphicButton(par1:Int,par2:Int,par3:Int,tileEntity:TileEntity,typ:Int) extends GuiButton(par1,par2,par3,16,16,"") {
  private val SWITCH_MODE_TEXTURE_SIZE: Int = 96
  private val CONVERTER_OUTPUT_TEXTURE_SIZE: Int = 32
  private val CONTROL_SYSTEM_TEXTURE_SIZE: Int = 32
  private val DEFENSE_STATION_TEXTURE_SIZE: Int = 96
  private val CAPACITOR_TEXTURE_SIZE: Int = 48
  private val PROJECTOR_TEXTURE_SIZE: Int = 64



  def drawButtonRect(x:Int,y:Int,frameX:Int,frameY:Int,width:Int,height:Int,textureSize:Int) =
  {
      val uvMult:Double=1.0/textureSize
      val tesselator:Tessellator=Tessellator.getInstance()
      val worldRenderer:WorldRenderer=tesselator.getWorldRenderer
      worldRenderer.startDrawingQuads();
      worldRenderer.addVertexWithUV(x,y+height,this.zLevel,frameX*uvMult,(frameY+height)*uvMult);
      worldRenderer.addVertexWithUV(x + width, y + height, this.zLevel, (frameX + width) * uvMult,
        (frameY + height) * uvMult)
      worldRenderer.addVertexWithUV(x + width, y, this.zLevel, (frameX + width) * uvMult, frameY * uvMult)
      worldRenderer.addVertexWithUV(x, y, this.zLevel, frameX * uvMult, frameY * uvMult)
      tesselator.draw()
      //tesselator.
  }

  override def drawButton(mc: Minecraft, mouseX: Int, mouseY: Int): Unit =
  {
    if(this.visible)
    {
      tileEntity match
      {
        case tile:TileEntityCapacitor => {
          if(typ==0)
          {
            mc.renderEngine.bindTexture(new ResourceLocation("modularforcefieldsystem:textures/gui/SwitchModes.png"));
            drawButtonRect(this.xPosition,this.yPosition,tile.getSwitchMode*16,0,this.width,this.height,SWITCH_MODE_TEXTURE_SIZE)
          }
          else
          if (typ == 1) {
            mc.renderEngine.bindTexture(new ResourceLocation("modularforcefieldsystem:textures/gui/CapacitorButtons.png"))
            val powerLinkMode: Int = tile.getPowerLinkMode
            drawButtonRect(xPosition, yPosition, (if (powerLinkMode < 3) powerLinkMode else (powerLinkMode - 3)) * 16, (if (powerLinkMode < 3) 0 else 16), width, height, CAPACITOR_TEXTURE_SIZE)
          }
        }
        case tile:MFFSMachine =>
        {
          if(typ==0)
          {
            mc.renderEngine.bindTexture(new ResourceLocation("modularforcefieldsystem:textures/gui/SwitchModes.png"));
            drawButtonRect(this.xPosition,this.yPosition,tile.getSwitchMode*16,0,this.width,this.height,SWITCH_MODE_TEXTURE_SIZE)
          }
        }

      }
    }
  }
}
