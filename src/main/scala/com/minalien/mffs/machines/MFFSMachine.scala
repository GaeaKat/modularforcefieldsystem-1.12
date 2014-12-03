package com.minalien.mffs.machines

import cofh.api.energy.{EnergyStorage, IEnergyHandler}
import com.minalien.mffs.items.upgrades.MachineUpgrade
import com.minalien.mffs.machines.MachineState._
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{NBTTagCompound, NBTTagList}
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.network.{NetworkManager, Packet}
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.{Constants, ForgeDirection}

import scala.util.Random

/**
 * Base Tile Entity class for any MFFS Machines.
 *
 * @param maxUpgrades Maximum number of upgrades the machine can hold.
 */
abstract class MFFSMachine(val maxUpgrades: Int) extends TileEntity with IEnergyHandler with IInventory {
	/**
	 * Stores a list of Upgrades equipped to the machine.
	 */
	val upgrades: Array[ItemStack] = new Array[ItemStack](getSizeInventory)
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
					if (upgradeStack != null) {
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
				}
				// Insert the item!
				var slot = 0
				val stack = new ItemStack(upgrade)
				for (i <- 0.until(getSizeInventory)) {
					if (getStackInSlot(i) == null && isItemValidForSlot(i, stack))
						slot = i

				}
				setInventorySlotContents(slot, stack)
				upgrade.applyUpgrade(this)
				true

			case _ =>
				false
		}
	}

	override def getSizeInventory: Int = maxUpgrades

	override def isItemValidForSlot(slot: Int, item: ItemStack): Boolean =
		item.getItem match {
			case s: MachineUpgrade => true
			case _ => false
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

		val upgradesTag = new NBTTagList

		for (i <- 0 until upgrades.length) {
			var stack = upgrades(i)
			if (stack != null) {
				val cmp = new NBTTagCompound()
				cmp.setByte("Slot", i.toByte)
				stack.writeToNBT(cmp)
				upgradesTag.appendTag(cmp)
			}
		}
		tagCompound.setTag("upgrades", upgradesTag)
		storage.writeToNBT(tagCompound)
	}

	override def onDataPacket(net: NetworkManager, pkt: S35PacketUpdateTileEntity): Unit = {
		super.onDataPacket(net, pkt)
		readFromCustomNBT(pkt.func_148857_g())
		worldObj.markBlockRangeForRenderUpdate(xCoord - 1, yCoord - 1, zCoord - 1, xCoord + 1, yCoord + 1, zCoord + 1)
	}

	def readFromCustomNBT(tagCompound: NBTTagCompound) {

		state = MachineState(tagCompound.getByte("state").toInt)

		val upgradesTag = tagCompound.getTagList("upgrades", Constants.NBT.TAG_COMPOUND)


		for (i <- 0 until upgradesTag.tagCount()) {
			val tag: NBTTagCompound = upgradesTag.getCompoundTagAt(i)
			val slot: Byte = tag.getByte("Slot")
			if (slot >= 0 && slot < upgrades.length) {
				upgrades(slot) = ItemStack.loadItemStackFromNBT(tag)
				upgrades(slot).getItem match {
					case upgrade: MachineUpgrade =>
						upgrade.applyUpgrade(this)
					case _ =>
				}

			}

		}
		storage.readFromNBT(tagCompound)
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

	override def receiveEnergy(from: ForgeDirection, maxReceive: Int, simulate: Boolean): Int = {

		val tmp = storage.receiveEnergy(maxReceive, simulate)
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord)

		tmp
	}

	override def extractEnergy(from: ForgeDirection, maxExtract: Int, simulate: Boolean): Int = {
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
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

	override def decrStackSize(slot: Int, amt: Int): ItemStack = {
		var stack = getStackInSlot(slot)
		if (stack != null) {
			if (stack.stackSize <= amt)
				setInventorySlotContents(slot, null)
			else {
				stack = stack.splitStack(amt)
				if (stack.stackSize == 0)
					setInventorySlotContents(slot, null)
			}
		}
		stack
	}

	override def closeInventory(): Unit = {}

	override def getInventoryStackLimit: Int = 64

	override def getStackInSlotOnClosing(slot: Int): ItemStack = {
		val stack = getStackInSlot(slot)
		if (stack != null)
			setInventorySlotContents(slot, null)
		stack
	}

	override def setInventorySlotContents(slot: Int, stack: ItemStack): Unit = {
		val oldStack = upgrades(slot)
		val cardExists = if (oldStack != null) oldStack.getItem match {
			case s: MachineUpgrade => true;
			case _ => false
		} else false
		upgrades(slot) = stack
		if (cardExists && stack == null) {
			oldStack.getItem match {
				case card: MachineUpgrade => card.removeUpgrade(this)
			}
		}
		else if (stack != null) {
			stack.getItem match {
				case card: MachineUpgrade => card.applyUpgrade(this)
				case _ =>
			}
		}
	}

	override def getStackInSlot(slot: Int): ItemStack = upgrades(slot)

	override def openInventory(): Unit = {

	}

	override def isUseableByPlayer(player: EntityPlayer): Boolean = worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64

	override def hasCustomInventoryName: Boolean = false

	override def getInventoryName: String = "MFFSMachine"
}
