package net.newgaea.mffs.common.tiles;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.newgaea.mffs.common.init.MFFSBlocks;
import net.newgaea.mffs.common.init.MFFSContainer;
import net.newgaea.mffs.common.init.MFFSTiles;
import net.newgaea.mffs.common.inventory.CapacitorContainer;
import net.newgaea.mffs.common.inventory.GeneratorContainer;
import net.newgaea.mffs.common.items.ItemUpgrade;
import net.newgaea.mffs.common.misc.EnumUpgrade;
import net.newgaea.mffs.common.storage.MFFSEnergyStorage;

public class TileCapacitor extends TileNetwork implements INamedContainerProvider {

    private IItemHandler upgrades=createUpgrades(this);
    private LazyOptional<IItemHandler> upgradesHandler = LazyOptional.of(()->upgrades);
    private LazyOptional<IItemHandler> jointHandler = LazyOptional.of(this::createJointHandler);
    public static IItemHandler createUpgrades(TileCapacitor capacitor) {
        return new ItemStackHandler(2){

            public boolean isItemValid(int slot, ItemStack stack) {
                switch (slot) {
                    case 0:
                        return stack.getItem() instanceof ItemUpgrade && ((ItemUpgrade) stack.getItem()).getUpgradeType()== EnumUpgrade.Capacity;
                    case 1:
                        return stack.getItem() instanceof ItemUpgrade && ((ItemUpgrade) stack.getItem()).getUpgradeType()== EnumUpgrade.Range;
                }
                return false;
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                switch(slot) {
                    case 0:
                    if(stack.getItem() instanceof ItemUpgrade && ((ItemUpgrade) stack.getItem()).getUpgradeType()== EnumUpgrade.Capacity)
                        return super.insertItem(slot, stack, simulate);
                    case 1:
                        if(stack.getItem() instanceof ItemUpgrade && ((ItemUpgrade) stack.getItem()).getUpgradeType()== EnumUpgrade.Range)
                            return super.insertItem(slot, stack, simulate);
                }
                return stack;
            }

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                if(capacitor!=null)
                    capacitor.markDirty();
            }
        };
    }

    private boolean master;

    public TileCapacitor(TileEntityType<? extends TileCapacitor> type) {
        super(type);
    }

    public boolean isMaster() {
        return master;
    }

    public TileCapacitor setMaster(boolean master) {
        this.master = master;
        return this;
    }

    @Override
    public void readExtra(CompoundNBT compound) {
        super.readExtra(compound);
        if(compound.contains("upgrades"))
            ((INBTSerializable<CompoundNBT>)this.upgrades).deserializeNBT(compound.getCompound("upgrades"));
    }

    @Override
    public CompoundNBT writeExtra(CompoundNBT compound) {
        CompoundNBT nbt=super.writeExtra(compound);
        CompoundNBT upgrades;
        upgrades=((INBTSerializable<CompoundNBT>)this.upgrades).serializeNBT();
        nbt.put("upgrades",upgrades);
        return nbt;

    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }

    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new CapacitorContainer(MFFSContainer.CAPACITOR.get(),id,playerEntity,upgrades);
    }

    private <T> IItemHandler createJointHandler() {
        return new CombinedInvWrapper((IItemHandlerModifiable)upgrades);
    }
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap,Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return jointHandler.cast();
        }
        return super.getCapability(cap, side);
    }
}
