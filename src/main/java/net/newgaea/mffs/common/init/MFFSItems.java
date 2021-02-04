package net.newgaea.mffs.common.init;

import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.newgaea.mffs.api.MFFSTags;
import net.newgaea.mffs.common.items.ItemDebugger;
import net.newgaea.mffs.common.items.ItemForcePowerCrystal;
import net.newgaea.mffs.common.items.ItemMonazitCell;
import net.newgaea.mffs.common.items.linkcards.ItemEmptyCard;
import net.newgaea.mffs.common.items.linkcards.ItemPowerLinkCard;
import net.newgaea.mffs.common.items.ItemUpgrade;
import net.newgaea.mffs.common.items.modules.*;
import net.newgaea.mffs.common.items.options.*;
import net.newgaea.mffs.common.libs.LibItems;
import net.newgaea.mffs.common.misc.EnumUpgrade;
import net.newgaea.mffs.common.misc.ItemGroupMFFS;

import static com.tterrag.registrate.providers.RegistrateRecipeProvider.hasItem;
import static net.newgaea.mffs.common.libs.LibMisc.MOD_ID;

public class MFFSItems {


    public static void init() {}

   // Items
    public static final ItemEntry<Item> MONAZIT_CRYSTAL = MFFSInit.REGISTRATE.object(LibItems.MONAZIT_CRYSTAL)
            .item(Item::new)
            .defaultModel()
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
            .recipe(
                    (ctx,prov) -> {
                        prov.smeltingAndBlasting(DataIngredient.stacks(new ItemStack(MFFSBlocks.MONAZIT_ORE.get())),ctx.get().delegate, 0.7f);
                    }
            )
            .tag(MFFSTags.CRYSTAL_MONAZIT)
            .register();

    public static final ItemEntry<Item> MONAZIT_CIRCUIT = MFFSInit.REGISTRATE.object(LibItems.MONAZIT_CIRCUIT)
            .item(Item::new)
            .defaultLang()
            .defaultModel()
            .group(ItemGroupMFFS::GetInstance)
            .recipe((ctx,prov) ->
                    ShapedRecipeBuilder.shapedRecipe(ctx.get(),3).addCriterion("monazit_has",hasItem(MFFSTags.CRYSTAL_MONAZIT))
                            .key('I', Tags.Items.INGOTS_IRON)
                            .key('M', MFFSTags.CRYSTAL_MONAZIT)
                            .patternLine("   ")
                            .patternLine("IMI")
                            .patternLine("   ")
                            .build(prov))
            .register();

    public static final ItemEntry<ItemEmptyCard> EMPTY_CARD=MFFSInit.REGISTRATE.object(LibItems.EMPTY_CARD)
            .item(ItemEmptyCard::new)
            .model((ctx, registrateItemModelProvider) ->
                    registrateItemModelProvider.withExistingParent(ctx.getName(),new ResourceLocation("item/handheld"))
                            .texture("layer0",new ResourceLocation(MOD_ID,"item/link_cards/"+ctx.getName())))
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
            .recipe((ctx,provider)->
                    ShapedRecipeBuilder.shapedRecipe(ctx.get()).addCriterion("circuit_has",hasItem(MFFSItems.MONAZIT_CIRCUIT.get()))
                            .key('P', Items.PAPER)
                            .key('C',MFFSItems.MONAZIT_CIRCUIT.get())
                            .patternLine("PPP")
                            .patternLine("PCP")
                            .patternLine("PPP")
                            .build(provider)
            )
            .register();
    public static final ItemEntry<ItemPowerLinkCard> POWER_LINK_CARD =MFFSInit.REGISTRATE.object(LibItems.POWER_LINK_CARD)
            .item(ItemPowerLinkCard::new)
            .model((ctx, registrateItemModelProvider) ->
                    registrateItemModelProvider.withExistingParent(ctx.getName(),new ResourceLocation("item/handheld"))
                            .texture("layer0",new ResourceLocation(MOD_ID,"item/link_cards/"+ctx.getName()))
            )
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
            .register();

