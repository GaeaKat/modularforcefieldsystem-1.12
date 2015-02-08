package com.minalien.mffs.utils

import com.minalien.mffs.tiles.MFFSMachine
import net.minecraft.inventory.{IInventory, Slot}
import net.minecraft.item.ItemStack

/**
 * Created by Katrina on 15/01/2015.
 */
class SlotHelper(par2Inventory:IInventory,par3:Int,par4:Int,par5:Int) extends Slot(par2Inventory,par3,par4,par5){

  val te:MFFSMachine=par2Inventory.asInstanceOf[MFFSMachine]
  val Slot=par3

  override def getSlotStackLimit: Int =
  {
    te.getSlotStackLimit(Slot)
  }

  override def isItemValid(stack: ItemStack): Boolean =
  {
    te.isItemValid(stack,Slot)
  }
}
