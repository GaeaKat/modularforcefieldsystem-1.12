package net.newgaea.mffs.common.tiles;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.newgaea.mffs.MFFS;
import net.newgaea.mffs.api.EnumProjectorModule;
import net.newgaea.mffs.api.MFFSTags;
import net.newgaea.mffs.common.blocks.BlockProjector;
import net.newgaea.mffs.common.init.MFFSContainer;
import net.newgaea.mffs.common.inventory.ProjectorContainer;
import net.newgaea.mffs.common.items.modules.ItemProjectorModule;

public class TileProjector extends TileNetwork implements INamedContainerProvider {

    private IItemHandler module=createModuleInv(this);
    private LazyOptional<IItemHandler> moduleHandler = LazyOptional.of(()->module);

    public TileProjector(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public static IItemHandler createModuleInv(TileProjector projector) {
        return new ItemStackHandler(1) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                MFFS.getLog().info(MFFSTags.PROJECTOR.contains(stack.getItem()));
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
    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Projector");
    }

    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return new ProjectorContainer(MFFSContainer.PROJECTOR.get(), p_createMenu_1_,p_createMenu_3_,module);
    }

    @Override
    public void readExtra(CompoundNBT compound) {
        super.readExtra(compound);
        if(compound.contains("module")) {
            ((INBTSerializable<CompoundNBT>)module).deserializeNBT(compound.getCompound("module"));
        }
    }

    @Override
    public CompoundNBT writeExtra(CompoundNBT compound) {
        CompoundNBT cmp=super.writeExtra(compound);
        cmp.put("module",((INBTSerializable<CompoundNBT>)module).serializeNBT());
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
    }


}
