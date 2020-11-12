package net.newgaea.mffs.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import net.newgaea.mffs.common.init.MFFSBlocks;
import net.newgaea.mffs.common.init.MFFSItems;
import net.newgaea.mffs.common.libs.LibMisc;

public class MFFSLangEn_Us extends LanguageProvider {

    public MFFSLangEn_Us(DataGenerator gen) {
        super(gen, LibMisc.MOD_ID,"en_us");
    }

    @Override
    protected void addTranslations() {
        add(MFFSBlocks.MONAZIT_ORE.get(),"Monazit Ore");
        add(MFFSItems.MONAZIT_CRYSTAL.get(),"Monazit Crystal");
        add(MFFSBlocks.GENERATOR.get(),"Generator");
        add(MFFSItems.LINK_CARD.get(), "Link Card");
        add(MFFSItems.MONAZIT_CIRCUIT.get(), "Monazit Circuit");
        add(MFFSBlocks.CAPACITOR.get(),"MFFS Capacitor");
    }
}
