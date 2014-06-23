package com.minalien.mffs.items.upgrades

import com.minalien.mffs.blocks.machines.BlockProjector
import com.minalien.mffs.items.fieldshapes.ItemFieldShapeCube
import com.minalien.mffs.machines.{MFFSMachine, TileEntityProjector}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.world.World

/**
 * Base class for items that implement Forcefield Shape modules.
 */
abstract class ItemUpgrade extends Item {
	setMaxStackSize(1)

	/**
	 * Allows the Upgrade item to be inserted into a Projector via right-click.
	 *
	 * @param itemStack ItemStack being used.
	 * @param player    Player using the ItemStack.
	 * @param world     World the event has occurred in.
	 * @param x         Block X Coordinate.
	 * @param y         Block Y Coordinate.
	 * @param z         Block Z Coordinate.
	 * @param side      Side the block was clicked on.
	 * @param nX        Normalized X value for the click.
	 * @param nY        Normalized Y value for the click.
	 * @param nZ        Normalized Z value for the click.
	 *
	 * @return  Whether or not the use should be consumed.
	 */
	override def onItemUse(itemStack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, nX: Float, nY: Float, nZ: Float): Boolean = {
		if(world.isRemote)
			return true

		val tile = world.getTileEntity(x, y, z).asInstanceOf[MFFSMachine]
		if(tile != null) {
			if(tile.attemptInsertItemStack(itemStack))
				itemStack.stackSize -= 1
		}

		world.isRemote
	}
}
