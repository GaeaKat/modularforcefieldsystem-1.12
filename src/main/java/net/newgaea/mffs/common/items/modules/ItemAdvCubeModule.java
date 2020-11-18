package net.newgaea.mffs.common.items.modules;

import net.minecraft.tileentity.TileEntity;
import net.newgaea.mffs.api.EnumProjectorModule;

public class ItemAdvCubeModule extends ItemProjectorModule{

    public ItemAdvCubeModule(Properties properties) {
        super(properties);
    }

    @Override
    public String getModuleType() {
        return EnumProjectorModule.Advanced_Cube.getString();
    }

    @Override
    public boolean enabledFoci() {
        return true;
    }
}
