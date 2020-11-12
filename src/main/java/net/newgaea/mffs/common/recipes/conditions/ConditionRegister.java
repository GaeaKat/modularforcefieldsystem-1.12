package net.newgaea.mffs.common.recipes.conditions;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import net.newgaea.mffs.common.libs.LibMisc;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(LibMisc.MOD_ID)
public class ConditionRegister {

    @SubscribeEvent //ModBus, can't use addListener due to nested genetics.
    public static void registerRecipeSerialziers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        CraftingHelper.register(GeneratorEnabled.Serializer.INSTANCE);
    }
}
