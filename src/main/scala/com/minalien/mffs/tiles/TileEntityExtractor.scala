package com.minalien.mffs.tiles

import cofh.api.energy.IEnergyHandler
import com.minalien.mffs.api.{EnumUpgradeType, IPowerLinkItem}
import com.minalien.mffs.containers.ContainerForceEnergyExtractor
import com.minalien.mffs.core.{LinkGrid, MFFSConfig}
import com.minalien.mffs.items.{ItemUpgrade, ItemForciumCell, ItemForcium}
import com.minalien.mffs.utils.NBTUtils
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Container
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.server.gui.IUpdatePlayerListBox
import net.minecraft.util.{ChatComponentText, EnumFacing, IChatComponent}
import net.minecraftforge.common.util.Constants

/**
 * Created by Katrina on 15/12/2014.
 */
class TileEntityExtractor extends TileEntityFEPoweredMachine with IUpdatePlayerListBox with IEnergyHandler{
  val inventory: Array[ItemStack] = new Array[ItemStack](5);
  var workmode: Int = 0
  var WorkEnergy: Int = 0
  var MaxWorkEnergy: Int = 4000
  var ForceEnergybuffer: Int = 0
  var MaxForceEnergyBuffer: Int = 1000000
  //                              100000
  var WorkCycle: Int = 0
  var workTicker: Int = 20
  var workdone: Int = 0
  var maxworkcycle: Int = 125
  var capacity: Int = 0


  def getCapacity():Int=capacity

  def setCapacity(cap:Int)=
  {
    if(this.capacity!=cap)
    {
      this.capacity=cap
      worldObj.markBlockForUpdate(pos)
    }
  }
  def getWorkDone()=workdone

  def setWorkDone(workdone:Int)=
  {
    if(this.workdone!=workdone)
    {
      this.workdone=workdone
      worldObj.markBlockForUpdate(pos)
    }
  }

  def getWorkCycle()=WorkCycle

  def setWorkCycle(WorkCycle:Int)=
  {
    if(this.WorkCycle!=WorkCycle)
    {
      this.WorkCycle=WorkCycle
      worldObj.markBlockForUpdate(pos)
    }
  }
  def getForceEnergyBuffer() = ForceEnergybuffer
  def setForceEnergyBuffer(force:Int) =
  {
    if(this.ForceEnergybuffer!=force)
    {
      this.ForceEnergybuffer=force
      worldObj.markBlockForUpdate(pos)
    }
  }

  override def dropPlugins: Unit =
  {
    for(i <- 0 until inventory.length)
      dropPlugins(i,this)
  }
  def checkSlots(init:Boolean)=
  {
    if(getStackInSlot(2) ne null)
    {
      getStackInSlot(2).getItem match
      {
         case s:ItemUpgrade=>
          s.getTypeUpgrade() match {
            case EnumUpgradeType.CapacitorUpgradeCapacity =>
              MaxForceEnergyBuffer= 1000000 +(getStackInSlot(2).stackSize * 1000000)
            case _ => MaxForceEnergyBuffer = 1000000
          }
         case _ => MaxForceEnergyBuffer= 1000000
      }
    }
    else
      MaxForceEnergyBuffer= 1000000

    if(getStackInSlot(3) ne null)
    {
      getStackInSlot(3).getItem match {
        case s: ItemUpgrade =>
          s.getTypeUpgrade() match {
            case EnumUpgradeType.ExtractorBooster => workTicker = 20 - getStackInSlot(3).stackSize
            case _ => workTicker = 20
          }
        case _ => workTicker=20
      }
    }
    else
      workTicker=20

    if(getStackInSlot(4) ne null)
    {
      getStackInSlot(4).getItem match
      {
        case ItemForciumCell => {
          workmode=1
          MaxWorkEnergy=200000

        }
      }
    }
    else
    {
      workmode=0
      MaxWorkEnergy=4000
    }
  }

  def hasPowerToConvert:Boolean=
  {
    if(WorkEnergy>=MaxWorkEnergy-1)
    {
      WorkEnergy=0
      true
    }
    else
      false
  }

