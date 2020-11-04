package net.newgaea.mffs.common.init;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.newgaea.mffs.common.libs.LibTiles;
import net.newgaea.mffs.common.tiles.TileGenerator;

import static net.newgaea.mffs.common.libs.LibMisc.MOD_ID;

public class MFFSTiles {

    private static final DeferredRegister<TileEntityType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MOD_ID);

    public static void init()
    {
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<TileEntityType<?>> GENERATOR = CONTAINERS.register(LibTiles.GENERATOR,() ->
            TileEntityType.Builder.create(TileGenerator::new,MFFSBlocks.GENERATOR.get()).build(null)
            );
}
