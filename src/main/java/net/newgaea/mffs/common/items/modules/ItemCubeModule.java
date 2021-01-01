package net.newgaea.mffs.common.items.modules;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.newgaea.mffs.api.EnumProjectorModule;
import net.newgaea.mffs.api.IModularProjector;
import net.newgaea.mffs.common.init.MFFSItems;

import java.util.Set;

public class ItemCubeModule extends ItemProjectorModule {
    public ItemCubeModule(Properties properties) {
        super(properties);
    }

    @Override
    public String getModuleType() {
        return EnumProjectorModule.Cube.getString();
    }
    @Override
    public boolean enabledFoci() {
        return false;
    }

    @Override
    public boolean enabledStrength() {
        return false;
    }

    @Override
    public boolean enabledDistance() {
        return true;
    }

    @Override
    public boolean is3D() {
        return true;
    }

    @Override
    public void calculateField(IModularProjector projector, Set<BlockPos> fieldPoints) {

    }

    @Override
    public void calculateField(IModularProjector projector, Set<BlockPos> fieldPoints, Set<BlockPos> interior) {

        int radius = projector.distanceItems() + 4;
        TileEntity te = (TileEntity) projector;

        int yDown = radius;
        int yTop = radius;
        if (projector.getPos().getY() + radius > 255) {
            yTop = 255 - projector.getPos().getY();
        }

        if (projector.hasOption(MFFSItems.FIELD_MANIPULATOR_OPTION.get())) {
            yDown = 0;
        }

        for (int y1 = -yDown; y1 <= yTop; y1++) {
            for (int x1 = -radius; x1 <= radius; x1++) {
                for (int z1 = -radius; z1 <= radius; z1++) {

                    if (x1 == -radius || x1 == radius || y1 == -radius
                            || y1 == yTop || z1 == -radius || z1 == radius) {
                        fieldPoints.add(new BlockPos(x1,y1,z1));
                        //ffLocs.add(new PointXYZ(x1, y1, z1, 0));
                    } else {
                        //ffInterior.add(new PointXYZ(x1, y1, z1, 0));
                    }
                }
            }
        }
    }
}
