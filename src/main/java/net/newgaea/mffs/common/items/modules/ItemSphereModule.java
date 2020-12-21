package net.newgaea.mffs.common.items.modules;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.newgaea.mffs.api.EnumProjectorModule;
import net.newgaea.mffs.api.IModularProjector;
import net.newgaea.mffs.common.init.MFFSItems;

import java.util.Set;

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

    @Override
    public boolean enabledStrength() {
        return true;
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

        int yDown = radius;

        if (projector.hasOption(MFFSItems.FIELD_MANIPULATOR_OPTION.get())) {
            yDown = 0;
        }

        for (int y1 = -yDown; y1 <= radius; y1++) {
            for (int x1 = -radius; x1 <= radius; x1++) {
                for (int z1 = -radius; z1 <= radius; z1++) {
                    int dx = x1;
                    int dy = y1;
                    int dz = z1;

                    int dist = (int) Math.round(Math.sqrt(dx * dx + dy * dy
                            + dz * dz));

                    if (dist <= radius
                            && dist > (radius - (projector.strengthItems() + 1))) {
                        fieldPoints.add(new BlockPos(x1, y1, z1));
                    } else if (dist <= radius) {
                        //ffInterior.add(new PointXYZ(x1, y1, z1, 0));
                    }
                }
            }
        }
    }
}
