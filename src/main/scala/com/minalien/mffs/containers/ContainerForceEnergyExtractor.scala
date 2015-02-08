package com.minalien.mffs.containers


import com.minalien.mffs.tiles.TileEntityExtractor
import com.minalien.mffs.utils.SlotHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.{ICrafting, Slot, Container}
import net.minecraft.item.ItemStack

/**
 * Created by Katrina on 15/01/2015.
 */
class ContainerForceEnergyExtractor(player:EntityPlayer,ForceEnergyExtractor:TileEntityExtractor) extends Container{
  private var WorkCylce: Int = -1
  private var workdone: Int = -1
  private var ForceEnergybuffer: Int = -1
  addSlotToContainer(new SlotHelper(ForceEnergyExtractor,0,82,26)) // Forcium
  addSlotToContainer(new SlotHelper(ForceEnergyExtractor,1,145,40)) // Powerlink
  addSlotToContainer(new SlotHelper(ForceEnergyExtractor,2,20,66)) // Cap upgrade
  addSlotToContainer(new SlotHelper(ForceEnergyExtractor,3,39,66)) // Boost upgrade
  addSlotToContainer(new SlotHelper(ForceEnergyExtractor,4,112,26)) // Forcium Cell

  for(i <- 0 until 3 ; j <- 0 until 9)
  {
    this.addSlotToContainer(new Slot(player.inventory,j+i*9+9,8+j*18,104+i*18))
  }

  for(i<-0 until 9)
    this.addSlotToContainer(new Slot(player.inventory,i,8+i*18,162))


  def getPlayer=player

  override def canInteractWith(playerIn: EntityPlayer): Boolean =
  {
    return ForceEnergyExtractor.isUseableByPlayer(player)
  }

  override def detectAndSendChanges(): Unit =
  {
    super.detectAndSendChanges()
    for(i <- 0 until crafters.size)
    {
      val icrafting:ICrafting=crafters.get(i).asInstanceOf[ICrafting]
      if(workdone!=ForceEnergyExtractor.getWorkDone)
        icrafting.sendProgressBarUpdate(this,0,ForceEnergyExtractor.getWorkDone)
      if(WorkCylce!=ForceEnergyExtractor.getWorkCycle)
        icrafting.sendProgressBarUpdate(this,1,ForceEnergyExtractor.getWorkCycle)
      if(ForceEnergybuffer!=ForceEnergyExtractor.ForceEnergybuffer)
      {
        icrafting.sendProgressBarUpdate(this,2,ForceEnergyExtractor.ForceEnergybuffer&0xfff)
        icrafting.sendProgressBarUpdate(this,3,ForceEnergyExtractor.ForceEnergybuffer >> 16)
      }
    }
  }

  override def transferStackInSlot(playerIn: EntityPlayer, index: Int): ItemStack =
  {
    var stack:ItemStack=null
    val slot:Slot=inventorySlots.get(index).asInstanceOf[Slot]
    if(slot!=null && slot.getHasStack)
    {
      val stack1:ItemStack=slot.getStack
      stack=stack1.copy()
      if(stack1.stackSize==0)
        slot.putStack(null)
      else
        slot.onSlotChanged()

      if(stack1.stackSize!=stack.stackSize)
        slot.onSlotChanged()
      else
        return null
    }
    stack
  }

  override def updateProgressBar(id: Int, data: Int): Unit =
  {
    //id match
    //{
      //case 0 => ForceEnergyExtractor.setWorkDone(data)
      //case 1 => ForceEnergyExtractor.setWorkCycle(data)
      //case 2 => ForceEnergyExtractor.ForceEnergybuffer=(ForceEnergybuffer& 0xffff0000)|data
      //case 3=>ForceEnergyExtractor.ForceEnergybuffer= (ForceEnergyExtractor.ForceEnergybuffer&0xffff) | (data << 16)
    //}
  }//

}
