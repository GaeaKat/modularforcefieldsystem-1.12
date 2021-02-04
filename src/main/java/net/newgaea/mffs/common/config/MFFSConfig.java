package net.newgaea.mffs.common.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;
import java.util.List;

@Mod.EventBusSubscriber
public class MFFSConfig {

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_POWER = "power";
    public static final String SUBCATEGORY_GENERATOR = "generator";
    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
    private static final String SUBCATEGORY_COSTS = "costs";
    private static final String SUBCATEGORY_EXTRACTOR = "extractor";

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.BooleanValue ADVENTURE_MODE;
    public static ForgeConfigSpec.BooleanValue GENERATOR_ENABLED;

    public static ForgeConfigSpec.BooleanValue CHUNKLOADING_ENABLED;
    public static  ForgeConfigSpec.IntValue GENERATOR_GENERATE;

    public static ForgeConfigSpec.IntValue FORCEFIELD_PER_TICK;
    public static ForgeConfigSpec.IntValue BASE_FORCEFIELD_COST;
    public static ForgeConfigSpec.IntValue FIELD_CREATE_MODIFIER;
    public static ForgeConfigSpec.IntValue ZAPPER_MODIFIER;

    public static ForgeConfigSpec.IntValue MONAZIT_CELL_WORK_CYCLE;
    public static ForgeConfigSpec.IntValue MONAZIT_WORK_CYCLE;
    public static ForgeConfigSpec.IntValue FE_PER_WORKCYCLE;
    static {
        COMMON_BUILDER.comment("General Settings").push(CATEGORY_GENERAL);
        setupGeneralSettings();
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Power settings").push(CATEGORY_POWER);
        setupGeneratorSettings();
        setupPowerCosts();
        setupExtractorCosts();
        COMMON_BUILDER.pop();
        COMMON_CONFIG=COMMON_BUILDER.build();
        CLIENT_CONFIG=CLIENT_BUILDER.build();
    }

    private static void setupExtractorCosts() {
        COMMON_BUILDER.comment("Extractor Settings").push(SUBCATEGORY_EXTRACTOR);
        MONAZIT_CELL_WORK_CYCLE = COMMON_BUILDER.comment("Work Cycle of a Monazit Cell inside an Extractor").defineInRange("monazit_cell_work_cycle",230,0, Integer.MAX_VALUE);
        MONAZIT_WORK_CYCLE = COMMON_BUILDER.comment("Work Cycle of a Monazit Crystal inside an Extractor").defineInRange("monazit_work_cycle",250,0, Integer.MAX_VALUE);
        FE_PER_WORKCYCLE = COMMON_BUILDER.comment("How much FE generated per work cycle").defineInRange("fe_per_cycle",12000,0,Integer.MAX_VALUE);
        COMMON_BUILDER.pop();
    }

    private static void setupPowerCosts() {
        COMMON_BUILDER.comment("Power Costs").push(SUBCATEGORY_COSTS);
        BASE_FORCEFIELD_COST = COMMON_BUILDER.comment("The Base cost to generate a forcefield block").defineInRange("base_cost",1,0,Integer.MAX_VALUE);
        FIELD_CREATE_MODIFIER = COMMON_BUILDER.comment("The modifier for the initial creation of a block").defineInRange("field_create_modifier",10,0,Integer.MAX_VALUE);
        ZAPPER_MODIFIER = COMMON_BUILDER.comment("The modifier for each block to do damage").defineInRange("zapper_modifier",2,0,Integer.MAX_VALUE);
       COMMON_BUILDER.pop();
    }

    private static void setupGeneralSettings() {
        FORCEFIELD_PER_TICK = COMMON_BUILDER.comment("Blocks per tick generated").defineInRange("forcefield_per_tick",5000,0,Integer.MAX_VALUE);
        CHUNKLOADING_ENABLED = COMMON_BUILDER.comment("If Enabled, Every MFFS machine chunkloads").define("enable_chunkloading",true);
        ADVENTURE_MODE = COMMON_BUILDER.comment("When set to AdventureMap Mode, the  Extractor needs no Monazit, and  the ForceField has no click damage").define("adventure_mode",false);
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
