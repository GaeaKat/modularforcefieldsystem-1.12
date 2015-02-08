package com.minalien.mffs.tiles

import java.util.Random

import com.minalien.mffs.api.Slots.Slots
import com.minalien.mffs.api.{BlockPoint, IMFFS_Wrench, ISwitchable}
import com.minalien.mffs.blocks.machines.BlockExtractor
import com.minalien.mffs.core.{LinkGrid, MFFSConfig, ModularForcefieldSystem}
import com.minalien.mffs.items.cards.{ItemCard, ItemCardEmpty}
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.{EntityPlayer, InventoryPlayer}
import net.minecraft.inventory.{Container, IInventory, ISidedInventory}
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.network.{NetworkManager, Packet}
import net.minecraft.server.gui.IUpdatePlayerListBox
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{BlockPos, EnumFacing}
import net.minecraft.world.{World, ChunkCoordIntPair}
import net.minecraftforge.common.ForgeChunkManager
import net.minecraftforge.common.ForgeChunkManager.Ticket

/**
 * Base Abstract Tile Entity for all MFFS machines
 * Created by Katrina on 13/12/2014.
 */
abstract class MFFSMachine extends TileEntity  with ISidedInventory with IMFFS_Wrench with ISwitchable with IUpdatePlayerListBox{
  private var Active: Boolean = false
  private var ticker: Int = 0
  protected var init: Boolean = true
  protected var DeviceName: String = "Please set Name!"
  protected var DeviceID: Int = 0
  protected var SwitchMode: Int = 0
  protected var SwitchValue: Boolean = false
  protected var random: Random = new Random
  protected var chunkTicket: ForgeChunkManager.Ticket = null
  //TODO: ADD in disabled
  def getPercentageCapacity: Int = 0

  def hasPowerSource: Boolean = false

  def getLinkedSecurityStation: MFFSMachine // TODO: Change to advanced security station tile

  def getmaxSwitchMode: Short = {
    return 0
  }

  def getminSwitchMode: Short = {
    return 0
  }

  def toogleSwitchMode {
    if (getSwitchMode == getmaxSwitchMode) {
      SwitchMode = getminSwitchMode
    }
    else {
      SwitchMode += 1
    }
    worldObj.markBlockForUpdate(pos)
  }

  def isRedstoneSignal: Boolean = {
    if (worldObj.getStrongPower(pos) > 0 || worldObj.isBlockIndirectlyGettingPowered(pos)>0) return true
    return false
  }


  override def update(): Unit =
  {
    if(!worldObj.isRemote)
      if(init)
        initialize()

    if(worldObj.isRemote)
      setTicker(getTicker + 1)
  }

  def initialize(): Unit =
  {
    //TODO : Set up link here
    DeviceID=LinkGrid.getWorldNet(worldObj).refreshID(this,DeviceID)
    if(MFFSConfig.Machines.chunkLoad)
      registerChunkLoading()
    this.init=false
  }
  def getSwitchMode: Int = {
    if (SwitchMode < getminSwitchMode) SwitchMode = getminSwitchMode
    return SwitchMode
  }

  def getSwitchValue: Boolean = {
    return SwitchValue
  }

  override def isSwitchable: Boolean = if(getSwitchMode==2) true else false

  override def toggleSwitchValue: Unit =
  {
    SwitchValue= !SwitchValue
    worldObj.markBlockForUpdate(pos)
  }

  def setDeviceName(DeviceName: String) {
    this.DeviceName = DeviceName
    worldObj.markBlockForUpdate(pos)
  }

  def getDeviceID: Int = DeviceID

  def setDeviceID(i: Int)  = this.DeviceID = i

  def getDeviceName: String = DeviceName

  def getMachinePoint: BlockPoint = new BlockPoint(pos, worldObj)

  def dropPlugins

  def dropPlugins(slot:Int,inventory:IInventory) {
    if(worldObj.isRemote)
    {
      this.setInventorySlotContents(slot,null)
      return
    }
    if(inventory.getStackInSlot(slot)!=null)
      inventory.getStackInSlot(slot).getItem match
      {
        case item:ItemCard => worldObj.spawnEntityInWorld(new EntityItem(worldObj,pos.getX,pos.getY,pos.getZ,new ItemStack(ItemCardEmpty)))
        case _ =>             worldObj.spawnEntityInWorld(new EntityItem(worldObj,pos.getX,pos.getY,pos.getZ,inventory.getStackInSlot(slot)))
      }

    inventory.setInventorySlotContents(slot,null)
    this.markDirty()
  }

  def getContainer(inventoryplayer: InventoryPlayer): Container


  override def wrenchCanManipulate(entityPlayer: EntityPlayer, facing: EnumFacing): Boolean =
  {
    return true; // TODO: Security Manager here plz
  }

  def getTicker: Integer = ticker

  def setTicker(ticker: Integer) = this.ticker = ticker


  def wrenchCanSetFacing(entityPlayer:EntityPlayer,facing:EnumFacing):Boolean=
  {
    if(facing==getSide())
      return false
    if(this.Active)
      return false
    wrenchCanManipulate(entityPlayer,facing)
  }

  override def shouldRefresh(world: World, pos: BlockPos, oldState: IBlockState, newSate: IBlockState): Boolean = false

