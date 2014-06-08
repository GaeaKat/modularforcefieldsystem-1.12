package com.minalien.mffs.blocks.machines

import net.minecraft.tileentity.TileEntity
import com.minalien.mffs.machines.TileEntityProjector

/**
 * Forcefield Projector.
 */
object BlockProjector extends MachineBlock("projector") {
	/**
	 * @return TileEntity class associated with this Machine.
	 */
	override def getTileEntityClass: Class[_ <: TileEntity] = classOf[TileEntityProjector]
}