    public static final ItemEntry<ItemUpgrade> CAPACITY_UPGRADE=
            MFFSInit.REGISTRATE.object(LibItems.CAPACITY_UPGRADE).item((properties -> new ItemUpgrade(properties, EnumUpgrade.Capacity)))
                    .model((ctx, registrateItemModelProvider) ->
                            registrateItemModelProvider.withExistingParent(ctx.getName(),new ResourceLocation("item/handheld"))
                            .texture("layer0",new ResourceLocation(MOD_ID,"item/upgrades/capacity_upgrade"))
                    )
                    .defaultLang()
                    .group(ItemGroupMFFS::GetInstance)
                    .recipe((ctx,prov) ->ShapedRecipeBuilder.shapedRecipe(ctx.get()).addCriterion("circuit_has",hasItem(MFFSItems.MONAZIT_CIRCUIT.get()))
                            //.key('B',MFFSItems.MONAZIT_CIRCUIT.get())
                            //.key('H',Tags.Items.INGOTS_BRICK)
                            .key('E',Items.GOLD_INGOT)
                            .key('x',MFFSItems.MONAZIT_CRYSTAL.get()) // todo: replace with monazit cell
                            .patternLine(" E ")
                            .patternLine("ExE")
                            .patternLine("EEE").build(prov))
                    .register();

    public static final ItemEntry<ItemUpgrade> RANGE_UPGRADE =
            MFFSInit.REGISTRATE.object(LibItems.RANGE_UPGRADE).item((properties -> new ItemUpgrade(properties, EnumUpgrade.Range)))
                    .model((ctx, registrateItemModelProvider) ->
                            registrateItemModelProvider.withExistingParent(ctx.getName(),new ResourceLocation("item/handheld"))
                                    .texture("layer0",new ResourceLocation(MOD_ID,"item/upgrades/range_upgrade"))
                    )
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
                    .recipe((ctx,prov) ->ShapedRecipeBuilder.shapedRecipe(ctx.get()).addCriterion("diamond_has",hasItem(Tags.Items.GEMS_DIAMOND))
                            .key('H',Tags.Items.INGOTS_BRICK)
                            .key('E',Items.GOLD_INGOT)
                            .key('i',Tags.Items.GEMS_DIAMOND)
                            .patternLine("HHH")
                            .patternLine("EiE")
                            .patternLine("HHH").build(prov))
            .register();

    public static final ItemEntry<ItemUpgrade> SPEED_UPGRADE =
            MFFSInit.REGISTRATE.object(LibItems.SPEED_UPGRADE).item((properties -> new ItemUpgrade(properties, EnumUpgrade.Speed)))
                .model((ctx,provider) ->
                        provider.withExistingParent(ctx.getName(),new ResourceLocation("item/handheld")).texture("layer0", provider.modLoc("item/upgrades/speed_upgrade"))
                )
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
                    .recipe((ctx,prov) ->ShapedRecipeBuilder.shapedRecipe(ctx.get()).addCriterion("diamond_has",hasItem(Tags.Items.GEMS_DIAMOND))
                            //.key('A',Items.IRON_INGOT)
                            .key('B',MFFSItems.MONAZIT_CIRCUIT.get())
                            .key('E',Items.GOLD_INGOT)
                            .patternLine(" E ")
                            .patternLine("EBE")
                            .patternLine(" E ").build(prov))
                    .register();
    public static final ItemEntry<Item> FOCUS_MATRIX = MFFSInit.REGISTRATE.object(LibItems.FOCUS_MATRIX)
            .item(Item::new)
            .defaultLang()
            .defaultModel()
            .group(ItemGroupMFFS::GetInstance)
            .recipe((ctx,prov) ->
                    ShapedRecipeBuilder.shapedRecipe(ctx.get()).addCriterion("diamond_has",hasItem(Tags.Items.GEMS_DIAMOND))
                            .key('A',Items.IRON_INGOT)
                            //.key('B',MFFSItems.MONAZIT_CIRCUIT.get())
                            .key('l',Tags.Items.GLASS )
                            .key('i',Tags.Items.GEMS_DIAMOND)
                            .patternLine("AlA")
                            .patternLine("lil")
                            .patternLine("AlA")
                            .build(prov)
            )
            .register();

    public static final ItemEntry<Item> DISTANCE_MODIFIER = MFFSInit.REGISTRATE.object(LibItems.DISTANCE_MODIFIER)
            .item(Item::new)
            .defaultModel()
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
            .recipe((ctx,prov) ->
                    ShapedRecipeBuilder.shapedRecipe(ctx.get()).addCriterion("focus_has",hasItem(FOCUS_MATRIX.get()))
                            .key('v',FOCUS_MATRIX.get())
                            .patternLine("vvv")
                            .patternLine("   ")
                            .patternLine("vvv").build(prov))
            .register();

