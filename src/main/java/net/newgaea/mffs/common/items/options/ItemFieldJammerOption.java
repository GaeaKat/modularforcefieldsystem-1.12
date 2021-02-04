package net.newgaea.mffs.common.items.options;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.newgaea.mffs.api.IModularProjector;
import net.newgaea.mffs.common.data.Grid;
import net.newgaea.mffs.common.tiles.TileProjector;

import java.util.Map;

public class ItemFieldJammerOption extends ItemOption{
    public ItemFieldJammerOption(Properties properties) {
        super(properties);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }


    public boolean CheckJammerInfluence(BlockPos pos, World world, TileProjector projector) {
        Map<Integer, TileProjector> InnerMap=null;
        InnerMap = Grid.getWorldGrid(world).getJammers();
        for(TileProjector tileProjector: InnerMap.values()) {
            boolean logic=false;
            logic = tileProjector.getPowerSourceID()!=projector.getPowerSourceID();
            if(logic && tileProjector.isActive()) {
                for(BlockPos tpos:tileProjector.getInteriorPoints()) {
                    if(tpos.equals(pos)) {
                        projector.burnout();
                    }
                }
            }
        }
        return false;
    }
}
