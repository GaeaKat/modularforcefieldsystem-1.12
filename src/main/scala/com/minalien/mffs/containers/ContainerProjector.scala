package com.minalien.mffs.containers

import com.minalien.mffs.machines.TileEntityProjector
import net.minecraft.entity.player.{EntityPlayer, InventoryPlayer}
import net.minecraft.inventory.{Container, Slot}

/**
 * Created by Katrina on 27/11/2014.
 */
class ContainerProjector(inventoryPlayer: InventoryPlayer, tileEntity: TileEntityProjector) extends Container {
  addSlotToContainer(new Slot(tileEntity, 0, 120, 82))
  addSlotToContainer(new Slot(tileEntity, 1, 138, 82))
  addSlotToContainer(new Slot(tileEntity, 2, 156, 82))
  addSlotToContainer(new Slot(tileEntity, 3, 11, 61))
  addSlotToContainer(new Slot(tileEntity, 4, 11, 38))
  bindPlayerInventory(inventoryPlayer)

  override def canInteractWith(player: EntityPlayer): Boolean = tileEntity.isUseableByPlayer(player)


  def bindPlayerInventory(inventoryPlayer: InventoryPlayer) = {
    for (i <- 0 until 3; j <- 0 until 9) {
      addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 104 + i * 18))
    }
    for (i <- 0 until 9)
      addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 162))
  }

}
