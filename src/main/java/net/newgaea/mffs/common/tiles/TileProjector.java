package net.newgaea.mffs.common.tiles;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeInternalHandler;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.newgaea.mffs.MFFS;
import net.newgaea.mffs.api.EnumProjectorModule;
import net.newgaea.mffs.api.IProjectorModule;
import net.newgaea.mffs.api.MFFSTags;
import net.newgaea.mffs.common.blocks.BlockProjector;
import net.newgaea.mffs.common.init.MFFSContainer;
import net.newgaea.mffs.common.init.MFFSItems;
import net.newgaea.mffs.common.inventory.ProjectorContainer;
import net.newgaea.mffs.common.items.modules.ItemProjectorModule;
import net.newgaea.mffs.common.misc.MFFSInventoryHelper;

public class TileProjector extends TileNetwork implements INamedContainerProvider {

    private IItemHandler module=createModuleInv(this);
    private LazyOptional<IItemHandler> moduleHandler = LazyOptional.of(()->module);

    private IItemHandler foci=createFocusInv(this);
    private LazyOptional<IItemHandler> fociHandler = LazyOptional.of(()->foci);

    private IItemHandler distanceModifier=createDistanceInv(this);



    private IItemHandler strengthModifier=createStrengthInv(this);

    public static IItemHandler createStrengthInv(TileProjector projector) {
        return new ItemStackHandler(1) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return stack.getItem()== MFFSItems.STRENGTH_MODIFIER.get();
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if(stack.getItem()!= MFFSItems.STRENGTH_MODIFIER.get())
                    return stack;

                return super.insertItem(slot, stack, simulate);
            }

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                if(projector!=null)
                    projector.markDirty();
            }
        };
    }
    @org.jetbrains.annotations.NotNull
    @org.jetbrains.annotations.Contract("_ -> new")
    public static IItemHandler createDistanceInv(TileProjector projector) {
        return new ItemStackHandler(1) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return stack.getItem()== MFFSItems.DISTANCE_MODIFIER.get();
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if(stack.getItem()!= MFFSItems.DISTANCE_MODIFIER.get())
                    return stack;

                return super.insertItem(slot, stack, simulate);
            }

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                if(projector!=null)
                    projector.markDirty();
            }
        };
    }

    public TileProjector(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }


    public static IItemHandler createFocusInv(TileProjector projector) {
        return new ItemStackHandler(4) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return stack.getItem()== MFFSItems.FOCUS_MATRIX.get();
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if(stack.getItem()!= MFFSItems.FOCUS_MATRIX.get())
                    return stack;

                return super.insertItem(slot, stack, simulate);
            }

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                if(projector!=null)
                    projector.markDirty();
            }
        };
    }
    public static IItemHandler createModuleInv(TileProjector projector) {
        return new ItemStackHandler(1) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return MFFSTags.PROJECTOR.contains(stack.getItem());
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if(!MFFSTags.PROJECTOR.contains(stack.getItem()))
                    return stack;

                return super.insertItem(slot, stack, simulate);
            }

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                if(projector!=null)
                    projector.markDirty();
            }
        };
    }
    public static Direction fociSlotToDirection(int index) {
        switch (index) {
            case 0:
                return Direction.NORTH;
            case 1:
                return Direction.EAST;
            case 2:
                return Direction.SOUTH;
            case 3:
                return Direction.WEST;
            default:
                return Direction.UP;
        }
    }
    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Projector");
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ProjectorContainer(MFFSContainer.PROJECTOR.get(), windowId,playerEntity,module,foci,distanceModifier,strengthModifier);
    }

    @Override
    public void readExtra(CompoundNBT compound) {
        super.readExtra(compound);
        if(compound.contains("module")) {
            ((INBTSerializable<CompoundNBT>)module).deserializeNBT(compound.getCompound("module"));
        }
        if(compound.contains("foci")) {
            ((INBTSerializable<CompoundNBT>)foci).deserializeNBT(compound.getCompound("foci"));
        }
        if(compound.contains("distance")) {
            ((INBTSerializable<CompoundNBT>)distanceModifier).deserializeNBT(compound.getCompound("distance"));
        }
        if(compound.contains("strength")) {
            ((INBTSerializable<CompoundNBT>)strengthModifier).deserializeNBT(compound.getCompound("strength"));
        }
    }

    @Override
    public CompoundNBT writeExtra(CompoundNBT compound) {
        CompoundNBT cmp=super.writeExtra(compound);
        cmp.put("module",((INBTSerializable<CompoundNBT>)module).serializeNBT());
        cmp.put("foci",((INBTSerializable<CompoundNBT>)foci).serializeNBT());
        cmp.put("distance",((INBTSerializable<CompoundNBT>)distanceModifier).serializeNBT());
        cmp.put("strength",((INBTSerializable<CompoundNBT>)strengthModifier).serializeNBT());
        return cmp;

    }

    @Override
    public void markDirty() {
        super.markDirty();
        String newModule;
        if(module.getStackInSlot(0).isEmpty()) {
               newModule=EnumProjectorModule.Empty.getString();
        }
        else
            newModule=((ItemProjectorModule)module.getStackInSlot(0).getItem()).getModuleType();

        if(this.getBlockState().get(BlockProjector.TYPE).getString()!=newModule) {

            this.getWorld().setBlockState(pos,this.getBlockState().with(BlockProjector.TYPE,EnumProjectorModule.getModuleFromString(newModule)), Constants.BlockFlags.BLOCK_UPDATE+Constants.BlockFlags.UPDATE_NEIGHBORS);
        }
        checkStatus();
    }

    public void checkStatus() {
        if(module.getStackInSlot(0)!=ItemStack.EMPTY) {
            IProjectorModule pModule= (IProjectorModule) module.getStackInSlot(0).getItem();
            if(!pModule.enabledFoci())
            {
                MFFSInventoryHelper.dropInventoryItems(world,getPos(),foci);

            }
        }
        else
            MFFSInventoryHelper.dropInventoryItems(world,getPos(),foci);
    }


}
