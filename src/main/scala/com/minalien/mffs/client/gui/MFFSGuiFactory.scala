package com.minalien.mffs.client.gui

import java.util

import com.minalien.mffs.client.gui.config.MFFSBaseConfigGUI
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.client.IModGuiFactory
import net.minecraftforge.fml.client.IModGuiFactory.{RuntimeOptionGuiHandler, RuntimeOptionCategoryElement}

/**
 * Created by Katrina on 14/01/2015.
 */
class MFFSGuiFactory extends IModGuiFactory{
  override def initialize(minecraftInstance: Minecraft): Unit = {}

  override def runtimeGuiCategories(): util.Set[RuntimeOptionCategoryElement] = null

  override def getHandlerFor(element: RuntimeOptionCategoryElement): RuntimeOptionGuiHandler = null

  override def mainConfigGuiClass(): Class[_ <: GuiScreen] = classOf[MFFSBaseConfigGUI]
}