  def activate(): Unit =
  {
    Active=true
    this.worldObj.setBlockState(pos,worldObj.getBlockState(pos).withProperty(BlockExtractor.ACTIVE,true))
    worldObj.markBlockForUpdate(pos)
  }
  def deactivate(): Unit =
  {
    Active=false
    this.worldObj.setBlockState(pos,worldObj.getBlockState(pos).withProperty(BlockExtractor.ACTIVE,false))
    worldObj.markBlockForUpdate(pos)
  }
  def isActive=Active
  def forceChunkLoading(ticket: Ticket): Unit =
  {
    if(chunkTicket==null)
      chunkTicket=ticket
    ForgeChunkManager.forceChunk(chunkTicket,new ChunkCoordIntPair(pos.getX >> 4,pos.getZ >> 4))
  }

  def registerChunkLoading() {
    if(chunkTicket==null)
    {
      chunkTicket==ForgeChunkManager.requestTicket(ModularForcefieldSystem,worldObj,ForgeChunkManager.Type.NORMAL)
    }
    if(chunkTicket==null)
    {
      ModularForcefieldSystem.logger.info("no Free chunkloaders available")

    }
    else
    {
      chunkTicket.getModData.setInteger("MachineX", pos.getX)
      chunkTicket.getModData.setInteger("MachineY", pos.getY)
      chunkTicket.getModData.setInteger("MachineZ", pos.getZ)
      ForgeChunkManager.forceChunk(chunkTicket,new ChunkCoordIntPair(pos.getX >> 4,pos.getZ >> 4))

      forceChunkLoading(chunkTicket)
    }
  }
  override def validate(): Unit =
  {
    //this.setSide(this.worldObj.getBlockState(pos).getValue(BlockExtractor.FACING).asInstanceOf[EnumFacing])
    super.validate()
  }

  override def invalidate() {
    ForgeChunkManager.releaseTicket(chunkTicket)
    super.invalidate()
  }


  override def openInventory(player: EntityPlayer): Unit =
  {

  }

  def isItemValid(par1ItemStack: ItemStack, Slot: Int): Boolean
  def getSlotStackLimit(slt: Int): Int
  override def closeInventory(player: EntityPlayer): Unit = {}

  override def getFieldCount: Int = 0

  override def getField(id: Int): Int = 0

  override def setField(id: Int, value: Int): Unit = {}

  def isUseableByPlayer(entityplayer: EntityPlayer): Boolean = if (worldObj.getTileEntity(pos) ne this)  false else entityplayer.getDistanceSq(pos) <= 4096D

  def getStackInSlotOnClosing(var1: Int): ItemStack = {
    return null
  }

  def getInventoryStackLimit: Int = {
    return 64
  }

  def countItemsInSlot(slt: Slots): Int = {
    if (this.getStackInSlot(slt.slot) != null) return this.getStackInSlot(slt.slot).stackSize
    return 0
  }


  override def readFromNBT(nbttagcompound: NBTTagCompound) {
    super.readFromNBT(nbttagcompound)
    readExtraNBT(nbttagcompound)
  }

  def readTransientNBT(nbttagcompount:NBTTagCompound): Unit =
  {

  }

  def readExtraNBT(nbttagcompound: NBTTagCompound) {
    Active = nbttagcompound.getBoolean("active")
    SwitchValue = nbttagcompound.getBoolean("SwitchValue")
    DeviceID = nbttagcompound.getInteger("DeviceID")
    DeviceName = nbttagcompound.getString("DeviceName")
    SwitchMode = nbttagcompound.getInteger("SwitchMode")
  }

  override def writeToNBT(nbttagcompound: NBTTagCompound) {
    super.writeToNBT(nbttagcompound)
    writeExtraNBT(nbttagcompound)
  }

  def writeExtraNBT(nbttagcompound: NBTTagCompound) {
    nbttagcompound.setInteger("SwitchMode", SwitchMode)
    nbttagcompound.setBoolean("active", Active)
    nbttagcompound.setBoolean("SwitchValue", SwitchValue)
    nbttagcompound.setInteger("DeviceID", DeviceID)
    nbttagcompound.setString("DeviceName", DeviceName)
  }

  def writeTransientNBT(nbttagcompount:NBTTagCompound): Unit =
  {

  }
  override def onDataPacket(net: NetworkManager, pkt: S35PacketUpdateTileEntity) {
    super.onDataPacket(net, pkt)
    readExtraNBT(pkt.getNbtCompound)
    readTransientNBT(pkt.getNbtCompound)
    worldObj.markBlockRangeForRenderUpdate(pos,pos)
  }

  override def getDescriptionPacket: Packet =
  {
    val nbttagcompound: NBTTagCompound = new NBTTagCompound
    this.writeExtraNBT(nbttagcompound)
    this.writeTransientNBT(nbttagcompound)
    return new S35PacketUpdateTileEntity(this.pos, 1, nbttagcompound)
  }
  override def getSide(): EnumFacing = worldObj.getBlockState(pos).getValue(BlockExtractor.FACING).asInstanceOf[EnumFacing]

  override def setSide(facing: EnumFacing): Unit = worldObj.setBlockState(pos,worldObj.getBlockState(pos).withProperty(BlockExtractor.FACING,facing))

}
