package com.nekokittygames.mffs.datagen;

import com.nekokittygames.mffs.common.init.MFFSBlocks;
import com.nekokittygames.mffs.common.init.MFFSItems;
import com.nekokittygames.mffs.common.libs.LibMisc;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class MFFSLanguageEnUS extends LanguageProvider {
    public MFFSLanguageEnUS(DataGenerator gen) {
        super(gen, LibMisc.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(MFFSItems.MONAZIT_CRYSTAL,"Monazit Crystal");
        add(MFFSBlocks.MONAZIT_ORE,"Monazit Ore");
        add(MFFSBlocks.GENERATOR,"Power Generator");
        add(MFFSBlocks.CAPACITOR,"Capacitor");
        add("itemGroup.modularforcefieldsystem","Modular Forcefield System");
        add("gui.narrate.forge_percent", "Forge Power at %s percent");
    }
}
