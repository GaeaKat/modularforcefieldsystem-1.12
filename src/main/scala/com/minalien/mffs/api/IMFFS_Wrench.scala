package com.minalien.mffs.api

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing

/**
 * Lets this block be wrnechanble with the MFFS Wrench
 * Created by Katrina on 09/01/2015.
 */
trait IMFFS_Wrench {
  def getSide():EnumFacing
  def setSide(facing:EnumFacing)
  def wrenchCanManipulate(entityPlayer:EntityPlayer,facing:EnumFacing):Boolean
}
