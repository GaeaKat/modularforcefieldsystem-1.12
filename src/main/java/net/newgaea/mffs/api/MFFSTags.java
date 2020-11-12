package net.newgaea.mffs.api;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class MFFSTags {

    public final static ResourceLocation CRYSTAL_MONAZIT_LOCATION=new ResourceLocation("forge","crystals/monazit");
    public final static ITag.INamedTag<Item> CRYSTAL_MONAZIT=ItemTags.createOptional(CRYSTAL_MONAZIT_LOCATION);
}
