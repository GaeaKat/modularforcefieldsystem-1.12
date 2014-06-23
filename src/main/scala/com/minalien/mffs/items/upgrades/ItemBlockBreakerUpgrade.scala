package com.minalien.mffs.items.upgrades

import com.minalien.mffs.core.MFFSCreativeTab
import com.minalien.mffs.machines.TileEntityProjector
import net.minecraft.item.Item
import net.minecraft.tileentity.TileEntity

/**
 * Provides Projectors the ability to break blocks when generating a forcefield.
 */
object ItemBlockBreakerUpgrade extends Item with MachineUpgrade {
	setCreativeTab(MFFSCreativeTab)
	setUnlocalizedName("upgradeBlockBreaker")
	setTextureName("mffs:upgrades/blockBreaker")

	/**
	 * @return A list of valid TileEntity classes the Upgrade can apply to.
	 */
	override def getValidMachines: Array[Class[_ <: TileEntity]] = Array(classOf[TileEntityProjector])

	/**
	 * Applies the effects to the tile entity.
	 *
	 * @param tileEntity Tile entity being upgraded.
	 */
	def applyUpgrade(tileEntity: TileEntity) {
		tileEntity match {
			case projector: TileEntityProjector =>
				projector.isInBreakMode = true
		}
	}

	/**
	 * Removes the effects from the tile entity.
	 *
	 * @param tileEntity Tile entity being downgraded.
	 */
	def removeUpgrade(tileEntity: TileEntity) {
		tileEntity match {
			case projector: TileEntityProjector =>
				projector.isInBreakMode = false
		}
	}
}
