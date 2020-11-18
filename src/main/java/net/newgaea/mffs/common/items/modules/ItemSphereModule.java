package net.newgaea.mffs.common.items.modules;

import net.minecraft.tileentity.TileEntity;
import net.newgaea.mffs.api.EnumProjectorModule;

public class ItemSphereModule extends ItemProjectorModule{
    public ItemSphereModule(Properties properties) {
        super(properties);
    }

    @Override
    public String getModuleType() {
        return EnumProjectorModule.Sphere.getString();
    }
    @Override
    public boolean enabledFoci() {
        return false;
    }
}
