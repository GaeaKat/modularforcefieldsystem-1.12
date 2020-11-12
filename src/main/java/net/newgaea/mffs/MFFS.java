package net.newgaea.mffs;


import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.newgaea.mffs.client.proxy.ClientProxy;
import net.newgaea.mffs.common.config.MFFSConfig;
import net.newgaea.mffs.common.init.MFFSBlocks;
import net.newgaea.mffs.common.init.MFFSContainer;
import net.newgaea.mffs.common.init.MFFSItems;
import net.newgaea.mffs.common.init.MFFSTiles;
import net.newgaea.mffs.common.libs.LibMisc;
import net.newgaea.mffs.common.proxy.CommonProxy;
import net.newgaea.mffs.common.proxy.IProxy;
import net.newgaea.mffs.world.OreGen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vazkii.patchouli.api.PatchouliAPI;

@Mod(LibMisc.MOD_ID)
public class MFFS {

    private static final Logger LOGGER = LogManager.getLogger();

    public static IProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public MFFS() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MFFSConfig.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MFFSConfig.COMMON_CONFIG);

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        MFFSConfig.loadConfig(MFFSConfig.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("mffs-client.toml"));
        MFFSConfig.loadConfig(MFFSConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("mffs-common.toml"));
        MFFSBlocks.init();
        MFFSItems.init();
        MFFSContainer.init();
        MFFSTiles.init();
        MinecraftForge.EVENT_BUS.register(new OreGen());
        PatchouliAPI.instance.setConfigFlag("mffs:generatorEnabled",MFFSConfig.GENERATOR_ENABLED.get());
    }

    private void setup(final FMLCommonSetupEvent event) {
        proxy.init();
    }
    public static Logger getLog() {
        return LOGGER;
    }
}
