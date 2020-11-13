package net.newgaea.mffs.common.init;

import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.newgaea.mffs.api.MFFSTags;
import net.newgaea.mffs.common.blocks.BlockCapacitor;
import net.newgaea.mffs.common.blocks.BlockGenerator;
import net.newgaea.mffs.common.blocks.BlockSimpleNetwork;
import net.newgaea.mffs.common.libs.LibBlocks;
import net.newgaea.mffs.common.libs.LibMisc;
import net.newgaea.mffs.common.misc.ItemGroupMFFS;
import net.newgaea.mffs.common.recipes.conditions.GeneratorEnabled;
import net.newgaea.mffs.common.tiles.TileCapacitor;
import net.newgaea.mffs.common.tiles.TileGenerator;

import static com.tterrag.registrate.providers.RegistrateRecipeProvider.hasItem;

public class MFFSBlocks {

    public static void init() {}
    protected static void registerSimpleActivatableMachine(Block block, String blockname, RegistrateBlockstateProvider provider) {
        ModelFile off = provider.models().orientable(blockname+"_off",provider.modLoc("block/"+blockname+"/side_inactive"),provider.modLoc("block/"+blockname+"/face_inactive"),provider.modLoc("block/"+blockname+"/side_inactive"));
        ModelFile on = provider.models().orientable(blockname+"_on",provider.modLoc("block/"+blockname+"/side_active"),provider.modLoc("block/"+blockname+"/face_active"),provider.modLoc("block/"+blockname+"/side_active"));
        provider.horizontalBlock(block,blockState -> blockState.get(BlockSimpleNetwork.ACTIVE) ?on:off);

    }

    public static final BlockEntry<Block> MONAZIT_ORE = MFFSInit.REGISTRATE.object(LibBlocks.MONAZIT_ORE)
            .block(Block::new)
            .defaultLoot()
            .defaultLang()
            .initialProperties(Material.IRON,MaterialColor.STONE)
            .properties(properties -> properties.hardnessAndResistance(5.0f,6.0f))
            .properties(properties -> properties.sound(SoundType.STONE))
            .properties(properties -> properties.harvestLevel(1))
            .properties(properties -> properties.harvestTool(ToolType.PICKAXE))
            .defaultBlockstate()
            .item()
                .group(ItemGroupMFFS::GetInstance)
                .model((ctx,prov) -> prov.withExistingParent(ctx.getName(),prov.modLoc("block/"+ctx.getName())))
                .tag(Tags.Items.ORES)
            .build()
            .tag(Tags.Blocks.ORES)
            .register();
    public static final BlockEntry<BlockGenerator> GENERATOR = MFFSInit.REGISTRATE.object(LibBlocks.GENERATOR)
            .block(BlockGenerator::new)
            .defaultLoot()
            .defaultLang()
            .initialProperties(Material.IRON,MaterialColor.STONE)
            .properties(properties -> properties.hardnessAndResistance(5.0f,6.0f))
            .properties(properties -> properties.sound(SoundType.STONE))
            .properties(properties -> properties.harvestLevel(1))
            .properties(properties -> properties.harvestTool(ToolType.PICKAXE))
            .blockstate((ctx,prov) ->prov.horizontalBlock(ctx.get(),prov.models().orientable(ctx.getName(),prov.mcLoc("block/furnace_side"),prov.mcLoc("block/furnace_front"),prov.mcLoc("block/furnace_top"))))
            .item()
                .group(ItemGroupMFFS::GetInstance)
                .model((ctx,prov) -> prov.withExistingParent(ctx.getName(),prov.modLoc("block/generator")))
            .build()
            .tag(BlockTags.WITHER_IMMUNE)
            .tileEntity(TileGenerator::new)
            .build()
            .recipe((ctx,prov)->
                    ShapedRecipeBuilder.shapedRecipe(MFFSBlocks.GENERATOR.get()).addCriterion("monazit_has",hasItem(MFFSTags.CRYSTAL_MONAZIT))
                            .key('I', Tags.Items.INGOTS_IRON)
                            .key('F', Blocks.FURNACE)
                            .key('M',MFFSTags.CRYSTAL_MONAZIT)
                            .patternLine("III")
                            .patternLine("IMI")
                            .patternLine("IFI")
                            .build(
                                    iFinishedRecipe ->
                                            ConditionalRecipe
                                                    .builder()
                                                    .addCondition(GeneratorEnabled.INSTANCE)
                                                    .addRecipe(iFinishedRecipe)
                                                    .build(prov,new ResourceLocation(LibMisc.MOD_ID, ctx.getName()))))
            .register();

    public static final BlockEntry<BlockCapacitor> CAPACITOR = MFFSInit.REGISTRATE.object(LibBlocks.CAPACITOR)
            .block(BlockCapacitor::new)
            .defaultLang()
            .defaultLoot()
            .initialProperties(Material.IRON, MaterialColor.IRON)
            .properties(properties -> properties.hardnessAndResistance(5.0f,6.0f))
            .properties(properties -> properties.sound(SoundType.METAL))
            .properties(properties -> properties.harvestLevel(1))
            .properties(properties -> properties.harvestTool(ToolType.PICKAXE))
            .blockstate((ctx,prov) -> {
                registerSimpleActivatableMachine(ctx.get(), ctx.getName(), prov);
            })
            .item()
                .model((ctx,prov) ->prov.withExistingParent(ctx.getName(),prov.modLoc("block/capacitor_off")))
                .group(ItemGroupMFFS::GetInstance)
            .build()
            .tag(BlockTags.WITHER_IMMUNE)
            .tileEntity(TileCapacitor::new)
            .build()
            .register();
}
