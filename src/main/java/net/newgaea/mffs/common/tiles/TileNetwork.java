package net.newgaea.mffs.common.tiles;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.newgaea.mffs.MFFS;
import net.newgaea.mffs.common.init.MFFSItems;
import net.newgaea.mffs.common.misc.ModeEnum;

public abstract class TileNetwork extends TileMFFS implements ITickableTileEntity {
    private final LazyOptional<IItemHandler> linkHandler = LazyOptional.of(this::createLinkHandler);

    public boolean isActive() {
        return active;
    }

    public TileNetwork setActive(boolean active) {
        this.active = active;
        return this;
    }

    private boolean active;
    private <T> IItemHandler createLinkHandler() {
        return new ItemStackHandler() {
            @Override
            public boolean isItemValid(int slot,  ItemStack stack) {
                return stack.getItem()== MFFSItems.LINK_CARD.get();
            }

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                markDirty();
            }
        };
    }

    public static final String NETWORK_ID="network_id";
    public static final String NETWORK_MODE="mode";
    private int networkID;
    private ModeEnum mode=ModeEnum.Off;
    private boolean initialized;


    @Override
    public void validate() {
        super.validate();
    }

    public TileNetwork(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void readExtra(CompoundNBT compound) {
        networkID=compound.getInt(NETWORK_ID);
        mode=ModeEnum.valueOf(compound.getString(NETWORK_MODE));
        if(compound.contains("link")) {
            CompoundNBT linkInv = compound.getCompound("link");
            linkHandler.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(linkInv));
        }
        if(compound.contains("active"))
            active = compound.getBoolean("active");
    }

    @Override
    public CompoundNBT writeExtra(CompoundNBT compound) {
        compound.putInt(NETWORK_ID,networkID);
        compound.putString(NETWORK_MODE,mode.toString());
        linkHandler.ifPresent(h -> {
            CompoundNBT linkInv = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("link",linkInv);
        });
        compound.putBoolean("active",active);
        return compound;
    }

    public ModeEnum getMode() {
        return mode;
    }

    public TileNetwork setMode(ModeEnum mode) {
        this.mode = mode;
        return this;
    }

    public void initialize() {

    }


    @Override
    public void tick() {
        if(!initialized) {
            initialized=true;
            initialize();
        }
        if(active) {
            if (world.getRedstonePowerFromNeighbors(this.getPos()) == 0) {
                setActive(false);
            }
        }
        else {
            if (world.getRedstonePowerFromNeighbors(this.getPos()) > 0) {
                setActive(true);
            }
        }
    }
}
