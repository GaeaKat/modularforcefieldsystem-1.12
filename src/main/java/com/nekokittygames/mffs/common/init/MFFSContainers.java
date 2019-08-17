package com.nekokittygames.mffs.common.init;

import com.nekokittygames.mffs.MFFS;
import com.nekokittygames.mffs.common.inventory.GeneratorContainer;
import com.nekokittygames.mffs.common.libs.LibBlocks;
import com.nekokittygames.mffs.common.libs.LibMisc;
import com.nekokittygames.mffs.common.tileentities.TileGenerator;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import static com.nekokittygames.mffs.common.misc.InjectionUtils.Null;

@ObjectHolder(LibMisc.MOD_ID)
@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MFFSContainers {

    public static final ContainerType<GeneratorContainer> GENERATOR=Null();

    @SubscribeEvent
    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
        event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            return new GeneratorContainer(windowId, MFFS.proxy.getClientWorld(), pos, inv, MFFS.proxy.getClientPlayer());
        }).setRegistryName(LibBlocks.GENERATOR));
    }


}
