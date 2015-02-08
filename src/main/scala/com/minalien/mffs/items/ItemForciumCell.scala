package com.minalien.mffs.items

import java.util
import com.minalien.mffs.items.ItemMFFSBase
import com.minalien.mffs.utils.NBTUtils
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Slot
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ChatComponentTranslation
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.collection.JavaConversions._
/**
 * Created by Katrina on 14/01/2015.
 */
object ItemForciumCell extends ItemMFFSBase{

  var active:Boolean=false
  setMaxStackSize(1)
  setMaxDamage(100)
  setNoRepair()




  def getItemDamage(itemstack:ItemStack)={
    101-((getForceciumlevel(itemstack)*100)/getMaxForceciumlevel)
  }

  override def onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity, itemSlot: Int, isSelected: Boolean): Unit = {
    var found:Boolean=false
    if(worldIn.isRemote==false)
    {
      if(active)
      {
          if(getForceciumlevel(stack)<getMaxForceciumlevel)
          {
              entityIn match
              {
                case player:EntityPlayer =>
                {
                  val slots:List[Slot]=player.inventoryContainer.inventorySlots.toList.asInstanceOf[List[Slot]]
                  for(slot:Slot <-slots)
                  {
                    if(slot.getStack!=null && !found)
                    {
                      slot.getStack.getItem match
                      {
                        case ItemForcium =>
                        {
                          setForceciumlevel(stack,getForceciumlevel(stack)+1)

                          if(slot.getStack.stackSize>1) {
                            val forcium: ItemStack = new ItemStack(ItemForcium, slot.getStack.stackSize - 1)
                            slot.putStack(forcium)
                          }
                          else
                            slot.putStack(null)
                          found=true
                        }
                      }
                    }
                  }
                }
              }
          }
        stack.setItemDamage(getItemDamage(stack))
      }
    }

  }


  override def addInformation(stack: ItemStack, playerIn: EntityPlayer, tooltip: util.List[_], advanced: Boolean): Unit =
  {
    super.addInformation(stack, playerIn, tooltip, advanced)
    val forcium=getForceciumlevel(stack)
    val tooltipadd=f"$forcium%d/$getMaxForceciumlevel%d"
    tooltip.asInstanceOf[java.util.List[String]].add(tooltipadd)

  }

  def useForceium(count:Int,stack:ItemStack)={
    if(count>getForceciumlevel(stack))
      false
    else
    {
      setForceciumlevel(stack,getForceciumlevel(stack)-count)
      true
    }
  }


  override def onItemRightClick(itemStackIn: ItemStack, worldIn: World, playerIn: EntityPlayer): ItemStack =
  {
    if(worldIn.isRemote==false)
    {
      if(!active) {
        active = true
        playerIn.addChatComponentMessage(new ChatComponentTranslation("mffs.forciciumCellActive"))
      }
      else
      {
        active = false
        playerIn.addChatComponentMessage(new ChatComponentTranslation("mffs.forciciumCellInactive"))
      }
    }
    itemStackIn
  }

  def getMaxForceciumlevel: Int = {
    return 1000
  }

  @SideOnly(Side.CLIENT)
  override def getSubItems(itemIn: Item, tab: CreativeTabs, subItems: util.List[_]): Unit =
  {
    val charged:ItemStack=new ItemStack(this,1)
    charged.setItemDamage(1)
    setForceciumlevel(charged,getMaxForceciumlevel)
    subItems.asInstanceOf[java.util.List[ItemStack]].add(charged)

    val empty:ItemStack=new ItemStack(this,1)
    empty.setItemDamage(100)
    setForceciumlevel(empty,getMaxForceciumlevel)
    subItems.asInstanceOf[java.util.List[ItemStack]].add(empty)


  }

  def setForceciumlevel(itemStack: ItemStack, Forceciumlevel: Int) {
    val nbtTagCompound: NBTTagCompound = NBTUtils.getTAGfromItemstack(itemStack)
    nbtTagCompound.setInteger("Forceciumlevel", Forceciumlevel)
  }

  def getForceciumlevel(itemstack: ItemStack): Int = {
    val nbtTagCompound: NBTTagCompound = NBTUtils.getTAGfromItemstack(itemstack)
    if (nbtTagCompound != null) {
      return nbtTagCompound.getInteger("Forceciumlevel")
    }
    return 0
  }

}
