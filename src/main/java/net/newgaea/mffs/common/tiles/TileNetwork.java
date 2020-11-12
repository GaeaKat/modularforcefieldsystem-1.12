package net.newgaea.mffs.common.tiles;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.newgaea.mffs.common.misc.ModeEnum;

public abstract class TileNetwork extends TileMFFS implements ITickableTileEntity {
    private final LazyOptional<IItemHandler> linkHandler = LazyOptional.of(this::createLinkHandler);

    private <T> IItemHandler createLinkHandler() {
        return new ItemStackHandler();
    }

    public static final String NETWORK_ID="network_id";
    public static final String NETWORK_MODE="mode";
    private int networkID;
    private ModeEnum mode;
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
    }

    @Override
    public CompoundNBT writeExtra(CompoundNBT compound) {
        compound.putInt(NETWORK_ID,networkID);
        compound.putString(NETWORK_MODE,mode.toString());
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
    }
}
