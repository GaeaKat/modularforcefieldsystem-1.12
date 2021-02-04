package net.newgaea.mffs.common.init;

import com.tterrag.registrate.util.entry.TileEntityEntry;
import net.minecraftforge.registries.ForgeRegistries;
import net.newgaea.mffs.common.tiles.*;

public class MFFSTiles {


    public static void init()
    {

    }

    public static final TileEntityEntry<TileGenerator> GENERATOR = TileEntityEntry.cast(MFFSBlocks.GENERATOR.getSibling(ForgeRegistries.TILE_ENTITIES));

    public static final TileEntityEntry<TileCapacitor> CAPACITOR= TileEntityEntry.cast(MFFSBlocks.CAPACITOR.getSibling(ForgeRegistries.TILE_ENTITIES));
    public static final TileEntityEntry<TileExtractor> EXTRACTOR = TileEntityEntry.cast(MFFSBlocks.EXTRACTOR.getSibling(ForgeRegistries.TILE_ENTITIES));
    public static final TileEntityEntry<TileForcefield> FORCEFIELD = TileEntityEntry.cast(MFFSBlocks.FORCEFIELD.getSibling(ForgeRegistries.TILE_ENTITIES));
    public static final TileEntityEntry<TileProjector> PROJECTOR = TileEntityEntry.cast(MFFSBlocks.PROJECTOR.getSibling(ForgeRegistries.TILE_ENTITIES));
}
