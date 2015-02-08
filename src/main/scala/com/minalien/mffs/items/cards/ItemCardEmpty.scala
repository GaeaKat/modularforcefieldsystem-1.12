package com.minalien.mffs.items.cards

import com.minalien.mffs.api.{BlockPoint, EnumCardType}
import com.minalien.mffs.api.EnumCardType.EnumCardType
import com.minalien.mffs.core.MFFSCreativeTab
import com.minalien.mffs.tiles.TileEntityCapacitor
import com.minalien.mffs.utils.Functions
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{EnumFacing, BlockPos}
import net.minecraft.world.World

/**
 * Created by Katrina on 12/01/2015.
 */
object ItemCardEmpty extends ItemCard{
  override def getCardType(): EnumCardType = EnumCardType.BlankCard
  this.setCreativeTab(MFFSCreativeTab)

  setMaxStackSize(16)

  setNoRepair()

  override def onItemUse(stack: ItemStack, playerIn: EntityPlayer, worldIn: World, pos: BlockPos, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean =
  {

    if(worldIn.isRemote)
      return false

    val tileEntity:TileEntity=worldIn.getTileEntity(pos)

    // TODO : Security station!

    tileEntity match
    {
      case machine:TileEntityCapacitor => {
        val newCard:ItemStack=new ItemStack(ItemPowerLinkCard)
        ItemPowerLinkCard.setInformation(newCard,new BlockPoint(pos,worldIn),"CapacitorID",machine.getPowerStorageID)

        stack.stackSize=stack.stackSize-1
        if(stack.stackSize<=0)
        {
          playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem,newCard)
        } else if(!playerIn.inventory.addItemStackToInventory(newCard))
          playerIn.dropItem(newCard,true,true)

        playerIn.inventory.markDirty()

        Functions.chatToPlayer(playerIn,"capacitor.cardCreated")
        true
      }
      case _ => false
    }
  }
}
