package com.minalien.mffs.items.fieldshapes

import com.minalien.mffs.blocks.machines.BlockProjector
import com.minalien.mffs.core.MFFSCreativeTab
import com.minalien.mffs.machines.TileEntityProjector
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{ItemStack, Item}
import net.minecraft.world.World

/**
 * Field Shape module providing a Cube forcefield.
 */
object ItemFieldShapeCube extends Item with ForcefieldShape {
	setCreativeTab(MFFSCreativeTab)
	setUnlocalizedName("fieldShapeCube")
	setTextureName("mffs:fieldshapes/cube")

	override def onItemUse(itemStack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, nX: Float, nY: Float, nZ: Float): Boolean = {
		if(world.isRemote)
			return true

		val block = world.getBlock(x, y, z)

		if(block == BlockProjector) {
			val tile = world.getTileEntity(x, y, z).asInstanceOf[TileEntityProjector]

			if(tile.fieldShapeStack == null) {
				tile.fieldShapeStack = new ItemStack(ItemFieldShapeCube)

				itemStack.stackSize -= 1
			}
		}

		world.isRemote
	}

	/**
	 * @param radius Distance from (not including) the center block for the field on each axis.
	 *
	 * @return Array of 3-Int Tuples representing coordinates relative to the center of the shape where the Projector
	 *         should attempt to place forcefield blocks.
	 */
	def getRelativeCoords(radius: (Int, Int, Int)): Array[(Int, Int, Int)] = {
		val radiusX = radius._1
		val radiusY = radius._2
		val radiusZ = radius._3

		// Calculate the size of the array needed for the coords.
		val coordList = new collection.mutable.ListBuffer[(Int, Int, Int)]

		for(y <- -radiusY to radiusY) {
			for(z <- -radiusZ to radiusZ) {
				coordList.append((radiusX, y, z))
				coordList.append((-radiusX, y, z))
			}
		}

		for(x <- -radiusX to radiusX) {
			for(z <- -radiusZ to radiusZ) {
				coordList.append((x, radiusY, z))
				coordList.append((x, -radiusY, z))
			}
		}

		for(x <- -radiusX to radiusX) {
			for(y <- -radiusY to radiusY) {
				coordList.append((x, y, radiusZ))
				coordList.append((x, y, -radiusZ))
			}
		}

		coordList.toArray
	}
}
