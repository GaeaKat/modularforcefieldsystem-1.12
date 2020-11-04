package net.newgaea.mffs.data;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;
import net.newgaea.mffs.common.init.MFFSBlocks;
import net.newgaea.mffs.common.libs.LibBlocks;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MFFSDrops extends BaseLootTableProvider {
    public MFFSDrops(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        lootTables.put(MFFSBlocks.MONAZIT_ORE.get(),createMundaneTable(LibBlocks.MONAZIT_ORE,MFFSBlocks.MONAZIT_ORE.get()));
    }





}
