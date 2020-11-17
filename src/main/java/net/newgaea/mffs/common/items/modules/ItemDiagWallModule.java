package net.newgaea.mffs.common.items.modules;

import net.newgaea.mffs.api.EnumProjectorModule;

public class ItemDiagWallModule extends ItemProjectorModule {
    public ItemDiagWallModule(Properties properties) {
        super(properties);
    }

    @Override
    public String getModuleType() {
        return EnumProjectorModule.Diagonal_Wall.getString();
    }
}
