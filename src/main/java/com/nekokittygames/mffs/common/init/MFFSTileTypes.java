package com.nekokittygames.mffs.common.init;

import com.nekokittygames.mffs.common.libs.LibBlocks;
import com.nekokittygames.mffs.common.libs.LibMisc;
import com.nekokittygames.mffs.common.tileentities.TileGenerator;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static com.nekokittygames.mffs.common.misc.InjectionUtils.Null;

@ObjectHolder(LibMisc.MOD_ID)
@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MFFSTileTypes {

    public static final TileEntityType<?> GENERATOR=Null();

    @SubscribeEvent
    public static void registerTileTypes(final RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().registerAll(
            TileEntityType.Builder.create(TileGenerator::new,MFFSBlocks.GENERATOR).build(null).setRegistryName(LibBlocks.GENERATOR)
        );
    }
}
