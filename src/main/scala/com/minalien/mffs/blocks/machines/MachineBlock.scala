package com.minalien.mffs.blocks.machines

import com.minalien.mffs.core.MFFSCreativeTab
import com.minalien.mffs.machines.MFFSMachine
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.world.World

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
	def getTileEntityClass: Class[_ <: MFFSMachine] = null

	/**
	 * Ensures that all upgrades are dropped from the machine.
	 *
	 * @param world     World the block was placed in.
	 * @param x         X Coordinate of the block.
	 * @param y         Y Coordinate of the block.
	 * @param z         Z Coordinate of the block.
	 * @param block     Block that was broken.
	 * @param unknown   Unknown.
	 */
	override def breakBlock(world: World, x: Int, y: Int, z: Int, block: Block, unknown: Int) {
		world.getTileEntity(x, y, z) match {
			case machine: MFFSMachine =>
				machine.dropUpgrades()

			case _ =>
		}

		super.breakBlock(world, x, y, z, block, unknown)
	}
}
