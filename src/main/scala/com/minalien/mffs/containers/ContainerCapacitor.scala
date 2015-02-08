package com.minalien.mffs.containers

import com.minalien.mffs.tiles.TileEntityCapacitor
import com.minalien.mffs.utils.SlotHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.{Slot, Container}

/**
 * Created by Katrina on 25/01/2015.
 */
class ContainerCapacitor(player:EntityPlayer,generatorEntity:TileEntityCapacitor)  extends Container{
  var forcePower= -1
  var linkedProjector= -1
  var powerLinkMode = -1
  var capacity = -1

  addSlotToContainer(new SlotHelper(generatorEntity,4,154,88)) //   Security Link
  addSlotToContainer(new SlotHelper(generatorEntity,0,154,47)) // Capacity upgrade
  addSlotToContainer(new SlotHelper(generatorEntity,1,154,67)) // Range upgrade
  addSlotToContainer(new SlotHelper(generatorEntity,2,87,76)) // Force Energy

  for(i <- 0 until 3 ; j <- 0 until 9)
  {
    this.addSlotToContainer(new Slot(player.inventory,j+i*9+9,8+j*18,125+i*18))
  }

  for(i<-0 until 9)
    this.addSlotToContainer(new Slot(player.inventory,i,8+i*18,183))

  override def canInteractWith(playerIn: EntityPlayer): Boolean = return generatorEntity.isUseableByPlayer(playerIn)


}
