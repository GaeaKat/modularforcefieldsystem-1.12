package com.minalien.mffs.tiles;

import net.minecraft.util.IStringSerializable;

/**
 * Created by Katrina on 21/12/2014.
 */
public enum MachineState implements IStringSerializable {
    ACTIVE,
    INACTIVE,
    DISABLED;


    public String toString()
    {
        return this.getName();
    }

    public String getName()
    {
        return this == ACTIVE ? "active" : this==INACTIVE ? "inactive" : "disabled";
    }

}
