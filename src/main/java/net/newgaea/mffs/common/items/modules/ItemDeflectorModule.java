package net.newgaea.mffs.common.items.modules;

import net.newgaea.mffs.api.EnumProjectorModule;

public class ItemDeflectorModule extends ItemProjectorModule{
    public ItemDeflectorModule(Properties properties) {
        super(properties);
    }

    @Override
    public String getModuleType() {
        return EnumProjectorModule.Deflector.getString();
    }
}
