package com.minalien.mffs.manager

import java.util.UUID

import com.minalien.mffs.api.EnumForcefieldType
import EnumForcefieldType.EnumForcefieldType
import net.minecraft.util.BlockPos

/**
 * Created by Katrina on 28/12/2014.
 */
class ForcefieldBlock(x: Int,y: Int,z: Int){

  var Absence:Boolean=false
  var OwnerPos:BlockPos=null
  var Owner:UUID=null
  var Type:EnumForcefieldType=EnumForcefieldType.Normal

}
