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
import net.newgaea.mffs.common.tiles.TileForcefield;
import net.newgaea.mffs.common.tiles.TileGenerator;
import net.newgaea.mffs.common.tiles.TileProjector;

import static net.newgaea.mffs.common.libs.LibMisc.MOD_ID;

public class MFFSTiles {


    public static void init()
    {

    }

    public static final TileEntityEntry<TileGenerator> GENERATOR = TileEntityEntry.cast(MFFSBlocks.GENERATOR.getSibling(ForgeRegistries.TILE_ENTITIES));

    public static final TileEntityEntry<TileCapacitor> CAPACITOR= TileEntityEntry.cast(MFFSBlocks.CAPACITOR.getSibling(ForgeRegistries.TILE_ENTITIES));
    public static final TileEntityEntry<TileForcefield> FORCEFIELD = TileEntityEntry.cast(MFFSBlocks.FORCEFIELD.getSibling(ForgeRegistries.TILE_ENTITIES));
    public static final TileEntityEntry<TileProjector> PROJECTOR = TileEntityEntry.cast(MFFSBlocks.PROJECTOR.getSibling(ForgeRegistries.TILE_ENTITIES));
}
