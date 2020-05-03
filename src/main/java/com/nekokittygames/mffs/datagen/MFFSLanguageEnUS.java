package com.nekokittygames.mffs.datagen;

import com.nekokittygames.mffs.common.init.MFFSBlocks;
import com.nekokittygames.mffs.common.init.MFFSItems;
import com.nekokittygames.mffs.common.init.MFFSRegistration;
import com.nekokittygames.mffs.common.libs.LibMisc;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class MFFSLanguageEnUS extends LanguageProvider {
    public MFFSLanguageEnUS(DataGenerator gen) {
        super(gen, LibMisc.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(MFFSRegistration.Items.MONAZIT_CRYSTAL.get(),"Monazit Crystal");
        add(MFFSRegistration.Blocks.MONAZIT_ORE.get(),"Monazit Ore");
        add(MFFSRegistration.Blocks.GENERATOR.get(),"Power Generator");
        add(MFFSRegistration.Blocks.CAPACITOR.get(),"Capacitor");
        add("itemGroup.modularforcefieldsystem","Modular Forcefield System");
        add("gui.narrate.forge_percent", "Forge Power at %s percent");
    }
}
