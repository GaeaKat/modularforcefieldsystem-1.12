package net.newgaea.mffs.common.init;

import net.minecraft.util.ResourceLocation;
import net.newgaea.mffs.common.libs.LibMisc;

public class MFFSLang {
    public static void init() {
        MFFSInit.REGISTRATE.addRawLang("itemGroup.mffs","MFFS");
        MFFSInit.REGISTRATE.addLang("misc",new ResourceLocation(LibMisc.MOD_ID,"upgrade/valid"),"Upgrades valid for %s");
        MFFSInit.REGISTRATE.addLang("misc",new ResourceLocation(LibMisc.MOD_ID,"monazitcell/active"),"[Monazit Cell] Active.");
        MFFSInit.REGISTRATE.addLang("misc",new ResourceLocation(LibMisc.MOD_ID,"monazitcell/inactive"),"[Monazit Cell] Inactive.");
        MFFSInit.REGISTRATE.addLang("misc",new ResourceLocation(LibMisc.MOD_ID,"monazitcell/info"),"%d / %d  Monazit");
        MFFSInit.REGISTRATE.addLang("misc",new ResourceLocation(LibMisc.MOD_ID,"monazitcell/active/message"),"\u00a72Active");
        MFFSInit.REGISTRATE.addLang("misc",new ResourceLocation(LibMisc.MOD_ID,"monazitcell/inactive/message"),"\u00a74Inactive");
        MFFSInit.REGISTRATE.addLang("misc",new ResourceLocation(LibMisc.MOD_ID,"powercrystal/info"),"%d FE/%d FE ");
        MFFSInit.REGISTRATE.addLang("cards",new ResourceLocation(LibMisc.MOD_ID,"linksto"),"Links to: %s");
        MFFSInit.REGISTRATE.addLang("cards",new ResourceLocation(LibMisc.MOD_ID,"world"),"World: %s");
        MFFSInit.REGISTRATE.addLang("cards",new ResourceLocation(LibMisc.MOD_ID,"linktarget"),"Coords: %s");
        MFFSInit.REGISTRATE.addLang("cards",new ResourceLocation(LibMisc.MOD_ID,"valid"),"\u00a72Valid");
        MFFSInit.REGISTRATE.addLang("cards",new ResourceLocation(LibMisc.MOD_ID,"invalid"),"\u00a74Invalid");
        MFFSInit.REGISTRATE.addLang("cards",new ResourceLocation(LibMisc.MOD_ID,"capacitor/created"),"[Capacitor] Success: Power Link Card Created.");
    }
}
