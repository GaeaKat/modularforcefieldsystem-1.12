package net.newgaea.mffs.common.items.modules;

import net.minecraft.tileentity.TileEntity;
import net.newgaea.mffs.api.EnumProjectorModule;

public class ItemContainmentModule extends ItemProjectorModule{
    public ItemContainmentModule(Properties properties) {
        super(properties);
    }

    @Override
    public String getModuleType() {
        return EnumProjectorModule.Containment.getString();
    }

    @Override
    public boolean enabledFoci() {
        return true;
    }
}
