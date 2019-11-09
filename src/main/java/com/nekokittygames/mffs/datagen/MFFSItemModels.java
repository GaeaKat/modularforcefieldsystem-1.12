package com.nekokittygames.mffs.datagen;

import com.nekokittygames.mffs.common.libs.LibMisc;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;

public class MFFSItemModels extends ItemModelProvider {
    public MFFSItemModels(DataGenerator generator,  ExistingFileHelper existingFileHelper) {
        super(generator, LibMisc.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //getBuilder("monazit_ore")
          //      .parent(getExistingFile(mcLoc("block/block")))
                //.texture("all")
    }

    @Override
    public String getName() {
        return "MFFSitems";
    }
}
