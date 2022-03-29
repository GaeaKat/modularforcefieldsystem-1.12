package dev.katcodes.mffs.common.init;

import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.katcodes.mffs.MFFSMod;
import dev.katcodes.mffs.api.MFFSTags;
import dev.katcodes.mffs.common.blockentities.CapacitorBlockEntity;
import dev.katcodes.mffs.common.blocks.CapacitorBlock;
import dev.katcodes.mffs.common.blocks.GeneratorBlock;
import dev.katcodes.mffs.common.blocks.SimpleNetworkBlock;
import dev.katcodes.mffs.common.blocks.TestBlock;
import dev.katcodes.mffs.common.libs.LibBlocks;
import dev.katcodes.mffs.common.recipes.condition.GeneratorEnabled;
import dev.katcodes.mffs.common.register.RegisterCommon;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import org.jetbrains.annotations.Contract;

public class MFFSBlocks {

    protected static void registerSimpleActivatedMachine(Block block, String blockname, RegistrateBlockstateProvider provider)
    {
        ModelFile off = provider.models().orientable(blockname+"_off",provider.modLoc("block/"+blockname+"/side_inactive"),provider.modLoc("block/"+blockname+"/face_inactive"),provider.modLoc("block/"+blockname+"/side_inactive"));
        ModelFile on = provider.models().orientable(blockname+"_on",provider.modLoc("block/"+blockname+"/side_active"),provider.modLoc("block/"+blockname+"/face_active"),provider.modLoc("block/"+blockname+"/side_active"));
        provider.horizontalBlock(block,blockState -> blockState.getValue(SimpleNetworkBlock.ACTIVE)?on:off);
    }


    @Contract(pure = true)
    private MFFSBlocks(){}

    /**
     * Test block, to be removed later
     */
    public static final RegistryEntry<TestBlock> TEST_BLOCK= RegisterCommon.REGISTRATE.block(LibBlocks.TEST_BLOCK,TestBlock::new)
            .item()
            .build()
            .defaultLang()
            .defaultLoot()
            .defaultBlockstate()
            .register();


    /**
     * Monazit ore block
     */
    public static final RegistryEntry<Block> MONAZIT_ORE = RegisterCommon.REGISTRATE.block(LibBlocks.MONAZIT_ORE,Block::new)
            .item()
            .tag(Tags.Items.ORES)
            .tag(Tags.Items.ORES_IN_GROUND_STONE)
            .tag(Tags.Items.ORE_RATES_SPARSE)
            .tag(MFFSTags.Items.MONAZIT_ORES)
            .build()
            .tag(Tags.Blocks.ORES)
            .tag(Tags.Blocks.ORES_IN_GROUND_STONE)
            .tag(Tags.Blocks.ORE_RATES_SPARSE)
            .tag(MFFSTags.Blocks.MONAZIT_ORES)
            .defaultLang()
            .defaultLoot()
            .defaultBlockstate()
            .register();

    public static final RegistryEntry<CapacitorBlock> CAPACITOR = RegisterCommon.REGISTRATE.object(LibBlocks.CAPACITOR)
            .block(CapacitorBlock::new)
            .defaultLang()
            .defaultLoot()
            .properties(p -> p.noOcclusion())
            .addLayer(() -> RenderType::solid)
            .blockstate((ctx, prov) -> {
                registerSimpleActivatedMachine(ctx.getEntry(),ctx.getName(),prov);
            } )
            .item()
            .model((ctx,prov) -> prov.withExistingParent(ctx.getName(),prov.modLoc("block/capacitor_off")))
            .build()
            .initialProperties(Material.METAL, MaterialColor.COLOR_LIGHT_GRAY)
            .properties(properties -> properties.strength(5.0f,6.0f))
            .properties(properties -> properties.sound(SoundType.METAL))
            .blockEntity(CapacitorBlockEntity::new).build()
            .register();

    public static final RegistryEntry<GeneratorBlock> GENERATOR = RegisterCommon.REGISTRATE.object(LibBlocks.GENERATOR)
            .block(GeneratorBlock::new)
            .defaultLang()
            .defaultLoot()
            .initialProperties(Material.STONE,MaterialColor.COLOR_LIGHT_GRAY)
            .blockstate( (ctx,prov) -> prov.horizontalBlock(ctx.get(),prov.models().orientable(ctx.getName(),prov.mcLoc("block/furnace_side"),prov.mcLoc("block/furnace_front"),prov.mcLoc("block/furnace_top"))))
            .item()
            .model((ctx,prov) -> prov.withExistingParent(ctx.getName(),prov.modLoc("block/generator")))
            .build()
            .tag(BlockTags.WITHER_IMMUNE)
            .recipe((ctx,prov) -> {
                ShapedRecipeBuilder.shaped(MFFSBlocks.GENERATOR.get()).unlockedBy("monazit_has", RegistrateRecipeProvider.has(MFFSTags.Items.MONAZIT_GEM))
                        .define('I', Tags.Items.INGOTS_IRON)
                        .define('F', Blocks.FURNACE)
                        .define('M',MFFSTags.Items.MONAZIT_GEM)
                        .pattern("III")
                        .pattern("IMI")
                        .pattern("IFI")
                        .save(iFinished -> {
                            ConditionalRecipe.builder()
                                    .addCondition(GeneratorEnabled.INSTANCE)
                                    .addRecipe(iFinished)
                                    .build(prov,new ResourceLocation(MFFSMod.MODID,ctx.getName()));
                        });
            })
            .register();
    public static void init() {
        // This function is just to initialize the contents
    }
}
