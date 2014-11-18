package com.minalien.mffs.blocks.machines

import com.minalien.mffs.machines.TileEntityExtractor
import net.minecraft.block.Block
import net.minecraft.world.World

/**
 * Created by Katrina on 17/11/2014.
 */
object BlockExtractor extends MachineBlock("extractor") {

  override def getTileEntityClass = classOf[TileEntityExtractor]


  override def onNeighborBlockChange(world: World, x: Int, y: Int, z: Int, block: Block) {
    val tileEntity = world.getTileEntity(x, y, z).asInstanceOf[TileEntityExtractor]
    val inputPower = world.getBlockPowerInput(x, y, z)

    if (tileEntity.isActive && inputPower == 0)
      tileEntity.deactivate()
    else if (!tileEntity.isActive && inputPower > 0)
      tileEntity.activate()
  }

}
