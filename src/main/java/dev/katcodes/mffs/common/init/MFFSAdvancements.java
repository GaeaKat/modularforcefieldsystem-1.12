package dev.katcodes.mffs.common.init;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import dev.katcodes.mffs.MFFSMod;
import dev.katcodes.mffs.common.register.RegisterCommon;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.TickTrigger;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import vazkii.patchouli.api.PatchouliAPI;

public class MFFSAdvancements {
    private MFFSAdvancements() {}

    public static void init(){
        ResourceLocation manual=new ResourceLocation(MFFSMod.MODID,"manual");
        String manualId=manual.toString();
        CompoundTag tag=new CompoundTag();
        tag.putString("patchouli:book",manualId);

        RegisterCommon.REGISTRATE.addDataGenerator(ProviderType.LOOT,loot-> loot.addLootAction(LootContextParamSets.ALL_PARAMS, resourceLocationBuilderBiConsumer -> {
            resourceLocationBuilderBiConsumer.accept(new ResourceLocation(MFFSMod.MODID, "grant_book_on_first_join"),
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1.0f))
                                            .add(
                                                    LootItem.lootTableItem(PatchouliAPI.get().getBookStack(manual).getItem())
                                                            .apply(SetNbtFunction.setTag(tag))
                                            )
                            )
            );
        })
        );
        RegisterCommon.REGISTRATE.addDataGenerator(ProviderType.ADVANCEMENT, adv->{
            Advancement.Builder.advancement().addCriterion("tick", new TickTrigger.TriggerInstance(EntityPredicate.Composite.ANY))
                    .rewards(AdvancementRewards.Builder.loot(new ResourceLocation(MFFSMod.MODID,"grant_book_on_first_join"))).save(adv,MFFSMod.MODID+":grant_book_on_first_join");
        });
    }
}
