package com.minalien.mffs.client.gui.config

import java.util

import com.minalien.mffs.core.{MFFSConfig, ModularForcefieldSystem}
import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement
import cpw.mods.fml.client.config.GuiConfigEntries.CategoryEntry
import cpw.mods.fml.client.config.{GuiConfig, GuiConfigEntries, IConfigElement}
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.resources.I18n
import net.minecraftforge.common.config.ConfigElement

object MFFSConfigGUI {
	def getConfigElements: java.util.List[IConfigElement[_]] = {
		val list = new util.ArrayList[IConfigElement[_]]()

		list.add(new DummyCategoryElement(MFFSConfig.CATEGORY_MACHINES.toLowerCase, "mffs.gui.config.category.machines", classOf[MachineConfigGui]))

		list
	}

	class MachineConfigGui(parent: GuiConfig, entryList: GuiConfigEntries, property: IConfigElement[_]) extends CategoryEntry(parent, entryList, property) {
		override def buildChildScreen() = new GuiConfig(owningScreen,
			new ConfigElement(MFFSConfig.configFile.getCategory(MFFSConfig.CATEGORY_MACHINES)).getChildElements, owningScreen.modID,
			MFFSConfig.CATEGORY_MACHINES, configElement.requiresWorldRestart() || owningScreen.allRequireWorldRestart,
			configElement.requiresMcRestart() || owningScreen.allRequireMcRestart,
			GuiConfig.getAbridgedConfigPath(MFFSConfig.configFile.toString))
	}
}

class MFFSConfigGUI(parent: GuiScreen) extends GuiConfig(parent, MFFSConfigGUI.getConfigElements,
	ModularForcefieldSystem.MOD_ID, false, false, I18n.format("mffs.gui.config.title")) {
}