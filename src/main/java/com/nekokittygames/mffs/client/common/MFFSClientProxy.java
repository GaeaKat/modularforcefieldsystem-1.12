package com.nekokittygames.mffs.client.common;

import com.nekokittygames.mffs.common.blocks.MFFSBlocks;
import com.nekokittygames.mffs.common.common.MFFSCommonProxy;
import com.nekokittygames.mffs.common.items.MFFSItems;

/**
 * Created by katsw on 03/04/2016.
 */
public class MFFSClientProxy extends MFFSCommonProxy {

    @Override
    public void setupBlocks() {
        super.setupBlocks();
        MFFSBlocks.setupClientBlocks();
    }

    @Override
    public void setupItems() {
        super.setupItems();
        MFFSItems.setupClientItems();

    }
}