  def hasFreeForceEnergyStorage:Boolean=
  {
    if(this.MaxForceEnergyBuffer> this.ForceEnergybuffer)
      true
    else
      false
  }
  def hasStuffToConvert:Boolean= {
    if (WorkCycle > 0)
      true
    else if(MFFSConfig.General.adventureMode)
    {
      maxworkcycle=MFFSConfig.Machines.ForciciumCellWorkCycle
      setWorkCycle(maxworkcycle)
      true

    } else if(getStackInSlot(0)!=null)
    {
      getStackInSlot(0).getItem match
      {
        case ItemForcium =>
        {
          maxworkcycle=MFFSConfig.Machines.ForciciumWorkCycle
          setWorkCycle(maxworkcycle)
          decrStackSize(0,1)
          true
        }
        case ItemForciumCell =>
        {
          if(ItemForciumCell.useForceium(1,getStackInSlot(0)))
          {
            maxworkcycle=MFFSConfig.Machines.ForciciumCellWorkCycle
            setWorkCycle(maxworkcycle)
            true
          }
          else
            false
        }
        case _ => false
      }
    }
    else
      false
  }
  def transferForceEnergy() =
  {
    if(this.ForceEnergybuffer>0)
    {
      if(this.hasPowerSource)
      {
        val PowerTransferRate=this.getMaximumPower/120
        val freeAmount=this.getMaximumPower-this.getAvailablePower
        if(this.ForceEnergybuffer>freeAmount)
        {
          if(freeAmount>PowerTransferRate)
          {
            emitPower(PowerTransferRate,false)
            this.setForceEnergyBuffer(ForceEnergybuffer-PowerTransferRate)
          }
          else
          {
            emitPower(freeAmount,false)
            this.setForceEnergyBuffer(ForceEnergybuffer-freeAmount)
          }
        }
        else
        {
          if(freeAmount>ForceEnergybuffer)
          {
            emitPower(ForceEnergybuffer,false)
            this.setForceEnergyBuffer(0)
          }
          else
          {
            emitPower(freeAmount,false)
            this.setForceEnergyBuffer(ForceEnergybuffer-freeAmount)
          }
        }
      }
    }
  }

  override def getmaxSwitchMode=3
  override def getminSwitchMode=1

  override def update(): Unit =
  {
    if(worldObj.isRemote==false)
    {
      if(this.init)
        checkSlots(true)

      if(getSwitchMode==1)
        if(!getSwitchValue && isRedstoneSignal)
          toggleSwitchValue
      if(getSwitchMode==1)
        if(getSwitchValue && !isRedstoneSignal)
          toggleSwitchValue
      if(!isActive && getSwitchValue)
        this.activate()
      if(isActive && !getSwitchValue)
        this.deactivate()
      // TODO: REmove this once we have some power API
      WorkEnergy=MaxWorkEnergy
      if(this.getTicker >=workTicker)
      {
        checkSlots(false)
        if(workmode==0 && isActive)
        {
          if(this.workdone!=WorkEnergy*100/MaxWorkEnergy)
            setWorkDone(WorkEnergy*100/MaxWorkEnergy)

          if(workdone>100)
            setWorkDone(workdone=100)

          if(getCapacity()!=(ForceEnergybuffer*100)/MaxForceEnergyBuffer)
          {
            setCapacity((ForceEnergybuffer*100)/MaxForceEnergyBuffer)
          }
          if(this.hasFreeForceEnergyStorage && this.hasStuffToConvert)
          {
            if(this.hasPowerToConvert)
            {
              setWorkCycle(WorkCycle-1)
              this.setForceEnergyBuffer(ForceEnergybuffer+MFFSConfig.Machines.ExtractorPassForceEnergyGenerate)
            }
          }
          transferForceEnergy()
          this.setTicker(0)
        }
        if(workmode==1 && isActive)
        {
          if(this.workdone!=WorkEnergy*100/MaxWorkEnergy)
            setWorkDone(WorkEnergy*100/MaxWorkEnergy)

          if(workdone>100)
            setWorkDone(workdone=100)
          if(ItemForciumCell.getForceciumlevel(getStackInSlot(4))<ItemForciumCell.getMaxForceciumlevel)
          {
            if(this.hasPowerToConvert && isActive)
            {
              ItemForciumCell.setForceciumlevel(getStackInSlot(4),ItemForciumCell.getForceciumlevel(getStackInSlot(4))+1)
            }
          }
          this.setTicker(0)
        }
      }
      this.setTicker(this.getTicker+1)
    }
    super.update()
  }

  override def getContainer(inventoryplayer: InventoryPlayer): Container = new ContainerForceEnergyExtractor(inventoryplayer.player,this)


  override def getStackInSlot(index: Int): ItemStack = inventory(index)


  override def getName: String = "Extractor"


  override def hasCustomName: Boolean = false

  override def getDisplayName: IChatComponent = new ChatComponentText("Extractor")


  override def getSizeInventory: Int = inventory.length


  override def setInventorySlotContents(index: Int, stack: ItemStack): Unit =
  {
    this.inventory(index) = stack

    if (stack != null && stack.stackSize > this.getInventoryStackLimit) {
      stack.stackSize = this.getInventoryStackLimit
    }

    this.markDirty
  }


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


