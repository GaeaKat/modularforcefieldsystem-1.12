package net.newgaea.mffs.common.items.modules;

import net.minecraft.tileentity.TileEntity;
import net.newgaea.mffs.api.EnumProjectorModule;

public class ItemDiagWallModule extends ItemProjectorModule {
    public ItemDiagWallModule(Properties properties) {
        super(properties);
    }

    @Override
    public String getModuleType() {
        return EnumProjectorModule.Diagonal_Wall.getString();
    }
    @Override
    public boolean enabledFoci() {
        return true;
    }
}
