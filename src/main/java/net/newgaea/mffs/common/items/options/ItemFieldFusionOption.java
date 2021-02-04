package net.newgaea.mffs.common.items.options;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.newgaea.mffs.common.data.Grid;
import net.newgaea.mffs.common.tiles.TileProjector;

import java.util.Map;

public class ItemFieldFusionOption  extends ItemOption{
    public ItemFieldFusionOption(Properties properties) {
        super(properties);
    }

    public boolean checkFieldFusionInfluence(BlockPos pos, World world, TileProjector projector) {
        Map<Integer, TileProjector> InnerMap = null;
        InnerMap = Grid.getWorldGrid(world).getFusions();
        for (TileProjector tileentity : InnerMap.values()) {

            boolean logicswitch = false;
            if (!projector.isPowerSourceItem())
                logicswitch = tileentity.getPowerSourceID() == projector
                        .getPowerSourceID()
                        && tileentity.getDeviceID() != projector.getDeviceID();

            if (logicswitch && tileentity.isActive()) {
                for (BlockPos tpng : tileentity.getInteriorPoints()) {
                    if (tpng.equals(pos))
                        return true;
                }
            }
        }
        return false;
    }
}
