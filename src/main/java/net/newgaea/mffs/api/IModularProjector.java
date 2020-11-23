package net.newgaea.mffs.api;

import net.minecraft.util.Direction;

public interface IModularProjector {
    Direction getSide();
    int focusItems(Direction direction);
    int strengthItems();
    int distanceItems();
}
