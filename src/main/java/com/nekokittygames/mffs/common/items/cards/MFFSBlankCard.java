package com.nekokittygames.mffs.common.items.cards;

import com.nekokittygames.mffs.common.libs.LibItemNames;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Katrina on 20/04/2016.
 */
public class MFFSBlankCard extends MFFSCard {

    public MFFSBlankCard()
    {
        this.setUnlocalizedName(LibItemNames.BLANK_CARD);
        this.setRegistryName(LibItemNames.BLANK_CARD);
        GameRegistry.register(this);
    }
}
