package com.minalien.mffs.api

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.DimensionManager

/**
 * Block Point that includes dimensions, because this is needed to help keep track of where a forcefield is
 *
 * Created by Katrina on 09/01/2015.
 */
class BlockPoint(var pos:BlockPos,var dimID:Int) {




  def this(x:Int,y:Int,z:Int,d:Int)=this(new BlockPos(x,y,z),d)
  def this(x:Int,y:Int,z:Int)=this(x,y,z,Int.MaxValue)
  def this(x:Int,y:Int,z:Int,w:World)=this(x,y,z,w.provider.getDimensionId)
  def this(b:BlockPos,w:World)=this(b,w.provider.getDimensionId)
  def this(nbt:NBTTagCompound)=this(nbt.getInteger("x"),nbt.getInteger("y"),nbt.getInteger("z"),nbt.getInteger("dim"))

  def distanceSq(other:BlockPoint):Double=
  {
    if(other.dimID==this.dimID)
    {
      this.pos.distanceSq(other.pos)
    }
    else
      Int.MaxValue
  }

  def getPointWorld:World={
    if(dimID!=Int.MaxValue)
      DimensionManager.getWorld(dimID)
    else
      null
  }

  def writeNBT(nbt:NBTTagCompound)=
  {
    nbt.setInteger("x",pos.getX)
    nbt.setInteger("y",pos.getY)
    nbt.setInteger("z",pos.getZ)
    nbt.setInteger("dim",dimID)
  }

  override def hashCode(): Int = ("Pos: "+pos.toString+" Dim: "+dimID).hashCode

  override def equals(obj: scala.Any): Boolean =
    obj match
    {
      case other:BlockPoint => {
        pos.equals(other.pos) && dimID==other.dimID
      }
      case _ =>
        false
    }

  override def toString: String = "X: "+pos.getX+" Y: "+pos.getY+" Z: "+pos.getZ
}
