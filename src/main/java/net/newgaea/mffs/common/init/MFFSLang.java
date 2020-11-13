package net.newgaea.mffs.common.init;

import net.minecraft.util.ResourceLocation;
import net.newgaea.mffs.common.libs.LibMisc;

public class MFFSLang {
    public static void init() {
        MFFSInit.REGISTRATE.addLang("misc",new ResourceLocation(LibMisc.MOD_ID,"upgrade/valid"),"Upgrades valid for %s");
    }
}
