package net.newgaea.mffs.common.tiles;

import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.newgaea.mffs.api.ISwitchable;
import net.newgaea.mffs.common.config.MFFSConfig;
import net.newgaea.mffs.common.data.Grid;
import net.newgaea.mffs.common.init.MFFSItems;
import net.newgaea.mffs.common.items.linkcards.ItemPowerLinkCard;
import net.newgaea.mffs.common.misc.ModeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class TileNetwork extends TileMFFS implements ITickableTileEntity, ISwitchable {



    public static final String NETWORK_ID="network_id";
    public static final String NETWORK_MODE="mode";
    protected int deviceID;
    protected ModeEnum mode=ModeEnum.Off;
    protected boolean initialized;

    protected String deviceName;
    protected boolean active;
    protected short ticker;
    protected boolean switchValue;
    protected Random random = new Random();
    // todo: Chunk loading


    public boolean getSwitchValue() {
        return switchValue;
    }

    public short getTicker() {
        return ticker;
    }

    public TileNetwork setTicker(short ticker) {
        this.ticker = ticker;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public TileNetwork setActive(boolean active) {
        this.active = active;
        return this;
    }
    public int getPercentageCapacity() { return 0; }

    public boolean hasPowerSource() {
        return false;
    }




    public abstract TileAdvSecurityStation getLinkedSecurityStation();

    public abstract Direction getSide();



    @Override
    public void validate() {
        super.validate();
    }

    public TileNetwork(TileEntityType<?> tileEntityTypeIn) {

        super(tileEntityTypeIn);
        this.active=false;
        switchValue=false;
        initialized=false;
        mode=ModeEnum.Off;
        ticker=0;
        deviceID=0;
        deviceName="Please set Name";

    }

    @Override
    public void readExtra(CompoundNBT compound) {
        mode=ModeEnum.valueOf(compound.getString(NETWORK_MODE));
        active = compound.getBoolean("active");
        deviceName = compound.getString("deviceName");
        switchValue=compound.getBoolean("switchValue");
        deviceID = compound.getInt("deviceID");
    }

    @Override
    public CompoundNBT writeExtra(CompoundNBT compound) {
        compound.putString(NETWORK_MODE,mode.toString());
        compound.putBoolean("active",active);
        compound.putString("deviceName",deviceName);
        compound.putBoolean("switchValue",switchValue);
        compound.putInt("deviceID",deviceID);
        return compound;
    }

    public ModeEnum getMode() {
        return mode;
    }

    public TileNetwork setMode(ModeEnum mode) {
        this.mode = mode;
        markDirty();
        return this;
    }

    public void initialize() {
        deviceID= Grid.getWorldGrid(world).refreshID(this,deviceID);
        if(MFFSConfig.CHUNKLOADING_ENABLED.get()) {
            registerChunkLoading();
        }
        initialized=true;
    }

    private void registerChunkLoading() {
        //todo: in a few weeks once chunk loading is better
    }



    public List<ModeEnum> getAllowedModes() {return new ArrayList<>();}
    public void toggleSwitchMode() {
        ModeEnum current=getMode();
        List<ModeEnum> allowed=getAllowedModes();
        int index=allowed.indexOf(current);
        if(index!=-1) {
            index++;
            if(index>=allowed.size())
                index=0;
        }
        else
            index=0;
        setMode(allowed.get(index));
        this.markDirty();
    }

    public boolean isRedstoneSignal() {
        return world.getRedstonePowerFromNeighbors(this.getPos()) > 0;
    }

    @Override
    public void tick() {

        if(!world.isRemote)
            if(!initialized)
                initialize();

        if(world.isRemote)
        {
            if(deviceID==0) {
                this.setTicker((short) (this.getTicker()+1));
            }
        }
        // todo once replaced remove
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

    public int getPowerSourceID() {
        return 0;
    }



    public String getDeviceName() {
        return deviceName;
    }

    public TileNetwork setDeviceName(String deviceName) {
        this.deviceName = deviceName;
        this.markDirty();
        return this;
    }

    public int getDeviceID() {
        return deviceID;
    }

    public TileNetwork setDeviceID(int deviceID) {
        this.deviceID = deviceID;
        return this;
    }

    @Override
    public boolean isSwitchable() {
        return getMode()==ModeEnum.Switch;
    }

    @Override
    public void toggleSwitchValue() {
        this.switchValue=!switchValue;
        this.markDirty();
    }
    public abstract void dropPlugins();

    public void dropPlugins(int slot, IItemHandler inventory) {
        if(world.isRemote)
            ((ItemStackHandler)inventory).setStackInSlot(slot,ItemStack.EMPTY);

        if(inventory.getStackInSlot(slot)!=ItemStack.EMPTY) {
            if(inventory.getStackInSlot(slot).getItem() instanceof ItemPowerLinkCard) {
                InventoryHelper.spawnItemStack(world,pos.getX(),pos.getY(),pos.getZ(),new ItemStack(MFFSItems.POWER_LINK_CARD.get()));
            }
            else {
                InventoryHelper.spawnItemStack(world,pos.getX(),pos.getY(),pos.getZ(),inventory.getStackInSlot(0));
            }
            ((ItemStackHandler)inventory).setStackInSlot(slot,ItemStack.EMPTY);
            this.markDirty();
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        world.notifyBlockUpdate(pos,getBlockState(),getBlockState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
    }

    @Override
    public void remove() {

        super.remove();
    }
}
