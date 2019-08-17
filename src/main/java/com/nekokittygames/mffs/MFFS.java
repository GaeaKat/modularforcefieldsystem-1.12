package com.nekokittygames.mffs;

import com.nekokittygames.mffs.client.proxy.ClientProxy;
import com.nekokittygames.mffs.common.config.MFFSSetup;
import com.nekokittygames.mffs.common.init.MFFSBlocks;
import com.nekokittygames.mffs.common.libs.LibMisc;
import com.nekokittygames.mffs.common.proxy.IProxy;
import com.nekokittygames.mffs.common.proxy.ServerProxy;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(LibMisc.MOD_ID)
public class MFFS
{
    public static Logger getLOGGER() {
        return LOGGER;
    }

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    public static MFFSSetup setup = new MFFSSetup();

    public MFFS() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        proxy.init();
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        registerOre();
    }

    private void registerOre() {
        Objects.requireNonNull(BiomeManager.getBiomes(BiomeManager.BiomeType.WARM)).forEach((BiomeManager.BiomeEntry biomeEntry) -> biomeEntry.biome.addFeature(
                GenerationStage.Decoration.UNDERGROUND_ORES,Biome.createDecoratedFeature(Feature.ORE,new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, MFFSBlocks.MONAZIT_ORE.getDefaultState(),8), Placement.COUNT_RANGE,new CountRangeConfig(20, 0, 0, 64))
        ));
        Objects.requireNonNull(BiomeManager.getBiomes(BiomeManager.BiomeType.COOL)).forEach((BiomeManager.BiomeEntry biomeEntry) -> biomeEntry.biome.addFeature(
                GenerationStage.Decoration.UNDERGROUND_ORES,Biome.createDecoratedFeature(Feature.ORE,new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, MFFSBlocks.MONAZIT_ORE.getDefaultState(),8), Placement.COUNT_RANGE,new CountRangeConfig(20, 0, 0, 64))
        ));
        Objects.requireNonNull(BiomeManager.getBiomes(BiomeManager.BiomeType.DESERT)).forEach((BiomeManager.BiomeEntry biomeEntry) -> biomeEntry.biome.addFeature(
                GenerationStage.Decoration.UNDERGROUND_ORES,Biome.createDecoratedFeature(Feature.ORE,new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, MFFSBlocks.MONAZIT_ORE.getDefaultState(),8), Placement.COUNT_RANGE,new CountRangeConfig(20, 0, 0, 64))
        ));
        Objects.requireNonNull(BiomeManager.getBiomes(BiomeManager.BiomeType.ICY)).forEach((BiomeManager.BiomeEntry biomeEntry) -> biomeEntry.biome.addFeature(
                GenerationStage.Decoration.UNDERGROUND_ORES,Biome.createDecoratedFeature(Feature.ORE,new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, MFFSBlocks.MONAZIT_ORE.getDefaultState(),8), Placement.COUNT_RANGE,new CountRangeConfig(20, 0, 0, 64))
        ));
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("modularforcefieldsystem", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