    public static final ItemEntry<Item> STRENGTH_MODIFIER = MFFSInit.REGISTRATE.object(LibItems.STRENGTH_MODIFIER)
            .item(Item::new)
            .defaultModel()
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
            .recipe((ctx,prov) ->
                    ShapedRecipeBuilder.shapedRecipe(ctx.get()).addCriterion("focus_has",hasItem(FOCUS_MATRIX.get()))
                            .key('v',FOCUS_MATRIX.get())
                            .patternLine("vvv")
                            .patternLine("vvv")
                            .patternLine("vvv").build(prov))
            .register();

    public static final ItemEntry<ItemForcePowerCrystal> FORCE_POWER_CRYSTAL=MFFSInit.REGISTRATE.object(LibItems.FORCE_POWER_CRYSTAL)
            .item(ItemForcePowerCrystal::new)
            .defaultLang()
            .model((ctx,prov)->
                    prov.getExistingFile(new ResourceLocation(MOD_ID,"force_power_crystal")))
            .group(ItemGroupMFFS::GetInstance)
            .initialProperties(MFFSItems::getDefaultProperties)
            .properties(properties -> properties.maxDamage(100))
            .recipe((ctx,prov) ->
                    ShapedRecipeBuilder.shapedRecipe(ctx.get()).addCriterion("circuit_has",hasItem(MONAZIT_CIRCUIT.get()))
                            .key('A',Items.IRON_INGOT)
                            .key('x',Items.DIAMOND)
                            .key('D',MFFSItems.MONAZIT_CIRCUIT.get())
                            .patternLine("AAA")
                            .patternLine("AxA")
                            .patternLine("ADA").build(prov))
            .register();

    public static final ItemEntry<ItemMonazitCell> MONAZIT_CELL=MFFSInit.REGISTRATE.object(LibItems.MONAZIT_CELL)
            .item(ItemMonazitCell::new)
            .defaultModel()
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
            .initialProperties(MFFSItems::getDefaultProperties)
            .properties(properties -> properties.maxDamage(100))
            //uuu uiu uuu
            .recipe((ctx,prov) ->
                    ShapedRecipeBuilder.shapedRecipe(ctx.get()).addCriterion("circuit_has",hasItem(MONAZIT_CIRCUIT.get()))
                            .key('u',MFFSItems.MONAZIT_CRYSTAL.get())
                            .key('i',Items.DIAMOND)
                            .patternLine("uuu")
                            .patternLine("uiu")
                            .patternLine("uuu").build(prov))
            .register();

    public static final ItemEntry<ItemDebugger> DEBUGGER = MFFSInit.REGISTRATE.object(LibItems.TOOL_DEBUGGER)
            .item(ItemDebugger::new)
            .defaultLang()
            .defaultModel()
            .group(ItemGroupMFFS::GetInstance)
            .register();

    //<editor-fold desc="Options">
    public static final ItemEntry<ItemFieldManipulatorOption> FIELD_MANIPULATOR_OPTION = MFFSInit.REGISTRATE.object(LibItems.Options.FIELD_MANIPULATOR)
            .item(ItemFieldManipulatorOption::new)
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
            .model((ctx,prov) -> prov.withExistingParent(ctx.getName(),prov.mcLoc("item/handheld")).texture("layer0",prov.modLoc("item/options/"+ctx.getName())))
            .recipe((ctx,prov) -> ShapedRecipeBuilder.shapedRecipe(ctx.get()).addCriterion("circuit_has",hasItem(MFFSItems.MONAZIT_CIRCUIT.get()))
            .key('C',MONAZIT_CIRCUIT.get())
                    .key('E',Items.GOLD_INGOT)
                    .patternLine(" C ")
                    .patternLine("CEC")
                    .patternLine(" C ").build(prov)
            )
            .register();
    public static final ItemEntry<ItemOption> BLOCK_BREAKER_OPTION = MFFSInit.REGISTRATE.object(LibItems.Options.BLOCK_BREAKER)
            .item(ItemOption::new)
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
            .model((ctx,prov) -> prov.withExistingParent(ctx.getName(),prov.mcLoc("item/handheld")).texture("layer0",prov.modLoc("item/options/"+ctx.getName())))
            .recipe((ctx,prov) -> ShapedRecipeBuilder.shapedRecipe(ctx.get()).addCriterion("circuit_has",hasItem(MFFSItems.MONAZIT_CIRCUIT.get()))
                    .key('C',MONAZIT_CIRCUIT.get())
                    .key('b',Items.IRON_PICKAXE)
                    .patternLine(" C ")
                    .patternLine("CbC")
                    .patternLine(" C ").build(prov)
            )
            .register();
    public static final ItemEntry<ItemFieldJammerOption> FIELD_JAMMER_OPTION = MFFSInit.REGISTRATE.object(LibItems.Options.FIELD_JAMMER)
            .item(ItemFieldJammerOption::new)
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
            .model((ctx,prov) -> prov.withExistingParent(ctx.getName(),prov.mcLoc("item/handheld")).texture("layer0",prov.modLoc("item/options/"+ctx.getName())))
            .recipe((ctx,prov) -> ShapedRecipeBuilder.shapedRecipe(ctx.get()).addCriterion("circuit_has",hasItem(MFFSItems.MONAZIT_CIRCUIT.get()))
                    .key('a',Items.ENDER_PEARL)
                    .key('v',FOCUS_MATRIX.get())
                    .patternLine(" a ")
                    .patternLine("ava")
                    .patternLine(" a ").build(prov)
            )
            .register();

