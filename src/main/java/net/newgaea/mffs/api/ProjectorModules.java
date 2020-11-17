package net.newgaea.mffs.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProjectorModules {
    private static List<String> modules=new ArrayList<>();

    static {
        modules.add("Empty");
        modules.add("AdvCube");
        modules.add("Containment");
        modules.add("Cube");
        modules.add("Deflector");
        modules.add("DiagWall");
        modules.add("Sphere");
        modules.add("Tube");
        modules.add("Wall");
    }

    public static void addModule(String module) {
        modules.add(module);
    }

    public static Collection<String> getModules() {
        return modules;
    }

}
