package com.nekokittygames.mffs.datagen;

import com.nekokittygames.mffs.MFFS;
import com.nekokittygames.mffs.common.init.MFFSBlocks;
import com.nekokittygames.mffs.common.libs.LibMisc;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MFFSBlockStates extends BlockStateProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    public MFFSBlockStates(DataGenerator gen,ExistingFileHelper exFileHelper) {
        super(gen, LibMisc.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        //boolean happens=existingFileHelper.exists(modLoc("block/monazit_ore"), ResourcePackType.CLIENT_RESOURCES,".png", "textures");
        ModelFile monazit = cubeAll(MFFSBlocks.MONAZIT_ORE);
        //ModelFile monazit =cubeAll("monazit",modLoc("block/monazit_ore"));
        simpleBlock(MFFSBlocks.MONAZIT_ORE,monazit);
    }
}
