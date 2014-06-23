package com.minalien.mffs.items.upgrades

import net.minecraft.tileentity.TileEntity

/**
 * Trait representing any item that can be used as a Machine Upgrade
 */
trait MachineUpgrade {
	/**
	 * @return A list of valid TileEntity classes the Upgrade can apply to.
	 */
	def getValidMachines: Array[Class[_ <: TileEntity]]

	/**
	 * Applies the effects to the tile entity.
	 *
	 * @param tileEntity Tile entity being upgraded.
	 */
	def applyUpgrade(tileEntity: TileEntity)

	/**
	 * Removes the effects from the tile entity.
	 *
	 * @param tileEntity Tile entity being downgraded.
	 */
	def removeUpgrade(tileEntity: TileEntity)
}
