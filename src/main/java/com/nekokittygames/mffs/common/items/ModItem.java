package com.nekokittygames.mffs.common.items;

import com.nekokittygames.mffs.common.common.MFFSCreativeTab;
import net.minecraft.item.Item;

/**
 * Created by Katrina on 20/04/2016.
 */
public class ModItem extends Item {



    public ModItem()
    {
        if(ShouldRegisterInTab())
            this.setCreativeTab(MFFSCreativeTab.get());
    }

    public void initBlock()
    {

    }

    public  boolean ShouldRegisterInTab()
    {
        return true;
    }
}
