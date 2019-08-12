package com.nekokittygames.mffs.common.tileentities;

import com.nekokittygames.mffs.common.init.MFFSBlocks;
import com.nekokittygames.mffs.common.init.MFFSTileTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.FurnaceContainer;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public class TileGenerator extends TileMFFSContainer {
    public TileGenerator() {
        super(MFFSTileTypes.GENERATOR_TYPE);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new net.minecraft.util.text.StringTextComponent("Test");
    }

    @Override
    public Container createMenu(int id, PlayerInventory playerInventory) {
        return null;
    }
}
