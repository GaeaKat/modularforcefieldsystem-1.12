package com.nekokittygames.mffs.common.init;

import com.nekokittygames.mffs.common.blocks.BlockGenerator;
import com.nekokittygames.mffs.common.blocks.BlockMonazitOre;
import com.nekokittygames.mffs.common.blocks.powered.BlockCapacitor;
import com.nekokittygames.mffs.common.libs.LibBlocks;
import com.nekokittygames.mffs.common.misc.ItemGroupMFFS;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nekokittygames.mffs.common.libs.LibMisc.MOD_ID;

public class MFFSRegistration {
    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MOD_ID);
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, MOD_ID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, MOD_ID);

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
public static class Blocks {
    public static final RegistryObject<BlockMonazitOre> MONAZIT_ORE=BLOCKS.register(LibBlocks.MONAZIT_ORE,()-> new BlockMonazitOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<BlockGenerator> GENERATOR=BLOCKS.register(LibBlocks.GENERATOR, ()->new BlockGenerator(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<BlockCapacitor> CAPACITOR=BLOCKS.register(LibBlocks.CAPACITOR,()->new BlockCapacitor(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));

    public static class ItemBlocks {
        public static final RegistryObject<Item> MONAZIT_ORE_ITEM=ITEMS.register(LibBlocks.MONAZIT_ORE,() -> new BlockItem(MONAZIT_ORE.get(),getDefaultItemProperties()));
        public static final RegistryObject<Item> GENERATOR_ITEM=ITEMS.register(LibBlocks.GENERATOR,() -> new BlockItem(GENERATOR.get(),getDefaultItemProperties()));
        public static final RegistryObject<Item> CAPACITOR_ITEM=ITEMS.register(LibBlocks.CAPACITOR,() -> new BlockItem(CAPACITOR.get(),getDefaultItemProperties()));

        private static Item.Properties getDefaultItemProperties() {
            return new Item.Properties().group(ItemGroupMFFS.GetInstance());
        }
    }
}


}
