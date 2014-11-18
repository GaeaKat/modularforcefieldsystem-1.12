package com.minalien.mffs.blocks.machines

import com.minalien.mffs.core.MFFSCreativeTab
import com.minalien.mffs.machines.MFFSMachine
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon
import net.minecraft.world.{IBlockAccess, World}

/**
 * Base class containing functionality common to all machines in MFFS.
 */
abstract class MachineBlock(blockName: String) extends Block(Material.iron) {
	setCreativeTab(MFFSCreativeTab)
	setHardness(5f)
	setResistance(15f)
	setBlockName(blockName)
	setHarvestLevel("pickaxe", 2)
	setBlockTextureName(s"mffs:$blockName" + "Inactive")
	val blockIcons: Array[IIcon] = new Array[IIcon](2)
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

	@SideOnly(Side.CLIENT)
	override def getIcon(world: IBlockAccess, x: Int, y: Int, z: Int, side: Int): IIcon = {
		val tileEntity = world.getTileEntity(x, y, z).asInstanceOf[MFFSMachine]
		if (tileEntity.isActive)
			blockIcons(1)
		else
			blockIcons(0)

	}

	@SideOnly(Side.CLIENT)
	override def getIcon(p_149691_1_ : Int, p_149691_2_ : Int): IIcon = blockIcons(1)


	@SideOnly(Side.CLIENT)
	override def registerBlockIcons(register: IIconRegister): Unit = {

		blockIcons(0) = register.registerIcon(s"mffs:$blockName" + "Inactive")
		blockIcons(1) = register.registerIcon(s"mffs:$blockName" + "Active")
	}
}
