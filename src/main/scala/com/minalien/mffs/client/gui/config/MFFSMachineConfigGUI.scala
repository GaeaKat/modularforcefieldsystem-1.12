package com.minalien.mffs.client.gui.config

import com.minalien.mffs.core.{ModularForcefieldSystem, MFFSConfig}
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.common.config.ConfigElement
import net.minecraftforge.fml.client.config.GuiConfig
import scala.collection.JavaConversions._
/**
 * Created by Katrina on 14/01/2015.
 */
class MFFSMachineConfigGUI(parent:GuiScreen) extends GuiConfig(parent,new ConfigElement(MFFSConfig.configFile.getCategory(MFFSConfig.CATEGORY_MACHINES)).getChildElements.toList,ModularForcefieldSystem.MOD_ID,false,false,GuiConfig.getAbridgedConfigPath(MFFSConfig.configFile.toString)) {

}
