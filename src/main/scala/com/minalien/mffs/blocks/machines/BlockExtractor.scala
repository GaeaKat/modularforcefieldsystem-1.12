package com.minalien.mffs.blocks.machines

import com.google.common.collect.ImmutableMap
import com.minalien.mffs.tiles.{MachineState, TileEntityExtractor, MFFSMachine}
import net.minecraft.block.properties.PropertyEnum.create
import net.minecraft.block.properties.{PropertyEnum, IProperty}
import net.minecraft.block.state.{IBlockState, BlockState}
import net.minecraft.util.EnumFacing

/**
 * Created by Katrina on 15/12/2014.
 */
object BlockExtractor  extends {
  val ACTIVE: PropertyEnum = create("active", classOf[MachineState])
} with MachineBlock("extractor") {

  this.setDefaultState(this.blockState.getBaseState.withProperty(ACTIVE, MachineState.INACTIVE))
  setHarvestLevel("pickaxe", 2)

  override def getStateFromMeta(meta: Int): IBlockState =
    meta match
    {
      case 0 => this.blockState.getBaseState.withProperty(ACTIVE,MachineState.INACTIVE)
      case 1 => this.blockState.getBaseState.withProperty(ACTIVE,MachineState.ACTIVE)
      case 2 => this.blockState.getBaseState.withProperty(ACTIVE,MachineState.DISABLED)
    }

  override def getMetaFromState(state: IBlockState): Int =
  {
    state.getValue(ACTIVE) match
    {
      case c:MachineState => {
        c match
        {
          case MachineState.INACTIVE => 0
          case MachineState.ACTIVE => 1
          case MachineState.DISABLED => 2
          case _ => 0
        }

    }
      case _ => 0
    }
  }

  /**
   * @return TileEntity class associated with this Machine.
   */
  override def getTileEntityClass: Class[_ <: MFFSMachine] = classOf[TileEntityExtractor]

  override def createBlockState(): BlockState = new BlockState(this, ACTIVE);


}
