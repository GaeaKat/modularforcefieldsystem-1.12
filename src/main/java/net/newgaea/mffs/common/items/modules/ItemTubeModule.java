package net.newgaea.mffs.common.items.modules;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.newgaea.mffs.api.EnumProjectorModule;
import net.newgaea.mffs.api.IModularProjector;
import net.newgaea.mffs.common.init.MFFSItems;

import java.util.Set;

public class ItemTubeModule extends ItemProjectorModule {
    public ItemTubeModule(Properties properties) {
        super(properties);
    }

    @Override
    public EnumProjectorModule getModuleType() {
        return EnumProjectorModule.Tube;
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
        int tpx = 0;
        int tpy = 0;
        int tpz = 0;
        int x_offset_s = 0;
        int y_offset_s = 0;
        int z_offset_s = 0;
        int x_offset_e = 0;
        int y_offset_e = 0;
        int z_offset_e = 0;

        int distance = projector.distanceItems() + 2;
        int Strength = projector.strengthItems();

        if (projector.getSide() == Direction.DOWN || projector.getSide() == Direction.UP) {
            tpy = Strength;
            tpx = distance;
            tpz = distance;

            y_offset_s = Strength - Strength;
            if (projector.hasOption(MFFSItems.FIELD_MANIPULATOR_OPTION.get())) {
                if (projector.getSide() == Direction.DOWN) {
                    y_offset_e = Strength;
                }
                if (projector.getSide() == Direction.UP) {
                    y_offset_s = Strength;
                }
            }
        }

        if (projector.getSide() == Direction.NORTH || projector.getSide() == Direction.SOUTH) {
            tpy = distance;
            tpz = Strength;
            tpx = distance;

            z_offset_s = Strength - Strength;
            if (projector.hasOption(MFFSItems.FIELD_MANIPULATOR_OPTION.get())) {
                if (projector.getSide() == Direction.NORTH) {
                    z_offset_e = Strength;
                }
                if (projector.getSide() == Direction.SOUTH) {
                    z_offset_s = Strength;
                }
            }
        }
        if (projector.getSide() == Direction.WEST || projector.getSide() == Direction.EAST) {
            tpy = distance;
            tpz = distance;
            tpx = Strength;

            x_offset_s = Strength - Strength;
            if (projector.hasOption(MFFSItems.FIELD_MANIPULATOR_OPTION.get())) {
                if (projector.getSide() == Direction.WEST) {
                    x_offset_e = Strength;
                }
                if (projector.getSide() == Direction.EAST) {
                    x_offset_s = Strength;
                }
            }
        }

        for (int z1 = 0 - tpz + z_offset_s; z1 <= tpz - z_offset_e; z1++) {
            for (int x1 = 0 - tpx + x_offset_s; x1 <= tpx - x_offset_e; x1++) {
                for (int y1 = 0 - tpy + y_offset_s; y1 <= tpy - y_offset_e; y1++) {
                    int tpx_temp = tpx;
                    int tpy_temp = tpy;
                    int tpz_temp = tpz;

                    if (tpx == Strength
                            && (projector.getSide() == Direction.WEST || projector.getSide() == Direction.EAST)) {
                        tpx_temp += 1;
                    }
                    if (tpy == Strength
                            && (projector.getSide() == Direction.UP || projector.getSide() == Direction.DOWN)) {
                        tpy_temp += 1;
                    }
                    if (tpz == Strength
                            && (projector.getSide() == Direction.NORTH || projector.getSide() == Direction.SOUTH)) {
                        tpz_temp += 1;
                    }

                    if ((x1 == 0 - tpx_temp || x1 == tpx_temp
                            || y1 == 0 - tpy_temp || y1 == tpy_temp
                            || z1 == 0 - tpz_temp || z1 == tpz_temp)
                            && ((projector.getPos().getY() + y1) >= 0)) {
                        fieldPoints.add(new BlockPos(x1, y1, z1));
                    } else {
                        //ffInterior.add(new PointXYZ(x1, y1, z1, 0));
                    }
                }
            }
        }
    }
}
