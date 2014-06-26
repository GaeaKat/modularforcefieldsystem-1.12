package com.minalien.mffs.client.gui

import com.minalien.mffs.client.gui.config.MFFSConfigGUI
import cpw.mods.fml.client.IModGuiFactory
import cpw.mods.fml.client.IModGuiFactory.RuntimeOptionCategoryElement
import net.minecraft.client.Minecraft

/**
 * Handles MFFS Config GUIs.
 */
class MFFSGuiFactory extends IModGuiFactory {
	override def initialize(minecraft: Minecraft) {}

	override def mainConfigGuiClass() = classOf[MFFSConfigGUI]

	override def runtimeGuiCategories() = null

	override def getHandlerFor(element: RuntimeOptionCategoryElement) = null
}
