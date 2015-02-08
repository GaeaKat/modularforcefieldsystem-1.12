package com.minalien.mffs.tiles

import com.minalien.mffs.api.{EnumForcefieldType, IModularProjector}
import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack

import scala.collection.mutable

/**
 * Created by Katrina on 26/01/2015.
 */
class TileEntityProjector extends TileEntityFEPoweredMachine with IModularProjector {

  var projectorItemStacks:Array[ItemStack]=new Array[ItemStack](13)
  var camoBlockName:String=""
  var camoBlockState:IBlockState=null
  var switchDelay=0
  var forceFieldBlockMeta=EnumForcefieldType.Default.id
  var projectorType=0
  var linkPower=0
  var blockPower=0
  var burnOut=false
  var accessType=0
  var capacity=0
  var fieldQueue:mutable.Stack[Int]=new mutable.Stack[Int]()


}
