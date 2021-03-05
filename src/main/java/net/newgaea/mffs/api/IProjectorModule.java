package net.newgaea.mffs.api;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Set;

public interface IProjectorModule {
    EnumProjectorModule getModuleType();
    boolean enabledFoci();
    boolean enabledStrength();
    boolean enabledDistance();
    boolean is3D();
    void calculateField(IModularProjector projector,
                        Set<BlockPos> fieldPoints);

    void calculateField(IModularProjector projector,
                        Set<BlockPos> fieldPoints,
                        Set<BlockPos> interior);

}
