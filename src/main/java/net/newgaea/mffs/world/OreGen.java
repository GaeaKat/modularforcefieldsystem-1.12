package net.newgaea.mffs.world;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.DepthAverageConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.newgaea.mffs.common.init.MFFSBlocks;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
public class OreGen {

    public static int MONAZIT_MAX_VEIN_SIZE = 8;
    public static int MONAZIT_BASELINE=15;
    public static int MONAZIT_SPREAD=16;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public  void generateOres(BiomeLoadingEvent event) {
        ConfiguredFeature<?, ?> ORE_MONAZIT = Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, MFFSBlocks.MONAZIT_ORE.get().getDefaultState(), MONAZIT_MAX_VEIN_SIZE )).range(64).square().func_242731_b(20);
        event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ORE_MONAZIT);
    }
}
