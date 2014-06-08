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

	/**
	 * Loads icons related to the item.
	 *
	 * @param iconRegister Icon Register used to load textures for stitching.
	 */
	override def registerIcons(iconRegister: IIconRegister) {
		itemIcon = iconRegister.registerIcon("mffs:forcicium")
	}
}
