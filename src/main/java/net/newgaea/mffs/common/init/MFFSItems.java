package net.newgaea.mffs.common.init;

import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.newgaea.mffs.MFFS;
import net.newgaea.mffs.api.MFFSTags;
import net.newgaea.mffs.common.items.ItemLinkCard;
import net.newgaea.mffs.common.items.ItemUpgrade;
import net.newgaea.mffs.common.libs.LibBlocks;
import net.newgaea.mffs.common.libs.LibItems;
import net.newgaea.mffs.common.libs.LibMisc;
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
                    .register();

    public static final ItemEntry<ItemUpgrade> RANGE_UPGRADE =
            MFFSInit.REGISTRATE.object(LibItems.RANGE_UPGRADE).item((properties -> new ItemUpgrade(properties, EnumUpgrade.Range)))
                    .model((ctx, registrateItemModelProvider) ->
                            registrateItemModelProvider.withExistingParent(ctx.getName(),new ResourceLocation("item/handheld"))
                                    .texture("layer0",new ResourceLocation(MOD_ID,"item/upgrades/range_upgrade"))
                    )
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
            .register();

    public static final ItemEntry<ItemUpgrade> SPEED_UPGRADE =
            MFFSInit.REGISTRATE.object(LibItems.SPEED_UPGRADE).item((properties -> new ItemUpgrade(properties, EnumUpgrade.Speed)))
                .model((ctx,provider) ->
                        provider.withExistingParent(ctx.getName(),new ResourceLocation("item/handheld")).texture("layer0", provider.modLoc("item/upgrades/speed_upgrade"))
                )
            .defaultLang()
            .group(ItemGroupMFFS::GetInstance)
            .register();


    protected static Item.Properties getDefaultProperties() {
        return new Item.Properties().group(ItemGroupMFFS.GetInstance()).maxStackSize(64);
    }
}
