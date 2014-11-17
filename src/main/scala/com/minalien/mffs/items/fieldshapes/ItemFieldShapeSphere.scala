package com.minalien.mffs.items.fieldshapes

import com.minalien.mffs.core.MFFSCreativeTab
import com.minalien.mffs.items.fieldshapes.ItemFieldShapeCube._
import com.minalien.mffs.items.upgrades.ItemUpgrade
import scala.math._
/**
 * Created by Katrina on 14/11/2014.
 */
object ItemFieldShapeSphere extends ItemUpgrade with ForcefieldShape
{
  setCreativeTab(MFFSCreativeTab)
  setUnlocalizedName("fieldShapeSphere")
  setTextureName("mffs:fieldshapes/sphere")
  setMaxStackSize(1)

  /**
   * @param radius Distance from (not including) the center block for the field on each axis.
   *
   * @return Array of 3-Int Tuples representing coordinates relative to the center of the shape where the Projector
   *         should attempt to place forcefield blocks.
   */
  override def getRelativeCoords(radius: (Int, Int, Int)): Array[(Int, Int, Int)] = {
    val (xRadius,yRadius,zRadius)=radius
    val MList=xRadius*2*2*2
    val NList=yRadius*2*2*2

    val coordList = new collection.mutable.ListBuffer[(Int, Int, Int)]
    for (m  <- 0 to MList;n<-0 to NList)
    {
        coordList.append(((sin(Math.PI*m/MList)*cos(2*Math.PI*n/NList)*xRadius).toInt,(sin(Math.PI*m/MList)*sin(2*Math.PI*n/NList)*yRadius).toInt,(cos(Math.PI*m/MList)*zRadius).toInt))
    }
    coordList.toArray
  }

  /**
   * @param radius Distance from (not including) the center block for the field on each axis.
   *
   * @return Array of 3-Int Tuples representing coordinates relative to the center of the shape where the Projector
   *         should attempt to perform any "internal" field checks.
   */
  override def getRelativeInternalCoords(radius: (Int, Int, Int)): Array[(Int, Int, Int)] = {
    val coordList = new collection.mutable.ListBuffer[(Int, Int, Int)]
    coordList.toArray
  }
}
