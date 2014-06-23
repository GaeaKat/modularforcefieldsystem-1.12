package com.minalien.mffs.machines

/**
 * Represents the various states MFFS Machines can be in.
 */
object MachineState extends Enumeration {
	type MachineState = Value

	val
		Inactive,   // Machine is currently Inactive.
		Active,     // Machine is currently Active.
		Disabled    // Machine has been disabled by a server operator.
		= Value
}
