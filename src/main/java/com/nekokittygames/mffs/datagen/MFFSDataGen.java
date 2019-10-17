package com.nekokittygames.mffs.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MFFSDataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator=event.getGenerator();
        generator.addProvider(new MFFSRecipes(generator));
        generator.addProvider(new MFFSTags(generator));
        generator.addProvider(new MFFSLootTables(generator));
    }
}
