package com.minalien.mffs.utils

import net.minecraft.item.ItemStack
import net.minecraft.nbt.{NBTTagList, NBTTagCompound}
import net.minecraft.util.EnumFacing

/**
 * Created by Katrina on 13/01/2015.
 */
object NBTUtils {
  def getTAGfromItemstack(stack: ItemStack): NBTTagCompound =
  {
    if (stack != null) {
      var tag: NBTTagCompound = stack.getTagCompound
      if (tag == null) {
        tag = new NBTTagCompound
        stack.setTagCompound(tag)
      }
      return tag
    }
    return null
  }

  def facingToNBT(facing:EnumFacing,nbtTagCompound:NBTTagCompound)=
  {

     nbtTagCompound.setInteger("facing",EnumFacing.values().indexOf(facing))
  }

  def NBTtoFacing(nbtTagCompound:NBTTagCompound):EnumFacing=EnumFacing.values()(nbtTagCompound.getInteger("facing"))

  def inventoryFromNBT(inventory:Array[ItemStack],nbtTagList:NBTTagList)=
  {
    for(i <- 0 until nbtTagList.tagCount)
    {
      val cmp:NBTTagCompound=nbtTagList.getCompoundTagAt(i)
      val byte0:Byte=cmp.getByte("Slot")
      if(byte0>=0 && byte0 < inventory.length)
      {
        inventory(byte0)=ItemStack.loadItemStackFromNBT(cmp)
      }
    }
  }
  def NBTFromInventory(inventory:Array[ItemStack]):NBTTagList=
  {
    val tagList:NBTTagList=new NBTTagList
    for(i <- 0 until inventory.length)
    {
      if(inventory(i)!=null) {
        val cmp: NBTTagCompound = new NBTTagCompound
        cmp.setByte("Slot", i.toByte)
        inventory(i).writeToNBT(cmp)
        tagList.appendTag(cmp)
      }
    }
    tagList
  }
}
