package net.newgaea.mffs.common.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.IntArray;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.newgaea.mffs.MFFS;
import net.newgaea.mffs.client.gui.screens.CapacitorScreen;
import net.newgaea.mffs.client.gui.screens.ExtractorScreen;
import net.newgaea.mffs.client.gui.screens.GeneratorScreen;
import net.newgaea.mffs.client.gui.screens.ProjectorScreen;
import net.newgaea.mffs.common.inventory.CapacitorContainer;
import net.newgaea.mffs.common.inventory.ExtractorContainer;
import net.newgaea.mffs.common.inventory.GeneratorContainer;
import net.newgaea.mffs.common.inventory.ProjectorContainer;
import net.newgaea.mffs.common.libs.LibContainer;
import net.newgaea.mffs.common.tiles.TileCapacitor;
import net.newgaea.mffs.common.tiles.TileProjector;

import static net.newgaea.mffs.common.libs.LibMisc.MOD_ID;

public class MFFSContainer {
    public static void init()
    {

    }


    public static final RegistryEntry<ContainerType<GeneratorContainer>> GENERATOR=MFFSInit.REGISTRATE.object(LibContainer.GENERATOR)
            .container(GeneratorContainer::new,()-> GeneratorScreen::new).register();


    public static final RegistryEntry<ContainerType<CapacitorContainer>> CAPACITOR = MFFSInit.REGISTRATE.object(LibContainer.CAPACITOR)
            .container(CapacitorContainer::new, () -> CapacitorScreen::new).register();
    public static final RegistryEntry<ContainerType<ExtractorContainer>> EXTRACTOR = MFFSInit.REGISTRATE.object(LibContainer.EXTRACTOR)
            .container(ExtractorContainer::new,() -> ExtractorScreen::new).register();
    public static final RegistryEntry<ContainerType<ProjectorContainer>> PROJECTOR = MFFSInit.REGISTRATE.object(LibContainer.PROJECTOR)
            .container(ProjectorContainer::new,()->ProjectorScreen::new).register();

}

