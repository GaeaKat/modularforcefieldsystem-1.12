package net.newgaea.mffs.common.items.modules;

import net.newgaea.mffs.api.EnumProjectorModule;

public class ItemWallModule extends ItemProjectorModule {
    public ItemWallModule(Properties properties) {
        super(properties);
    }


    @Override
    public String getModuleType() {
        return EnumProjectorModule.Wall.getString();
    }
}
