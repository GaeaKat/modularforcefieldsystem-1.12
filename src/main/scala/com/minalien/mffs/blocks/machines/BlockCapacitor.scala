package com.minalien.mffs.blocks.machines

import com.minalien.mffs.core.GuiHandler
import com.minalien.mffs.tiles.{TileEntityCapacitor, MFFSMachine}

/**
 * Created by KatrinaGon 25/01/2015.
 */
object BlockCapacitor extends MachineBlock("generator") {
  setHarvestLevel("pickaxe", 2)

  override def GuiID: Int = GuiHandler.CAPACITOR_GUI

  /**
   * @return TileEntity class associated with this Machine.
   */
  override def getTileEntityClass: Class[_ <: MFFSMachine] = classOf[TileEntityCapacitor]
}
