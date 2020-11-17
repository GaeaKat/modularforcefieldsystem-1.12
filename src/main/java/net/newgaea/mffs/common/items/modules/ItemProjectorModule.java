package net.newgaea.mffs.common.items.modules;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.newgaea.mffs.api.IProjectorModule;

public abstract class ItemProjectorModule extends Item implements IProjectorModule {

    public ItemProjectorModule(Properties properties) {
        super(properties);

    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }


}
