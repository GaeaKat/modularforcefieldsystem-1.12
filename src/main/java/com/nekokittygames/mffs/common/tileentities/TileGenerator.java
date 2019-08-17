package com.nekokittygames.mffs.common.tileentities;

import com.nekokittygames.mffs.common.init.MFFSItems;
import com.nekokittygames.mffs.common.init.MFFSTileTypes;
import com.nekokittygames.mffs.common.inventory.GeneratorContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileGenerator extends TileMFFS implements  ITickableTileEntity,INamedContainerProvider {

    private LazyOptional<IItemHandler> fuelHandler = LazyOptional.of(this::createFuelHandler);
    private LazyOptional<IItemHandler> monazitHandler = LazyOptional.of(this::createMonazitHandler);
    private LazyOptional<IItemHandler> jointHandler = LazyOptional.of(this::createJointHandler);

    private <T> IItemHandler createJointHandler() {
        return new CombinedInvWrapper((IItemHandlerModifiable)fuelHandler.cast(),(IItemHandlerModifiable)monazitHandler.cast());
    }


    public TileGenerator() {
        super(MFFSTileTypes.GENERATOR);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }


    private <T> IItemHandler createMonazitHandler() {
        return new ItemStackHandler(1){
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return stack.getItem() == MFFSItems.MONAZIT_CRYSTAL;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if(stack.getItem() != MFFSItems.MONAZIT_CRYSTAL)
                    return stack;

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    protected <T> IItemHandler createFuelHandler() {
        return new ItemStackHandler(1){
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return AbstractFurnaceTileEntity.isFuel(stack);
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if(!AbstractFurnaceTileEntity.isFuel(stack))
                    return stack;

                return super.insertItem(slot, stack, simulate);
            }
        };
    }



    @Override
    public void tick() {

    }

    public boolean canOpen(PlayerEntity p_213904_1_) {
        return true;
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new GeneratorContainer(id,world,pos,playerInventory,playerEntity);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        if (compound.contains("fuel")) {
            CompoundNBT invTag = compound.getCompound("fuel");
            fuelHandler.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        }

        if(compound.contains("monazit")) {
            CompoundNBT invTag = compound.getCompound("monazit");
            monazitHandler.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {

        fuelHandler.ifPresent(h -> {
            CompoundNBT invTag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("fuel", invTag);
        });
        monazitHandler.ifPresent(h -> {
            CompoundNBT invTag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("monazit", invTag);
        });
        return super.write(compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            switch (side)
            {

                case DOWN:
                    return fuelHandler.cast();
                case UP:
                    return monazitHandler.cast();
                default:
                    return jointHandler.cast();
            }
        }
        return super.getCapability(cap, side);
    }
}
