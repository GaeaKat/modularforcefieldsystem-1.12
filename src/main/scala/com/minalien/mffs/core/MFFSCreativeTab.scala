package com.minalien.mffs.core

import net.minecraft.creativetab.CreativeTabs
import com.minalien.mffs.items.ItemForcicium

/**
 * Creative Tab containing all MFFS Blocks & Items.
 */
object MFFSCreativeTab extends CreativeTabs("MFFS") {
	/**
	 * @return Item instance whose texture will be used for the Creative Tab.
	 */
	override def getTabIconItem = ItemForcicium
}
