package com.minalien.mffs.blocks.machines

import com.minalien.mffs.core.{GuiHandler, ModularForcefieldSystem}
import com.minalien.mffs.machines.TileEntityProjector
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

/**
 * Forcefield Projector.
 */
object BlockProjector extends MachineBlock("projector") {
	/**
	 * @return TileEntity class associated with this Machine.
	 */
	override def getTileEntityClass = classOf[TileEntityProjector]

	/**
	 * Listens for a neighbor block to change to notify TileEntity of possible redstone state change.
	 *
	 * @param world World the blocks exist in.
	 * @param x     X Coordinate of the Projector.
	 * @param y     Y Coordinate of the Projector.
	 * @param z     Z Coordinate of the Projector.
	 * @param block Neighbor block changed.
	 */
	override def onNeighborBlockChange(world: World, x: Int, y: Int, z: Int, block: Block) {
		val tileEntity = world.getTileEntity(x, y, z).asInstanceOf[TileEntityProjector]
		val inputPower = world.getBlockPowerInput(x, y, z)

		if(tileEntity.isActive && inputPower == 0)
			tileEntity.deactivate()
		else if(!tileEntity.isActive && inputPower > 0)
			tileEntity.activate()
	}

	override def onBlockActivated(p_149727_1_ : World, p_149727_2_ : Int, p_149727_3_ : Int, p_149727_4_ : Int, p_149727_5_ : EntityPlayer, p_149727_6_ : Int, p_149727_7_ : Float, p_149727_8_ : Float, p_149727_9_ : Float): Boolean = {
		p_149727_5_.openGui(ModularForcefieldSystem, GuiHandler.PROJECTOR_GUI, p_149727_1_, p_149727_2_, p_149727_3_, p_149727_4_)
		true
	}
}
