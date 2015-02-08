package com.minalien.mffs.tiles


import com.minalien.mffs.api.{IPowerLinkItem, IFEPowerMachine}
import net.minecraft.item.ItemStack

/**
 * Created by Katrina on 13/01/2015.
 */
abstract class TileEntityFEPoweredMachine extends MFFSMachine with IFEPowerMachine{


  override def getPercentageCapacity: Int = {
    val linkCard: ItemStack = getPowerLinkStack
    if (hasPowerSource) {
      return (linkCard.getItem.asInstanceOf[IPowerLinkItem]).getPercentageCapacity(linkCard, this.pos, worldObj)
    }
    return 0
  }


  override def hasPowerSource():Boolean = {
    val linkCard: ItemStack = getPowerLinkStack
    if (linkCard != null && linkCard.getItem.isInstanceOf[IPowerLinkItem]) return true
    return false
  }
  override def getAvailablePower: Int = {
    val linkCard: ItemStack = getPowerLinkStack
    if (hasPowerSource) {
      return (linkCard.getItem.asInstanceOf[IPowerLinkItem]).getAvailablePower(linkCard, this.pos, worldObj)
    }
    return 0
  }
  override def getPowerSourceID: Int = {
    val linkCard: ItemStack = getPowerLinkStack
    if (hasPowerSource) {
      return (linkCard.getItem.asInstanceOf[IPowerLinkItem]).getPowersourceID(linkCard, this.pos, worldObj)
    }
    return 0
  }

  override def getMaximumPower: Int = {
    val linkCard: ItemStack = getPowerLinkStack
    if (hasPowerSource) {
      return (linkCard.getItem.asInstanceOf[IPowerLinkItem]).getMaximumPower(linkCard, this.pos, worldObj)
    }
    return 0
  }

  override def consumePower(powerAmount: Int, simulation: Boolean): Boolean = {
    val linkCard: ItemStack = getPowerLinkStack
    if (hasPowerSource) {
      return (linkCard.getItem.asInstanceOf[IPowerLinkItem]).consumePower(linkCard, powerAmount, simulation, this.pos, worldObj)
    }
    return false
  }

  override def emitPower(powerAmount: Int, simulation: Boolean): Boolean = {
    val linkCard: ItemStack = getPowerLinkStack
    if (hasPowerSource) {
      return (linkCard.getItem.asInstanceOf[IPowerLinkItem]).insertPower(linkCard, powerAmount, simulation, this.pos, worldObj)
    }
    return false
  }

  override def isPowersourceItem: Boolean = {
    val linkCard: ItemStack = getPowerLinkStack
    if (hasPowerSource) {
      return (linkCard.getItem.asInstanceOf[IPowerLinkItem]).isPowersourceItem
    }
    return false
  }
}
