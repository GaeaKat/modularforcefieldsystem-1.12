package net.newgaea.mffs.common.items.modules;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.newgaea.mffs.api.EnumProjectorModule;
import net.newgaea.mffs.api.IModularProjector;

import java.util.Set;

public class ItemWallModule extends ItemProjectorModule {
    public ItemWallModule(Properties properties) {
        super(properties);
    }


    @Override
    public String getModuleType() {
        return EnumProjectorModule.Wall.getString();
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

        int left = projector.focusItems(Direction.WEST);
        int right = projector.focusItems(Direction.EAST);
        int up = projector.focusItems(Direction.UP);
        int down = projector.focusItems(Direction.DOWN);
        int distance = projector.distanceItems();
        int strength = projector.strengthItems();
        for (int x1 = 0 - left; x1 < right + 1; x1++) {
            for (int z1 = 0 - down; z1 < up + 1; z1++) {
                for (int y1 = 1; y1 < strength; y1++) {
                    switch (projector.getSide()) {
                        case DOWN:
                            tpy = y1 - y1 - y1 - distance;
                            tpx = x1;
                            tpz = z1 - z1 - z1;
                            break;
                        case UP:
                            tpy = y1 + distance;
                            tpx = x1;
                            tpz = z1 - z1 - z1;
                            break;
                        case NORTH:
                            tpz = y1 - y1 - y1 - distance;
                            tpx = x1 - x1 - x1;
                            tpy = z1;
                            break;
                        case SOUTH:
                            tpz = y1 + distance;
                            tpx = x1;
                            tpy = z1;
                            break;
                        case WEST:
                            tpx = y1 - y1 - y1 - distance;
                            tpz = x1;
                            tpy = z1;
                            break;
                        case EAST:
                            tpx = y1 + distance;
                            tpz = x1 - x1 - x1;
                            tpy = z1;
                            break;
                    }
                    if ((projector.getSide() == Direction.UP || projector.getSide() == Direction.DOWN) &&
                            ((tpx == 0 && tpz != 0)
                                    || (tpz == 0 && tpx != 0) || (tpz == 0 && tpx == 0))
                            || (projector.getSide() == Direction.NORTH || projector.getSide() == Direction.SOUTH)
                            && ((tpx == 0 && tpy != 0)
                            || (tpy == 0 && tpx != 0) || (tpy == 0 && tpx == 0))
                            || (projector.getSide() == Direction.EAST || projector.getSide() == Direction.WEST)
                            && ((tpz == 0 && tpy != 0)
                            || (tpy == 0 && tpz != 0) || (tpy == 0 && tpz == 0))
                    ) {
                        fieldPoints.add(new BlockPos(tpx, tpy, tpz));
                    }
                }
            }
        }
    }

    @Override
    public void calculateField(IModularProjector projector, Set<BlockPos> fieldPoints, Set<BlockPos> interior) {

    }
}
