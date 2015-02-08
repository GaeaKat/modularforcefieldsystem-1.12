package com.minalien.mffs.tiles

import com.minalien.mffs.api.{IPowerLinkItem, IForceEnergyItems, EnumUpgradeType, IForceStorageEnergyBlock}
import com.minalien.mffs.core.LinkGrid
import com.minalien.mffs.items.{ItemForciumCell, ItemUpgrade}
import com.minalien.mffs.utils.NBTUtils
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Container
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.{EnumFacing, ChatComponentText, IChatComponent}
import net.minecraftforge.common.util.Constants

/**
 * Created by Katrina on 15/01/2015.
 */
class TileEntityCapacitor extends TileEntityFEPoweredMachine with IForceStorageEnergyBlock{
  var inventory: Array[ItemStack] = new Array[ItemStack](5)
  var forcePower: Int = 0
  var linkedprojector: Int = 0
  var capacity: Int = 0
  var powerlinkmode: Int = 0
  var TransmitRange: Int = 8

  override def getPowerStorageID:Int = getDeviceID


  def setTransmitRange(transmitRange:Int)=
  {
    if(transmitRange!=TransmitRange)
    {
      TransmitRange=transmitRange
      worldObj.markBlockForUpdate(pos)
    }
  }

  def getTransmitRange = TransmitRange

  def setCapacity(capacity:Int) =
  {
    if(getPercentageStorageCapacity!=capacity)
    {
      this.capacity=capacity
      worldObj.markBlockForUpdate(pos)
    }
  }
  def getPowerLinkMode=powerlinkmode
  def setPowerLinkMode(mode:Int)=
  {
    if(mode!=powerlinkmode)
    {
      powerlinkmode=mode
      worldObj.markBlockForUpdate(pos)
    }
  }
  override def getPercentageStorageCapacity=capacity

  def getLinkedProjector = linkedprojector

  def setLinkedProjector(id:Int): Unit =
  {
    if(id!=linkedprojector)
    {
      linkedprojector=id
      worldObj.markBlockForUpdate(pos)
    }
  }

  def getStorageAvailablePower: Int = {
    return forcePower
  }

  def setForcePower(f: Int) {
    forcePower = f
  }

  def getSizeInventory: Int = {
    return inventory.length
  }


  override def getLinkedSecurityStation: MFFSMachine = null

  override  def getStorageMaxPower:Int=
  {
    if(getStackInSlot(0)!=null)
      getStackInSlot(0).getItem match
      {
        case s:ItemUpgrade => s.getTypeUpgrade() match {case EnumUpgradeType.CapacitorUpgradeCapacity =>
        {
            if(this.forcePower > 10000000 + (2000000 * getStackInSlot(0).stackSize))
              setForcePower(10000000 + (2000000 * getStackInSlot(0).stackSize))
          return 10000000 + (2000000 * getStackInSlot(0).stackSize)
        }
        }
      }
    if(forcePower> 10000000)
      setForcePower(10000000)
    return 10000000
  }


