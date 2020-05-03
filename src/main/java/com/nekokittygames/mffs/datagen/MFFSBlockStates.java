package com.nekokittygames.mffs.datagen;

import com.google.common.base.Preconditions;
import com.nekokittygames.mffs.common.init.MFFSRegistration;
import com.nekokittygames.mffs.common.libs.LibMisc;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.state.properties.BlockStateProperties;
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
        simpleBlock(MFFSRegistration.Blocks.MONAZIT_ORE.get(),monazit_ore);

        ModelFile generator=models().orientable(name(MFFSRegistration.Blocks.GENERATOR.get()),mcLoc("block/furnace_side"),mcLoc("block/furnace_front"),mcLoc("block/furnace_top"));
        this.horizontalBlock(MFFSRegistration.Blocks.GENERATOR.get(),generator);

        ModelFile capacitor_active= models().orientable(name(MFFSRegistration.Blocks.CAPACITOR.get())+"_active",modLoc("block/capacitor/side_active"),modLoc("block/capacitor/face_active"),modLoc("block/capacitor/side_active"));
        ModelFile capacitor_inactive= models().orientable(name(MFFSRegistration.Blocks.CAPACITOR.get())+"_inactive",modLoc("block/capacitor/side_active"),modLoc("block/capacitor/face_active"),modLoc("block/capacitor/side_active"));
        horizontalFaceBlock(MFFSRegistration.Blocks.CAPACITOR.get(),state -> state.get(BlockStateProperties.ENABLED)?capacitor_active:capacitor_inactive);
    }





    public ExistingFileHelper getExistingFileHelper() {
        return models().existingFileHelper;
    }
}
