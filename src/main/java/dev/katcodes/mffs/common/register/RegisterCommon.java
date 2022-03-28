package dev.katcodes.mffs.common.register;

import com.tterrag.registrate.Registrate;
import dev.katcodes.mffs.MFFSMod;
import dev.katcodes.mffs.common.init.MFFSTab;
import org.jetbrains.annotations.Contract;

/**
 * Holds the registrate instance for the mod.
 */
public class RegisterCommon {
    @Contract(pure = true)
    private RegisterCommon(){
    }

    /**
     * Registrate instance with a default creative tab.
     */
    public static final Registrate REGISTRATE = Registrate.create(MFFSMod.MODID).creativeModeTab(MFFSTab::new,"MFFS");;
}
