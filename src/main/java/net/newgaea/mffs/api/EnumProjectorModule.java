package net.newgaea.mffs.api;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.IExtensibleEnum;

public enum EnumProjectorModule implements IStringSerializable {
    Empty,
    Advanced_Cube,
    Containment,
    Cube,
    Deflector,
    Diagonal_Wall,
    Sphere,
    Tube,
    Wall,
    Custom;

    @Override
    public String getString() {
        return toString().toLowerCase();
    }

    public String getModule() { return getString()+"_module";}

    public static EnumProjectorModule getModuleFromString(String name) {
        if ("Empty".equalsIgnoreCase(name)) {
            return Empty;
        } else if ("Advanced_Cube".equalsIgnoreCase((name))) {
            return Advanced_Cube;
        } else if ("Containment".equalsIgnoreCase((name))) {
            return Containment;
        } else if ("Cube".equalsIgnoreCase((name))) {
            return Cube;
        } else if ("Deflector".equalsIgnoreCase((name))) {
            return Deflector;
        } else if ("Diagonal_Wall".equalsIgnoreCase((name))) {
            return Diagonal_Wall;
        } else if ("Sphere".equalsIgnoreCase((name))) {
            return Sphere;
        } else if ("Tube".equalsIgnoreCase((name))) {
            return Tube;
        } else if ("Wall".equalsIgnoreCase((name))) {
            return Wall;
        }
        return Custom;
    }
}
