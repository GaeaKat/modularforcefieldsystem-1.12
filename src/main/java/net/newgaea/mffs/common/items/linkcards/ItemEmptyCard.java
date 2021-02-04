package net.newgaea.mffs.common.items.linkcards;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.newgaea.mffs.common.init.MFFSItems;
import net.newgaea.mffs.common.items.ModItem;
import net.newgaea.mffs.common.tiles.TileCapacitor;

import java.util.UUID;

public class ItemEmptyCard extends ModItem {
    public ItemEmptyCard(Properties properties) {
        super(properties);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 16;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if(context.getWorld().isRemote)
            return ActionResultType.PASS;
        TileEntity tileEntity=context.getWorld().getTileEntity(context.getPos());
        // todo: Security Station
        if(tileEntity instanceof TileCapacitor) {
            // todo: check security
            ItemStack newCard=new ItemStack(MFFSItems.POWER_LINK_CARD.get());
            ((ItemPowerLinkCard)newCard.getItem()).setInformation(newCard,context.getPos(),"capacitorID",((TileCapacitor)tileEntity).getPowerStorageID(),context.getWorld());
            ItemCard.setAreaName(newCard,((TileCapacitor)tileEntity).getDeviceName());
            context.getItem().setCount(context.getItem().getCount()-1);
            if(!context.getPlayer().inventory.addItemStackToInventory(newCard))
                context.getPlayer().dropItem(newCard,false);
            context.getPlayer().sendMessage(new TranslationTextComponent("cards.mffs.capacitor.created"), Util.DUMMY_UUID);
            return ActionResultType.SUCCESS;
        }
        return super.onItemUse(context);
    }
}
