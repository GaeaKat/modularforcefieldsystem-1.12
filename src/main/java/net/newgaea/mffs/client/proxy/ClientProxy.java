package net.newgaea.mffs.client.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.newgaea.mffs.client.loaders.ForceFieldLoader;
import net.newgaea.mffs.common.init.MFFSBlocks;
import net.newgaea.mffs.common.libs.LibMisc;
import net.newgaea.mffs.common.proxy.IProxy;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static net.newgaea.mffs.common.libs.LibMisc.MOD_ID;

@Mod.EventBusSubscriber(value = Dist.CLIENT,modid = MOD_ID)
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
