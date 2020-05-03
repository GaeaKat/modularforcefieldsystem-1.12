package com.nekokittygames.mffs.datagen;

import com.nekokittygames.mffs.common.init.MFFSBlocks;
import com.nekokittygames.mffs.common.init.MFFSRegistration;
import com.nekokittygames.mffs.common.libs.LibBlocks;
import net.minecraft.data.DataGenerator;

public class MFFSLootTables extends BaseLootTableProvider {

    public MFFSLootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        lootTables.put(MFFSRegistration.Blocks.MONAZIT_ORE.get(),createStandardTable(LibBlocks.MONAZIT_ORE,MFFSRegistration.Blocks.MONAZIT_ORE.get()));
    }
}
