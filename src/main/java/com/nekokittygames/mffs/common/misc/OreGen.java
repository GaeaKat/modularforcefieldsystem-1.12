package com.nekokittygames.mffs.common.misc;

import com.nekokittygames.mffs.common.init.MFFSBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class OreGen {
    public static ConfiguredFeature<?, ?> MONAZIT_FEATURE;

    public static int MONAZIT_MAX_VEIN_SIZE = 8;
    public static int MONAZIT_COUNT_PER_CHUNK = 20;
    public static int MONAZIT_BOTTOM_OFFSET = 0;
    public static int MONAZIT_TOP_OFFSET = 0;
    public static int MONAZIT_MAXIMUM = 128;

    public static void registerOre() {
        MONAZIT_FEATURE = getSimpleOreFeature(MFFSBlocks.MONAZIT_ORE.getDefaultState(), MONAZIT_MAX_VEIN_SIZE, (new CountRangeConfig(MONAZIT_COUNT_PER_CHUNK, MONAZIT_BOTTOM_OFFSET, MONAZIT_TOP_OFFSET, MONAZIT_MAXIMUM)));
        ForgeRegistries.BIOMES.forEach(biome -> {
            if(isValidBiome(biome)){
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, MONAZIT_FEATURE);
            }
        });
    }

    public static boolean isValidBiome(Biome biome) {
        return biome.getCategory() != Biome.Category.THEEND && biome.getCategory() != Biome.Category.NETHER;
    }

    public static ConfiguredFeature<?, ?> getSimpleOreFeature(BlockState state, int maxVeinSize, CountRangeConfig config){
        return Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, state, maxVeinSize)).withPlacement(Placement.COUNT_RANGE.configure(config));
    }

}
