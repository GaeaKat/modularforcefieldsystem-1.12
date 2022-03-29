package dev.katcodes.mffs.common.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.katcodes.mffs.MFFSMod;
import dev.katcodes.mffs.common.register.RegisterCommon;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.TickTrigger;
import net.minecraft.resources.ResourceLocation;

public class MFFSAdvancements {
    private MFFSAdvancements() {}

    public static void init(){
        RegisterCommon.REGISTRATE.addDataGenerator(ProviderType.ADVANCEMENT, adv->{
            Advancement.Builder.advancement().addCriterion("tick", new TickTrigger.TriggerInstance(EntityPredicate.Composite.ANY))
                    .rewards(AdvancementRewards.Builder.loot(new ResourceLocation(MFFSMod.MODID,"grant_book_on_first_join"))).build(new ResourceLocation(MFFSMod.MODID,"grant_book_on_first_join"));
        });
    }
}
