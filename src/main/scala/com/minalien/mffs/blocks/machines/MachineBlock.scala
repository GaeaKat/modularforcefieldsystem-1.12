package com.minalien.mffs.blocks.machines

import com.minalien.mffs.core.{ModularForcefieldSystem, MFFSCreativeTab}
import com.minalien.mffs.items.cards.ItemCardEmpty
import com.minalien.mffs.tiles.MFFSMachine
import net.minecraft.block.material.Material
import net.minecraft.block.properties.{PropertyBool, PropertyDirection}
import net.minecraft.block.state.{BlockState, IBlockState}
import net.minecraft.block.{Block, BlockPistonBase, ITileEntityProvider}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{BlockPos, EnumFacing}
import net.minecraft.world.{Explosion, IBlockAccess, World}

/**
 * Created by Katrina on 13/12/2014.
 */
abstract class MachineBlock(blockName:String) extends {
  val FACING: PropertyDirection = PropertyDirection.create("facing")
  val ACTIVE: PropertyBool = PropertyBool.create("active")
} with Block(Material.iron) with ITileEntityProvider {
  setCreativeTab(MFFSCreativeTab)
  setHardness(5f)
  setResistance(15f)
  setUnlocalizedName(blockName)
  this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false))







  override def getStateFromMeta(meta: Int): IBlockState =
  {
    val facing:EnumFacing=BlockPistonBase.getFacing(meta)
    return this.getDefaultState.withProperty(FACING,facing).withProperty(ACTIVE,(meta & 8) > 0)
  }

  override def getMetaFromState(state: IBlockState): Int =
  {
    val b0: Byte = 0
    var i: Int = b0 | state.getValue(FACING).asInstanceOf[EnumFacing].getIndex
    if(state.getValue(ACTIVE)==true)
      i |= 8
    i
  }

  //setBlockTextureName(s"mffs:$blockName" + "Inactive")
  //val blockIcons: Array[IIcon] = new Array[IIcon](2)

  /**
   * @return TileEntity class associated with this Machine.
   */
  def getTileEntityClass: Class[_ <: MFFSMachine] = null

  override def createNewTileEntity(worldIn: World, meta: Int): TileEntity = getTileEntityClass.newInstance()

  override def breakBlock(worldIn: World, pos: BlockPos, state: IBlockState) {
    val tileEntity:TileEntity=worldIn.getTileEntity(pos)
    tileEntity match
    {
      case s:MFFSMachine => s.dropPlugins
    }
    worldIn.removeTileEntity(pos)
  }

  def isActive(world:IBlockAccess,pos:BlockPos): Unit =
  {
    val entity=world.getTileEntity(pos)
    entity match
    {
      case s:MFFSMachine => s.isActive
      case _ => return false
    }
  }

  override def createBlockState(): BlockState = new BlockState(this, FACING,ACTIVE);

  override def onBlockPlaced(worldIn: World, pos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase): IBlockState = {

        this.getDefaultState.withProperty(FACING,BlockPistonBase.getFacingFromEntity(worldIn,pos,placer))
  }

  def GuiID:Int
  override def getExplosionResistance(world: World, pos: BlockPos, exploder: Entity, explosion: Explosion): Float =
  {
    var tileEntity:TileEntity=world.getTileEntity(pos)
    tileEntity match
    {
      case mffs:MFFSMachine =>
      {
        if(mffs.isActive)
          999F
        else
          100F
      }
      case _=> 100F
    }
  }

  override def onBlockEventReceived(worldIn: World, pos: BlockPos, state: IBlockState, eventID: Int, eventParam: Int): Boolean = {
    super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam)
    val tileentity: TileEntity = worldIn.getTileEntity(pos)
    return if (tileentity == null) false else tileentity.receiveClientEvent(eventID, eventParam)
  }

  override def onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean =
  {
    if(playerIn.isSneaking)
      return false

    if(worldIn.isRemote)
      return true

    val tileentity:MFFSMachine=worldIn.getTileEntity(pos).asInstanceOf[MFFSMachine]
    val equipped: Item = (if (playerIn.getCurrentEquippedItem != null) playerIn.getCurrentEquippedItem.getItem else null)
    if(equipped!=null)
    {
      val item:Item=Item.getItemFromBlock(Blocks.lever)
      equipped match
      {
        case ItemCardEmpty => return false
        case  item => return false
      }


    }
    if(tileentity==null)
      return false

    // TODO : add in security here
    playerIn.openGui(ModularForcefieldSystem,GuiID,worldIn,pos.getX,pos.getY,pos.getZ)
    true
  }

}
