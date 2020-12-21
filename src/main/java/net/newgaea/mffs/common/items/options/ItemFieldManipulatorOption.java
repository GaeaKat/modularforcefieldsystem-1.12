package net.newgaea.mffs.common.items.options;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.newgaea.mffs.api.IProjectorOption;

public class ItemFieldManipulatorOption extends Item implements IProjectorOption {
    public ItemFieldManipulatorOption(Properties properties) {
        super(properties);

    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

}
