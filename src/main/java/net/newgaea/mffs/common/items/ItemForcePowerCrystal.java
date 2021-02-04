package net.newgaea.mffs.common.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.newgaea.mffs.api.IForceEnergyItem;
import net.newgaea.mffs.api.IPowerLinkItem;
import net.newgaea.mffs.common.tiles.TileNetwork;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemForcePowerCrystal extends ModItem implements IForceEnergyItem, IPowerLinkItem {

    public ItemForcePowerCrystal(Properties properties) {
        super(properties);
    }

    @Override
    public int getAvailablePower(ItemStack itemStack) {
        return getAvailablePower(itemStack,null,null);
    }

    @Override
    public int getMaximumPower(ItemStack itemStack) {
        return 5000000;
    }

    @Override
    public boolean consumePower(ItemStack itemStack, int powerAmount, boolean simulation) {
        return consumePower(itemStack,powerAmount,simulation,null,null);
    }

    @Override
    public void setAvailablePower(ItemStack stack, int amount) {
        CompoundNBT tag=stack.getOrCreateChildTag("force");
        tag.putInt("power",amount);
        stack.setDamage(getItemDamage(stack));
    }

    @Override
    public int getPowerTransferRate() {
        return 100000;
    }

    @Override
    public int getDamage(ItemStack stack) {
        return 101-((getAvailablePower(stack)*100) / getMaximumPower(stack));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("misc.mffs.powercrystal.info",getAvailablePower(stack),getMaximumPower(stack)));
    }

    @Override
    public int getItemDamage(ItemStack stackInSlot) {
        return 0;
    }

    @Override
    public int getPercentageCapacity(ItemStack stack, TileNetwork machine, World world) {
        return ((getAvailablePower(stack,null,null)/1000) * 100) / (getMaximumPower(stack) / 1000);
    }

    @Override
    public int getAvailablePower(ItemStack stack, TileNetwork machine, World world) {
        CompoundNBT tag=stack.getOrCreateChildTag("force");
        if(tag.contains("power"))
            return tag.getInt("power");
        return 0;
    }

    @Override
    public int getMaximumPower(ItemStack stack, TileNetwork machine, World world) {
        return getMaximumPower(stack);
    }

    @Override
    public boolean consumePower(ItemStack stack, int powerAmount, boolean simulation, TileNetwork machine, World world) {
        if(getAvailablePower(stack,machine,world) >= powerAmount) {
            if(!simulation)
            {
                setAvailablePower(stack,getAvailablePower(stack, machine, world));
            }
            return true;
        }
        return false;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        super.fillItemGroup(group, items);
        if(this.isInGroup(group)) {
            ItemStack stack=new ItemStack(this);
            setAvailablePower(stack,getMaximumPower(stack));
            items.add(stack);
        }
    }

    @Override
    public boolean insertPower(ItemStack stack, int powerAmount, boolean simulation, TileNetwork machine, World world) {
        if(getAvailablePower(stack) + powerAmount <= getMaximumPower(stack)) {
            if(!simulation)
                setAvailablePower(stack,getAvailablePower(stack,null,null) + powerAmount);
            return true;
        }
        return false;
    }

    @Override
    public int getPowersourceID(ItemStack stack, TileNetwork machine, World world) {
        return -1;
    }

    @Override
    public int getFreeStorageAmount(ItemStack stack, TileNetwork machine, World world) {
        return getMaximumPower(stack) - getAvailablePower(stack,null,null);
    }

    @Override
    public boolean isPowerSourceItem() {
        return true;
    }
}
