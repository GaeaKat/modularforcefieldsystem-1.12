package com.minalien.mffs.blocks.machines

import com.minalien.mffs.core.MFFSCreativeTab
import com.minalien.mffs.tiles.MFFSMachine
import net.minecraft.block.{BlockContainer, Block}
import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

/**
 * Created by Katrina on 13/12/2014.
 */
abstract class MachineBlock(blockName:String) extends BlockContainer(Material.iron) {
  setCreativeTab(MFFSCreativeTab)
  setHardness(5f)
  setResistance(15f)
  setUnlocalizedName(blockName)

  //setBlockTextureName(s"mffs:$blockName" + "Inactive")
  //val blockIcons: Array[IIcon] = new Array[IIcon](2)

  /**
   * @return TileEntity class associated with this Machine.
   */
  def getTileEntityClass: Class[_ <: MFFSMachine] = null

  override def createNewTileEntity(worldIn: World, meta: Int): TileEntity = getTileEntityClass.newInstance()
}
