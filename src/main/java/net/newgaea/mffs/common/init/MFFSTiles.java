package net.newgaea.mffs.common.init;

import com.tterrag.registrate.util.entry.TileEntityEntry;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.newgaea.mffs.common.libs.LibTiles;
import net.newgaea.mffs.common.tiles.TileCapacitor;
import net.newgaea.mffs.common.tiles.TileGenerator;

import static net.newgaea.mffs.common.libs.LibMisc.MOD_ID;

public class MFFSTiles {

    private static final DeferredRegister<TileEntityType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MOD_ID);

    public static void init()
    {
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final TileEntityEntry<TileGenerator> GENERATOR = TileEntityEntry.cast(MFFSBlocks.GENERATOR.getSibling(ForgeRegistries.TILE_ENTITIES));

    public static final TileEntityEntry<TileCapacitor> CAPACITOR= TileEntityEntry.cast(MFFSBlocks.CAPACITOR.getSibling(ForgeRegistries.TILE_ENTITIES));
}
