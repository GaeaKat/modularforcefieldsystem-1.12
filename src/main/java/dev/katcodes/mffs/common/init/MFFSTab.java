package dev.katcodes.mffs.common.init;

import dev.katcodes.mffs.MFFSMod;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Creative mode tab for MFFS
 */
public class MFFSTab extends CreativeModeTab {

    /**
     * Constructs a plain creative tab
     */
    public MFFSTab() {
        super(MFFSMod.MODID);
    }


    /**
     * Returns the ItemStack to use for the tab icon
     * @return itemstack of the tab icon
     */
    @Override
    public @NotNull ItemStack makeIcon() {
        return new ItemStack(MFFSItems.MONAZIT_CRYSTAL.get());
    }
}
