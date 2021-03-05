package net.newgaea.mffs.common.tiles;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.newgaea.mffs.api.IPowerLinkItem;
import net.newgaea.mffs.common.init.MFFSItems;
import org.apache.commons.lang3.concurrent.LazyInitializer;

public  abstract class TileFENetwork extends TileNetwork {


    protected final IItemHandler link=createLinkHandler(this);


    public IItemHandler getLink() {
        return link;
    }

    public IItemHandler getPowerLinkHandler() {
        return link;
    }

    private final LazyOptional<IItemHandler> linkHandler = LazyOptional.of(()->link);




    public static IItemHandler createLinkHandler(TileFENetwork tileFENetwork) {
        return new ItemStackHandler() {
            @Override
            public boolean isItemValid(int slot,  ItemStack stack) {
                return stack.getItem() instanceof IPowerLinkItem;
            }

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                if(tileFENetwork!=null)
                    tileFENetwork.markDirty();
            }
        };
    }
    public TileFENetwork(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public int getPercentageCapacity() {
        ItemStack linkCard=link.getStackInSlot(0);
        if(hasPowerSource()) {
            return ((IPowerLinkItem)linkCard.getItem()).getPercentageCapacity(linkCard,this,world);
        }
        return 0;
    }

    @Override
    public void readExtra(CompoundNBT compound) {
        super.readExtra(compound);
        CompoundNBT linkInv = compound.getCompound("link");
        linkHandler.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(linkInv));
    }

    @Override
    public CompoundNBT writeExtra(CompoundNBT compound) {
        super.writeExtra(compound);
        linkHandler.ifPresent(h -> {
            CompoundNBT linkInv = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("link",linkInv);
        });
        return compound;
    }

    public boolean hasPowerSource() {
        ItemStack linkCard=getPowerLinkHandler().getStackInSlot(0);
        return !linkCard.isEmpty();
    }

    public int getAvailablePower() {
        ItemStack linkCard=link.getStackInSlot(0);
        if(hasPowerSource()) {
            return ((IPowerLinkItem)linkCard.getItem()).getAvailablePower(linkCard,this,world);
        }
        return 0;
    }

    @Override
    public int getPowerSourceID() {
        ItemStack linkCard=link.getStackInSlot(0);
        if(hasPowerSource()) {
            return ((IPowerLinkItem)linkCard.getItem()).getPowersourceID(linkCard,this,world);
        }
        return 0;
    }

    public int getMaximumPower() {
        ItemStack linkCard=link.getStackInSlot(0);
        if(hasPowerSource()) {
            return ((IPowerLinkItem)linkCard.getItem()).getMaximumPower(linkCard,this,world);
        }
        return 0;
    }

    public boolean consumePower(int powerAmount, boolean simulation) {
        ItemStack linkCard=link.getStackInSlot(0);
        if(hasPowerSource()) {
            return ((IPowerLinkItem)linkCard.getItem()).consumePower(linkCard,powerAmount,simulation,this,world);
        }
        return false;
    }

    public boolean emitPower(int powerAmount,boolean simulation) {
        ItemStack linkCard=link.getStackInSlot(0);
        if(hasPowerSource()) {
            return ((IPowerLinkItem)linkCard.getItem()).insertPower(linkCard,powerAmount,simulation,this,world);
        }
        return false;
    }

    public boolean isPowerSourceItem() {
        ItemStack linkCard=link.getStackInSlot(0);
        if(hasPowerSource()) {
            return ((IPowerLinkItem) linkCard.getItem()).isPowerSourceItem();
        }
        return false;
    }
}
