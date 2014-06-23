package com.minalien.mffs.blocks

import net.minecraft.block.material.Material
import net.minecraft.block.Block
import com.minalien.mffs.core.MFFSCreativeTab
import net.minecraft.client.renderer.texture.IIconRegister

/**
 * A block made purely of Forcicium, used for storage purposes.
 */
object BlockForciciumStorage extends Block(Material.rock) {
	setCreativeTab(MFFSCreativeTab)
	setHardness(3f)
	setResistance(5f)
	setBlockName("forciciumBlock")
	setHarvestLevel("pickaxe", 2)
	setBlockTextureName("mffs:forciciumBlock")
}
