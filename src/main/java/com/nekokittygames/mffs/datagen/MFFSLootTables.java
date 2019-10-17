package com.nekokittygames.mffs.datagen;

import com.nekokittygames.mffs.common.init.MFFSBlocks;
import com.nekokittygames.mffs.common.libs.LibBlocks;
import net.minecraft.data.DataGenerator;

public class MFFSLootTables extends BaseLootTableProvider {

    public MFFSLootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        lootTables.put(MFFSBlocks.MONAZIT_ORE,createStandardTable(LibBlocks.MONAZIT_ORE,MFFSBlocks.MONAZIT_ORE));
    }
}
