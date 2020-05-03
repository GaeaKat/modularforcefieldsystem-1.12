package com.nekokittygames.mffs.common.misc;

import com.nekokittygames.mffs.common.init.MFFSItems;
import com.nekokittygames.mffs.common.init.MFFSRegistration;
import com.nekokittygames.mffs.common.libs.LibMisc;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ItemGroupMFFS extends ItemGroup {
    private static ItemGroupMFFS INSTANCE;

    public static ItemGroupMFFS GetInstance() {
        if(INSTANCE==null)
            INSTANCE=new ItemGroupMFFS();
        return INSTANCE;
    }

    public ItemGroupMFFS() {
        super(LibMisc.MOD_ID);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(MFFSRegistration.Items.MONAZIT_CRYSTAL.get());
    }
}
