package com.minalien.mffs.api

import net.minecraft.item.ItemStack

/**
 * Created by Katrina on 22/01/2015.
 */
trait IForceEnergyItems {
  def getAvailablePower(itemStack: ItemStack): Int

  def getMaximumPower(itemStack: ItemStack): Int

  def consumePower(itemStack: ItemStack, powerAmount: Int, simulation: Boolean): Boolean

  def setAvailablePower(itemStack: ItemStack, amount: Int)

  def getPowerTransferRate: Int

  def getItemDamage(stackInSlot: ItemStack): Int
}
