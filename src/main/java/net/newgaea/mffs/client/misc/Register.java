package net.newgaea.mffs.client.misc;

import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.newgaea.mffs.client.rendering.tileentities.CapacitorRenderer;
import net.newgaea.mffs.client.rendering.tileentities.ExtractorRenderer;
import net.newgaea.mffs.common.init.MFFSBlocks;
import net.newgaea.mffs.common.init.MFFSItems;
import net.newgaea.mffs.common.init.MFFSTiles;
import net.newgaea.mffs.common.libs.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID,value = Dist.CLIENT,bus = Mod.EventBusSubscriber.Bus.MOD)
public class Register {
    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent event) {
        registerRenderers();
        registerPredicates();
    }

    private static void registerPredicates() {
        ItemModelsProperties.registerProperty(MFFSItems.FORCE_POWER_CRYSTAL.get(),new ResourceLocation(LibMisc.MOD_ID,"power_level"),(stack, world, entity) -> {
            if(!stack.isEmpty() && stack.getItem() == MFFSItems.FORCE_POWER_CRYSTAL.get()) {
                return ((100-stack.getDamage()) / 20);
            }
            return 0;
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers() {
        RenderTypeLookup.setRenderLayer(MFFSBlocks.CAPACITOR.get(), renderType -> true);
        ClientRegistry.bindTileEntityRenderer(MFFSTiles.CAPACITOR.get(), CapacitorRenderer::new);
        RenderTypeLookup.setRenderLayer(MFFSBlocks.EXTRACTOR.get(), renderType -> true);
        ClientRegistry.bindTileEntityRenderer(MFFSTiles.EXTRACTOR.get(), ExtractorRenderer::new);
    }
}
