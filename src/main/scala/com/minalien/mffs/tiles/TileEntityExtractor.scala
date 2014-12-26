package com.minalien.mffs.tiles

import net.minecraft.util.EnumFacing

/**
 * Created by Katrina on 15/12/2014.
 */
class TileEntityExtractor extends MFFSMachine{
  override def validate(): Unit =
  {
    super.validate()
    println(worldObj.getBlockState(pos))
  }
}