  def checkSlots(init:Boolean)=
  {
    if(getStackInSlot(1)!=null) {
      getStackInSlot(1).getItem match {
        case s: ItemUpgrade => {
          s.getTypeUpgrade() match {
            case EnumUpgradeType.CapacitorUpgradeRange => {
              setTransmitRange(8 * (getStackInSlot(1).stackSize + 1))
            }
            case _ => setTransmitRange(8)

          }
        }
        case _ => setTransmitRange(8)
      }
    }
    else
      setTransmitRange(8)


    if(getStackInSlot(2)!=null)
    {
      getStackInSlot(2).getItem match
      {
        case item:IForceEnergyItems =>
        {
          if(this.getPowerLinkMode!=3 && this.getPowerLinkMode!=4)
            this.setPowerLinkMode(3);

          getPowerLinkMode match
          {
            case 3=>
            {
              if(item.getAvailablePower(getStackInSlot(2)) < item.getMaximumPower(null))
              {
                val maxTransfer=item.getPowerTransferRate;
                val freeAmount=item.getMaximumPower(null)-item.getAvailablePower(getStackInSlot(2))
                if(this.getStorageAvailablePower > 0)
                {
                  if(this.getStorageAvailablePower>maxTransfer)
                  {
                    if(freeAmount>maxTransfer) {
                      item.setAvailablePower(getStackInSlot(2), item.getAvailablePower(getStackInSlot(2)) + maxTransfer)
                      this.setForcePower(this.getStorageAvailablePower - maxTransfer)
                    } else {
                      item.setAvailablePower(getStackInSlot(2), item.getAvailablePower(getStackInSlot(2)) + freeAmount)
                      this.setForcePower(this.getStorageAvailablePower - freeAmount)
                    }
                  } else {
                    if(freeAmount> this.getStorageAvailablePower)
                    {
                      item.setAvailablePower(getStackInSlot(2), item.getAvailablePower(getStackInSlot(2)) + this.getStorageAvailablePower)
                      this.setForcePower(0)
                    } else {
                      item.setAvailablePower(getStackInSlot(2), item.getAvailablePower(getStackInSlot(2)) + freeAmount)
                      this.setForcePower(this.getStorageAvailablePower - freeAmount)
                    }
                  }
                  // getStackInSlot(2).setItemDamage() WHY was this in the original?
                }
              }

            }
            case 4 =>
            {
              if(item.getAvailablePower(getStackInSlot(2))>0)
              {
                val maxTransfer=item.getPowerTransferRate
                val freeAmount=this.getStorageMaxPower-this.getStorageAvailablePower
                val amountLeft=item.getAvailablePower(getStackInSlot(2))

                if(freeAmount>amountLeft)
                {
                  if(amountLeft>maxTransfer)
                  {
                    item.setAvailablePower(getStackInSlot(2),item.getAvailablePower(getStackInSlot(2))-maxTransfer)
                    this.setForcePower(this.getStorageAvailablePower+maxTransfer)
                  }
                  else
                  {
                    item.setAvailablePower(getStackInSlot(2),item.getAvailablePower(getStackInSlot(2))-amountLeft)
                    this.setForcePower(this.getStorageAvailablePower+amountLeft)
                  }
                } else {
                  item.setAvailablePower(getStackInSlot(2),item.getAvailablePower(getStackInSlot(2))-freeAmount)
                  this.setForcePower(this.getStorageAvailablePower+freeAmount)
                }
              }
            }

          }
        }
        case ItemForciumCell =>
        {
          if(this.getPowerLinkMode!=0 && this.getPowerLinkMode!=1 && this.getPowerLinkMode!=2 )
            this.setPowerLinkMode(0);
        }
      }
    }
  }


  override def dropPlugins: Unit =
  {
    for(a <- 0 until inventory.length)
      dropPlugins(a,this)
  }


  override def invalidate(): Unit =
  {
    LinkGrid.getWorldNet(worldObj).Capacitors.remove(getDeviceID)
    super.invalidate()
  }



