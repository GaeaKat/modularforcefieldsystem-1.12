package com.minalien.mffs.machines

import com.minalien.core.nbt.NBTUtility
import com.minalien.mffs.blocks.BlockForcefield
import com.minalien.mffs.items.fieldshapes.ForcefieldShape
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.IFluidBlock

import scala.collection.mutable.ArrayBuffer

/**
 * Forcefield Projector.
 */
class TileEntityProjector extends MFFSMachine(4) {
	/**
	 * ArrayBuffer containing a list of tuples representing the tile coordinates for each forcefield block owned by the Projector.
	 */
	val fieldBlockCoords = new collection.mutable.ArrayBuffer[(Int, Int, Int)]

	/**
	 * ItemStack providing the Field Shape.
	 */
	var fieldShapeStack: ItemStack = null

	/**
	 * A Tuple representing the offset of the field on each axis.
	 */
	var fieldOffset = (0, 0, 0)

	/**
	 * A Tuple representing the radius of the field on each axis.
	 */
	var fieldRadius = (3, 3, 3)

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
	 * Adds the Field Shape stack
	 *
	 * @return Array of Upgrade ItemStack instances to drop when the block is broken.
	 */
	override def getUpgradesToDrop: Array[ItemStack] = {
		val upgrades = new ArrayBuffer[ItemStack]
		upgrades.appendAll(super.getUpgradesToDrop)

		if(fieldShapeStack != null)
			upgrades.append(fieldShapeStack)

		upgrades.toArray
	}

	/**
	 * Attempts to insert the specified ItemStack as an Upgrade.
	 *
	 * @param itemStack ItemStack the player is attempting to insert.
	 *
	 * @return Whether or not the item was successfully inserted.
	 */
	override def attemptInsertItemStack(itemStack: ItemStack): Boolean = {
		itemStack.getItem match {
			case fieldShape: ForcefieldShape =>
				if(this.fieldShapeStack == null) {
					this.fieldShapeStack = new ItemStack(fieldShape)
					true
				}
				else
					false

			case _ =>
				super.attemptInsertItemStack(itemStack)
		}
	}

	/**
	 * Creates the forcefield cube, adding the coordinates of any placed BlockForcefield instances to fieldBlockCoords.
	 */
	def activate() {
		if(isActive || fieldShapeStack == null)
			return

		state = MachineState.Active

		val offsetX = fieldOffset._1
		val offsetY = fieldOffset._2
		val offsetZ = fieldOffset._3

		def setFieldBlock(x: Int, y: Int, z: Int) {
			val block = worldObj.getBlock(x, y, z)
			val isFluid = block.isInstanceOf[IFluidBlock] || block == Blocks.water || block == Blocks.lava || block == Blocks.flowing_water || block == Blocks.flowing_lava

			if(block.isAir(worldObj, x, y, z) ||
					(isInBreakMode && block.getBlockHardness(worldObj, x, y, z) != -1) ||
					(isInSpongeMode && isFluid)) {
				worldObj.setBlock(x, y, z, BlockForcefield, 0, 2)
				fieldBlockCoords.append((x, y, z))
			}
		}

		val shape = fieldShapeStack.getItem.asInstanceOf[ForcefieldShape]
		if(shape != null) {
			for(coord <- shape.getRelativeCoords(fieldRadius)) {
				val x = xCoord + offsetX + coord._1
				val y = yCoord + offsetY + coord._2
				val z = zCoord + offsetZ + coord._3

				if(y >= 0)
					setFieldBlock(x, y, z)
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

		// Write the Field Shape ItemStack.
		if(fieldShapeStack != null) {
			val fieldShapeTag = new NBTTagCompound
			fieldShapeStack.writeToNBT(fieldShapeTag)
			tagCompound.setTag("fieldShapeStack", fieldShapeTag)
		}

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

		// Load the field shape tag
		if(tagCompound.hasKey("fieldShapeStack"))
			fieldShapeStack = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("fieldShapeStack"))

		val coordListTag: NBTTagCompound = if(isActive) tagCompound.getCompoundTag("tileCoordList") else null

		if(coordListTag != null) {
			val size = coordListTag.getInteger("size")

			for(idx <- 0 until size)
				fieldBlockCoords.append(NBTUtility.read3IntTupleFromNBT(coordListTag.getCompoundTag(s"tile$idx")))
		}
	}
}
