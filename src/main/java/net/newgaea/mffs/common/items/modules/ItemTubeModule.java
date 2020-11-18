package net.newgaea.mffs.common.items.modules;

import net.minecraft.tileentity.TileEntity;
import net.newgaea.mffs.api.EnumProjectorModule;

public class ItemTubeModule extends ItemProjectorModule {
    public ItemTubeModule(Properties properties) {
        super(properties);
    }

    @Override
    public String getModuleType() {
        return EnumProjectorModule.Tube.getString();
    }
    @Override
    public boolean enabledFoci() {
        return false;
    }
}
