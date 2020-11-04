package net.newgaea.mffs.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.newgaea.mffs.common.libs.LibItems;
import net.newgaea.mffs.common.libs.LibMisc;

public class MFFSItemsProvider extends ItemModelProvider {
    public MFFSItemsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, LibMisc.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        singleTexture(LibItems.MONAZIT_CRYSTAL,new ResourceLocation("item/handheld"),"layer0",new ResourceLocation(LibMisc.MOD_ID,"item/monazit_crystal"));
    }
}
