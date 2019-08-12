package com.nekokittygames.mffs.common.tileentities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public abstract class TileMFFSContainer extends TileMFFS implements INamedContainerProvider {

    public TileMFFSContainer(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public boolean canOpen(PlayerEntity p_213904_1_)
    {
        return true;
    }
    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return this.canOpen(p_createMenu_3_) ? this.createMenu(p_createMenu_1_, p_createMenu_2_) : null;
    }

    public abstract Container createMenu(int id, PlayerInventory playerInventory);
}
