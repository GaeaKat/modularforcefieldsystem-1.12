package net.newgaea.mffs.client.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.newgaea.mffs.client.gui.screens.CapacitorScreen;
import net.newgaea.mffs.client.gui.screens.GeneratorScreen;
import net.newgaea.mffs.client.loaders.ForceFieldLoader;
import net.newgaea.mffs.common.init.MFFSBlocks;
import net.newgaea.mffs.common.init.MFFSContainer;
import net.newgaea.mffs.common.proxy.IProxy;

import static net.newgaea.mffs.common.libs.LibMisc.MOD_ID;
@Mod.EventBusSubscriber
public class ClientProxy implements IProxy {
    @Override
    public void init() {

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerModels);
        RenderTypeLookup.setRenderLayer(MFFSBlocks.FORCEFIELD.get(),(renderType -> true));
    }
    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(MOD_ID,"forcefield"),new ForceFieldLoader());

    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }
}
