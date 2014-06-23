package com.minalien.mffs.items.fieldshapes

import com.minalien.mffs.core.MFFSCreativeTab
import com.minalien.mffs.items.upgrades.ItemUpgrade

/**
 * Field Shape module providing a Cube forcefield.
 */
object ItemFieldShapeCube extends ItemUpgrade with ForcefieldShape {
	setCreativeTab(MFFSCreativeTab)
	setUnlocalizedName("fieldShapeCube")
	setTextureName("mffs:fieldshapes/cube")

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
