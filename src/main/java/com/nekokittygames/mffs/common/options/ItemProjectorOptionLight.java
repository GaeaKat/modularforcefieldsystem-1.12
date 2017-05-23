package com.nekokittygames.mffs.common.options;

import com.nekokittygames.mffs.libs.LibItemNames;
import com.nekokittygames.mffs.libs.LibMisc;

/**
 * Created by katsw on 01/12/2016.
 */
public class ItemProjectorOptionLight extends ItemProjectorOptionBase {
    public ItemProjectorOptionLight() {
        super();
        setUnlocalizedName(LibMisc.UNLOCALIZED_PREFIX+ LibItemNames.OPTION_LIGHT);
        setRegistryName(LibItemNames.OPTION_LIGHT);
    }
}
