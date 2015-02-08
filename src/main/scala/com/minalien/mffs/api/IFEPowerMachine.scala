package com.minalien.mffs.api

import net.minecraft.item.ItemStack

/**
 * Created by Katrina on 13/01/2015.
 */
trait IFEPowerMachine {
  def getPowerLinkStack: ItemStack

  def getPowerlinkSlot: Int

  def getPercentageCapacity: Int

  def hasPowerSource: Boolean

  def getAvailablePower: Int

  def getPowerSourceID: Int

  def getMaximumPower: Int

  def consumePower(powerAmount: Int, simulation: Boolean): Boolean

  def emitPower(powerAmount: Int, simulation: Boolean): Boolean

  def isPowersourceItem: Boolean

}
