package com.minalien.mffs.core

import com.minalien.mffs.items.ItemForcium
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item

/**
 * Created by Katrina on 13/12/2014.
 */
object MFFSCreativeTab extends CreativeTabs("MFFS") {
  override def getTabIconItem: Item = ItemForcium
}
