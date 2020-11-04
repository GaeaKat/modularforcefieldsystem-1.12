package net.newgaea.mffs.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.newgaea.mffs.common.init.MFFSBlocks;
import net.newgaea.mffs.common.libs.LibMisc;

import java.nio.file.Path;

public class MFFSTags extends BlockTagsProvider {
    public MFFSTags(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, LibMisc.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        this.getOrCreateBuilder(Tags.Blocks.ORES).add(MFFSBlocks.MONAZIT_ORE.get());
    }

    @Override
    protected Path makePath(ResourceLocation id) {
        return super.makePath(id);
    }

    @Override
    public String getName() {
        return "MFFSTags";
    }
}
