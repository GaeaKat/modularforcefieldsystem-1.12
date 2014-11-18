package com.minalien.mffs.machines

/**
 * Created by Katrina on 17/11/2014.
 */
class TileEntityExtractor extends MFFSMachine(2){
  /**
   * Activates the machine.
   */
  override def activate(): Unit = {
    state = MachineState.Active
    super.activate()
  }

  /**
   * Deactivates the machine.
   */
  override def deactivate(): Unit = {
    state = MachineState.Inactive
    super.deactivate()
  }
}
