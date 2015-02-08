package com.minalien.mffs.client.gui.config

import net.minecraft.client.gui.{GuiButton, GuiScreen}
import net.minecraft.client.resources.I18n
/**
 * Created by Katrina on 14/01/2015.
 */
class MFFSBaseConfigGUI(parent:GuiScreen) extends GuiScreen{
  override def initGui(): Unit =
  {
    this.buttonList.clear()
    this.buttonList.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(0,10,10,I18n.format("mffs.config.machine")));
    this.buttonList.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(2,10,30,I18n.format("mffs.config.general")));





    this.buttonList.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(1,10,10,this.width-20,20,I18n.format("mffs.config.done")));

  }

  override def actionPerformed(button: GuiButton): Unit =
  {
    if(button.id==0)
      mc.displayGuiScreen(new MFFSMachineConfigGUI(this))
    if(button.id==1)
      mc.displayGuiScreen(parent)
    if(button.id==2)
      mc.displayGuiScreen(new MFFSGeneralConfigGUI(this))
  }
}
