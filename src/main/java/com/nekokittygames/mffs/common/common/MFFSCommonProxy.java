package com.nekokittygames.mffs.common.common;

import com.nekokittygames.mffs.common.blocks.MFFSBlocks;
import com.nekokittygames.mffs.common.items.MFFSItems;

/**
 * Created by katsw on 03/04/2016.
 */
public class MFFSCommonProxy {



    public  void setupBlocks()
    {
        MFFSBlocks.createBlocks();
    }



    public void setupItems()
    {
        MFFSItems.createItems();
    }
}
