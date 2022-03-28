package dev.katcodes.mffs.common.init;

import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.katcodes.mffs.api.MFFSTags;
import dev.katcodes.mffs.common.blocks.SimpleNetworkBlock;
import dev.katcodes.mffs.common.register.RegisterCommon;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.Contract;

public class MFFSItems {
    @Contract( pure = true)
    private MFFSItems() {
    }



    public static final RegistryEntry<Item> MONAZIT_CRYSTAL = RegisterCommon.REGISTRATE.item("monazit_crystal",Item::new)
            .tag(MFFSTags.Items.MONAZIT_GEM)
            .recipe(((ctx, provider) ->
                provider.smeltingAndBlasting(DataIngredient.tag(MFFSTags.Items.MONAZIT_ORES),ctx,0.7f)
            ))
            .defaultModel()
            .defaultLang()
            .register();



    @Contract(pure = true)
    public static void init() {
        // Just here to make sure the static variables get noticed in time to register.
    }
}
