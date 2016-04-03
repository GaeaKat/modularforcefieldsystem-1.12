package com.nekokittygames.mffs.common.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

/**
 * Created by katsw on 03/04/2016.
 */
public class MFFSCreativeTab extends CreativeTabs {

    public static MFFSCreativeTab instance;

    public MFFSCreativeTab() {
        super("MFFS");
    }

    public static MFFSCreativeTab get()
    {
        if(instance==null)
            instance=new MFFSCreativeTab();
        return instance;
    }

    @Override
    public Item getTabIconItem() {
        return Items.item_frame;
    }
}
