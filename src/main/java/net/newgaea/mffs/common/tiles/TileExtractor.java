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
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.newgaea.mffs.MFFS;
import net.newgaea.mffs.common.blocks.BlockSimpleNetwork;
import net.newgaea.mffs.common.config.MFFSConfig;
import net.newgaea.mffs.common.data.Grid;
import net.newgaea.mffs.common.init.MFFSContainer;
import net.newgaea.mffs.common.init.MFFSItems;
import net.newgaea.mffs.common.inventory.ExtractorContainer;
import net.newgaea.mffs.common.items.ItemMonazitCell;
import net.newgaea.mffs.common.items.ItemUpgrade;
import net.newgaea.mffs.common.misc.EnumUpgrade;
import net.newgaea.mffs.common.misc.ModeEnum;
import org.apache.commons.lang3.concurrent.LazyInitializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class TileExtractor extends TileFENetwork implements INamedContainerProvider {
    private IItemHandler upgrades=createUpgrades(this);
    private IItemHandler monazitCell=createMonazitCell(this);

    private IItemHandler monazit = createMonazit(this);
    private IEnergyStorage energy = createEnergyStorage(this);

    public Direction getSide() {
        return this.getBlockState().get(BlockSimpleNetwork.FACING);
    }
    private IEnergyStorage createEnergyStorage(TileExtractor tileExtractor) {
        return new EnergyStorage(1000) {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                //MFFS.getLog().info("receiving " + maxReceive+ " energy");
                return super.receiveEnergy(maxReceive, simulate);
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                int energy=super.extractEnergy(maxExtract, simulate);
                //MFFS.getLog().info("extracting " + energy+ " energy, requested "+maxExtract);
                return energy;
            }
        };
    }

    public static IItemHandler createMonazit(TileExtractor tileExtractor) {
        return new ItemStackHandler(1) {
            public boolean isItemValid(int slot, ItemStack stack) {
                return (stack.getItem() ==MFFSItems.MONAZIT_CRYSTAL.get() && !stack.isEmpty()) || (stack.getItem() instanceof ItemMonazitCell && !stack.isEmpty());
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if(isItemValid(slot, stack))
                    return super.insertItem(slot, stack, simulate);

                return stack;
            }

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                if(tileExtractor!=null)
                    tileExtractor.markDirty();
            }
        };
    }

    public static IItemHandler createMonazitCell(TileExtractor tileExtractor) {
        return new ItemStackHandler(1) {
            public boolean isItemValid(int slot, ItemStack stack) {
                        return stack.getItem() instanceof ItemMonazitCell && !stack.isEmpty();
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if(isItemValid(slot, stack))
                    return super.insertItem(slot, stack, simulate);

                return stack;
            }

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                if(tileExtractor!=null)
                    tileExtractor.markDirty();
            }
        };
    }

    private LazyOptional<IItemHandler> upgradesHandler = LazyOptional.of(()->upgrades);
    private LazyOptional<IItemHandler> monazitHandler = LazyOptional.of(()->monazit);

    private LazyOptional<IItemHandler> jointHandler = LazyOptional.of(this::createJointHandler);
    private LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(()->energy);
    public static IItemHandler createUpgrades(TileExtractor tileExtractor) {
        return new ItemStackHandler(2){

            public boolean isItemValid(int slot, ItemStack stack) {
                switch (slot) {
                    case 0:
                        return stack.getItem() instanceof ItemUpgrade && ((ItemUpgrade) stack.getItem()).getUpgradeType()== EnumUpgrade.Capacity;
                    case 1:
                        return stack.getItem() instanceof ItemUpgrade && ((ItemUpgrade) stack.getItem()).getUpgradeType()== EnumUpgrade.Speed;
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
                        if(stack.getItem() instanceof ItemUpgrade && ((ItemUpgrade) stack.getItem()).getUpgradeType()== EnumUpgrade.Speed)
                            return super.insertItem(slot, stack, simulate);
                }
                return stack;
            }

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                if(tileExtractor!=null)
                    tileExtractor.markDirty();
            }
        };
    }
    private <T> IItemHandler createJointHandler() {
        return new CombinedInvWrapper((IItemHandlerModifiable)monazitCell, (IItemHandlerModifiable) monazit);
    }

    public IItemHandler getUpgrades() {
        return upgrades;
    }
    public IItemHandler getMonazitCell() { return monazitCell;}
    public IItemHandler getMonazit() { return monazit;}

    private int workMode = 0;

    protected int workEnergy;
    protected int maxWorkEnergy;
    private int forceEnergyBuffer;
    private int maxForceEnergyBuffer;
    private int workCycle;
    private int workTicker;
    private int workDone;
    private int maxWorkCycle;
    private int capacity;
    //private IPowerEmitter powerEmitter;
    private boolean addedToEnergyNet;



    public TileExtractor(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        workCycle=0;
        maxWorkEnergy=4000;
        forceEnergyBuffer=0;
        maxForceEnergyBuffer=1000000;
        workCycle=0;
        workTicker=20;
        maxWorkCycle=125;
        capacity=0;
        addedToEnergyNet=false;
    }

    public int getCapacity() {
        return capacity;
    }

    public TileExtractor setCapacity(int capacity) {
        if(this.capacity!=capacity) {
            this.capacity = capacity;
            markDirty();
        }
        return this;
    }

    public int getMaxWorkCycle() {
        return maxWorkCycle;
    }

    public TileExtractor setMaxWorkCycle(int maxWorkCycle) {
        this.maxWorkCycle = maxWorkCycle;
        return this;
    }

    public int getWorkDone() {
        return workDone;
    }

    public TileExtractor setWorkDone(int workDone) {
        if(this.workDone!=workDone) {
            this.workDone = workDone;
            markDirty();
        }
        return this;
    }

    public int getForceEnergyBuffer() {
        return forceEnergyBuffer;
    }

    public TileExtractor setForceEnergyBuffer(int forceEnergyBuffer) {
        this.forceEnergyBuffer = forceEnergyBuffer;
        return this;
    }

    public int getMaxForceEnergyBuffer() {
        return maxForceEnergyBuffer;
    }

    public TileExtractor setMaxForceEnergyBuffer(int maxForceEnergyBuffer) {
        this.maxForceEnergyBuffer = maxForceEnergyBuffer;
        return this;
    }

    public int getWorkTicker() {
        return workTicker;
    }

    public TileExtractor setWorkTicker(int workTicker) {
        this.workTicker = workTicker;
        return this;
    }

    public int getWorkCycle() {
        return workCycle;
    }

    public TileExtractor setWorkCycle(int workCycle) {
        if(this.workCycle!=workCycle) {
            this.workCycle = workCycle;
            markDirty();
        }
        return this;
    }

    public int getWorkEnergy() {
        return workEnergy;
    }

    public TileExtractor setWorkEnergy(int workEnergy) {
        this.workEnergy = workEnergy;
        return this;
    }

    public int getMaxWorkEnergy() {
        return maxWorkEnergy;
    }

    public TileExtractor setMaxWorkEnergy(int maxWorkEnergy) {
        this.maxWorkEnergy = maxWorkEnergy;
        return this;
    }

    public void checkSlots(boolean init) {
        if(!upgrades.getStackInSlot(0).isEmpty()) {
            if(((ItemUpgrade)upgrades.getStackInSlot(0).getItem()).getUpgradeType()==EnumUpgrade.Capacity) {
                setMaxForceEnergyBuffer(1000000+ (upgrades.getStackInSlot(0).getCount()*100000));
            } else {
                setMaxForceEnergyBuffer(1000000);
            }
        }
        if(!upgrades.getStackInSlot(1).isEmpty()) {
            if(((ItemUpgrade)upgrades.getStackInSlot(1).getItem()).getUpgradeType()==EnumUpgrade.Speed) {
                setWorkTicker(20 - upgrades.getStackInSlot(1).getCount());
            } else {
                setWorkTicker(20);
            }
        }
        if(!monazitCell.getStackInSlot(0).isEmpty()) {
            if(monazitCell.getStackInSlot(0).getItem() == MFFSItems.MONAZIT_CELL.get()) {
                workMode = 1;
                setMaxWorkEnergy(200000);
            }
        } else {
            workMode = 0;
            setMaxWorkEnergy(4000);
        }
        
    }

    private boolean hasPowerToConvert() {
        if(workEnergy >= maxWorkEnergy-1) {
            setWorkEnergy(0);
            return true;
        }
        return false;
    }
    private boolean hasFreeForceEnergyStorage() {
        if(this.maxForceEnergyBuffer > this.forceEnergyBuffer)
            return true;
        return false;
    }

    private boolean hasStuffToConvert() {
        if(workCycle>0)
            return true;
        else {
            if(MFFSConfig.ADVENTURE_MODE.get()) {
                setMaxWorkCycle(MFFSConfig.MONAZIT_CELL_WORK_CYCLE.get());
                setWorkCycle(getMaxWorkCycle());
                return true;
            }
            if(!monazit.getStackInSlot(0).isEmpty()) {
                if(monazit.getStackInSlot(0).getItem() == MFFSItems.MONAZIT_CRYSTAL.get()) {
                    setMaxWorkCycle(MFFSConfig.MONAZIT_WORK_CYCLE.get());
                    setWorkCycle(getMaxWorkCycle());
                    monazit.getStackInSlot(0).setCount(monazit.getStackInSlot(0).getCount()-1);
                    return true;
                }
                if(monazit.getStackInSlot(0).getItem() == MFFSItems.MONAZIT_CELL.get()) {
                    if(((ItemMonazitCell)monazit.getStackInSlot(0).getItem()).useMonazit(1,monazit.getStackInSlot(0))) {
                        setMaxWorkCycle(MFFSConfig.MONAZIT_CELL_WORK_CYCLE.get());
                        setWorkCycle(getMaxWorkCycle());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void transferForceEnergy() {
        if(getForceEnergyBuffer() > 0) {
            if(this.hasPowerSource()) {
                int powerTransferRate = this.getMaximumPower() / 120;
                int freeAmount = this.getMaximumPower() - this.getAvailablePower();
                if(this.getForceEnergyBuffer() > freeAmount) {
                    if(freeAmount > powerTransferRate) {
                        emitPower(powerTransferRate,false);
                        this.setForceEnergyBuffer(this.getForceEnergyBuffer()-powerTransferRate);
                    } else {
                        emitPower(freeAmount,false);
                        this.setForceEnergyBuffer(this.getForceEnergyBuffer()-freeAmount);
                    }
                } else {
                    if(freeAmount > this.getForceEnergyBuffer()) {
                        emitPower(this.getForceEnergyBuffer(),false);
                        this.setForceEnergyBuffer(0);
                    } else {
                        emitPower(freeAmount,false);
                        this.setForceEnergyBuffer(this.getForceEnergyBuffer()-freeAmount);
                    }
                }
            }
        }
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


    @Override
    public void tick() {
        if (!world.isRemote) {
            if (!initialized) {
                checkSlots(true);
            }
            if (!addedToEnergyNet)
                addedToEnergyNet = true;
            if (getMode() == ModeEnum.Redstone) {
                if (!getSwitchValue() && isRedstoneSignal())
                    toggleSwitchValue();
                if (getSwitchValue() && !isRedstoneSignal())
                    toggleSwitchValue();
            }

            if (!isActive() && getSwitchValue())
                setActive(true);
            if (isActive() && !getSwitchValue())
                setActive(false);

            if (isActive()) {
                convertPowerToWorkEnergy();
            }
            if (this.getTicker() >= getWorkTicker()) {
                checkSlots(false);
                if (workMode == 0 && isActive()) {
                    if (this.getWorkDone() != getWorkEnergy() * 100 / getMaxWorkEnergy())
                        setWorkDone(getWorkEnergy() * 100 / getMaxWorkEnergy());

                    if (getWorkDone() > 100)
                        setWorkDone(100);

                    if (this.getCapacity() != (getForceEnergyBuffer() * 100) / getMaxForceEnergyBuffer())
                        setCapacity((getForceEnergyBuffer() * 100) / getMaxForceEnergyBuffer());
                    if (this.hasFreeForceEnergyStorage() && this.hasStuffToConvert()) {
                        if (this.hasPowerToConvert()) {
                            setWorkCycle(getWorkCycle() - 1);
                            setForceEnergyBuffer(getForceEnergyBuffer() + MFFSConfig.FE_PER_WORKCYCLE.get());
                        }
                    }
                    transferForceEnergy();
                    this.setTicker((short) 0);
                }

                if (workMode == 1 && isActive()) {
                    if(!monazit.getStackInSlot(0).isEmpty() && monazit.getStackInSlot(0).getItem() instanceof ItemMonazitCell) {
                        if (this.getWorkDone() != getWorkEnergy() * 100 / getMaxWorkEnergy())
                            setWorkDone(getWorkEnergy() * 100 / getMaxWorkEnergy());
                        if (((ItemMonazitCell) monazit.getStackInSlot(0).getItem()).getMonazitLevel(monazit.getStackInSlot(0)) < ((ItemMonazitCell) monazit.getStackInSlot(0).getItem()).getMaxMonazitLevel(monazit.getStackInSlot(0))) {
                            if (this.hasPowerToConvert() && isActive()) {
                                ((ItemMonazitCell) monazit.getStackInSlot(0).getItem()).setMonazitLevel(monazit.getStackInSlot(0), ((ItemMonazitCell) monazit.getStackInSlot(0).getItem()).getMonazitLevel(monazit.getStackInSlot(0)) + 1);
                            }
                        }
                        this.setTicker((short) 0);
                    }
                }
            }
            this.setTicker((short) (this.getTicker() + 1));
        }
        super.tick();
    }

    private void convertPowerToWorkEnergy() {
        if(this.getWorkEnergy() < this.getMaxWorkEnergy()) {
            int use=energy.extractEnergy((int) (this.getMaxWorkEnergy()-this.getWorkEnergy() / 2.5),false);
            if(getWorkEnergy() + (use * 2.5) > getMaxWorkEnergy()) {
                setWorkEnergy(getMaxWorkEnergy());
            }
            else {
                setWorkEnergy((int) (getWorkEnergy() + (use * 2.5)));
            }
        }
    }

    @Override
    public TileAdvSecurityStation getLinkedSecurityStation() {
        return null;
    }

    @Override
    public void dropPlugins() {

    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getDeviceName());
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ExtractorContainer(windowId,playerEntity,this);
    }

    @Override
    public void readExtra(CompoundNBT compound) {
        super.readExtra(compound);
        if(compound.contains("upgrades"))
            ((INBTSerializable<CompoundNBT>)this.upgrades).deserializeNBT(compound.getCompound("upgrades"));
        if(compound.contains("monazitCell"))
            ((INBTSerializable<CompoundNBT>)this.monazitCell).deserializeNBT(compound.getCompound("monazitCell"));
        if(compound.contains("monazit"))
            ((INBTSerializable<CompoundNBT>)this.monazit).deserializeNBT(compound.getCompound("monazit"));
        forceEnergyBuffer = compound.getInt("forceEnergyBuffer");
        workEnergy = compound.getInt("workEnergy");
        workCycle = compound.getInt("workCycle");
        capacity = compound.getInt("capacity");
        workDone = compound.getInt("workDone");

    }

    @Override
    public CompoundNBT writeExtra(CompoundNBT compound) {
        compound=super.writeExtra(compound);
        CompoundNBT upgrades;
        upgrades=((INBTSerializable<CompoundNBT>)this.upgrades).serializeNBT();
        compound.put("upgrades",upgrades);
        CompoundNBT monazitCellNBT;
        monazitCellNBT=((INBTSerializable<CompoundNBT>)this.monazitCell).serializeNBT();
        compound.put("monazitCell",monazitCellNBT);

        CompoundNBT monazitNBT;
        monazitNBT = ((INBTSerializable<CompoundNBT>)this.monazit).serializeNBT();
        compound.put("monazit",monazitNBT);

        compound.putInt("forceEnergyBuffer",forceEnergyBuffer);
        compound.putInt("workEnergy",workEnergy);
        compound.putInt("workCycle",workCycle);
        compound.putInt("capacity",capacity);
        compound.putInt("workDone",workDone);
        return compound;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            switch (side)
            {
                case UP:
                    return monazitHandler.cast();
                default:
                    return jointHandler.cast();
            }
        }
        else if(cap == CapabilityEnergy.ENERGY)
            return energyHandler.cast();

        return super.getCapability(cap, side);
    }

    @Override
    public void remove() {
        Grid.getWorldGrid(world).getExtractors().remove(getDeviceID());
        super.remove();
    }
}
