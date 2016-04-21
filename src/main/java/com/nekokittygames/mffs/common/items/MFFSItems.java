package com.nekokittygames.mffs.common.items;

import com.nekokittygames.mffs.client.rendering.MeshDefinition;
import com.nekokittygames.mffs.common.items.cards.MFFSBlankCard;
import com.nekokittygames.mffs.common.items.cards.MFFSLinkCard;
import com.nekokittygames.mffs.common.libs.LibItemNames;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

/**
 * Created by Katrina on 20/04/2016.
 */
public class MFFSItems {

    public static MFFSBlankCard blank_card;
    public static MFFSLinkCard link_card;


    public static void createItems()
    {
        blank_card=new MFFSBlankCard();
        link_card=new MFFSLinkCard();
    }


    public static void setupClientItems()
    {
        setupClientItem(blank_card, LibItemNames.BLANK_CARD);
        setupClientItem(link_card,LibItemNames.LINK_CARD);
    }


    public static void setupClientItem(Item item, String name)
    {
        final ModelResourceLocation loc=new ModelResourceLocation("mffs:"+name,"inventory");
        ModelLoader.setCustomMeshDefinition(item,new MeshDefinition(loc));
    }



}
