package com.nekokittygames.mffs.common.block;

import com.google.common.base.Preconditions;
import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.tileentity.*;
import com.nekokittygames.mffs.libs.LibBlockNames;
import com.nekokittygames.mffs.libs.LibMisc;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashSet;
import java.util.Set;

import static com.nekokittygames.mffs.common.Functions.Null;

@GameRegistry.ObjectHolder(ModularForceFieldSystem.MODID)
public class ModBlocks {
    public static final BlockAdvSecurtyStation ADV_SECURITY=Null();
    public static final BlockAreaDefenseStation AREA_DEFENSE=Null();
    public static final BlockCapacitor CAPACITOR=Null();
    public static final BlockControlSystem CONTROL_SYSTEM=Null();
    public static final BlockExtractor EXTRACTOR=Null();
    public static final BlockForceField FORCE_FIELD=Null();
    public static final BlockMonazitOre MONAZIT_ORE=Null();
    public static final BlockProjector PROJECTOR=Null();
    public static final BlockSecurtyStorage SECURITY_STORAGE=Null();

    @Mod.EventBusSubscriber(modid = ModularForceFieldSystem.MODID)
    public static class RegistrationHandler {
        public static final Set<ItemBlock> ITEM_BLOCKS = new HashSet<ItemBlock>();
        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event) {
            final IForgeRegistry<Block> registry = event.getRegistry();
            final Block[] blocks=
                    {
                            new BlockAdvSecurtyStation(),
                            new BlockAreaDefenseStation(),
                            new BlockCapacitor(),
                            new BlockControlSystem(),
                            new BlockExtractor(),
                            new BlockForceField(),
                            new BlockMonazitOre(),
                            new BlockProjector(),
                            new BlockSecurtyStorage()
                    };
            registry.registerAll(blocks);
            registerTileEntities();
        }

        @SubscribeEvent
        public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
            final ItemBlock[] items = {
                    new ItemBlock(ADV_SECURITY),
                    new ItemBlock(AREA_DEFENSE),
                    new ItemBlock(CAPACITOR),
                    new ItemBlock(CONTROL_SYSTEM),
                    new ItemBlock(EXTRACTOR),
                    new ItemBlock(FORCE_FIELD),
                    new ItemBlock(MONAZIT_ORE),
                    new ItemBlock(PROJECTOR),
                    new ItemBlock(SECURITY_STORAGE)
            };
            final IForgeRegistry<Item> registry = event.getRegistry();

            for (final ItemBlock item : items) {
                final Block block = item.getBlock();
                final ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName(), "Block %s has null registry name", block);
                registry.register(item.setRegistryName(registryName));
                ITEM_BLOCKS.add(item);
            }
        }
    }
    private static void registerTileEntities() {
        registerTileEntity(TileEntityAdvSecurityStation.class, LibBlockNames.ADV_SECURITY);
        registerTileEntity(TileEntityAreaDefenseStation.class,LibBlockNames.AREA_DEFENSE);
        registerTileEntity(TileEntityCapacitor.class,LibBlockNames.CAPACITOR);
        registerTileEntity(TileEntityControlSystem.class,LibBlockNames.CONTROL_SYSTEM);
        registerTileEntity(TileEntityExtractor.class,LibBlockNames.EXTRACTOR);
        registerTileEntity(TileEntityForceField.class,LibBlockNames.FORCE_FIELD);
        registerTileEntity(TileEntityProjector.class,LibBlockNames.PROJECTOR);
        registerTileEntity(TileEntitySecStorage.class,LibBlockNames.SECURITY_STORAGE);

    }

    private static void registerTileEntity(final Class<? extends TileEntity> tileEntityClass, final String name) {
        GameRegistry.registerTileEntity(tileEntityClass, LibMisc.RESOURCE_PREFIX+ name);
    }
}
