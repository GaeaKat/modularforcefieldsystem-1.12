package com.nekokittygames.mffs.datagen;

import com.nekokittygames.mffs.common.init.MFFSBlocks;
import com.nekokittygames.mffs.common.init.MFFSRegistration;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ForgeBlockTagsProvider;

import java.nio.file.Path;

public class MFFSTags extends BlockTagsProvider {
    public MFFSTags(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerTags() {
        this.getBuilder(Tags.Blocks.ORES).add(MFFSRegistration.Blocks.MONAZIT_ORE.get());

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
