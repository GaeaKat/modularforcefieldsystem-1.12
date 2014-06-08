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
}
