package com.nekokittygames.mffs.common.upgrades;

import com.nekokittygames.mffs.common.tileentities.TileMFFS;

public abstract class BaseUpgrade {

    public abstract void onInsert(TileMFFS tile);
    public abstract void onRemove(TileMFFS tile);
    public abstract void onTick(TileMFFS tile);
}
