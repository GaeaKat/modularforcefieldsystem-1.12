package net.newgaea.mffs.common.items.modules;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.newgaea.mffs.api.EnumProjectorModule;
import net.newgaea.mffs.api.IModularProjector;

import java.util.Set;

public class ItemDiagWallModule extends ItemProjectorModule {
    public ItemDiagWallModule(Properties properties) {
        super(properties);
    }

    @Override
    public EnumProjectorModule getModuleType() {
        return EnumProjectorModule.Diagonal_Wall;
    }

    @Override
    public boolean enabledFoci() {
        return true;
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
        return false;
    }

    @Override
    public void calculateField(IModularProjector projector, Set<BlockPos> fieldPoints) {
        int tpx = 0;
        int tpy = 0;
        int tpz = 0;

        int xstart = 0;
        int xend = 0;

        int zstart = 0;
        int zend = 0;

        if (projector.focusItems(Direction.NORTH) > 0) {

            xend = Math.max(xend, Math.max(
                    projector.focusItems(Direction.NORTH),
                    projector.focusItems(Direction.EAST)));
            zend = Math.max(zend, Math.max(
                    projector.focusItems(Direction.NORTH),
                    projector.focusItems(Direction.EAST)));
        }

        if (projector.focusItems(Direction.SOUTH) > 0) {

            xstart = Math.max(xend, Math.max(
                    projector.focusItems(Direction.SOUTH),
                    projector.focusItems(Direction.WEST)));
            zstart = Math.max(zend, Math.max(
                    projector.focusItems(Direction.SOUTH),
                    projector.focusItems(Direction.WEST)));
        }

        if (projector.focusItems(Direction.WEST) > 0) {

            xend = Math.max(xend, Math.max(
                    projector.focusItems(Direction.NORTH),
                    projector.focusItems(Direction.WEST)));
            zstart = Math.max(zstart, Math.max(
                    projector.focusItems(Direction.NORTH),
                    projector.focusItems(Direction.WEST)));
        }

        if (projector.focusItems(Direction.EAST) > 0) {

            zend = Math.max(zend, Math.max(
                    projector.focusItems(Direction.SOUTH),
                    projector.focusItems(Direction.EAST)));
            xstart = Math.max(xstart, Math.max(
                    projector.focusItems(Direction.SOUTH),
                    projector.focusItems(Direction.EAST)));
        }

        for (int x1 = 0 - zstart; x1 < zend + 1; x1++) {
            for (int z1 = 0 - xstart; z1 < xend + 1; z1++) {
                for (int y1 = 1; y1 < projector
                        .strengthItems() + 1 + 1; y1++) {

                    if (projector.getSide() == Direction.DOWN) {
                        tpy = y1 - y1 - y1
                                - projector.distanceItems();
                        tpx = x1;
                        tpz = z1 - z1 - z1;
                    }

                    if (projector.getSide() == Direction.UP) {
                        tpy = y1 + projector.distanceItems();
                        tpx = x1;
                        tpz = z1 - z1 - z1;
                    }

                    if (projector.getSide() == Direction.NORTH) {
                        tpz = y1 - y1 - y1
                                - projector.distanceItems();
                        tpx = x1 - x1 - x1;
                        tpy = z1;
                    }

                    if (projector.getSide() == Direction.SOUTH) {
                        tpz = y1 + projector.distanceItems();
                        tpx = x1;
                        tpy = z1;
                    }

                    if (projector.getSide() == Direction.WEST) {
                        tpx = y1 - y1 - y1
                                - projector.distanceItems();
                        tpz = x1;
                        tpy = z1;
                    }
                    if (projector.getSide() == Direction.EAST) {
                        tpx = y1 + projector.distanceItems();
                        tpz = x1 - x1 - x1;
                        tpy = z1;
                    }

                    if ((projector.getSide() == Direction.UP || projector.getSide() == Direction.DOWN)
                            && (Math.abs(tpx) == Math.abs(tpz)
                            && ((tpx != 0 && tpz != 0) || (tpx == 0 && tpz == 0)) && ((projector
                            .focusItems(Direction.NORTH) != 0
                            && (tpx >= 0 && tpz <= 0)
                            && tpx <= projector
                            .focusItems(Direction.NORTH) && tpz <= projector
                            .focusItems(Direction.NORTH))
                            || (projector
                            .focusItems(Direction.SOUTH) != 0
                            && (tpx <= 0 && tpz >= 0)
                            && tpx <= projector
                            .focusItems(Direction.SOUTH) && tpz <= projector
                            .focusItems(Direction.SOUTH))
                            || (projector
                            .focusItems(Direction.EAST) != 0
                            && (tpx >= 0 && tpz >= 0)
                            && tpx <= projector
                            .focusItems(Direction.EAST) && tpz <= projector
                            .focusItems(Direction.EAST)) || (projector
                            .focusItems(Direction.WEST) != 0
                            && (tpx <= 0 && tpz <= 0)
                            && tpx <= projector
                            .focusItems(Direction.WEST) && tpz <= projector
                            .focusItems(Direction.WEST))))
                            || (projector.getSide() == Direction.NORTH || projector.getSide() == Direction.SOUTH)
                            && (Math.abs(tpx) == Math.abs(tpy)
                            && ((tpx != 0 && tpy != 0) || (tpx == 0 && tpy == 0)) && ((projector
                            .focusItems(Direction.NORTH) != 0
                            && (tpx >= 0 && tpy >= 0)
                            && tpx <= projector
                            .focusItems(Direction.NORTH) && tpy <= projector
                            .focusItems(Direction.NORTH))
                            || (projector
                            .focusItems(Direction.SOUTH) != 0
                            && (tpx <= 0 && tpy <= 0)
                            && tpx <= projector
                            .focusItems(Direction.SOUTH) && tpy <= projector
                            .focusItems(Direction.SOUTH))
                            || (projector
                            .focusItems(Direction.EAST) != 0
                            && (tpx >= 0 && tpy <= 0)
                            && tpx <= projector
                            .focusItems(Direction.EAST) && tpy <= projector
                            .focusItems(Direction.EAST)) || (projector
                            .focusItems(Direction.WEST) != 0
                            && (tpx <= 0 && tpy >= 0)
                            && tpx <= projector
                            .focusItems(Direction.WEST) && tpy <= projector
                            .focusItems(Direction.WEST))))
                            || (projector.getSide() == Direction.WEST || projector.getSide() == Direction.EAST)
                            && (Math.abs(tpz) == Math.abs(tpy)
                            && ((tpx != 0 && tpy != 0) || (tpz == 0 && tpy == 0)) && ((projector
                            .focusItems(Direction.NORTH) != 0
                            && (tpz >= 0 && tpy >= 0)
                            && tpz <= projector
                            .focusItems(Direction.NORTH) && tpy <= projector
                            .focusItems(Direction.NORTH))
                            || (projector
                            .focusItems(Direction.SOUTH) != 0
                            && (tpz <= 0 && tpy <= 0)
                            && tpz <= projector
                            .focusItems(Direction.SOUTH) && tpy <= projector
                            .focusItems(Direction.SOUTH))
                            || (projector
                            .focusItems(Direction.EAST) != 0
                            && (tpz >= 0 && tpy <= 0)
                            && tpz <= projector
                            .focusItems(Direction.EAST) && tpy <= projector
                            .focusItems(Direction.EAST)) || (projector
                            .focusItems(Direction.WEST) != 0
                            && (tpz <= 0 && tpy >= 0)
                            && tpz <= projector
                            .focusItems(Direction.WEST) && tpy <= projector
                            .focusItems(Direction.WEST))))

                    ) fieldPoints.add(new BlockPos(tpx, tpy, tpz));
                }
            }
        }
    }

    @Override
    public void calculateField(IModularProjector projector, Set<BlockPos> fieldPoints, Set<BlockPos> interior) {

    }
}
