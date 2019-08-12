package com.nekokittygames.mffs.client.model;

import com.nekokittygames.mffs.common.init.MFFSBlocks;
import com.nekokittygames.mffs.common.libs.LibMisc;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.extensions.IForgeBlockState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModelManager {
    private static final ModelManager INSTANCE = new ModelManager();
    private final Set<Item> itemsRegistered = new HashSet<>();

    private ModelManager() {

    }

    public static ModelManager getInstance() {
        return INSTANCE;
    }

    @SubscribeEvent
    public static void registerAllModels(final ModelRegistryEvent event) {
        INSTANCE.registerBlockModels();
        INSTANCE.registerItemModels();
    }

    private void registerBlockModels() {

    }

    private void registerItemModels() {
        OBJLoader.INSTANCE.addDomain(LibMisc.MOD_ID);
        MFFSBlocks.RegistrationHandler.ITEM_BLOCKS.stream().filter(item -> !itemsRegistered.contains(item)).forEach(this::registerItemModel);
    }


    private void registerItemModel(final Item item)
    {
        final ResourceLocation registryName = Objects.requireNonNull(item.getRegistryName());
        registerItemModel(item, registryName.toString());
    }

    private void registerItemModel(Item item, String modelLocation) {

    }
}