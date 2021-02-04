package net.newgaea.mffs.common.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.newgaea.mffs.common.init.MFFSItems;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemMonazitCell extends  ModItem{
    public ItemMonazitCell(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }


    public int getItemDamage(ItemStack stack) {
        return 101 - ((getMonazitLevel(stack) * 100) / getMaxMonazitLevel(stack));
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        if(worldIn.isRemote==false) {
            if(getActive(stack)) {
                if(getMonazitLevel(stack) < getMaxMonazitLevel(stack)) {
                    if(entityIn instanceof PlayerEntity) {
                        for(ItemStack invStack: ((PlayerEntity)entityIn).inventory.mainInventory) {
                            if(!invStack.isEmpty()) {
                                if(invStack.getItem() == MFFSItems.MONAZIT_CRYSTAL.get()) {
                                    setMonazitLevel(stack,getMonazitLevel(stack)+1);
                                    invStack.setCount(invStack.getCount()-1);
                                    break;
                                }
                            }
                        }
                    }
                }
                stack.setDamage(getItemDamage(stack));
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if(worldIn.isRemote == false) {
            if(!getActive(itemstack)) {
                setActive(itemstack,true);
                playerIn.sendMessage(new TranslationTextComponent("misc.mffs.monazitcell.active"), Util.DUMMY_UUID);
            }
            else {
                setActive(itemstack,false);
                playerIn.sendMessage(new TranslationTextComponent("misc.mffs.monazitcell.inactive"), Util.DUMMY_UUID);
            }
        }
        return ActionResult.resultConsume(itemstack);
    }

    public int getMonazitLevel(ItemStack stack) {
        CompoundNBT tag=stack.getOrCreateChildTag("monazit");
        if(tag.contains("amount"))
            return tag.getInt("amount");
        return 0;
    }
    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        super.fillItemGroup(group, items);
        if(this.isInGroup(group)) {
            ItemStack stack=new ItemStack(this);
            setMonazitLevel(stack,getMaxMonazitLevel(stack));
            items.add(stack);
        }
    }
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("misc.mffs.monazitcell.info", getMonazitLevel(stack), getMaxMonazitLevel(stack)));
        if(getActive(stack))
            tooltip.add(new TranslationTextComponent("misc.mffs.monazitcell.active.message"));
        else
            tooltip.add(new TranslationTextComponent("misc.mffs.monazitcell.inactive.message"));
    }


    public void setMonazitLevel(ItemStack stack, int amount) {
        CompoundNBT tag=stack.getOrCreateChildTag("monazit");
        tag.putInt("amount",amount);
    }

    public boolean getActive(ItemStack stack) {
        CompoundNBT tag=stack.getOrCreateChildTag("monazit");
        if(tag.contains("active"))
            return tag.getBoolean("active");
        return false;
    }

    public void setActive(ItemStack stack,boolean active) {
        CompoundNBT tag=stack.getOrCreateChildTag("monazit");
        tag.putBoolean("active",active);
    }

    public int getMaxMonazitLevel(ItemStack stack) {
        return 1000;
    }

    public boolean useMonazit(int count,ItemStack stack) {
        if(count > getMonazitLevel(stack))
            return false;
        else
        {
            setMonazitLevel(stack,getMonazitLevel(stack)-count);
            return true;
        }

    }

}
