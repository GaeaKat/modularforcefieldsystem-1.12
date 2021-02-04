package net.newgaea.mffs.common.libs;

import net.newgaea.mffs.common.blocks.BlockNetwork;
import net.newgaea.mffs.common.tiles.TileNetwork;

import java.util.HashMap;
import java.util.Map;

public class LibMachines {

    private static final Map<String, Class> machines=new HashMap<>();

    public static Map<String, Class> getMachines() {
        return machines;
    }
    public static final String Projector="Projector";
    public static final String Capacitor="Capacitor";
}
