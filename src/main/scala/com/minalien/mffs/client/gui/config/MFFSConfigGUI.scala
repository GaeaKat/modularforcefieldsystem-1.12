package com.minalien.mffs.client.gui.config

import com.minalien.mffs.core.{MFFSConfig, ModularForcefieldSystem}
import cpw.mods.fml.client.config.GuiConfig
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.common.config.ConfigElement
import scala.collection.JavaConversions._

class MFFSConfigGUI(parent: GuiScreen) extends GuiConfig(parent,
	new ConfigElement(MFFSConfig.configFile.getCategory(MFFSConfig.CATEGORY_MACHINES)).getChildElements.toList,
	ModularForcefieldSystem.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(MFFSConfig.configFile.toString)) {
}