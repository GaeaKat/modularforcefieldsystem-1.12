package com.minalien.core.nbt

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.item.ItemStack

/**
 * Collection of Utility methods for dealing with NBT Data.
 */
object NBTUtility {
	/**
	 * @param itemStack Item stack the NBT compound is being retrieved from.
	 *
	 * @return NBT Tag Compound for the item.
	 */
	def getTagCompoundFromItemStack(itemStack: ItemStack): NBTTagCompound = {
		if(!itemStack.hasTagCompound)
			itemStack.setTagCompound(new NBTTagCompound)

		itemStack.getTagCompound
	}

	/**
	 * Writes the specified 3-integer tuple into the NBTTagCompound.
	 *
	 * @param tuple         Tuple to be written.
	 * @param tagCompound   Target to be written
	 */
	def write3IntTupleToNBT(tuple: (Int, Int, Int), tagCompound: NBTTagCompound) {
		tagCompound.setInteger("x", tuple._1)
		tagCompound.setInteger("y", tuple._2)
		tagCompound.setInteger("z", tuple._3)
	}

	/**
	 * Reads a 3-integer tuple from an NBTTagCompound.
	 *
	 * @param tagCompound   Tag compound to be read from.
	 */
	def read3IntTupleFromNBT(tagCompound: NBTTagCompound): (Int, Int, Int) = {
		val x = tagCompound.getInteger("x")
		val y = tagCompound.getInteger("y")
		val z = tagCompound.getInteger("z")

		(x, y, z)
	}
}
