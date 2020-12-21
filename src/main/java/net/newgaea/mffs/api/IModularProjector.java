package net.newgaea.mffs.api;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public interface IModularProjector {
    Direction getSide();
    int focusItems(Direction direction);
    int strengthItems();
    int distanceItems();
    List<IProjectorOption> options();
    default boolean hasOption(IProjectorOption option) {
        return options().contains(option);
    }
    BlockPos getPos();
}
