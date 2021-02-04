package net.newgaea.mffs.common.items.modules;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.newgaea.mffs.api.EnumProjectorModule;
import net.newgaea.mffs.api.IModularProjector;

import java.util.Set;

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

        int xMout = projector.focusItems(Direction.WEST)+1;
        int xPout = projector.focusItems(Direction.EAST)+1;
        int zMout = projector.focusItems(Direction.SOUTH)+1;
        int zPout = projector.focusItems(Direction.NORTH)+1;
        int distance = projector.distanceItems();
        int Strength = projector.strengthItems() + 1;

        for (int y1 = 0; y1 <= Strength; y1++) {
            for (int x1 = 0 - xMout; x1 < xPout + 1; x1++) {
                for (int z1 = 0 - zPout; z1 < zMout + 1; z1++) {
                    if (projector.getSide() == Direction.UP) {

                        tpy = y1 - y1 - y1 - distance - 1;
                        tpx = x1;
                        tpz = z1;
                    }

                    if (projector.getSide() == Direction.DOWN) {

                        tpy = y1 + distance + 1;
                        tpx = x1;
                        tpz = z1;
                    }

                    if (projector.getSide() == Direction.NORTH) {

                        tpz = y1 - y1 - y1 - distance - 1;
                        tpy = z1 - z1 - z1;
                        tpx = x1 - x1 - x1;
                    }

                    if (projector.getSide() == Direction.SOUTH) {

                        tpz = y1 + distance + 1;
                        tpy = z1 - z1 - z1;
                        tpx = x1;
                    }

                    if (projector.getSide() == Direction.WEST) {

                        tpx = y1 - y1 - y1 - distance - 1;
                        tpy = z1 - z1 - z1;
                        tpz = x1;
                    }
                    if (projector.getSide() == Direction.EAST) {

                        tpx = y1 + distance + 1;
                        tpy = z1 - z1 - z1;
                        tpz = x1 - x1 - x1;
                    }

                    if (y1 == 0 || y1 == Strength || x1 == 0 - xMout
                            || x1 == xPout || z1 == 0 - zPout || z1 == zMout) {
                        fieldPoints.add(new BlockPos(tpx,tpy,tpz));

                    } else {

                        //ffInterior.add(new PointXYZ(tpx, tpy, tpz, 0));
                    }
                }
            }
        }
    }
}
