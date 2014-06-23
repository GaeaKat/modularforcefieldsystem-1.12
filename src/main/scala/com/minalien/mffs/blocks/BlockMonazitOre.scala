package com.minalien.mffs.blocks

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import com.minalien.mffs.core.MFFSCreativeTab
import net.minecraft.client.renderer.texture.IIconRegister

/**
 * Monazit Ore Block
 */
object BlockMonazitOre extends Block(Material.rock) {
	setCreativeTab(MFFSCreativeTab)
	setHardness(3f)
	setResistance(5f)
	setBlockName("monazitOre")
	setHarvestLevel("pickaxe", 2)
	setBlockTextureName("mffs:monazitOre")
}
