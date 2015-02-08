package com.minalien.mffs.utils


import com.minalien.mffs.items.cards.ItemCardEmpty
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ChatComponentText

/**
 * Created by Katrina on 25/01/2015.
 */
object Functions {

  def chatToPlayer(player:EntityPlayer,message:String, params: Object*) =
  {
    chatToPlayerUnlocalised(player,I18n.format(message,params: _*))
  }
  def chatToPlayerUnlocalised(player:EntityPlayer,message:String)=
  {
    player.addChatComponentMessage(new ChatComponentText(message))
  }

  def setIteminSlot(itemstack: ItemStack, entityplayer: EntityPlayer, tileEntity: TileEntity, Slot: Int, Cardname: String): Boolean = {
    if ((tileEntity.asInstanceOf[IInventory]).getStackInSlot(Slot) == null) {
      (tileEntity.asInstanceOf[IInventory]).setInventorySlotContents(Slot, itemstack)
      entityplayer.inventory.mainInventory(entityplayer.inventory.currentItem) = null
      Functions.chatToPlayer(entityplayer, "generic.card.installed", Cardname)
      (tileEntity.asInstanceOf[IInventory]).markDirty()
      return true
    }
    else {
      if ((tileEntity.asInstanceOf[IInventory]).getStackInSlot(Slot).getItem eq ItemCardEmpty) {
        val itemstackcopy: ItemStack = itemstack.copy
        (tileEntity.asInstanceOf[IInventory]).setInventorySlotContents(Slot, itemstackcopy)
        Functions.chatToPlayer(entityplayer, "generic.card.copied", Cardname)
        (tileEntity.asInstanceOf[IInventory]).markDirty()
        return true
      }
      Functions.chatToPlayer(entityplayer, "generic.card.fail")
      return false
    }
  }
}
