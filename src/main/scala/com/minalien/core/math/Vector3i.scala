package com.minalien.core.math

/**
 * Three-dimensional vector of Integers
 *
 * @param x X Coordinate
 * @param y Y Coordinate
 * @param z Z Coordinate
 */
class Vector3i(var x: Int, var y: Int, var z: Int) {
	/**
	 * Default Constructor
	 */
	def this() { this(0, 0, 0) }

	/**
	 * Creates an instance from an Int Tuple.
	 */
	def this(tuple: (Int, Int, Int)) { this(tuple._1, tuple._2, tuple._3) }

	/**
	 * @return A tuple representation of the vector's coordinates.
	 */
	def toTuple = (x, y, z)

	/**
	 * @return String representation of the vector.
	 */
	override def toString = s"(${x}i, ${y}i, ${z}i)"
}
