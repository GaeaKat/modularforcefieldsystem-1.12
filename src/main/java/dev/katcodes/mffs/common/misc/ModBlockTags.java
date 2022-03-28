package dev.katcodes.mffs.common.misc;

import dev.katcodes.mffs.MFFSMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModBlockTags extends BlockTagsProvider {


    public ModBlockTags(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, MFFSMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        //tag().add(ModBlocks.MONAZIT_ORE.get());
    }
}
