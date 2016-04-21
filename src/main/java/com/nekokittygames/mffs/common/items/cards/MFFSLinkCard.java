package com.nekokittygames.mffs.common.items.cards;

import com.nekokittygames.mffs.common.libs.LibItemNames;
import com.teambr.bookshelf.client.gui.GuiColor;
import elec332.core.util.StatCollector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;
import java.util.UUID;

/**
 * Created by Katrina on 20/04/2016.
 */
public class MFFSLinkCard extends MFFSCard {

    public static String NETWORK_ID_MSB ="NetworkIdMSB";
    public static String NETWORK_ID_LSB ="NetworkIdLSB";
    public MFFSLinkCard()
    {
        this.setUnlocalizedName(LibItemNames.LINK_CARD);
        this.setRegistryName(LibItemNames.LINK_CARD);
        GameRegistry.register(this);
    }


    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        if(GetCardInfo(stack).hasKey(NETWORK_ID_MSB) && GetCardInfo(stack).getLong(NETWORK_ID_MSB)!=-1)
        {
            long upperNetworkID=GetCardInfo(stack).getLong(NETWORK_ID_MSB);
            long lowerNetworkID=GetCardInfo(stack).getLong(NETWORK_ID_LSB);
            UUID networkID=new UUID(upperNetworkID,lowerNetworkID);
        }
        else {
            tooltip.add(GuiColor.RED+ StatCollector.translateToLocal("mffs.text.invalidLinkCard"));
        }
    }
}