  override def update(): Unit =
  {
    if (worldObj.isRemote == false) {
      if (init)
        checkSlots(true)
      if (getSwitchMode == 1)
        if (!getSwitchValue && isRedstoneSignal)
          toggleSwitchValue

      if (getSwitchMode == 1)
        if (getSwitchValue && !isRedstoneSignal)
          toggleSwitchValue

      if (getSwitchValue) {
        if (isActive != true)
          activate()
      }
      else
      if (isActive != false)
        deactivate()

      if (this.getTicker == 10) {
        if(getLinkedProjector!=LinkGrid.WorldNets.get(worldObj).connectedToCapacitor(this,getTransmitRange))
          setLinkedProjector(LinkGrid.WorldNets.get(worldObj).connectedToCapacitor(this,getTransmitRange))
        if (this.getPercentageStorageCapacity != ((getStorageAvailablePower / 1000) * 100) / (getStorageMaxPower / 1000))
          setCapacity(((getStorageAvailablePower / 1000) * 100) / (getStorageMaxPower / 1000))
        checkSlots(false)
        if (isActive)
          powerTransfer();
        this.setTicker(0)
      }
      this.setTicker(this.getTicker+1)
    }
    super.update()
  }
  def powerTransfer() =
  {
    if(hasPowerSource())
    {
      val powerTransferRate=this.getMaximumPower/120
      val freeStorageAmount=this.getMaximumPower-this.getAvailablePower
      val balanceLevel=this.getStorageAvailablePower-this.getAvailablePower
      getPowerLinkMode match
      {
        case 0 =>
        {
          if(getPercentageStorageCapacity>=95 && getPercentageStorageCapacity!=100)
          {
            if(freeStorageAmount>powerTransferRate)
            {
              emitPower(powerTransferRate,false)
              consumePowerfromStorage(powerTransferRate,false)
            }
            else
            {
              emitPower(freeStorageAmount,false)
              consumePowerfromStorage(freeStorageAmount,false)
            }
          }

        }
        case 1 =>
        {
          if(getPercentageCapacity< this.getPercentageStorageCapacity)
          {
            if(balanceLevel>powerTransferRate)
            {
              emitPower(powerTransferRate,false)
              consumePowerfromStorage(powerTransferRate,false)
            }
            else
            {
              emitPower(balanceLevel,false)
              consumePowerfromStorage(balanceLevel,false)
            }
          }
        }
        case 2 =>
        {
          if(getStorageAvailablePower>0 && getPercentageCapacity!=100)
          {
            if(getStorageAvailablePower>powerTransferRate)
            {
              if(freeStorageAmount>powerTransferRate)
              {
                emitPower(powerTransferRate,false)
                consumePowerfromStorage(powerTransferRate,false)
              }
              else
              {
                emitPower(freeStorageAmount,false)
                consumePowerfromStorage(freeStorageAmount,false)
              }
            }
            else {
              if(freeStorageAmount>getStorageAvailablePower)
              {
                emitPower(getStorageAvailablePower,false)
                consumePowerfromStorage(getStorageAvailablePower,false)
              }
              else
              {
                emitPower(freeStorageAmount,false)
                consumePowerfromStorage(freeStorageAmount,false)
              }
            }
          }
        }
      }
    }
  }


  override def getStackInSlot(index: Int): ItemStack = inventory(index)
  override def decrStackSize(index: Int, count: Int): ItemStack =
  {
    if (this.inventory(index) != null) {
      var itemstack: ItemStack = null
      if (this.inventory(index).stackSize <= count) {
        itemstack = this.inventory(index)
        this.inventory(index) = null
        this.markDirty
        return itemstack
      }
      else {
        itemstack = this.inventory(index).splitStack(count)
        if (this.inventory(index).stackSize == 0) {
          this.inventory(index) = null
        }
        this.markDirty
        return itemstack
      }
    }
    else {
      return null
    }
  }


  override def setInventorySlotContents(index: Int, stack: ItemStack): Unit = {
    this.inventory(index) = stack

    if (stack != null && stack.stackSize > this.getInventoryStackLimit) {
      stack.stackSize = this.getInventoryStackLimit
    }

    this.markDirty
  }



  override def getName: String = "Generator"

  override def hasCustomName: Boolean = false

  override def getDisplayName: IChatComponent = new ChatComponentText("Generator");


  override def getfreeStorageAmount: Int = this.getStorageMaxPower-this.getStorageAvailablePower


  override def insertPowertoStorage(powerAmount: Int, simulation: Boolean): Boolean = {
    if(simulation)
    {
      if(getStorageAvailablePower+powerAmount<=getStorageMaxPower)
        true
      else
        false
    }
    else {
      setForcePower(getStorageAvailablePower + powerAmount)
      true
    }
  }


