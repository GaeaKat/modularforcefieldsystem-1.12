package dev.katcodes.mffs.common.world;

import dev.katcodes.mffs.common.init.MFFSBlocks;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import org.jetbrains.annotations.NotNull;

public class OreGeneration {

    private OreGeneration() {}

    private static Holder<PlacedFeature> overworldOregenMonazit;
    public static void registerOres() {
        OreConfiguration overworldMonazitConfig = new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES, MFFSBlocks.MONAZIT_ORE.get().defaultBlockState(), 20);
        overworldOregenMonazit = registerPlacedOreFeature("overworld_monazit_ore",new ConfiguredFeature<>(Feature.ORE,overworldMonazitConfig), CountPlacement.of(100),
                InSquarePlacement.spread(),
                BiomeFilter.biome(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(0),VerticalAnchor.absolute(75)));
    }

    /**
     * The registerPlacedOreFeature function registers a feature to be placed in the world as an ore block.
     *
     *
     *
     * @param registryName Used to Register the feature with a name.
     * @param feature Used to Specify the type of feature that is being registered.
     * @param placementModifiers Used to Specify the placement of a feature.
     * @return A holder of a placedfeature.
     *
     */
    private static <C extends FeatureConfiguration, F extends Feature<C>> @NotNull Holder<PlacedFeature> registerPlacedOreFeature(String registryName, ConfiguredFeature<C, F> feature, PlacementModifier... placementModifiers) {
        return PlacementUtils.register(registryName, Holder.direct(feature), placementModifiers);
    }

    /**
     * The onBiomeLoadingEvent function is called when a biome is loaded.
     * It adds the ore generation to the world.
     *
     * @param event Used to Get the category of biome that is currently being loaded.
     */
    public static void onBiomeLoadingEvent(BiomeLoadingEvent event) {
        if (event.getCategory() != Biome.BiomeCategory.NETHER && event.getCategory() != Biome.BiomeCategory.THEEND) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, overworldOregenMonazit);
        }
    }
}
