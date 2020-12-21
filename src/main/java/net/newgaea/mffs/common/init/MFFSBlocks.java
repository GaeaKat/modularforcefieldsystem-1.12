package net.newgaea.mffs.common.init;

import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.newgaea.mffs.api.EnumProjectorModule;
import net.newgaea.mffs.api.MFFSTags;
import net.newgaea.mffs.common.blocks.*;
import net.newgaea.mffs.common.libs.LibBlocks;
import net.newgaea.mffs.common.misc.ItemGroupMFFS;
import net.newgaea.mffs.common.recipes.conditions.GeneratorEnabled;
import net.newgaea.mffs.common.tiles.TileCapacitor;
import net.newgaea.mffs.common.tiles.TileForcefield;
import net.newgaea.mffs.common.tiles.TileGenerator;
import net.newgaea.mffs.common.tiles.TileProjector;

import static com.tterrag.registrate.providers.RegistrateRecipeProvider.hasItem;
import static net.newgaea.mffs.common.libs.LibMisc.MOD_ID;

public class MFFSBlocks {

    public static void init() {}
    protected static void registerSimpleActivatableMachine(Block block, String blockname, RegistrateBlockstateProvider provider) {
        ModelFile off = provider.models().orientable(blockname+"_off",provider.modLoc("block/"+blockname+"/side_inactive"),provider.modLoc("block/"+blockname+"/face_inactive"),provider.modLoc("block/"+blockname+"/side_inactive"));
        ModelFile on = provider.models().orientable(blockname+"_on",provider.modLoc("block/"+blockname+"/side_active"),provider.modLoc("block/"+blockname+"/face_active"),provider.modLoc("block/"+blockname+"/side_active"));
        provider.horizontalBlock(block,blockState -> blockState.get(BlockSimpleNetwork.ACTIVE) ?on:off);

    }

    protected static void registerProjector(Block block, String blockname, RegistrateBlockstateProvider provider) {

        provider.getVariantBuilder(block)
                .forAllStates(state -> {
                    Direction dir = state.get(BlockStateProperties.FACING);
                    return ConfiguredModel.builder()
                            .modelFile(projectorModels(blockname,state.get(BlockSimpleNetwork.ACTIVE),state.get(BlockProjector.TYPE),provider))
                            .rotationX(dir == Direction.DOWN ? 90 : !dir.getAxis().isHorizontal() ? 270 : 0)
                            .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.getHorizontalAngle()) + 180) % 360)
                            .build();
                });
    }
    protected static ModelFile projectorModels(String blockname, boolean active, EnumProjectorModule type, RegistrateBlockstateProvider provider)
    {

        ModelFile off=provider.models().orientable(blockname+"_"+type.getString()+"_off",provider.modLoc("block/"+blockname+"/"+type.getString()+"/side_inactive"),provider.modLoc("block/"+blockname+"/"+type.getString()+"/face_inactive"),provider.modLoc("block/"+blockname+"/"+type.getString()+"/side_inactive"));
        ModelFile on = provider.models().orientable(blockname+"_"+type.getString()+"_on",provider.modLoc("block/"+blockname+"/"+type.getString()+"/side_active"),provider.modLoc("block/"+blockname+"/"+type.getString()+"/face_active"),provider.modLoc("block/"+blockname+"/"+type.getString()+"/side_active"));
        return active?on:off;
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
                                                    .build(prov,new ResourceLocation(MOD_ID, ctx.getName()))))
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

    public static final BlockEntry<BlockForceField> FORCEFIELD = MFFSInit.REGISTRATE.object(LibBlocks.FORCEFIELD)
            .block(BlockForceField::new)
            .defaultLang()
            .initialProperties(Material.LEAVES,MaterialColor.AIR)
            .properties(properties -> properties.hardnessAndResistance(999f))
            .properties(AbstractBlock.Properties::noDrops)
            .properties(AbstractBlock.Properties::notSolid)
            .properties(properties -> properties.setAllowsSpawn(MFFSBlocks::neverAllowSpawn))
            .properties(properties -> properties.setOpaque(MFFSBlocks::isntSolid))
            .properties(properties -> properties.setSuffocates(MFFSBlocks::isntSolid))
            .properties(properties -> properties.setBlocksVision(MFFSBlocks::isntSolid))
            .blockstate((ctx,prov) -> {
                ModelFile file=prov.models().getExistingFile(new ResourceLocation(MOD_ID,"forcefield"));
                prov.simpleBlock(ctx.get(),file);
            })
            .tileEntity(TileForcefield::new).build()
            .register();

    public static final BlockEntry<BlockProjector> PROJECTOR = MFFSInit.REGISTRATE.object(LibBlocks.PROJECTOR)
            .block(BlockProjector::new)
            .defaultLang()
            .defaultLoot()
            .initialProperties(Material.IRON,MaterialColor.IRON)
            .properties(properties -> properties.sound(SoundType.METAL))
            .properties(properties -> properties.harvestLevel(1))
            .properties(properties -> properties.harvestTool(ToolType.PICKAXE))
            .blockstate((ctx,prov)-> registerProjector(ctx.get(),ctx.getName(),prov))
            .item()
            .model((ctx,prov) -> prov.withExistingParent(ctx.getName(),prov.modLoc("block/projector_empty_off")))
            .group(ItemGroupMFFS::GetInstance)
            .build()
            .tileEntity(TileProjector::new).build()
            .register();

    private static Boolean neverAllowSpawn(BlockState state, IBlockReader reader, BlockPos pos, EntityType<?> entity) {
        return (boolean)false;
    }

    private static boolean isntSolid(BlockState state, IBlockReader reader, BlockPos pos) {
        return false;
    }

}