  override def isItemValid(par1ItemStack: ItemStack, Slot: Int): Boolean =
  {
    Slot match
    {
      case 0=>
      {
        par1ItemStack.getItem match
        {
          case ItemForciumCell => getStackInSlot(4)==null
          case ItemForcium => getStackInSlot(4)==null
          case _ => false
        }
      }
      case 1 =>
      {
        par1ItemStack.getItem match {
          case item: IPowerLinkItem => true
          case _ => false
        }
      }
      case 2 =>
      {
        par1ItemStack.getItem match
        {
          case item: ItemUpgrade => item.getTypeUpgrade() match { case EnumUpgradeType.CapacitorUpgradeCapacity => true case _ => false}
          case _ => false
        }
      }
      case 3 =>
        par1ItemStack.getItem match
        {
          case item: ItemUpgrade => item.getTypeUpgrade() match { case EnumUpgradeType.ExtractorBooster=> true case _ => false}
          case _ => false
        }
      case 4 =>
      {
        par1ItemStack.getItem match
        {
          case ItemForciumCell => true
          case _ => false
        }
      }
    }
  }


  override def getSlotStackLimit(slt: Int): Int = {
    slt match
    {
      case 0 =>64
      case 1 => 1
      case 2 => 9
      case 3 => 19
      case 4 => 1
      case _ => 1
    }
  }


  override def receiveEnergy(from: EnumFacing, maxReceive: Int, simulate: Boolean): Int =
  {
    val freeSpace:Double=MaxWorkEnergy-WorkEnergy
    if(freeSpace>= maxReceive)
    {
      if(!simulate)
        WorkEnergy=WorkEnergy+maxReceive
      0
    }
    else
    {
      WorkEnergy=MaxWorkEnergy
      (maxReceive-freeSpace).toInt
    }
  }

  override def extractEnergy(from: EnumFacing, maxExtract: Int, simulate: Boolean): Int = 0

  override def getEnergyStored(from: EnumFacing): Int = WorkEnergy

  override def getMaxEnergyStored(from: EnumFacing): Int = MaxWorkEnergy

  override def canConnectEnergy(from: EnumFacing): Boolean = true



  override def getPowerLinkStack: ItemStack = this.getStackInSlot(getPowerlinkSlot)

  override def getPowerlinkSlot: Int = 1


  override def getLinkedSecurityStation: MFFSMachine = null // TODO: Finish once have security station



  override def isItemValidForSlot(index: Int, stack: ItemStack): Boolean = isItemValid(stack,index)



  override def getSlotsForFace(side: EnumFacing): Array[Int] =
  {
    side match
    {
      case EnumFacing.UP=> Array(0)
      case EnumFacing.DOWN=> Array(1)
      case EnumFacing.NORTH=>Array(4)
      case EnumFacing.EAST=>Array(4)
      case EnumFacing.SOUTH=>Array(4)
      case EnumFacing.WEST=>Array(4)
      case _ => Array()
    }
  }

  override def canExtractItem(index: Int, stack: ItemStack, direction: EnumFacing): Boolean =true

  override def canInsertItem(index: Int, itemStackIn: ItemStack, direction: EnumFacing): Boolean = true

  override def clear(): Unit = {

    for (i <- 0 until this.inventory.length) {
      this.inventory(i) = null
    }


  }



  override def readExtraNBT(nbttagcompound: NBTTagCompound): Unit =
  {
    super.readExtraNBT(nbttagcompound)
    ForceEnergybuffer=nbttagcompound.getInteger("ForceEnergyBuffer")
    WorkEnergy=nbttagcompound.getInteger("WorkEnergy")
    WorkCycle=nbttagcompound.getInteger("WorkCycle")
    NBTUtils.inventoryFromNBT(inventory,nbttagcompound.getTagList("Items",Constants.NBT.TAG_COMPOUND))
  }
  override def readTransientNBT(nbttagcompound:NBTTagCompound)=
  {
    capacity=nbttagcompound.getInteger("Capacity")
    workdone=nbttagcompound.getInteger("WorkDone")
  }
  override def writeExtraNBT(nbttagcompound: NBTTagCompound): Unit =
  {
    super.writeExtraNBT(nbttagcompound)
    nbttagcompound.setInteger("ForceEnergyBuffer",ForceEnergybuffer)
    nbttagcompound.setInteger("WorkEnergy",WorkEnergy)
    nbttagcompound.setInteger("WorkCycle",WorkCycle)
    nbttagcompound.setTag("Items",NBTUtils.NBTFromInventory(inventory))
  }

  override def writeTransientNBT(nbttagcompound:NBTTagCompound)=
  {
    nbttagcompound.setInteger("Capacity",capacity)
    nbttagcompound.setInteger("WorkDone",workdone)
  }

  override def invalidate(): Unit =
  {
    LinkGrid.getWorldNet(worldObj).Extractors.remove(DeviceID)
    super.invalidate()
  }
}
