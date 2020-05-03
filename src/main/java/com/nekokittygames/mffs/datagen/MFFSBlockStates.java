package com.nekokittygames.mffs.datagen;

import com.google.common.base.Preconditions;
import com.nekokittygames.mffs.MFFS;
import com.nekokittygames.mffs.common.init.MFFSBlocks;
import com.nekokittygames.mffs.common.init.MFFSRegistration;
import com.nekokittygames.mffs.common.libs.LibMisc;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MFFSBlockStates extends BlockStateProvider {

    private static final Logger LOGGER = LogManager.getLogger();
    private ExistingFileHelper helper;
    public MFFSBlockStates(DataGenerator gen,ExistingFileHelper exFileHelper) {
        super(gen, LibMisc.MOD_ID, exFileHelper);
        helper=exFileHelper;
    }
    private ResourceLocation registryName(final Item item) {
        return Preconditions.checkNotNull(item.getRegistryName(), "Item %s has a null registry name", item);
    }

    private ResourceLocation registryName(final Block block) {
        return Preconditions.checkNotNull(block.getRegistryName(), "Block %s has a null registry name", block);
    }

    private String name(final Item item) {
        return registryName(item).getPath();
    }
    private String name(final Block block) {
        return registryName(block).getPath();
    }
    @Override
    protected void registerStatesAndModels() {

        ModelFile monazit_ore=models().cubeAll(name(MFFSRegistration.Blocks.MONAZIT_ORE.get()),modLoc("block/monazit_ore"));
        ModelFile generator=models().orientable(name(MFFSRegistration.Blocks.GENERATOR.get()),mcLoc("block/furnace_side"),mcLoc("block/furnace_front"),mcLoc("block/furnace_top"));
        //boolean happens=existingFileHelper.exists(modLoc("block/monazit_ore"), ResourcePackType.CLIENT_RESOURCES,".png", "textures");
        //ModelFile monazit = cubeAll(MFFSRegistration.Blocks.MONAZIT_ORE.get());
        //ModelFile monazit =cubeAll("monazit",modLoc("block/monazit_ore"));
        simpleBlock(MFFSRegistration.Blocks.MONAZIT_ORE.get(),monazit_ore);
        this.horizontalBlock(MFFSRegistration.Blocks.GENERATOR.get(),generator);

    }

    public ExistingFileHelper getExistingFileHelper() {
        return models().existingFileHelper;
    }
}
