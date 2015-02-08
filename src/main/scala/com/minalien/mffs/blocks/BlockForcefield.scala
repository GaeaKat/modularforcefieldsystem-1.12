package com.minalien.mffs.blocks

import java.util
import java.util.Random

import com.minalien.mffs.core.MFFSCreativeTab
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.util.{EnumWorldBlockLayer, EnumFacing, BlockPos}
import net.minecraft.world.IBlockAccess
import net.minecraftforge.fml.relauncher.{SideOnly, Side}

/**
 * Created by Katrina on 27/12/2014.
 */
object BlockForcefield extends Block(Material.glass){

  setCreativeTab(MFFSCreativeTab)
  setUnlocalizedName("forcefield")
  setResistance(10000)
  setBlockUnbreakable()
  /**
   * @return Forcefields are not opaque.
   */
  override def isOpaqueCube = false

  /**
   * @return False, so that light will correctly pass through this block.
   */
  override def quantityDropped(random: Random): Int = {
    return 0
  }

  override def shouldSideBeRendered(world: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean =
  {
    val iblockstate: IBlockState = world.getBlockState(pos)
    val block: Block = iblockstate.getBlock
    if (world.getBlockState(pos.offset(side.getOpposite)) ne iblockstate) {
      return true
    }

    if (block eq this) {
      return false
    }

    super.shouldSideBeRendered(world, pos, side)
  }

  @SideOnly(Side.CLIENT)
  override def getBlockLayer: EnumWorldBlockLayer = {
    return EnumWorldBlockLayer.CUTOUT
  }

  override def isFullCube: Boolean = {
    return false
  }

}
