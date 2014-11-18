package com.minalien.mffs.machines

import cofh.api.energy.{EnergyStorage, IEnergyHandler}
import com.minalien.mffs.items.upgrades.MachineUpgrade
import com.minalien.mffs.machines.MachineState._
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.network.{NetworkManager, Packet}
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection

import scala.util.Random

/**
 * Base Tile Entity class for any MFFS Machines.
 *
 * @param maxUpgrades Maximum number of upgrades the machine can hold.
 */
abstract class MFFSMachine(val maxUpgrades: Int) extends TileEntity  with IEnergyHandler{
	/**
	 * Stores a list of Upgrades equipped to the machine.
	 */
	val upgrades = new collection.mutable.ArrayBuffer[ItemStack]()
	val storage: EnergyStorage = new EnergyStorage(32000)
	/**
	 * Tracks the current state of the machine.
	 */
	var state: MachineState = MachineState.Inactive

	/**
	 * Activates the machine.
	 */
	def activate() = {
		this.markDirty()
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
	}

	/**
	 * Attempts to insert the specified ItemStack as an Upgrade.
	 *
	 * @param itemStack ItemStack the player is attempting to insert.
	 *
	 * @return Whether or not the item was successfully inserted.
	 */
	def attemptInsertItemStack(itemStack: ItemStack): Boolean = {
		itemStack.getItem match {
			case upgrade: MachineUpgrade =>
				if (upgrades.size >= maxUpgrades)
					false

				// Make sure the upgrade hasn't already been inserted
				for (upgradeStack <- upgrades) {
					if (upgradeStack.isItemEqual(itemStack)) {
						if (upgrade.canStack) {
							if (upgradeStack.stackSize >= upgrade.stackSize)
								return false
							upgradeStack.stackSize += 1
							upgrade.applyUpgrade(this)
							return true

						}
						return false
					}

				}
				// Insert the item!
				upgrades.append(new ItemStack(upgrade))
				upgrade.applyUpgrade(this)
				true

			case _ =>
				false
		}
	}

	/**
	 * Forces the tile entity to drop all of its upgrades into the world.
	 */
	def dropUpgrades() {
		val prng = new Random()

		// Drop all items in upgrade slots
		for (upgrade <- getUpgradesToDrop) {
			if (upgrade != null && upgrade.stackSize > 0) {
				val rX = prng.nextDouble() * 0.8 + 0.1
				val rY = prng.nextDouble() * 0.8 + 0.1
				val rZ = prng.nextDouble() * 0.8 + 0.1

				val itemEntity = new EntityItem(worldObj, xCoord + rX, yCoord + rY, zCoord + rZ, upgrade.copy())

				val jumpFactor = 0.05f
				itemEntity.motionX = prng.nextGaussian() * jumpFactor
				itemEntity.motionY = prng.nextGaussian() * jumpFactor + 0.2f
				itemEntity.motionZ = prng.nextGaussian() * jumpFactor

				worldObj.spawnEntityInWorld(itemEntity)

				upgrade.stackSize = 0
			}
		}
	}

	/**
	 * @return Array of Upgrade ItemStack instances to drop when the block is broken.
	 */
	def getUpgradesToDrop: Array[ItemStack] = upgrades.toArray

	/**
	 * Ensures that the machine's deactivation is executed before it is broken.
	 */
	override def invalidate() {
		if (isActive)
			deactivate()
	}

	/**
	 * @return Whether or not the machine is currently active, based on its state.
	 */
	def isActive: Boolean = state == MachineState.Active

	/**
	 * Deactivates the machine.
	 */
	def deactivate() = {
		this.markDirty()
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
	}

	/**
	 * Saves the machine's state and its upgrade list.
	 *
	 * @param tagCompound   NBTTagCompound that tile data is being written to.
	 */
	override def writeToNBT(tagCompound: NBTTagCompound) {
		super.writeToNBT(tagCompound)

		writeToCustomNBT(tagCompound)
	}

	override def getDescriptionPacket: Packet = {
		val nbttagcompound = new NBTTagCompound()
		writeToCustomNBT(nbttagcompound)
		new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbttagcompound)
	}

	def writeToCustomNBT(tagCompound: NBTTagCompound) {
		tagCompound.setByte("state", state.id.toByte)

		val upgradesTag = new NBTTagCompound

		upgradesTag.setByte("count", upgrades.size.toByte)
		for(i <- 0 until upgrades.size) {
			val itemTag = new NBTTagCompound

			upgrades(i).writeToNBT(itemTag)

			upgradesTag.setTag(s"$i", itemTag)
		}

		tagCompound.setTag("upgrades", upgradesTag)
		storage.writeToNBT(tagCompound)
	}

	override def onDataPacket(net: NetworkManager, pkt: S35PacketUpdateTileEntity): Unit = {
		super.onDataPacket(net, pkt)
		readFromCustomNBT(pkt.func_148857_g())
		worldObj.markBlockRangeForRenderUpdate(xCoord - 1, yCoord - 1, zCoord - 1, xCoord + 1, yCoord + 1, zCoord + 1)
	}

	/**
	 * Loads the machine's state and upgrade list.
	 *
	 * @param tagCompound NBTTagCompound that tile data is being read from.
	 */
	override def readFromNBT(tagCompound: NBTTagCompound) {
		super.readFromNBT(tagCompound)

		readFromCustomNBT(tagCompound)
	}

	def readFromCustomNBT(tagCompound: NBTTagCompound) {

		state = MachineState(tagCompound.getByte("state").toInt)

		val upgradesTag = tagCompound.getCompoundTag("upgrades")
		val upgradeCount = upgradesTag.getByte("count").toInt

		for (i <- 0 until upgradeCount) {
			val itemStack = ItemStack.loadItemStackFromNBT(upgradesTag.getCompoundTag(s"$i"))
			val item = itemStack.getItem

			item match {
				case upgrade: MachineUpgrade =>
					upgrade.applyUpgrade(this)
			}

			upgrades.append(itemStack)
			storage.readFromNBT(tagCompound)
		}
	}

	override def receiveEnergy(from: ForgeDirection, maxReceive: Int, simulate: Boolean): Int ={
		storage.receiveEnergy(maxReceive,simulate)
	}

	override def extractEnergy(from: ForgeDirection, maxExtract: Int, simulate: Boolean): Int = {
		storage.extractEnergy(maxExtract,simulate)
	}

	override def getEnergyStored(from: ForgeDirection): Int =
	{
		storage.getEnergyStored
	}

	override def getMaxEnergyStored(from: ForgeDirection): Int =
	{
		storage.getMaxEnergyStored
	}

	override def canConnectEnergy(from: ForgeDirection): Boolean =	true
}
