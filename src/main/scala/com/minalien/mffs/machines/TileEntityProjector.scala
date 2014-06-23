package com.minalien.mffs.machines

import com.minalien.core.nbt.NBTUtility
import com.minalien.mffs.blocks.BlockForcefield
import net.minecraft.init.Blocks
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.IFluidBlock

/**
 * Forcefield Projector.
 */
class TileEntityProjector extends MFFSMachine {
	/**
	 * ArrayBuffer containing a list of tuples representing the tile coordinates for each forcefield block owned by the Projector.
	 */
	val fieldBlockCoords = new collection.mutable.ArrayBuffer[(Int, Int, Int)]

	/**
	 * A Tuple representing the offset of the field on each axis.
	 */
	var fieldOffset = (0, 0, 0)

	/**
	 * A Tuple representing the radius of the field on each axis.
	 */
	var fieldRadius = (24, 24, 24)

	/**
	 * Whether or not the projector is operating in "Break" mode, in which case it will all blocks instead of only replacing air blocks
	 * when generating the field.
	 */
	var isInBreakMode = false

	/**
	 * Whether or not the project or is operating in "Sponge" mode, in which case it will destroy any fluid blocks that are in the way of
	 * any possible new Field blocks.
	 */
	var isInSpongeMode = false

	/**
	 * Creates the forcefield cube, adding the coordinates of any placed BlockForcefield instances to fieldBlockCoords.
	 */
	def activate() {
		if(isActive)
			return

		state = MachineState.Active

		val offsetX = fieldOffset._1
		val offsetY = fieldOffset._2
		val offsetZ = fieldOffset._3

		val radiusX = fieldRadius._1
		val radiusY = fieldRadius._2
		val radiusZ = fieldRadius._3

		def setFieldBlock(x: Int, y: Int, z: Int) {
			val blockX = offsetX + x
			val blockY = offsetY + y
			val blockZ = offsetZ + z

			val block = worldObj.getBlock(blockX, blockY, blockZ)
			val isFluid = block.isInstanceOf[IFluidBlock] || block == Blocks.water || block == Blocks.lava || block == Blocks.flowing_water || block == Blocks.flowing_lava

			if(block.isAir(worldObj, blockX, blockY, blockZ) ||
					(isInBreakMode && block.getBlockHardness(worldObj, blockX, blockY, blockZ) != -1) ||
					(isInSpongeMode && isFluid)) {
				worldObj.setBlock(blockX, blockY, blockZ, BlockForcefield, 0, 2)
				fieldBlockCoords.append((blockX, blockY, blockZ))
			}
		}

		for(y <- -radiusY to radiusY) {
			for(z <- -radiusZ to radiusZ) {
				def makeXWall(blockX: Int) {
					val blockY = yCoord + y
					val blockZ = zCoord + z
					setFieldBlock(blockX, blockY, blockZ)
				}

				makeXWall(xCoord + radiusX)
				makeXWall(xCoord - radiusX)
			}
		}

		for(x <- -radiusX to radiusX) {
			for(z <- -radiusZ to radiusZ) {
				def makeYWall(blockY: Int) {
					val blockX = xCoord + x
					val blockZ = zCoord + z
					setFieldBlock(blockX, blockY, blockZ)
				}

				makeYWall(yCoord + radiusY)
				makeYWall(yCoord - radiusY)
			}
		}

		for(x <- -radiusX to radiusX) {
			for(y <- -radiusY to radiusY) {
				def makeZWall(blockZ: Int) {
					val blockX = xCoord + x
					val blockY = yCoord + y
					setFieldBlock(blockX, blockY, blockZ)
				}

				makeZWall(zCoord + radiusZ)
				makeZWall(zCoord - radiusZ)
			}
		}
	}

	/**
	 * Iterates over stored block coords and destroys the block if it is a BlockForcefield.
	 */
	def deactivate() {
		if(!isActive)
			return

		state = MachineState.Inactive

		for(coord <- fieldBlockCoords)
			if(worldObj.getBlock(coord._1, coord._2, coord._3) == BlockForcefield)
				worldObj.setBlockToAir(coord._1, coord._2, coord._3)

		fieldBlockCoords.clear()
	}

	/**
	 * Saves the positions of all owned forcefield blocks.
	 *
	 * @param tagCompound   NBTTagCompound that tile data is being written to.
	 */
	override def writeToNBT(tagCompound: NBTTagCompound) {
		super.writeToNBT(tagCompound)

		// Write the Offset to the Tag Compound
		val offsetTag = new NBTTagCompound
		NBTUtility.write3IntTupleToNBT(fieldOffset, offsetTag)
		tagCompound.setTag("fieldOffset", offsetTag)

		// Write the Radius to the Tag Compound
		val radiusTag = new NBTTagCompound
		NBTUtility.write3IntTupleToNBT(fieldRadius, radiusTag)
		tagCompound.setTag("fieldRadius", radiusTag)

		if(isActive) {
			val coordListTag = new NBTTagCompound

			coordListTag.setInteger("size", fieldBlockCoords.size)
			var idx = 0

			for(coord <- fieldBlockCoords) {
				val coordTag = new NBTTagCompound
				NBTUtility.write3IntTupleToNBT(coord, coordTag)
				coordListTag.setTag(s"tile$idx", coordTag)
				idx += 1
			}

			tagCompound.setTag("tileCoordList", coordListTag)
		}
	}

	/**
	 * Loads the activity state and (if active) positions of all owned forcefield blocks.
	 *
	 * @param tagCompound NBTTagCompound that tile data is being read from.
	 */
	override def readFromNBT(tagCompound: NBTTagCompound) {
		super.readFromNBT(tagCompound)

		fieldOffset = NBTUtility.read3IntTupleFromNBT(tagCompound.getCompoundTag("fieldOffset"))
		fieldRadius = NBTUtility.read3IntTupleFromNBT(tagCompound.getCompoundTag("fieldRadius"))

		val coordListTag: NBTTagCompound = if(isActive) tagCompound.getCompoundTag("tileCoordList") else null

		if(coordListTag != null) {
			val size = coordListTag.getInteger("size")

			for(idx <- 0 until size)
				fieldBlockCoords.append(NBTUtility.read3IntTupleFromNBT(coordListTag.getCompoundTag(s"tile$idx")))
		}
	}
}
