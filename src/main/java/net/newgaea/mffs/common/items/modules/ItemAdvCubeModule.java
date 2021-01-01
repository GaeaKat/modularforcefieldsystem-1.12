package net.newgaea.mffs.common.items.modules;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.newgaea.mffs.api.EnumProjectorModule;
import net.newgaea.mffs.api.IModularProjector;
import net.newgaea.mffs.common.init.MFFSItems;

import java.util.Set;

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

    @Override
    public boolean enabledStrength() {
        return true;
    }

    @Override
    public boolean enabledDistance() {
        return false;
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

        int xMout = projector.focusItems(Direction.WEST);
        int xPout = projector.focusItems(Direction.EAST);
        int zMout = projector.focusItems(Direction.SOUTH);
        int zPout = projector.focusItems(Direction.NORTH);
        int distance = projector.distanceItems();
        int Strength = projector.strengthItems() + 2;

        for (int y1 = 0; y1 <= Strength; y1++) {
            for (int x1 = 0 - xMout; x1 < xPout + 1; x1++) {
                for (int z1 = 0 - zPout; z1 < zMout + 1; z1++) {
                    if (projector.getSide() == Direction.UP) {

                        tpy = y1 - y1 - y1 + 1;
                        tpx = x1;
                        tpz = z1;
                    }

                    if (projector.getSide() == Direction.DOWN) {

                        tpy = y1 - 1;
                        tpx = x1;
                        tpz = z1;
                    }

                    if (projector.getSide() == Direction.NORTH) {

                        tpz = y1 - y1 - y1 + 1;
                        tpy = z1 - z1 - z1;
                        tpx = x1 - x1 - x1;
                    }

                    if (projector.getSide() == Direction.SOUTH) {

                        tpz = y1 - 1;
                        tpy = z1 - z1 - z1;
                        tpx = x1;
                    }

                    if (projector.getSide() == Direction.WEST) {

                        tpx = y1 - y1 - y1 + 1;
                        tpy = z1 - z1 - z1;
                        tpz = x1;
                    }
                    if (projector.getSide() == Direction.EAST) {

                        tpx = y1 - 1;
                        tpy = z1 - z1 - z1;
                        tpz = x1 - x1 - x1;
                    }

                    if (y1 == 0 || y1 == Strength || x1 == 0 - xMout
                            || x1 == xPout || z1 == 0 - zPout || z1 == zMout) {
                        if (projector.hasOption(MFFSItems.FIELD_MANIPULATOR_OPTION.get())) {
                            switch (projector.getSide()) {
                                case UP:
                                    if ((projector.getPos().getY() + tpy) > (projector.getPos().getY()))
                                        continue;
                                    break;
                                case DOWN:
                                    if ((projector.getPos().getY() + tpy) < (projector.getPos().getY()))
                                        continue;
                                    break;
                                case NORTH:
                                    if ((projector.getPos().getZ() + tpz) > (projector.getPos().getZ()))
                                        continue;
                                    break;
                                case SOUTH:
                                    if ((projector.getPos().getZ() + tpz) < (projector.getPos().getZ()))
                                        continue;
                                    break;
                                case WEST:
                                    if ((projector.getPos().getX() + tpx) > (projector.getPos().getX()))
                                        continue;
                                    break;
                                case EAST:
                                    if ((projector.getPos().getX() + tpx) < (projector.getPos().getX()))
                                        continue;
                                    break;
                            }
                        }
                        fieldPoints.add(new BlockPos(tpx,tpy,tpz));
                        //ffLocs.add(new PointXYZ(tpx, tpy, tpz, 0));

                    } else {

                        //ffInterior.add(new PointXYZ(tpx, tpy, tpz, 0));
                    }
                }
            }
        }
    }
}
