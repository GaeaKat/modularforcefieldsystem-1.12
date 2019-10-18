package com.nekokittygames.mffs;

import com.nekokittygames.mffs.client.proxy.ClientProxy;
import com.nekokittygames.mffs.common.config.MFFSConfig;
import com.nekokittygames.mffs.common.config.MFFSSetup;
import com.nekokittygames.mffs.common.init.MFFSBlocks;
import com.nekokittygames.mffs.common.libs.LibMisc;
import com.nekokittygames.mffs.common.proxy.IProxy;
import com.nekokittygames.mffs.common.proxy.ServerProxy;
import com.nekokittygames.mffs.datagen.conditions.GeneratorEnabled;
import net.minecraft.block.Block;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MFFSConfig.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MFFSConfig.COMMON_CONFIG);

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);


        MFFSConfig.loadConfig(MFFSConfig.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("modularforcefieldsystem-client.toml"));
        MFFSConfig.loadConfig(MFFSConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("modularforcefieldsystem-common.toml"));
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private static void registerOre(Biome biome) {
        if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND)
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, MFFSBlocks.MONAZIT_ORE.getDefaultState(), 8), Placement.COUNT_RANGE, new CountRangeConfig(20, 0, 0, 64)));
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        proxy.init();
        // some preinit code

        registerOre();
    }

    private void registerOre() {
        ForgeRegistries.BIOMES.forEach(
                MFFS::registerOre);

    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client

    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
    }

    private void processIMC(final InterModProcessEvent event)
    {

    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts

    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
        }
    }
}
