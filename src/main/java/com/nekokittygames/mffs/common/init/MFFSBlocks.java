package com.nekokittygames.mffs.common.init;

import com.google.common.base.Preconditions;
import com.nekokittygames.mffs.common.blocks.BlockGenerator;
import com.nekokittygames.mffs.common.blocks.BlockMonazitOre;
import com.nekokittygames.mffs.common.libs.LibBlocks;
import com.nekokittygames.mffs.common.libs.LibMisc;
import com.nekokittygames.mffs.common.misc.ItemGroupMFFS;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeBlockState;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.HashSet;
import java.util.Set;

import static com.nekokittygames.mffs.common.misc.InjectionUtils.Null;

@SuppressWarnings("WeakerAccess")
@ObjectHolder(LibMisc.MOD_ID)
public class MFFSBlocks {

    public static final BlockMonazitOre MONAZIT_ORE=Null();
    public static final BlockGenerator GENERATOR=Null();
    @Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistrationHandler {
        public static final Set<BlockItem> ITEM_BLOCKS = new HashSet<>();

        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event) {
            final IForgeRegistry<Block> registry = event.getRegistry();

            final Block[] blocks = {
                    (Block)new BlockMonazitOre(Block.Properties.create(Material.ROCK)).setRegistryName(LibBlocks.MONAZIT_ORE),
                    (Block)new BlockGenerator(Block.Properties.create(Material.IRON)).setRegistryName(LibBlocks.GENERATOR)
            };

            for (final Block block : blocks) {
                registry.register(block);
            }


        }

        @SubscribeEvent
        public static void registerTileEtntities(final RegistryEvent.Register<TileEntityType<?>> event)
        {
            final IForgeRegistry<TileEntityType<?>> registry=event.getRegistry();

        }
        @SubscribeEvent
        public static void registerBlockItems(final RegistryEvent.Register<Item> event) {
            final BlockItem[] items = {
                    new BlockItem(MONAZIT_ORE,getDefaultItemProperties()),
                    new BlockItem(GENERATOR,getDefaultItemProperties())
            };
            final IForgeRegistry<Item> registry = event.getRegistry();

            for (final BlockItem item : items) {
                final Block block = item.getBlock();
                final ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName(), "Block %s has null registry name", block);
                registry.register(item.setRegistryName(registryName));
                ITEM_BLOCKS.add(item);
            }
        }


        private static Item.Properties getDefaultItemProperties() {
            return new Item.Properties().group(ItemGroupMFFS.GetInstance());
        }
    }
}
