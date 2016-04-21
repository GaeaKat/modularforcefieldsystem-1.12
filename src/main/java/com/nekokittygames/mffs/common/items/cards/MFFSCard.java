package com.nekokittygames.mffs.common.items.cards;

import com.nekokittygames.mffs.common.items.ModItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Katrina on 20/04/2016.
 */
public class MFFSCard extends ModItem {
    public static String CARD_INFO="card";
    public NBTTagCompound GetCardInfo(ItemStack stack)
    {
        return stack.getSubCompound(CARD_INFO,true);
    }
}
