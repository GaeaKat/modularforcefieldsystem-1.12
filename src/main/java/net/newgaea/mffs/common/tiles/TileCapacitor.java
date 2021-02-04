package net.newgaea.mffs.common.tiles;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
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
import net.newgaea.mffs.api.IForceEnergyItem;
import net.newgaea.mffs.api.IForceEnergyStorageBlock;
import net.newgaea.mffs.common.blocks.BlockNetwork;
import net.newgaea.mffs.common.blocks.BlockSimpleNetwork;
import net.newgaea.mffs.common.data.Grid;
import net.newgaea.mffs.common.inventory.CapacitorContainer;
import net.newgaea.mffs.common.items.ItemUpgrade;
import net.newgaea.mffs.common.items.linkcards.ItemPowerLinkCard;
import net.newgaea.mffs.common.misc.EnumUpgrade;
import net.newgaea.mffs.common.misc.ModeEnum;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class TileCapacitor extends TileFENetwork implements INamedContainerProvider, IForceEnergyStorageBlock {


    private IItemHandler upgrades=createUpgrades(this);
    protected final IItemHandler powerItem= createPowerItem(this);
    private LazyOptional<IItemHandler> upgradesHandler = LazyOptional.of(()->upgrades);

    private final LazyOptional<IItemHandler> powerHandler = LazyOptional.of(() -> powerItem);
    public static IItemHandler createPowerItem(TileCapacitor capacitor) {
        return new ItemStackHandler(1) {
            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                if(isItemValid(slot,stack))
                    return super.insertItem(slot, stack, simulate);
                return stack;
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {

                return stack.getItem() instanceof IForceEnergyItem || stack.getItem() instanceof ItemPowerLinkCard;
            }

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                if(capacitor!=null)
                    capacitor.markDirty();
            }
        };
    }
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
            protected int getStackLimit(int slot, @NotNull ItemStack stack) {
                return 9;
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

    private int forcePower = 0;
    private short linkedProjector= 0;
    private int capacity= 0;
    private int powerlinkmode= 0;
    private int transmitRange= 0;

    public TileCapacitor(TileEntityType<? extends TileCapacitor> type) {
        super(type);
        this.mode=ModeEnum.Redstone;
    }

    @Override
    public int getPowerStorageID() {
        return getDeviceID();
    }

    public void setCapacity(int capacity) {
        if(getPercentageStorageCapacity()!=capacity) {
            this.capacity=capacity;
            markDirty();
        }
    }

    public int getLinkedDevices() {
        return linkedProjector;
    }

    @Override
    public List<ModeEnum> getAllowedModes() {
        ModeEnum[] enums=new ModeEnum[]{
                ModeEnum.Redstone,
                ModeEnum.Switch,
                ModeEnum.Computer
        };
        return Arrays.asList(enums);
    }

    public void setLinkedProjector(short linkedProjector) {
        if(this.linkedProjector!=linkedProjector)
        {
            this.linkedProjector=linkedProjector;
            markDirty();
        }
    }

    public void setForcePower(int f) {
        forcePower=f;
    }

    @Override
    public TileAdvSecurityStation getLinkedSecurityStation() {
        return null;
    }

    @Override
    public void readExtra(CompoundNBT compound) {
        super.readExtra(compound);
        forcePower=compound.getInt("forcePower");
        powerlinkmode=compound.getInt("powerLinkMode");
        if(compound.contains("upgrades"))
            ((INBTSerializable<CompoundNBT>)this.upgrades).deserializeNBT(compound.getCompound("upgrades"));
        if(compound.contains("powerItem"))
            ((INBTSerializable<CompoundNBT>)this.powerItem).deserializeNBT(compound.getCompound("powerItem"));
        linkedProjector= (short) compound.getInt("linkedDevices");
        capacity = compound.getInt("capacity");
        transmitRange = compound.getInt("transmitRange");
    }

    @Override
    public CompoundNBT writeExtra(CompoundNBT compound) {
        CompoundNBT nbt=super.writeExtra(compound);
        CompoundNBT upgrades;
        upgrades=((INBTSerializable<CompoundNBT>)this.upgrades).serializeNBT();
        nbt.put("upgrades",upgrades);
        CompoundNBT poweritems;
        poweritems=((INBTSerializable<CompoundNBT>)this.powerItem).serializeNBT();
        nbt.put("powerItem",poweritems);
        nbt.putInt("forcePower",forcePower);
        nbt.putInt("powerLinkMode",powerlinkmode);
        nbt.putInt("linkedDevices",linkedProjector);
        nbt.putInt("capacity",capacity);
        nbt.putInt("transmitRange",transmitRange);
        return nbt;

    }

    @Override
    public void dropPlugins() {
        for(int i=0;i<upgrades.getSlots();i++) {
            dropPlugins(i,upgrades);
        }
        dropPlugins(0,powerItem);
    }


    @Override
    public void tick() {
        if(world.isRemote==false) {
            if(!initialized)
                checkSlots(true);
            if(getMode()== ModeEnum.Redstone) {
                if (!getSwitchValue() && isRedstoneSignal())
                    toggleSwitchValue();
                if (getSwitchValue() && !isRedstoneSignal())
                    toggleSwitchValue();
            }
            if(getSwitchValue()) {
                if(isActive()!=true)
                    setActive(true);
            } else {
                if(isActive()!=false)
                    setActive(false);
            }

            if(this.getTicker()==10) {
                int linked=Grid.getWorldGrid(world).connectedToCapacitor(this, (float) Math.pow(getTransmitRange(),2));
                if(this.getLinkedDevices()!=linked)
                    this.setLinkedProjector((short) linked);
                int available=getStorageAvailablePower();
                int max=getStorageMaxPower();
                int val=((available / 1000) * 100) / (max / 1000);
                if(this.getPercentageStorageCapacity() != val)
                    setCapacity(((getStorageAvailablePower() / 1000) * 100) / (getStorageMaxPower() / 1000));
                checkSlots(false);
                if(isActive())
                    powerTransfer();
                this.setTicker((short) 0);
            }
            this.setTicker((short) (this.getTicker()+1));

        }
        super.tick();

    }

    private void powerTransfer() {
        if(hasPowerSource()) {
            int powerTransferRate = this.getMaximumPower() / 120;
            int freeStorageAmount = this.getMaximumPower() - this.getAvailablePower();
            int balanceLevel = this.getStorageAvailablePower() - getAvailablePower();
            switch (this.getPowerlinkMode()) {
                case 0:
                    if(getPercentageStorageCapacity() >=95 && getPercentageStorageCapacity()!=100) {
                        if(freeStorageAmount > powerTransferRate) {
                            emitPower(powerTransferRate,false);
                            consumePowerfromStorage(powerTransferRate,false);
                        } else {
                            emitPower(freeStorageAmount,false);
                            consumePowerfromStorage(freeStorageAmount,false);
                        }
                    }
                    break;
                case 1:
                    if(getPercentageCapacity() < this.getPercentageStorageCapacity()) {
                        if(balanceLevel > powerTransferRate) {
                            emitPower(powerTransferRate,false);
                            consumePowerfromStorage(powerTransferRate,false);
                        } else {
                            emitPower(balanceLevel,false);
                            consumePowerfromStorage(balanceLevel,false);
                        }
                    }
                    break;
                case 2:
                    if(getStorageAvailablePower() > 0 && getPercentageCapacity()!=100) {
                        if(getStorageAvailablePower() > powerTransferRate) {
                            if(freeStorageAmount > powerTransferRate) {
                                emitPower(powerTransferRate,false);
                                consumePowerfromStorage(powerTransferRate,false);
                            } else {
                                emitPower(freeStorageAmount,false);
                                consumePowerfromStorage(freeStorageAmount,false);
                            }
                        } else {
                            if(freeStorageAmount > getStorageAvailablePower()) {
                                emitPower(getStorageAvailablePower(),false);
                                consumePowerfromStorage(getStorageAvailablePower(),false);
                            } else {
                                emitPower(freeStorageAmount,false);
                                consumePowerfromStorage(freeStorageAmount,false);
                            }
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }

    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new CapacitorContainer(id,playerEntity,this);
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

    public float getTransmitRange() {
        return transmitRange;
    }

    @Override
    public int getPercentageStorageCapacity() {
        return capacity;
    }



    @Override
    public int getStorageMaxPower() {
        if(!upgrades.getStackInSlot(0).isEmpty() && ((ItemUpgrade) upgrades.getStackInSlot(0).getItem()).getUpgradeType() == EnumUpgrade.Capacity) {
            if(this.forcePower > 10000000 + (2000000 * upgrades.getStackInSlot(0).getCount())) {
                setForcePower(10000000 + (2000000 * upgrades.getStackInSlot(0).getCount()));
            }
            return 10000000 + (2000000 * upgrades.getStackInSlot(0).getCount());
        }
        if(this.forcePower > 10000000)
            setForcePower(10000000);
        return 10000000;
    }

    private void checkSlots(boolean init) {
        if (!upgrades.getStackInSlot(1).isEmpty() && ((ItemUpgrade) upgrades.getStackInSlot(1).getItem()).getUpgradeType() == EnumUpgrade.Range) {
            setTransmitRange(8 * (upgrades.getStackInSlot(1).getCount() + 1));
        } else
            setTransmitRange(8);
        if (!powerItem.getStackInSlot(0).isEmpty()) {
            if (powerItem.getStackInSlot(0).getItem() instanceof IForceEnergyItem) {
                if (this.getPowerlinkMode() != 3 && this.powerlinkmode != 4)
                    this.setPowerlinkMode(3);
                IForceEnergyItem forceEnergyItem = (IForceEnergyItem) powerItem.getStackInSlot(0).getItem();
                ItemStack forceEnergyStack = powerItem.getStackInSlot(0);
                switch (this.getPowerlinkMode()) {
                    case 3:
                        if (forceEnergyItem.getAvailablePower(forceEnergyStack) < forceEnergyItem.getMaximumPower(forceEnergyStack)) {
                            int maxTransfer = forceEnergyItem.getPowerTransferRate();
                            int freeAmount = forceEnergyItem.getMaximumPower(forceEnergyStack) - forceEnergyItem.getAvailablePower(forceEnergyStack);

                            if (this.getStorageAvailablePower() > 0) {
                                if (this.getStorageAvailablePower() > maxTransfer) {
                                    if (freeAmount > maxTransfer) {
                                        forceEnergyItem.setAvailablePower(forceEnergyStack, forceEnergyItem.getAvailablePower(forceEnergyStack) + maxTransfer);
                                        this.setForcePower(this.getStorageAvailablePower() - maxTransfer);
                                    } else {
                                        forceEnergyItem.setAvailablePower(forceEnergyStack, forceEnergyItem.getAvailablePower(forceEnergyStack) + freeAmount);
                                        this.setForcePower(this.getStorageAvailablePower() - freeAmount);
                                    }
                                } else {
                                    if (freeAmount > this.getStorageAvailablePower()) {
                                        forceEnergyItem.setAvailablePower(forceEnergyStack, forceEnergyItem.getAvailablePower(forceEnergyStack) + this.getStorageAvailablePower());
                                        this.setForcePower(0);

                                    } else {
                                        forceEnergyItem.setAvailablePower(forceEnergyStack, forceEnergyItem.getAvailablePower(forceEnergyStack) + freeAmount);
                                        this.setForcePower(this.getStorageAvailablePower() - freeAmount);
                                    }
                                }
                                forceEnergyStack.setDamage(forceEnergyItem.getItemDamage(forceEnergyStack));
                            }
                        }
                        break;
                    case 4:
                        if (forceEnergyItem.getAvailablePower(forceEnergyStack) > 0) {
                            int maxTransfer = forceEnergyItem.getPowerTransferRate();
                            int freeAmount = this.getStorageMaxPower() - this.getStorageAvailablePower();
                            int amountLeft = forceEnergyItem.getAvailablePower(forceEnergyStack);
                            if (freeAmount >= amountLeft) {
                                // todo: work out if this should both be >= or just this
                                if (amountLeft >= maxTransfer) {
                                    forceEnergyItem.setAvailablePower(forceEnergyStack, forceEnergyItem.getAvailablePower(forceEnergyStack) - maxTransfer);
                                    this.setForcePower(this.getStorageAvailablePower() + maxTransfer);
                                } else {
                                    forceEnergyItem.setAvailablePower(forceEnergyStack, forceEnergyItem.getAvailablePower(forceEnergyStack) - amountLeft);
                                    this.setForcePower(this.getStorageAvailablePower() + amountLeft);
                                }
                            } else {
                                forceEnergyItem.setAvailablePower(forceEnergyStack, forceEnergyItem.getAvailablePower(forceEnergyStack) - freeAmount);
                                this.setForcePower(this.getStorageAvailablePower() + freeAmount);
                            }
                            forceEnergyStack.setDamage(forceEnergyItem.getItemDamage(forceEnergyStack));
                        }
                        break;
                }
            }
            if (powerItem.getStackInSlot(0).getItem() instanceof ItemPowerLinkCard) {
                if (this.getPowerlinkMode() != 0 && this.getPowerlinkMode() != 1 && this.getPowerlinkMode() != 2)
                    this.setPowerlinkMode(0);
            }
        }
    }
    @Override
    public int getStorageAvailablePower() {
        return forcePower;
    }

    @Override
    public boolean consumePowerfromStorage(int powerAmount, boolean simulation) {
        if(simulation) {
            if(getStorageAvailablePower() >=powerAmount)
                return true;
            return false;
        } else {
            if(getStorageAvailablePower() - powerAmount >= 0)
                setForcePower(getStorageAvailablePower()-powerAmount);
            else
                setForcePower(0);
            return true;
        }
    }

    @Override
    public boolean insertPowertoStorage(int powerAmount, boolean simulation) {
        if(simulation) {
            if(getStorageAvailablePower() + powerAmount <=getStorageMaxPower())
                return true;
            return false;
        } else {
            setForcePower(getStorageAvailablePower()+powerAmount);
            return true;
        }
    }

    @Override
    public int getfreeStorageAmount() {
        return this.getStorageMaxPower() - this.getStorageAvailablePower();
    }

    @Override
    public void remove() {
        Grid.getWorldGrid(world).getCapacitors().remove(getDeviceID());
        super.remove();
    }

    public TileCapacitor setTransmitRange(int transmitRange) {
        this.transmitRange = transmitRange;
        markDirty();
        return this;
    }

    public int getPowerlinkMode() {
        return powerlinkmode;
    }

    public TileCapacitor setPowerlinkMode(int powerlinkmode) {
        this.powerlinkmode = powerlinkmode;
        return this;
    }

    public IItemHandler getUpgrades() {
        return upgrades;
    }

    public IItemHandler getPowerItem() {
        return powerItem;
    }

    public Direction getSide() {
        return this.getBlockState().get(BlockSimpleNetwork.FACING);
    }

    public void togglePowerMode() {
        if(!powerItem.getStackInSlot(0).isEmpty()) {
            if(powerItem.getStackInSlot(0).getItem() instanceof IForceEnergyItem) {
                if(this.getPowerlinkMode() == 4)
                    this.setPowerlinkMode(3);
                else
                    this.setPowerlinkMode(4);
                return;
            }
            if(powerItem.getStackInSlot(0).getItem() instanceof ItemPowerLinkCard) {
                if(this.getPowerlinkMode() < 2)
                    this.setPowerlinkMode(this.getPowerlinkMode()+1);
                else
                    this.setPowerlinkMode(0);
                return;
            }

        }
        if(this.getPowerlinkMode()!=4) {
            this.setPowerlinkMode(this.getPowerlinkMode()+1);
        }
        else
            this.setPowerlinkMode(0);
    }


}
