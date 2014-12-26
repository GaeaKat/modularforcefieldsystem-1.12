package com.minalien.mffs.tiles

import cofh.api.energy.{EnergyStorage, IEnergyHandler}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{ChatComponentTranslation, ChatComponentText, EnumFacing, IChatComponent}

/**
 * Base Abstract Tile Entity for all MFFS machines
 * Created by Katrina on 13/12/2014.
 */
abstract class MFFSMachine extends TileEntity with IEnergyHandler with IInventory{
  /**
   * Inventory storage
   */
  val inventory: Array[ItemStack] = new Array[ItemStack](getSizeInventory)

  /**
   * Energy storage (RF internally)
   */
  val storage: EnergyStorage = new EnergyStorage(32000)


  /**
   * Recieves Energy
   * @param from
	 * Orientation the energy is received from.
   * @param maxReceive
	 * Maximum amount of energy to receive.
   * @param simulate
	 * If TRUE, the charge will only be simulated.
   **/
  override def receiveEnergy(from: EnumFacing, maxReceive: Int, simulate: Boolean): Int = storage.receiveEnergy(maxReceive,simulate)

  /**
   * Extracts Energy (not really used)
   * @param from
	 * Orientation the energy is extracted from.
   * @param maxExtract
	 * Maximum amount of energy to extract.
   * @param simulate
	 * If TRUE, the extraction will only be simulated.
   **/
  override def extractEnergy(from: EnumFacing, maxExtract: Int, simulate: Boolean): Int = storage.extractEnergy(maxExtract,simulate)

  /**
   * Gets energy in the Machine
   * @param from
   * @return
   */
  override def getEnergyStored(from: EnumFacing): Int = storage.getEnergyStored

  /**
   * gets max amount able to be stored
   * @param from
   * @return
   */
  override def getMaxEnergyStored(from: EnumFacing): Int = storage.getMaxEnergyStored

  def updateItems()=
  {

  }

  override def decrStackSize(index: Int, count: Int): ItemStack =
  {
    var stack = getStackInSlot(index)
    if (stack != null) {
      if (stack.stackSize <= count)
        setInventorySlotContents(index, null)
      else {
        stack = stack.splitStack(count)
        if (stack.stackSize == 0)
          setInventorySlotContents(count, null)
      }
      updateItems()
    }
    stack
  }

  override def closeInventory(player: EntityPlayer): Unit = {}

  override def getSizeInventory: Int = 5

  override def getInventoryStackLimit: Int = 64

  override def clear(): Unit =
  {
      for(i <- 0 until inventory.length)
      {
        inventory(i)=null
      }
  }

  override def isItemValidForSlot(index: Int, stack: ItemStack): Boolean =
  {
    return true //TODO: change this to check for instance of upgrade, card, or other allowed
  }

  override def getStackInSlotOnClosing(index: Int): ItemStack =
  {
    val stack = getStackInSlot(index)
    if (stack != null)
      setInventorySlotContents(index, null)
    stack
  }

  override def openInventory(player: EntityPlayer): Unit =
  {

  }

  override def getFieldCount: Int = 0

  override def getField(id: Int): Int = 0

  override def setInventorySlotContents(index: Int, stack: ItemStack): Unit = {
    this.inventory(index) = stack

    if (stack != null && stack.stackSize > this.getInventoryStackLimit) {
      stack.stackSize = this.getInventoryStackLimit
    }

    this.markDirty()
  }

  override def isUseableByPlayer(player: EntityPlayer): Boolean = true

  override def getStackInSlot(index: Int): ItemStack = if (index >= 0 && index < this.inventory.length) this.inventory(index) else null

  override def setField(id: Int, value: Int): Unit = {}

  override def canConnectEnergy(from: EnumFacing): Boolean = true

  override def getDisplayName: IChatComponent = {
    if (this.hasCustomName) {
      new ChatComponentText(this.getName)
    }
    else
      new ChatComponentTranslation(this.getName, new Array[AnyRef](0))
  }


  override def getName: String = "mffs"

  override def hasCustomName: Boolean = false
}
