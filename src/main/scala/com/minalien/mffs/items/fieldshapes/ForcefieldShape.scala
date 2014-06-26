package com.minalien.mffs.items.fieldshapes

/**
 * Any item implementing this trait will be able to specify a shape for a Forcefield.
 */
trait ForcefieldShape {
	/**
	 * @param radius Distance from (not including) the center block for the field on each axis.
	 *
	 * @return Array of 3-Int Tuples representing coordinates relative to the center of the shape where the Projector
	 *         should attempt to place forcefield blocks.
	 */
	def getRelativeCoords(radius: (Int, Int, Int)): Array[(Int, Int, Int)]

	/**
	 * @param radius Distance from (not including) the center block for the field on each axis.
	 *
	 * @return Array of 3-Int Tuples representing coordinates relative to the center of the shape where the Projector
	 *         should attempt to perform any "internal" field checks.
	 */
	def getRelativeInternalCoords(radius: (Int, Int, Int)): Array[(Int, Int, Int)]
}
