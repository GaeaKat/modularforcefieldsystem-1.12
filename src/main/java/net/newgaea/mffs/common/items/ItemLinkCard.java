package net.newgaea.mffs.common.items;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.newgaea.mffs.api.EnumPowerLink;
import net.newgaea.mffs.api.IPowerLinkItem;

public class ItemLinkCard extends ModItem  implements IPowerLinkItem {

    public ItemLinkCard(Properties properties) {
        super(properties);
    }

    @Override
    public EnumPowerLink getLinkType(ItemStack stack, World world, Entity entity) {
        CompoundNBT tag=stack.getOrCreateChildTag("link");
        String type=tag.getString("type");
        type=(type!=null)?type:"";
        if(type.equalsIgnoreCase("")) {
            return EnumPowerLink.None;
        }
        else if(type.equalsIgnoreCase("link")) {
            return EnumPowerLink.Link;
        }
        else if(type.equalsIgnoreCase("creative")) {
            return EnumPowerLink.Creative;
        }
        return EnumPowerLink.None;
    }


}
