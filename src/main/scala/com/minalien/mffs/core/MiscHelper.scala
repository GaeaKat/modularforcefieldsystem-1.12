package com.minalien.mffs.core

import net.minecraft.server.MinecraftServer

/**
 * Created by Katrina on 22/01/2015.
 */
object MiscHelper {
  def server: MinecraftServer = {
    return MinecraftServer.getServer
  }

  def printCurrentStackTrace(message: String) {
    if (message != null) ModularForcefieldSystem.logger.info(message)
    for (element <- Thread.currentThread.getStackTrace) ModularForcefieldSystem.logger.info(element)
  }
}
