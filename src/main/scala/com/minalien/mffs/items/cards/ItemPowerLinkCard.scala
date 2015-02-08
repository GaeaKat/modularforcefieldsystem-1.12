package com.minalien.mffs.items.cards



import com.minalien.mffs.api.EnumCardType.EnumCardType
import com.minalien.mffs.api.{IPowerLinkItem, EnumCardType, IForceStorageEnergyBlock}
import com.minalien.mffs.core.LinkGrid
import com.minalien.mffs.tiles.{MFFSMachine, TileEntityExtractor, TileEntityCapacitor}
import com.minalien.mffs.utils.Functions
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{EnumFacing, BlockPos}
import net.minecraft.world.World

/**
 * Created by Katrina on 25/01/2015.
 */
object ItemPowerLinkCard extends ItemCard with IPowerLinkItem{
  var storage: IForceStorageEnergyBlock = null

  override def getCardType(): EnumCardType = EnumCardType.PowerLinkCard

  override def onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity, itemSlot: Int, isSelected: Boolean): Unit = {
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected)
    if (Tick > 600) {
      val capID = this.getValuefromKey("CapacitorID", stack)
      if (capID != 0) {
        val cap = LinkGrid.getWorldNet(worldIn).Capacitors.lift(capID)
        if(!cap.isEmpty) {
          cap match {
            case Some(capacitor) => {
              if (!capacitor.getDeviceName.equals(this.getForArea(stack)))
                this.setForArea(stack, capacitor.getDeviceName)
            }
          }
        }

      }
      Tick = 0

    }
    Tick = Tick + 1
  }

  override def onItemUseFirst(stack: ItemStack, player: EntityPlayer, world: World, pos: BlockPos, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
    val tileEntity: TileEntity = world.getTileEntity(pos)
    // TODO: Give this section security!
    if (!world.isRemote) {
      tileEntity match {
        case tile: TileEntityCapacitor => {
          Functions.setIteminSlot(stack, player, tile, 2, "<Power-Link>")
        }
        case tile: TileEntityExtractor => {
          if ((tileEntity.asInstanceOf[TileEntityExtractor]).getStackInSlot(1) == null) {
            (tileEntity.asInstanceOf[TileEntityExtractor]).setInventorySlotContents(1, stack)
            player.inventory.mainInventory(player.inventory.currentItem) = null
            Functions.chatToPlayer(player, "linkCard.installed")
            true
          } else {
            if ((tileEntity.asInstanceOf[TileEntityExtractor]).getStackInSlot(1).getItem eq ItemCardEmpty) {
              val itemstackcopy: ItemStack = stack.copy
              (tileEntity.asInstanceOf[TileEntityExtractor]).setInventorySlotContents(1, itemstackcopy)
              Functions.chatToPlayer(player, "linkCard.copied")
              true
            }
            else {

              Functions.chatToPlayer(player, "linkCard.notEmpty")
              false
            }
          }
        }
        case _ => false
      }
    }
    else
      false

  }


  def getForceEnergyStorageBlock(itemStack: ItemStack, tem: MFFSMachine, world: World): IForceStorageEnergyBlock = {
    if (itemStack != null && itemStack.getItem.isInstanceOf[ItemCard]) {
      if (itemStack.getItem.asInstanceOf[ItemCard].isValid(itemStack)) {
        val png = this.getCardTargetPoint(itemStack)
        if (png != null) {
          if (png.dimID != world.provider.getDimensionId)
            return null
          if (world.getTileEntity(png.pos).isInstanceOf[TileEntityCapacitor]) {
            val cap = world.getTileEntity(png.pos).asInstanceOf[TileEntityCapacitor]
            if (cap != null) {
              if (cap.getPowerStorageID == this.getValuefromKey("CapacitorID", itemStack) && this.getValuefromKey("CapacitorID", itemStack) != 0) {
                if (!cap.getDeviceName.equals(this.getForArea(itemStack)))
                  this.setForArea(itemStack, cap.getDeviceName)

                if ((cap.getTransmitRange * cap.getTransmitRange) >= tem.getMachinePoint.distanceSq(cap.getMachinePoint))
                  return cap
              }
              return null
            }
          }
          else {
            val CapID = this.getValuefromKey("CapacitorID", itemStack)
            if (CapID != 0) {
              val cap = LinkGrid.getWorldNet(world).Capacitors.lift(CapID)
              cap match {
                case Some(capacitor) => {
                  this.setInformation(itemStack, capacitor.getMachinePoint, "CapacitorID", CapID)
                  if ((capacitor.getTransmitRange*capacitor.getTransmitRange) >= tem.getMachinePoint.distanceSq(capacitor.getMachinePoint))
                    return capacitor
                  return null
                }
              }
            }
          }
          if (world.getChunkFromBlockCoords(png.pos).isLoaded)
            this.setInvalid(itemStack)

        }
      }
    }
    return null
  }

  override def getPercentageCapacity(itemStack: ItemStack, pos: BlockPos, world: World): Int =
  {
    storage=getForceEnergyStorageBlock(itemStack,world.getTileEntity(pos).asInstanceOf[MFFSMachine],world)
    if(storage!=null)
      storage.getPercentageStorageCapacity
    else
      0
  }

  override def insertPower(itemStack: ItemStack, powerAmount: Int, simulation: Boolean, pos: BlockPos, world: World): Boolean =
  {
    storage=getForceEnergyStorageBlock(itemStack,world.getTileEntity(pos).asInstanceOf[MFFSMachine],world)
    if(storage!=null)
      storage.insertPowertoStorage(powerAmount,simulation)
    else
      false
  }

  override def getAvailablePower(itemStack: ItemStack, pos: BlockPos, world: World): Int =
  {
    storage=getForceEnergyStorageBlock(itemStack,world.getTileEntity(pos).asInstanceOf[MFFSMachine],world)
    if(storage!=null)
      storage.getStorageAvailablePower
    else
      0
  }

  override def consumePower(itemStack: ItemStack, powerAmount: Int, simulation: Boolean, pos: BlockPos, world: World): Boolean =
  {
    storage=getForceEnergyStorageBlock(itemStack,world.getTileEntity(pos).asInstanceOf[MFFSMachine],world)
    if(storage!=null)
      storage.consumePowerfromStorage(powerAmount,simulation)
    else
      false
  }

  override def getfreeStorageAmount(itemStack: ItemStack, pos: BlockPos, world: World): Int =

  {
    storage=getForceEnergyStorageBlock(itemStack,world.getTileEntity(pos).asInstanceOf[MFFSMachine],world)
    if(storage!=null)
      storage.getfreeStorageAmount
    else
      0
  }

  override def getPowersourceID(itemStack: ItemStack, pos: BlockPos, world: World): Int =
  {
    storage=getForceEnergyStorageBlock(itemStack,world.getTileEntity(pos).asInstanceOf[MFFSMachine],world)
    if(storage!=null)
      storage.getPowerStorageID
    else
      0
  }

  override def isPowersourceItem: Boolean = false

  override def getMaximumPower(itemStack: ItemStack, pos: BlockPos, world: World): Int =
  {
    storage=getForceEnergyStorageBlock(itemStack,world.getTileEntity(pos).asInstanceOf[MFFSMachine],world)
    if(storage!=null)
      storage.getStorageMaxPower
    else
      1
  }
}
