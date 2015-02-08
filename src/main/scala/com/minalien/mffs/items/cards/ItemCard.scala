package com.minalien.mffs.items.cards


import java.util

import com.minalien.mffs.api.BlockPoint
import com.minalien.mffs.api.EnumCardType.EnumCardType
import com.minalien.mffs.utils.NBTUtils
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.DimensionManager

/**
 * Created by Katrina on 10/01/2015.
 */
abstract class ItemCard extends Item{
    def getCardType():EnumCardType
    setMaxStackSize(1)
    var Tick=0
    setNoRepair()

    def setForArea(itemStack:ItemStack,areaName:String)=
    {
        val nbtTagCompound:NBTTagCompound=NBTUtils.getTAGfromItemstack(itemStack)
        nbtTagCompound.setString("Areaname",areaName)
    }

    def getForArea(itemStack:ItemStack):String =
    {
        val nbtTagCompound:NBTTagCompound=NBTUtils.getTAGfromItemstack(itemStack)
        if(nbtTagCompound!=null)
            nbtTagCompound.getString("Areaname")
        else
            "not set"
    }

    def isValid(itemStack: ItemStack): Boolean = {
        val tag: NBTTagCompound = NBTUtils.getTAGfromItemstack(itemStack)
        if (tag.hasKey("valid"))
            tag.getBoolean("valid")
        else
            false
    }

    def setInvalid(itemStack: ItemStack) {
        val nbtTagCompound: NBTTagCompound =NBTUtils.getTAGfromItemstack(itemStack)
        nbtTagCompound.setBoolean("valid", false)
    }

    def setInformation(itemStack: ItemStack, png: BlockPoint, key: String, value: Int) {
        val nbtTagCompound: NBTTagCompound = NBTUtils.getTAGfromItemstack(itemStack)
        nbtTagCompound.setInteger(key, value)
        nbtTagCompound.setString("worldname", DimensionManager.getWorld(png.dimID).getWorldInfo.getWorldName)
        val cmp:NBTTagCompound=new NBTTagCompound()
        png.writeNBT(cmp)
        nbtTagCompound.setTag("linkTarget", cmp)
        nbtTagCompound.setBoolean("valid", true)
    }

    def getValuefromKey(key: String, itemStack: ItemStack): Int = {
        val tag: NBTTagCompound = NBTUtils.getTAGfromItemstack(itemStack)
        if (tag.hasKey(key))
            tag.getInteger(key)
        else
            0
    }

    def getCardTargetPoint(itemStack: ItemStack): BlockPoint = {
        val tag: NBTTagCompound = NBTUtils.getTAGfromItemstack(itemStack)
        if (tag.hasKey("linkTarget")) {
            new BlockPoint(tag.getCompoundTag("linkTarget"))
        }
        else {
            tag.setBoolean("valid", false)
            null
        }
    }

    override def addInformation(stack: ItemStack, playerIn: EntityPlayer, tooltip: util.List[_], advanced: Boolean): Unit = {
        val tag: NBTTagCompound = NBTUtils.getTAGfromItemstack(stack)
        tooltip.asInstanceOf[java.util.List[String]].add("Links To: " + getForArea(stack))
        if (tag.hasKey("worldname")) tooltip.asInstanceOf[java.util.List[String]].add("World: " + tag.getString("worldname"))
        if (tag.hasKey("linkTarget")) tooltip.asInstanceOf[java.util.List[String]].add("Coords: " + new BlockPoint(tag.getCompoundTag("linkTarget")).toString)
        if (tag.hasKey("valid")) tooltip.asInstanceOf[java.util.List[String]].add(if ((tag.getBoolean("valid"))) "\u00a72Valid" else "\u00a74Invalid")
    }
}
