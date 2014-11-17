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
	setMaxStackSize(1)

	/**
	 * @param radius Distance from (not including) the center block for the field on each axis.
	 *
	 * @return Array of 3-Int Tuples representing coordinates relative to the center of the shape where the Projector
	 *         should attempt to place forcefield blocks.
	 */
	def getRelativeCoords(radius: (Int, Int, Int)) = {
		val radiusX = radius._1
		val radiusY = radius._2
		val radiusZ = radius._3

		val coordList = new collection.mutable.ListBuffer[(Int, Int, Int)]

		for(y <- -radiusY to radiusY ; z <- -radiusZ to radiusZ) {
				coordList.append((radiusX, y, z))
				coordList.append((-radiusX, y, z))
		}

		for(x <- -radiusX to radiusX ; z <- -radiusZ to radiusZ) {
				coordList.append((x, radiusY, z))
				coordList.append((x, -radiusY, z))
		}

		for(x <- -radiusX to radiusX ; y <- -radiusY to radiusY) {
				coordList.append((x, y, radiusZ))
				coordList.append((x, y, -radiusZ))
		}

		coordList.toArray
	}

	/**
	 * @param radius Distance from (not including) the center block for the field on each axis.
	 *
	 * @return Array of 3-Int Tuples representing coordinates relative to the center of the shape where the Projector
	 *         should attempt to perform any "internal" field checks.
	 */
	def getRelativeInternalCoords(radius: (Int, Int, Int)) = {
		val radiusX = math.max(radius._1 - 1, 0)
		val radiusY = math.max(radius._2 - 1, 0)
		val radiusZ = math.max(radius._3 - 1, 0)

		val coordList = new collection.mutable.ListBuffer[(Int, Int, Int)]

		for(x <- -radiusX to radiusX ; y <- -radiusY to radiusY ; z <- -radiusZ to radiusZ)
					coordList.append((x, y, z))

		coordList.toArray
	}
}
