package com.minalien.mffs.machines

/**
 * Tile Entity for the extractor
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

  /**
   * Finds all machines in the range of this extractor
   * @return List containing all machines that can be powered
   */
  def findMachinesInRange(): List[MFFSMachine] = {

    List()
  }
}
