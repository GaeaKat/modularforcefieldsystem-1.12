package net.newgaea.mffs.api;

import net.minecraft.tileentity.TileEntity;

public interface IProjectorModule {
    String getModuleType();
    boolean enabledFoci();
}
