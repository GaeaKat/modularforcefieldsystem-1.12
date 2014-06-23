package com.minalien.mffs.blocks.machines

import net.minecraft.block.material.Material
import net.minecraft.block.Block
import com.minalien.mffs.core.MFFSCreativeTab
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraft.client.renderer.texture.IIconRegister

/**
 * Base class containing functionality common to all machines in MFFS.
 */
abstract class MachineBlock(blockName: String) extends Block(Material.iron) {
	setCreativeTab(MFFSCreativeTab)
	setHardness(5f)
	setResistance(15f)
	setBlockName(blockName)
	setHarvestLevel("pickaxe", 2)
	setBlockTextureName(s"mffs:$blockName")

	/**
	 * @return True. All Machines are backed by a TileEntity.
	 */
	override def hasTileEntity(metadata: Int) = true

	/**
	 * @return A new instance of the block's tile entity class.
	 */
	override def createTileEntity(world: World, metadata: Int) = getTileEntityClass.newInstance()

	/**
	 * @return TileEntity class associated with this Machine.
	 */
	def getTileEntityClass: Class[_ <: TileEntity] = null
}