  override def consumePowerfromStorage(powerAmount: Int, simulation: Boolean): Boolean =
  {
    if(simulation)
    {
      if(getStorageAvailablePower>=powerAmount)
        true
      else
        false
    }
    else {
      if(getStorageAvailablePower-powerAmount>=0)
        setForcePower(getStorageAvailablePower-powerAmount)
      else
        setForcePower(0)
      true
    }
  }


  override def isItemValid(par1ItemStack: ItemStack, Slot: Int): Boolean = {
    Slot match
    {
      case 0 =>
        par1ItemStack.getItem match { case s:ItemUpgrade => { s.getTypeUpgrade()==EnumUpgradeType.CapacitorUpgradeCapacity} case _ => false}
      case 1 =>
        par1ItemStack.getItem match { case s:ItemUpgrade => { s.getTypeUpgrade()==EnumUpgradeType.CapacitorUpgradeRange} case _ => false}
      case 2=>
        par1ItemStack.getItem match { case s:IForceEnergyItems => true case s:IPowerLinkItem => true case _ => false}
      //case 4=>   // TODO: Item card security link
      //  par1ItemStack.getItem match { case s:ItemUpgrade => { s.getTypeUpgrade()==EnumUpgradeType.CapacitorUpgradeCapacity} case _ => false}
    }
  }

  override def getmaxSwitchMode: Short = {
    return 3
  }

  override def getminSwitchMode: Short= {
    return 1
  }


  override def getPowerLinkStack: ItemStack = getStackInSlot(getPowerlinkSlot)


  override def isItemValidForSlot(index: Int, stack: ItemStack): Boolean = isItemValid(stack,index)


  override def clear(): Unit = {

    for (i <- 0 until this.inventory.length) {
      this.inventory(i) = null
    }


  }




  override def getSlotsForFace(side: EnumFacing): Array[Int] = Array(2)

  override def canInsertItem(index: Int, itemStackIn: ItemStack, direction: EnumFacing): Boolean = true

  override def canExtractItem(index: Int, stack: ItemStack, direction: EnumFacing): Boolean = true



  override def getContainer(inventoryplayer: InventoryPlayer): Container = ???

  def getSlotStackLimit(Slot: Int): Int = {
    Slot match {
      case 0 =>
        return 9
      case 1 =>
        return 9
      case 2 =>
        return 64
    }
    return 1
  }

  override def getPowerlinkSlot: Int = 2

  override def readTransientNBT(nbttagcompount: NBTTagCompound): Unit =
  {
    super.readTransientNBT(nbttagcompount)
    linkedprojector=nbttagcompount.getInteger("linkedProjector")
    capacity=nbttagcompount.getInteger("capacity")
    TransmitRange=nbttagcompount.getInteger("TransmitRange")
  }

  override def readExtraNBT(nbttagcompound: NBTTagCompound): Unit =
  {
    super.readExtraNBT(nbttagcompound)

    forcePower=nbttagcompound.getInteger("forcepower")
    powerlinkmode=nbttagcompound.getInteger("PowerLinkMode")
    inventory=new Array[ItemStack](5)
    NBTUtils.inventoryFromNBT(inventory,nbttagcompound.getTagList("Items",Constants.NBT.TAG_COMPOUND))
  }

  override def writeExtraNBT(nbttagcompound: NBTTagCompound): Unit =
  {
    super.writeExtraNBT(nbttagcompound)
    nbttagcompound.setInteger("forcepower",forcePower)
    nbttagcompound.setInteger("PowerLinkMode",getPowerLinkMode)
    nbttagcompound.setTag("Items",NBTUtils.NBTFromInventory(inventory))
  }

  override def writeTransientNBT(nbttagcompount: NBTTagCompound): Unit =
  {
    super.writeTransientNBT(nbttagcompount)

    nbttagcompount.setInteger("linkedProjector",linkedprojector)
    nbttagcompount.setInteger("capacity",capacity)
    nbttagcompount.setInteger("TransmitRange",TransmitRange)
  }
}
