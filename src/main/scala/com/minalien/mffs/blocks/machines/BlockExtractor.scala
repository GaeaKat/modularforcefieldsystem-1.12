package com.minalien.mffs.blocks.machines

import com.minalien.mffs.core.GuiHandler
import com.minalien.mffs.tiles.{MFFSMachine, TileEntityExtractor}

/**
 * Created by Katrina on 15/12/2014.
 */
object BlockExtractor  extends {

} with MachineBlock("extractor") {
  setHarvestLevel("pickaxe", 2)


  override def GuiID: Int = GuiHandler.EXTRACTOR_GUI

  /**
   * @return TileEntity class associated with this Machine.
   */
  override def getTileEntityClass: Class[_ <: MFFSMachine] = classOf[TileEntityExtractor]
}
