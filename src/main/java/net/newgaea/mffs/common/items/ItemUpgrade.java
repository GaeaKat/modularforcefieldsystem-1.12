package net.newgaea.mffs.common.items;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.ForgeI18n;
import net.newgaea.mffs.common.blocks.ModBlock;
import net.newgaea.mffs.common.misc.EnumUpgrade;

import java.util.List;

public class ItemUpgrade extends ModItem {
    private final EnumUpgrade upgradeType;
    public ItemUpgrade(Properties properties, EnumUpgrade upgradeType) {
        super(properties);
        this.upgradeType=upgradeType;
    }

    public EnumUpgrade getUpgradeType() {
        return upgradeType;
    }
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        StringBuilder text= new StringBuilder();
        for (ModBlock block:upgradeType.getValidBlocks())
            text.append(I18n.format(block.getTranslationKey())).append(", ");
        int cur=text.lastIndexOf(", ");
        text.delete(cur,cur+2);
        tooltip.add(new TranslationTextComponent("misc.mffs.upgrade.valid", text.toString()));
    }
}
