package com.minalien.mffs.machines

import com.minalien.mffs.items.upgrades.MachineUpgrade
import com.minalien.mffs.machines.MachineState._
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity

/**
 * Base Tile Entity class for any MFFS Machines.
 */
abstract class MFFSMachine extends TileEntity {
	/**
	 * Stores a list of Upgrades equipped to the machine.
	 */
	val upgrades = new collection.mutable.ArrayBuffer[ItemStack]()

	/**
	 * Tracks the current state of the machine.
	 */
	var state: MachineState = MachineState.Inactive

	/**
	 * @return Whether or not the machine is currently active, based on its state.
	 */
	def isActive: Boolean = state == MachineState.Active

	/**
	 * Activates the machine.
	 */
	def activate()

	/**
	 * Deactivates the machine.
	 */
	def deactivate()

	/**
	 * Ensures that the machine's deactivation is executed before it is broken.
	 */
	override def invalidate() {
		if(isActive)
			deactivate()
	}

	/**
	 * Saves the machine's state and its upgrade list.
	 *
	 * @param tagCompound   NBTTagCompound that tile data is being written to.
	 */
	override def writeToNBT(tagCompound: NBTTagCompound) {
		super.writeToNBT(tagCompound)

		tagCompound.setByte("state", state.id.toByte)

		val upgradesTag = new NBTTagCompound

		upgradesTag.setByte("count", upgrades.size.toByte)
		for(i <- 0 until upgrades.size) {
			val itemTag = new NBTTagCompound

			upgrades(i).writeToNBT(itemTag)

			upgradesTag.setTag(s"$i", itemTag)
		}

		tagCompound.setTag("upgrades", upgradesTag)
	}

	/**
	 * Loads the machine's state and upgrade list.
	 *
	 * @param tagCompound NBTTagCompound that tile data is being read from.
	 */
	override def readFromNBT(tagCompound: NBTTagCompound) {
		super.readFromNBT(tagCompound)

		state = MachineState(tagCompound.getByte("state").toInt)

		val upgradesTag = tagCompound.getCompoundTag("upgrades")
		val upgradeCount = upgradesTag.getByte("count").toInt

		for(i <- 0 until upgradeCount) {
			val itemStack = ItemStack.loadItemStackFromNBT(upgradesTag.getCompoundTag(s"$i"))
			val item = itemStack.getItem

			item match {
				case upgrade: MachineUpgrade =>
					upgrade.applyUpgrade(this)
			}

			upgrades.append(itemStack)
		}
	}
}
