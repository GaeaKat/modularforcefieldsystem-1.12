package dev.katcodes.mffs.common.init;

import dev.katcodes.mffs.MFFSMod;
import dev.katcodes.mffs.common.recipes.condition.GeneratorEnabled;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(MFFSMod.MODID)
public class MFFSConditions {

    @SubscribeEvent //ModBus, can't use addListener due to nested genetics.
    public static void registerRecipeSerialziers(RegistryEvent.Register<RecipeSerializer<?>> event) {
        CraftingHelper.register(GeneratorEnabled.Serializer.INSTANCE);
    }
}