    public static final ItemEntry<ItemCamouflageOption> CAMOUFLAGE_OPTION = MFFSInit.REGISTRATE.object(LibItems.Options.CAMOUFLAGE)
            .item(ItemCamouflageOption::new)
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
            .model((ctx,prov) -> prov.withExistingParent(ctx.getName(),prov.mcLoc("item/handheld")).texture("layer0",prov.modLoc("item/options/"+ctx.getName())))
            .recipe((ctx,prov) -> ShapedRecipeBuilder.shapedRecipe(ctx.get()).addCriterion("circuit_has",hasItem(MFFSItems.MONAZIT_CIRCUIT.get()))
                    .key('e',Items.ENDER_EYE)
                    .key('v',FOCUS_MATRIX.get())
                    .patternLine(" e ")
                    .patternLine("eve")
                    .patternLine(" e ").build(prov)
            )
            .register();

    public static final ItemEntry<ItemOption> ZAPPER_OPTION = MFFSInit.REGISTRATE.object(LibItems.Options.ZAPPER)
            .item(ItemOption::new)
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
            .model((ctx,prov) -> prov.withExistingParent(ctx.getName(),prov.mcLoc("item/handheld")).texture("layer0",prov.modLoc("item/options/"+ctx.getName())))
            .recipe((ctx,prov) -> ShapedRecipeBuilder.shapedRecipe(ctx.get()).addCriterion("circuit_has",hasItem(MFFSItems.MONAZIT_CIRCUIT.get()))
                    .key('b',Items.LAVA_BUCKET)
                    .key('e',FOCUS_MATRIX.get())
                    .patternLine(" e ")
                    .patternLine("ebe")
                    .patternLine(" e ").build(prov)
            )
            .register();

    public static final ItemEntry<ItemFieldFusionOption> FUSION_OPTION = MFFSInit.REGISTRATE.object(LibItems.Options.FUSION)
            .item(ItemFieldFusionOption::new)
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
            .model((ctx,prov) -> prov.withExistingParent(ctx.getName(),prov.mcLoc("item/handheld")).texture("layer0",prov.modLoc("item/options/"+ctx.getName())))
            .recipe((ctx,prov) -> ShapedRecipeBuilder.shapedRecipe(ctx.get()).addCriterion("circuit_has",hasItem(MFFSItems.MONAZIT_CIRCUIT.get()))
                    .key('b',FOCUS_MATRIX.get())
                    .key('e',MONAZIT_CIRCUIT.get())
                    .patternLine(" e ")
                    .patternLine("ebe")
                    .patternLine(" e ").build(prov)
            )
            .register();
    //</editor-fold>

