package com.minalien.mffs.api

/**
 * Created by Katrina on 28/12/2014.
 */
object EnumForcefieldType {
  sealed abstract class ForceFieldType(val id:Int,val cost:Int)
  {

  }
  case object Default extends ForceFieldType(1,1)
  case object Zapper extends ForceFieldType(2,3)
  case object Camouflage extends ForceFieldType(3,2)
  case object Area extends ForceFieldType(4,1)
  case object Containment extends ForceFieldType(5,1)

}
