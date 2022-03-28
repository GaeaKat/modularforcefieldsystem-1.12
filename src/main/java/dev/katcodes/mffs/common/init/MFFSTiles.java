package dev.katcodes.mffs.common.init;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.katcodes.mffs.common.blockentities.CapacitorBlockEntity;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Contract;

public class MFFSTiles {
    @Contract(pure = true)
    private MFFSTiles() {

    }

    public static void init() {
        // initialize static
    }
    public static final BlockEntityEntry<CapacitorBlockEntity> CAPACITOR=BlockEntityEntry.cast(MFFSBlocks.CAPACITOR.getSibling(ForgeRegistries.BLOCK_ENTITIES));

}
