package net.newgaea.mffs.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.newgaea.mffs.api.MFFSTags;
import net.newgaea.mffs.common.init.MFFSBlocks;
import net.newgaea.mffs.common.init.MFFSItems;
import net.newgaea.mffs.common.libs.LibMisc;

import java.nio.file.Path;

public class MFFSItemTags extends ItemTagsProvider {
    public MFFSItemTags(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, LibMisc.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {

        this.getOrCreateBuilder(Tags.Items.ORES).add(MFFSItems.MONAZIT_ORE.get());
        this.createBuilderIfAbsent(MFFSTags.CRYSTAL_MONAZIT);
        this.getOrCreateBuilder(MFFSTags.CRYSTAL_MONAZIT).add(MFFSItems.MONAZIT_CRYSTAL.get());
    }

    @Override
    protected Path makePath(ResourceLocation id) {
        return super.makePath(id);
    }

    @Override
    public String getName() {
        return "MFFSItemTags";
    }
}
