package com.minalien.mffs.items

import net.minecraft.item.Item
import com.minalien.mffs.core.MFFSCreativeTab
import net.minecraft.client.renderer.texture.IIconRegister

/**
 * Raw Forcicium, manufactured from Monazit Ore.
 */
object ItemForcicium extends Item {
	setCreativeTab(MFFSCreativeTab)
	setUnlocalizedName("forcicium")
	setTextureName("mffs:forcicium")
}
