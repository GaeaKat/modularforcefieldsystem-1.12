package com.minalien.mffs.client

import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer

/**
 * Created by Katrina on 22/01/2015.
 */
object ClientHelper {

  def getPlayer:EntityPlayer=Minecraft.getMinecraft.thePlayer
}
