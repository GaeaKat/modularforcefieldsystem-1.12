package com.minalien.mffs.api

/**
 * Created by Katrina on 15/01/2015.
 */
trait IForceStorageEnergyBlock {
  def getPercentageStorageCapacity: Int

  def getPowerStorageID: Int

  def getStorageMaxPower: Int

  def getStorageAvailablePower: Int

  def consumePowerfromStorage(powerAmount: Int, simulation: Boolean): Boolean

  def insertPowertoStorage(powerAmount: Int, simulation: Boolean): Boolean

  def getfreeStorageAmount: Int
}
