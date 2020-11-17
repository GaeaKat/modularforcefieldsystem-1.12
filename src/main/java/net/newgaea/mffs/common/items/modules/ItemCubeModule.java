package net.newgaea.mffs.common.items.modules;

import net.newgaea.mffs.api.EnumProjectorModule;

public class ItemCubeModule extends ItemProjectorModule {
    public ItemCubeModule(Properties properties) {
        super(properties);
    }

    @Override
    public String getModuleType() {
        return EnumProjectorModule.Cube.getString();
    }
}
