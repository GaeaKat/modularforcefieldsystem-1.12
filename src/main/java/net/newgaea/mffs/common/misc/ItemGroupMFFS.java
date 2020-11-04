package net.newgaea.mffs.common.misc;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.newgaea.mffs.common.init.MFFSItems;
import net.newgaea.mffs.common.libs.LibMisc;

public class ItemGroupMFFS extends ItemGroup {
    public static ItemGroupMFFS INSTANCE;

    public ItemGroupMFFS() {
        super(LibMisc.MOD_ID);
    }

    public static ItemGroupMFFS GetInstance() {
        if(INSTANCE==null)
            INSTANCE=new ItemGroupMFFS();
        return INSTANCE;
    }
    @Override
    public ItemStack createIcon() {
        return new ItemStack(MFFSItems.MONAZIT_CRYSTAL.get());
    }
}
