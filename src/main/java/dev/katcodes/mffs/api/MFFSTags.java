package dev.katcodes.mffs.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


/**
 * Tags added by MFFS
 */
public final class MFFSTags {
    @Contract(pure = true)
    private MFFSTags() {}

    @Contract(pure = true)
    public static void init() {
        // Initializing static methods
    }

    /**
     * BlockTags added by MFFS
     */
    public static final class Blocks {
        @Contract(pure = true)
        private Blocks() {

        }

        /**
         * Any Monazit base ores pre smelting.
         */
        public static final TagKey<Block> MONAZIT_ORES = BlockTags.create(forge("ores/monazit"));
    }

    /**
     * ItemTags added by MFFS
     */
    public static final class Items {
        @Contract(pure = true)
        private Items() {}

        /**
         * Any Monazit base ores pre smelting.
         */
        public static final TagKey<Item> MONAZIT_ORES = ItemTags.create(forge("ores/monazit"));
        public static final TagKey<Item> MONAZIT_GEM = ItemTags.create(forge("gems/monazit"));
    }



    /**
     * The forge function is a helper function that returns a resource location for the given path.
     * The returned object is guaranteed to be non-null and not an empty path.
     *
     *
     *
     * @param path Used to Specify the location of the resource.
     * @return A new resourcelocation object with the specified path.
     *
     */
    @Contract("_ -> new")
    private static @NotNull ResourceLocation forge(String path) {
        return new ResourceLocation("forge", path);
    }
}
