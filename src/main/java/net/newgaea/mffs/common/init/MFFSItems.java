package net.newgaea.mffs.common.init;

import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.newgaea.mffs.api.MFFSTags;
import net.newgaea.mffs.common.items.ItemLinkCard;
import net.newgaea.mffs.common.items.ItemUpgrade;
import net.newgaea.mffs.common.items.modules.*;
import net.newgaea.mffs.common.libs.LibItems;
import net.newgaea.mffs.common.misc.EnumUpgrade;
import net.newgaea.mffs.common.misc.ItemGroupMFFS;
import net.newgaea.mffs.common.misc.MFFSInventoryHelper;

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
                    ShapedRecipeBuilder.shapedRecipe(MFFSItems.MONAZIT_CIRCUIT.get(),3).addCriterion("monazit_has",hasItem(MFFSTags.CRYSTAL_MONAZIT))
                            .key('I', Tags.Items.INGOTS_IRON)
                            .key('M', MFFSTags.CRYSTAL_MONAZIT)
                            .patternLine("   ")
                            .patternLine("IMI")
                            .patternLine("   ")
                            .build(prov))
            .register();
    public static final ItemEntry<ItemLinkCard> LINK_CARD=MFFSInit.REGISTRATE.object(LibItems.LINK_CARD)
            .item(ItemLinkCard::new)
            .model((ctx,prov)->
                    prov.getExistingFile(new ResourceLocation(MOD_ID,"link_card")))
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
            .recipe((ctx,provider)->
                    ShapedRecipeBuilder.shapedRecipe(MFFSItems.LINK_CARD.get()).addCriterion("circuit_has",hasItem(MFFSItems.MONAZIT_CIRCUIT.get()))
                            .key('P', Items.PAPER)
                            .key('C',MFFSItems.MONAZIT_CIRCUIT.get())
                            .patternLine("PPP")
                            .patternLine("PCP")
                            .patternLine("PPP")
                            .build(provider)
                    )
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
                            .patternLine("vvv"))
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
                            .patternLine("vvv"))
            .register();

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
