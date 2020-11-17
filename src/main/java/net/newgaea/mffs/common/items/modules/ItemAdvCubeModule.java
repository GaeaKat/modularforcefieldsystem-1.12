package net.newgaea.mffs.common.items.modules;

import net.newgaea.mffs.api.EnumProjectorModule;

public class ItemAdvCubeModule extends ItemProjectorModule{

    public ItemAdvCubeModule(Properties properties) {
        super(properties);
    }

    @Override
    public String getModuleType() {
        return EnumProjectorModule.Advanced_Cube.getString();
    }
}
