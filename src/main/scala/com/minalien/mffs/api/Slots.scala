package com.minalien.mffs.api

/**
 * Created by Katrina on 13/01/2015.
 */
object Slots extends Enumeration {
  type Slots=Slot
  val LinkCard=Slots(0)
  val TypeMod=Slots(1)
  val Option1=Slots(2)
  val Option2=Slots(3)
  val Option3=Slots(4)
  val Distance=Slots(5)
  val Strength=Slots(6)
  val FocusUp=Slots(7)
  val FocusDown=Slots(8)
  val FocusRight=Slots(9)
  val FocusLeft=Slots(10)
  val CenterSlot=Slots(11)
  val SecCard=Slots(12)
  class Slot(val slot:Int)
}
