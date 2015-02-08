package com.minalien.mffs.core

import com.google.common.collect.MapMaker
import com.minalien.mffs.tiles.{TileEntityCapacitor, TileEntityExtractor, MFFSMachine}
import net.minecraft.world.World

import scala.collection._
import scala.util.Random

/**
 * Created by Katrina on 15/01/2015.
 */
object LinkGrid {
  val WorldNets=new MapMaker().weakKeys().makeMap[World,WorldGrid]()

  class WorldGrid
  {
    val Capacitors =mutable.Map.empty[Integer,TileEntityCapacitor]
    val Extractors =mutable.Map.empty[Integer,TileEntityExtractor]


    def connectedToCapacitor(cap:TileEntityCapacitor,range:Int):Int= {
      var counter = 0
      for (tileentity <- Capacitors.values) {
        if (tileentity.getPowerSourceID == cap.getPowerStorageID) {
          if ((range * range) >= tileentity.getMachinePoint.distanceSq(cap.getMachinePoint)) {
            counter += 1
          }
        }
      }
      for (tileentity <- Extractors.values) {
        if (tileentity.getPowerSourceID == cap.getPowerStorageID) {
          if ((range * range) >= tileentity.getMachinePoint.distanceSq(cap.getMachinePoint)) {
            counter += 1
          }
        }

      }
      counter
    }
    def refreshID(tileEntity:MFFSMachine,remDeviceID:Int):Int =
    {
      val random:Random=new Random()

      var deviceID=random.nextInt()
      tileEntity match
      {
        case s:TileEntityExtractor =>
        {
          if(remDeviceID==0)
          {
            while(! Extractors.lift(deviceID).isEmpty)
              deviceID=random.nextInt

          }
          else
            deviceID=remDeviceID

          Extractors.put(deviceID,s)
          deviceID
        }
        case s:TileEntityCapacitor => {
          if(remDeviceID==0)
          {
            while (!Capacitors.lift(deviceID).isEmpty)
              deviceID=random.nextInt
          }
          else
            deviceID=remDeviceID
          Capacitors.put(deviceID,s)
          deviceID
        }
      }
    }



  }

  def getWorldNet(world:World):WorldGrid=
  {
    if(world!=null)
    {
      if(!WorldNets.containsKey(world))
        WorldNets.put(world,new WorldGrid)
      WorldNets.get(world)
    }
    else
      null
  }
}