    //<editor-fold desc="Modules">
    public static final ItemEntry<ItemAdvCubeModule> ADV_CUBE_MODULE = MFFSInit.REGISTRATE.object(LibItems.Modules.ADV_CUBE)
            .item(ItemAdvCubeModule::new)
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
            .tag(MFFSTags.PROJECTOR)
            .model((ctx,prov)->prov.withExistingParent(ctx.getName(),new ResourceLocation("item/handheld")).texture("layer0", prov.modLoc("item/modules/"+ctx.get().getModuleType())))
            .recipe((ctx,prov) ->ShapedRecipeBuilder.shapedRecipe(ctx.get()).addCriterion("circuit_has",hasItem(MFFSItems.MONAZIT_CIRCUIT.get()))
                    .key('A',Items.IRON_INGOT)
                    //.key('B',MFFSItems.MONAZIT_CIRCUIT.get())
                    .key('C',MFFSItems.MONAZIT_CRYSTAL.get())
            .patternLine("AAA")
            .patternLine("ACA")
            .patternLine("AAA").build(prov))
            .register();
    public static final ItemEntry<ItemContainmentModule> CONTAINMENT_MODULE = MFFSInit.REGISTRATE.object(LibItems.Modules.CONTAINMENT)
            .item(ItemContainmentModule::new)
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
            .tag(MFFSTags.PROJECTOR)
            .model((ctx,prov)->prov.withExistingParent(ctx.getName(),new ResourceLocation("item/handheld")).texture("layer0", prov.modLoc("item/modules/"+ctx.get().getModuleType())))
            .recipe((ctx,prov) ->ShapedRecipeBuilder.shapedRecipe(ctx.get()).addCriterion("circuit_has",hasItem(MFFSItems.MONAZIT_CIRCUIT.get()))
                    .key('A',Items.IRON_INGOT)
                    .key('B',MFFSItems.MONAZIT_CIRCUIT.get())
                    //.key('C',MFFSItems.MONAZIT_CRYSTAL.get())
                    .patternLine("BBB")
                    .patternLine("BAB")
                    .patternLine("BBB").build(prov))
            .register();
    public static final ItemEntry<ItemCubeModule> CUBE_MODULE = MFFSInit.REGISTRATE.object(LibItems.Modules.CUBE)
            .item(ItemCubeModule::new)
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
            .tag(MFFSTags.PROJECTOR)
            .model((ctx,prov)->prov.withExistingParent(ctx.getName(),new ResourceLocation("item/handheld")).texture("layer0", prov.modLoc("item/modules/"+ctx.get().getModuleType())))
            .recipe((ctx,prov) ->ShapedRecipeBuilder.shapedRecipe(ctx.get()).addCriterion("circuit_has",hasItem(MFFSItems.MONAZIT_CIRCUIT.get()))
                    .key('A',Items.IRON_INGOT)
                    .key('B',MFFSItems.MONAZIT_CIRCUIT.get())
                    //.key('C',MFFSItems.MONAZIT_CRYSTAL.get())
                    .patternLine("B B")
                    .patternLine(" A ")
                    .patternLine("B B").build(prov))
            .register();
    public static final ItemEntry<ItemDeflectorModule> DEFLECTOR_MODULE = MFFSInit.REGISTRATE.object(LibItems.Modules.DEFLECTOR)
            .item(ItemDeflectorModule::new)
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
            .tag(MFFSTags.PROJECTOR)
            .model((ctx,prov)->prov.withExistingParent(ctx.getName(),new ResourceLocation("item/handheld")).texture("layer0", prov.modLoc("item/modules/"+ctx.get().getModuleType())))
            .recipe((ctx,prov) ->ShapedRecipeBuilder.shapedRecipe(ctx.get()).addCriterion("circuit_has",hasItem(MFFSItems.MONAZIT_CIRCUIT.get()))
                    .key('A',Items.IRON_INGOT)
                    .key('B',MFFSItems.MONAZIT_CIRCUIT.get())
                    //.key('C',MFFSItems.MONAZIT_CRYSTAL.get())
                    .patternLine("AAA")
                    .patternLine("ABA")
                    .patternLine("AAA").build(prov))
            .register();
    public static final ItemEntry<ItemDiagWallModule> DIAG_WALL_MODULE = MFFSInit.REGISTRATE.object(LibItems.Modules.DIAGWALL)
            .item(ItemDiagWallModule::new)
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
            .tag(MFFSTags.PROJECTOR)
            .model((ctx,prov)->prov.withExistingParent(ctx.getName(),new ResourceLocation("item/handheld")).texture("layer0", prov.modLoc("item/modules/"+ctx.get().getModuleType())))
            .recipe((ctx,prov) ->ShapedRecipeBuilder.shapedRecipe(ctx.get()).addCriterion("circuit_has",hasItem(MFFSItems.MONAZIT_CIRCUIT.get()))
                    .key('A',Items.IRON_INGOT)
                    .key('B',MFFSItems.MONAZIT_CIRCUIT.get())
                    //.key('C',MFFSItems.MONAZIT_CRYSTAL.get())
                    .patternLine("A A")
                    .patternLine(" B ")
                    .patternLine("A A").build(prov))
            .register();
    public static final ItemEntry<ItemSphereModule> SPHERE_MODULE = MFFSInit.REGISTRATE.object(LibItems.Modules.SPHERE)
            .item(ItemSphereModule::new)
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
            .tag(MFFSTags.PROJECTOR)
            .model((ctx,prov)->prov.withExistingParent(ctx.getName(),new ResourceLocation("item/handheld")).texture("layer0", prov.modLoc("item/modules/"+ctx.get().getModuleType())))
            .recipe((ctx,prov) ->ShapedRecipeBuilder.shapedRecipe(ctx.get()).addCriterion("circuit_has",hasItem(MFFSItems.MONAZIT_CIRCUIT.get()))
                    .key('A',Items.IRON_INGOT)
                    .key('B',MFFSItems.MONAZIT_CIRCUIT.get())
                    //.key('C',MFFSItems.MONAZIT_CRYSTAL.get())
                    .patternLine(" B ")
                    .patternLine("BAB")
                    .patternLine(" B ").build(prov))
            .register();
    public static final ItemEntry<ItemTubeModule> TUBE_MODULE = MFFSInit.REGISTRATE.object(LibItems.Modules.TUBE)
            .item(ItemTubeModule::new)
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
            .tag(MFFSTags.PROJECTOR)
            .model((ctx,prov)->prov.withExistingParent(ctx.getName(),new ResourceLocation("item/handheld")).texture("layer0", prov.modLoc("item/modules/"+ctx.get().getModuleType())))
            .recipe((ctx,prov) ->ShapedRecipeBuilder.shapedRecipe(ctx.get()).addCriterion("circuit_has",hasItem(MFFSItems.MONAZIT_CIRCUIT.get()))
                    .key('A',Items.IRON_INGOT)
                    .key('B',MFFSItems.MONAZIT_CIRCUIT.get())
                    //.key('C',MFFSItems.MONAZIT_CRYSTAL.get())
                    .patternLine("AAA")
                    .patternLine(" B ")
                    .patternLine("AAA").build(prov))
            .register();
    public static final ItemEntry<ItemWallModule> WALL_MODULE = MFFSInit.REGISTRATE.object(LibItems.Modules.WALL)
            .item(ItemWallModule::new)
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
            .tag(MFFSTags.PROJECTOR)
            .model((ctx,prov)->prov.withExistingParent(ctx.getName(),new ResourceLocation("item/handheld")).texture("layer0", prov.modLoc("item/modules/"+ctx.get().getModuleType())))
            .recipe((ctx,prov) ->ShapedRecipeBuilder.shapedRecipe(ctx.get()).addCriterion("circuit_has",hasItem(MFFSItems.MONAZIT_CIRCUIT.get()))
                    .key('A',Items.IRON_INGOT)
                    .key('B',MFFSItems.MONAZIT_CIRCUIT.get())
                    //.key('C',MFFSItems.MONAZIT_CRYSTAL.get())
                    .patternLine("AA ")
                    .patternLine("AA ")
                    .patternLine("BB ").build(prov))
            .register();
    //</editor-fold>

   //public static final Map<EnumProjectorModule, ItemProjectorModule>  PROJECTOR_TYPE_MAP ;
//
//    static {
//        PROJECTOR_TYPE_MAP=new HashMap<>();
//        for(EnumProjectorModule type: EnumProjectorModule.values()) {
//            if(type==EnumProjectorModule.Empty)
//                continue;
//            ItemEntry<ItemProjectorModule> projectorItem=MFFSInit.REGISTRATE.object(type.getItemName())
//                    .item(properties -> new ItemProjectorModule(properties,type))
//                    .defaultLang()
//                    .group(ItemGroupMFFS::GetInstance)
//                    .model(
//                            (ctx,prov) ->
//                                    prov.withExistingParent(ctx.getName(),new ResourceLocation("item/handheld")).texture("layer0", prov.modLoc("item/modules/"+type.getString()))
//                    )
//                    .tag(MFFSTags.PROJECTOR)
//                    .register();
//        }
//    }

    protected static Item.Properties getDefaultProperties() {
        return new Item.Properties().group(ItemGroupMFFS.GetInstance()).maxStackSize(64);
    }
}
