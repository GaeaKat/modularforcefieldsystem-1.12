package com.minalien.mffs.client.gui.config

import com.minalien.mffs.core.{ModularForcefieldSystem, MFFSConfig}
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.common.config.{Configuration, ConfigElement}
import net.minecraftforge.fml.client.config.GuiConfig
import scala.collection.JavaConversions._
/**
 * Created by Katrina on 14/01/2015.
 */
class MFFSGeneralConfigGUI(parent:GuiScreen) extends GuiConfig(parent,new ConfigElement(MFFSConfig.configFile.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements.toList,ModularForcefieldSystem.MOD_ID,false,false,GuiConfig.getAbridgedConfigPath(MFFSConfig.configFile.toString)) {

}
