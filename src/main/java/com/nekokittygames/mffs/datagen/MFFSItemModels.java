package com.nekokittygames.mffs.datagen;

import com.google.common.base.Preconditions;
import com.nekokittygames.mffs.common.init.MFFSRegistration;
import com.nekokittygames.mffs.common.init.MFFSTileTypes;
import com.nekokittygames.mffs.common.libs.LibMisc;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.fml.RegistryObject;

public class MFFSItemModels extends ItemModelProvider {
    public MFFSItemModels(DataGenerator generator,  ExistingFileHelper existingFileHelper) {
        super(generator, LibMisc.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //getBuilder("monazit_ore")
                //.wi
        blockItem(MFFSRegistration.Blocks.MONAZIT_ORE.get(),MFFSRegistration.Blocks.ItemBlocks.MONAZIT_ORE_ITEM.get());
        blockItem(MFFSRegistration.Blocks.GENERATOR.get(),MFFSRegistration.Blocks.ItemBlocks.GENERATOR_ITEM.get());

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

    private void blockItem(Block block, Item item) {

        withExistingParent(name(item), modLoc("block/" + name(item)));
    }



    @Override
    public String getName() {
        return "MFFSitems";
    }
}
