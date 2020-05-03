package com.nekokittygames.mffs.common.init;

import com.nekokittygames.mffs.MFFS;
import com.nekokittygames.mffs.common.blocks.BlockGenerator;
import com.nekokittygames.mffs.common.blocks.BlockMonazitOre;
import com.nekokittygames.mffs.common.blocks.powered.BlockCapacitor;
import com.nekokittygames.mffs.common.inventory.GeneratorContainer;
import com.nekokittygames.mffs.common.items.ItemForciumCrystal;
import com.nekokittygames.mffs.common.libs.LibBlocks;
import com.nekokittygames.mffs.common.libs.LibItems;
import com.nekokittygames.mffs.common.misc.ItemGroupMFFS;
import com.nekokittygames.mffs.common.tileentities.TileCapacitor;
import com.nekokittygames.mffs.common.tileentities.TileGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nekokittygames.mffs.common.libs.LibMisc.MOD_ID;

public class MFFSRegistration {
    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MOD_ID);
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MOD_ID);

    public static final DeferredRegister<Item> ITEMBLOCKS = new DeferredRegister<>(ForgeRegistries.ITEMS, MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, MOD_ID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, MOD_ID);

    public static void init() {
        Blocks.hack=1;
        Blocks.ItemBlocks.hack=1;
        Items.hack=1;
        TileEntity.hack=1;
        Container.hack=1;
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMBLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());

    }
public static class Blocks {
    public static int hack=0;
    public static final RegistryObject<Block> MONAZIT_ORE=BLOCKS.register(LibBlocks.MONAZIT_ORE,()-> new BlockMonazitOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<BlockGenerator> GENERATOR=BLOCKS.register(LibBlocks.GENERATOR, ()->new BlockGenerator(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<BlockCapacitor> CAPACITOR=BLOCKS.register(LibBlocks.CAPACITOR,()->new BlockCapacitor(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 3.0F)));

    public static class ItemBlocks {
        public static int hack=0;
        public static final RegistryObject<Item> MONAZIT_ORE_ITEM=ITEMBLOCKS.register(LibBlocks.MONAZIT_ORE,() -> new BlockItem(MONAZIT_ORE.get(),getDefaultItemProperties()));
        public static final RegistryObject<Item> GENERATOR_ITEM=ITEMBLOCKS.register(LibBlocks.GENERATOR,() -> new BlockItem(GENERATOR.get(),getDefaultItemProperties()));
        public static final RegistryObject<Item> CAPACITOR_ITEM=ITEMBLOCKS.register(LibBlocks.CAPACITOR,() -> new BlockItem(CAPACITOR.get(),getDefaultItemProperties()));


    }
}
public static class Items {
    public static int hack=0;
        public static final RegistryObject<Item> MONAZIT_CRYSTAL=ITEMS.register(LibItems.MONAZIT_CRYSTAL,() -> new ItemForciumCrystal(getDefaultItemProperties()));
}


public static class TileEntity {
    public static int hack=0;
    public static final RegistryObject<TileEntityType<TileGenerator>> GENERATOR =TILES.register(LibBlocks.GENERATOR,() -> TileEntityType.Builder.create(TileGenerator::new,MFFSRegistration.Blocks.GENERATOR.get()).build(null));
    public static final RegistryObject<TileEntityType<TileCapacitor>> CAPACITOR=TILES.register(LibBlocks.CAPACITOR,() -> TileEntityType.Builder.create(TileCapacitor::new, Blocks.CAPACITOR.get()).build(null));
}

public static class Container {
        public static int hack=0;
    public static final RegistryObject<ContainerType<GeneratorContainer>> GENERATOR =CONTAINERS.register(LibBlocks.GENERATOR, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        return new GeneratorContainer(windowId, MFFS.proxy.getClientWorld(), pos, inv, MFFS.proxy.getClientPlayer());
    }));
}


    private static Item.Properties getDefaultItemProperties() {
        return new Item.Properties().group(ItemGroupMFFS.GetInstance());
    }

}
