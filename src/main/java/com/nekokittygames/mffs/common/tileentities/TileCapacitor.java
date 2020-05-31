package com.nekokittygames.mffs.common.tileentities;

import com.nekokittygames.mffs.common.init.MFFSRegistration;
import com.nekokittygames.mffs.common.libs.NetworkComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;

public class TileCapacitor extends TileNetworkComponent implements ITickableTileEntity, INamedContainerProvider {
    public TileCapacitor() {
        super(MFFSRegistration.TileEntity.CAPACITOR.get());
    }

    @Override
    public NetworkComponents getNetworkType() {
        return NetworkComponents.CAPACITOR;
    }


    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return null;
    }

    @Override
    public void tick() {

    }
}
