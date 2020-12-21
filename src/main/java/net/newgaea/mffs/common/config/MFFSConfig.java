package net.newgaea.mffs.common.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class MFFSConfig {

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_POWER = "power";
    public static final String SUBCATEGORY_GENERATOR = "generator";
    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.BooleanValue GENERATOR_ENABLED;
    public static  ForgeConfigSpec.IntValue GENERATOR_GENERATE;

    public static ForgeConfigSpec.IntValue FORCEFIELD_PER_TICK;
    static {
        COMMON_BUILDER.comment("General Settings").push(CATEGORY_GENERAL);
        setupGeneralSettings();
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Power settings").push(CATEGORY_POWER);
        setupGeneratorSettings();
        COMMON_BUILDER.pop();
        COMMON_CONFIG=COMMON_BUILDER.build();
        CLIENT_CONFIG=CLIENT_BUILDER.build();
    }

    private static void setupGeneralSettings() {
        FORCEFIELD_PER_TICK = COMMON_BUILDER.comment("Blocks per tick generated").defineInRange("forcefield_per_tick",10,0,Integer.MAX_VALUE);
    }

    private static void setupGeneratorSettings() {
        COMMON_BUILDER.comment("Generator Settings").push(SUBCATEGORY_GENERATOR);
        GENERATOR_ENABLED = COMMON_BUILDER.comment("Is the Generator block Enabled").define("generator_enabled",true);
        GENERATOR_GENERATE = COMMON_BUILDER.comment("Power generation per Monazit Crystal").defineInRange("generator_generate",1000,0,Integer.MAX_VALUE);

        COMMON_BUILDER.pop();
    }


    public static void loadConfig(ForgeConfigSpec spec, Path path)
    {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
        configData.load();
        spec.setConfig(configData);
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {

    }

}
