package com.nekokittygames.mffs.common.init;

import com.google.common.base.Preconditions;
import com.nekokittygames.mffs.common.items.ItemForciumCrystal;
import com.nekokittygames.mffs.common.libs.LibItems;
import com.nekokittygames.mffs.common.libs.LibMisc;
import com.nekokittygames.mffs.common.misc.ItemGroupMFFS;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static com.nekokittygames.mffs.common.misc.InjectionUtils.Null;
@SuppressWarnings("WeakerAccess")
@ObjectHolder(LibMisc.MOD_ID)
public class MFFSItems {

    public static final Item MONAZIT_CRYSTAL=Null();

    @Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            final Item[] items = {
                    new ItemForciumCrystal(getDefaultItemProperties()).setRegistryName(LibItems.MONAZIT_CRYSTAL)
            };
            final IForgeRegistry<Item> registry = event.getRegistry();
            for (final Item item : items) {
                registry.register(item);
            }
        }

        private static Item.Properties getDefaultItemProperties() {
            return new Item.Properties().group(ItemGroupMFFS.GetInstance());
        }
    }
}
