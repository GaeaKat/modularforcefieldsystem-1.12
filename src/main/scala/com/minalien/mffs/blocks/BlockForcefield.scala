package com.minalien.mffs.blocks

import java.util.Random

import com.minalien.mffs.core.MFFSCreativeTab
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.Entity
import net.minecraft.util.Facing
import net.minecraft.world.IBlockAccess

/**
 * Forcefield Block
 */
object BlockForcefield extends Block(Material.glass) {
	setCreativeTab(MFFSCreativeTab)
	setBlockName("forcefield")
	setResistance(10000)
	setBlockUnbreakable()
	setBlockTextureName("mffs:fieldArea")

	/**
	 * @return Forcefields are not opaque.
	 */
	override def isOpaqueCube = false

	/**
	 * @return False, so that light will correctly pass through this block.
	 */
	override def renderAsNormalBlock = false

	/**
	 * @return Forcefield blocks can never be destroyed, by anything.
	 */
	override def canEntityDestroy(world: IBlockAccess, x: Int, y: Int, z: Int, entity: Entity) = false

	/**
	 * @return  Forcefield blocks never drop anything.
	 */
	override def getItemDropped(a: Int, b: Random, c: Int) = null

	/**
	 * Prevents Forcefield blocks from rendering sides that are right next to each other.
	 *
	 * @param world World in which the blocks are placed.
	 * @param x     Block X Coordinate.
	 * @param y     Block Y Coordinate.
	 * @param z     Block Z Coordinate.
	 * @param side  Side of the block being checked.
	 *
	 * @return  Whether or not the individual side should be rendered.
	 */
	override def shouldSideBeRendered(world: IBlockAccess, x: Int, y: Int, z: Int, side: Int): Boolean = {
		val block = world.getBlock(x, y, z)

		if(world.getBlockMetadata(x, y, z) != world.getBlockMetadata(x - Facing.offsetsXForSide(side), y - Facing.offsetsYForSide(side), z - Facing.offsetsZForSide(side)))
			return true

		if(block == this)
			return false

		super.shouldSideBeRendered(world, x, y, z, side)
	}
}
