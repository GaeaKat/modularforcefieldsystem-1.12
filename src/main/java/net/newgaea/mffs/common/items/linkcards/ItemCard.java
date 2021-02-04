package net.newgaea.mffs.common.items.linkcards;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.newgaea.mffs.common.items.ModItem;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ItemCard extends ModItem {


    protected int Tick;
    public ItemCard(Properties properties) {
        super(properties);
        Tick=0;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    public static String getAreaName(ItemStack stack) {
        CompoundNBT tag=stack.getOrCreateChildTag("link");
        if(tag.contains("areaName")) {
            return tag.getString("areaName");

        }
        return "not set";
    }

    public static  void setAreaName(ItemStack stack, String deviceName) {
        CompoundNBT tag=stack.getOrCreateChildTag("link");
        tag.putString("areaName",deviceName);
    }

    public boolean isValid(ItemStack stack) {
        CompoundNBT tag=stack.getOrCreateChildTag("link");
        if(tag.contains("valid")) {
            return tag.getBoolean("valid");
        }
        return false;
    }

    public void setInvalid(ItemStack stack ) {
        setValid(stack, false);
    }

    public void setValid(ItemStack stack, boolean valid) {
        CompoundNBT tag=stack.getOrCreateChildTag("link");
        tag.putBoolean("valid",valid);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        CompoundNBT tag=stack.getOrCreateChildTag("link");
        tooltip.add(new TranslationTextComponent("cards.mffs.linksto",getAreaName(stack)));
        if(tag.contains("worldName"))
            tooltip.add(new TranslationTextComponent("cards.mffs.world",tag.getString("worldName")));
        if(tag.contains("linkTarget"))
            tooltip.add(new TranslationTextComponent("cards.mffs.linktarget", NBTUtil.readBlockPos(tag.getCompound("linkTarget")).toString()));
        if(tag.contains("valid"))
            if(tag.getBoolean("valid"))
                tooltip.add(new TranslationTextComponent("cards.mffs.valid"));
            else
                tooltip.add(new TranslationTextComponent("cards.mffs.invalid"));
    }

    public void setInformation(ItemStack stack, BlockPos pos, String key, int value, World world) {
        CompoundNBT tag=stack.getOrCreateChildTag("link");
        tag.putInt(key, value);
        tag.putString("worldName",world.getProviderName());
        tag.put("linkTarget",NBTUtil.writeBlockPos(pos));
        tag.putBoolean("valid",true);
    }
    public BlockPos getCardTargetPoint(ItemStack stack) {
        CompoundNBT tag=stack.getOrCreateChildTag("link");
        if(tag.contains("linkTarget")) {
            return NBTUtil.readBlockPos(tag.getCompound("linkTarget"));

        }
        return null;
    }

    public int getValueFromKey(String key,ItemStack stack) {
        CompoundNBT tag=stack.getOrCreateChildTag("link");
        if(tag.contains(key)) {
            return tag.getInt(key);

        }
        return 0;
    }
}
