package net.newgaea.mffs.common.items.modules;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.newgaea.mffs.api.EnumProjectorModule;
import net.newgaea.mffs.api.IModularProjector;
import org.lwjgl.system.CallbackI;

import java.util.Set;

public class ItemDeflectorModule extends ItemProjectorModule{
    public ItemDeflectorModule(Properties properties) {
        super(properties);
    }

    @Override
    public EnumProjectorModule getModuleType() {
        return EnumProjectorModule.Deflector;
    }
    @Override
    public boolean enabledFoci() {
        return true;
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
        return false;
    }

    @Override
    public void calculateField(IModularProjector projector, Set<BlockPos> fieldPoints) {
        int tpx = 0;
        int tpy = 0;
        int tpz = 0;

        for (int x1 = 0 - projector.focusItems(Direction.WEST); x1 < projector
                .focusItems(Direction.EAST) + 1; x1++) {
            for (int z1 = 0 - projector.focusItems(Direction.NORTH); z1 < projector
                    .focusItems(Direction.SOUTH) + 1; z1++) {
                if (projector.getSide() == Direction.UP) {
                    tpy = 0 - projector.distanceItems() - 1;
                    tpx = x1;
                    tpz = z1;
                }

                if (projector.getSide() == Direction.DOWN) {
                    tpy = 0 + projector.distanceItems() + 1;
                    tpx = x1;
                    tpz = z1;
                }

                if (projector.getSide() == Direction.NORTH) {
                    tpz = 0 - projector.distanceItems() - 1;
                    tpy = z1 - z1 - z1;
                    tpx = x1 - x1 - x1;
                }

                if (projector.getSide() == Direction.SOUTH) {
                    tpz = 0 + projector.distanceItems() + 1;
                    tpy = z1 - z1 - z1;
                    tpx = x1;
                }

                if (projector.getSide() == Direction.WEST) {
                    tpx = 0 - projector.distanceItems() - 1;
                    tpy = z1 - z1 - z1;
                    tpz = x1;
                }
                if (projector.getSide() == Direction.EAST) {
                    tpx = 0 + projector.distanceItems() + 1;
                    tpy = z1 - z1 - z1;
                    tpz = x1 - x1 - x1;
                }

                fieldPoints.add(new BlockPos(tpx,tpy,tpz));
            }
        }
    }

    @Override
    public void calculateField(IModularProjector projector, Set<BlockPos> fieldPoints, Set<BlockPos> interior) {

    }
}
