package net.newgaea.mffs.common.tiles;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.newgaea.mffs.MFFS;
import net.newgaea.mffs.api.*;
import net.newgaea.mffs.common.blocks.BlockNetwork;
import net.newgaea.mffs.common.blocks.BlockProjector;
import net.newgaea.mffs.common.data.Grid;
import net.newgaea.mffs.common.forcefield.FFGenerator;
import net.newgaea.mffs.common.init.MFFSContainer;
import net.newgaea.mffs.common.init.MFFSItems;
import net.newgaea.mffs.common.inventory.ProjectorContainer;
import net.newgaea.mffs.common.items.modules.ItemProjectorModule;
import net.newgaea.mffs.common.misc.EnumFieldType;
import net.newgaea.mffs.common.misc.MFFSInventoryHelper;
import net.newgaea.mffs.common.misc.ModeEnum;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TileProjector extends TileFENetwork implements INamedContainerProvider, IModularProjector {

    private IItemHandler module=createModuleInv(this);
    private LazyOptional<IItemHandler> moduleHandler = LazyOptional.of(()->module);

    private IItemHandler foci=createFocusInv(this);
    private LazyOptional<IItemHandler> fociHandler = LazyOptional.of(()->foci);

    private IItemHandler distanceModifier=createDistanceInv(this);

    private IItemHandler strengthModifier=createStrengthInv(this);

    private IItemHandler options=createOptionsInv(this);



    private FFGenerator generator=new FFGenerator(this);
    private boolean burnout;
    private int accessType;
    private int capacity;
    private int switchDelay=0;
    private int linkPower;
    public int getLinkPower() {
        return linkPower;
    }

    public void setLinkPower(int linkPower) {
        this.linkPower = linkPower;
    }

    public int getCapacity() { return capacity;}
    public void setCapacity(int capacity) { this.capacity=capacity;}

    public int getAccessType() {
        return accessType;
    }
    public void setAccessType(int accessType) { this.accessType=accessType;}

    public boolean isBurnout() { return burnout;}

    @Override
    public void setBurnedOut(boolean burnedOut) {
        this.burnout=burnedOut;
        markDirty();
    }

    @Override
    public void tick() {
        if(world.isRemote==false) {
            if(!initialized) {
                checkSlots();
                if(this.isActive())
                {
                    generator.calculateField(true);
                }
            }
            if(hasPowerSource()){
                setLinkPower(getAvailablePower());
                if(isPowerSourceItem() && this.getAccessType()!=0)
                    setAccessType(0);
            }
            else
                setLinkPower(0);
            if(getMode() == ModeEnum.Redstone) {
                if(!getSwitchValue() && isRedstoneSignal())
                    toggleSwitchValue();
                if(getSwitchValue() && !isRedstoneSignal())
                    toggleSwitchValue();
            }
            if((getSwitchValue() && (switchDelay>=40)) && hasValidTypeMod()
            && hasPowerSource()
            && this.getLinkPower() > neededForcePower(5)) {
                if(isActive()!=true) {
                    setActive(true);
                    switchDelay=0;
                    try {
                        if(generator.calculateField(true))
                            generator.GenerateBlocks(true);
                    } catch(ArrayIndexOutOfBoundsException ex) {

                    }

                }
            }
            if((!getSwitchValue() && switchDelay >=40) || !hasValidTypeMod()
                || !hasPowerSource() || burnout || this.getLinkPower() <= neededForcePower(1)) {
                if(isActive()!=false) {
                    setActive(false);
                    switchDelay=0;
                    generator.destroyField();
                }
            }

            if(this.getTicker() == 20) {
                if(isActive()) {
                    generator.GenerateBlocks(false);
                    // todo: Handle Mob defence and player defence options
                }
                this.setTicker((short) 0);

            }
            this.setTicker((short) (this.getTicker()+1));
        }
        switchDelay++;
        super.tick();
    }

    private boolean hasValidTypeMod() {
        return getTypeModule()!=null;
    }

    private int neededForcePower(int factor) {
        return generator.neededForcePower(factor);
    }


    @Override
    public TileNetwork setActive(boolean active) {
        TileNetwork ret = super.setActive(active);
        if(active) {
            this.world.setBlockState(getPos(),this.getBlockState().with(BlockNetwork.ACTIVE,true),Constants.BlockFlags.BLOCK_UPDATE+Constants.BlockFlags.DEFAULT_AND_RERENDER);
        }
        else
        {
            this.world.setBlockState(getPos(),this.getBlockState().with(BlockNetwork.ACTIVE,false),Constants.BlockFlags.BLOCK_UPDATE+Constants.BlockFlags.DEFAULT_AND_RERENDER);
        }
        return ret;
    }

    @Override
    public TileAdvSecurityStation getLinkedSecurityStation() {
        return null;
    }

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

    public static IItemHandler createOptionsInv(TileProjector tileProjector) {
        return new ItemStackHandler(3) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return super.isItemValid(slot, stack) && stack.getItem() instanceof IProjectorOption;
            }

            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                if(!(stack.getItem() instanceof IProjectorOption))
                    return stack;
                return super.insertItem(slot, stack, simulate);
            }

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                if(tileProjector!=null)
                    tileProjector.markDirty();
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
    @Contract(pure = true)
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

    @Contract(pure = true)
    public static int DirectionToFociSlot(Direction dir) {
        switch (dir) {
            case NORTH:
                return 0;
            case EAST:
                return 1;
            case SOUTH:
                return 2;
            case WEST:
                return 3;
        }
        return 0;
    }
    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Projector");
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ProjectorContainer(MFFSContainer.PROJECTOR.get(), windowId,playerEntity,module,foci,distanceModifier,strengthModifier,options);
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
        if(compound.contains("options")) {
            ((INBTSerializable<CompoundNBT>)options).deserializeNBT(compound.getCompound("options"));
        }
        this.accessType=compound.getInt("accessType");
        this.burnout=compound.getBoolean("burnout");

    }

    @Override
    public CompoundNBT writeExtra(CompoundNBT compound) {
        CompoundNBT cmp=super.writeExtra(compound);
        cmp.put("module",((INBTSerializable<CompoundNBT>)module).serializeNBT());
        cmp.put("foci",((INBTSerializable<CompoundNBT>)foci).serializeNBT());
        cmp.put("distance",((INBTSerializable<CompoundNBT>)distanceModifier).serializeNBT());
        cmp.put("strength",((INBTSerializable<CompoundNBT>)strengthModifier).serializeNBT());
        cmp.put("options",((INBTSerializable<CompoundNBT>)options).serializeNBT());
        cmp.putInt("accessType",accessType);
        cmp.putBoolean("burnout",burnout);
        return cmp;

    }

    @Override
    public void dropPlugins() {
        for(int i=0;i<foci.getSlots();i++) {
            dropPlugins(i,foci);
        }
        for(int i=0;i<options.getSlots();i++) {
            dropPlugins(i,options);
        }
        dropPlugins(0,module);
        dropPlugins(0,distanceModifier);
        dropPlugins(0,strengthModifier);
        dropPlugins(0,link);

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
        checkSlots();
    }

    public void checkSlots() {

        if(hasValidTypeMod()) {
            IProjectorModule pModule= (IProjectorModule) module.getStackInSlot(0).getItem();
            if(!pModule.enabledFoci())
            {
                MFFSInventoryHelper.dropInventoryItems(world,getPos(),foci);

            }
            if(!pModule.enabledDistance())
                MFFSInventoryHelper.dropInventoryItems(world,getPos(),distanceModifier);
            if(!pModule.enabledDistance())
                MFFSInventoryHelper.dropInventoryItems(world,getPos(),distanceModifier);

        }
        else
            MFFSInventoryHelper.dropInventoryItems(world,getPos(),foci);
        if(hasOption(MFFSItems.CAMOUFLAGE_OPTION.get())) {
            generator.setFieldType(EnumFieldType.Default);
        }

        if(hasOption(MFFSItems.ZAPPER_OPTION.get())) {
            generator.setFieldType(EnumFieldType.Zapper);
        }

        if(hasOption(MFFSItems.FUSION_OPTION.get())) {
            if(!Grid.getWorldGrid(world).getFusions().containsKey(getDeviceID()))
                Grid.getWorldGrid(world).getFusions().put(getDeviceID(),this);
        }
        else {
            if(Grid.getWorldGrid(world).getFusions().containsKey(getDeviceID()))
                Grid.getWorldGrid(world).getFusions().remove(getDeviceID(),this);
        }
        if(hasOption(MFFSItems.FIELD_JAMMER_OPTION.get())) {
            if(!Grid.getWorldGrid(world).getJammers().containsKey(getDeviceID()))
                Grid.getWorldGrid(world).getJammers().put(getDeviceID(),this);
        }
        else {
            if(Grid.getWorldGrid(world).getJammers().containsKey(getDeviceID()))
                Grid.getWorldGrid(world).getJammers().remove(getDeviceID(),this);
        }

    }


    @Override
    public Direction getSide() {
        return getBlockState().get(BlockProjector.FACING);
    }

    @Override
    public int focusItems(Direction direction) {
        return foci.getStackInSlot(DirectionToFociSlot(direction)).getCount();
    }

    @Override
    public int strengthItems() {
        return strengthModifier.getStackInSlot(0).getCount();
    }

    @Override
    public int distanceItems() {
        return distanceModifier.getStackInSlot(0).getCount();
    }

    @Override
    public List<IProjectorOption> options() {
        List<IProjectorOption> optionsList=new ArrayList<>();
        for(int i=0;i<options.getSlots();i++) {
            ItemStack stack=options.getStackInSlot(i);
            if(!stack.isEmpty())
                optionsList.add((IProjectorOption) stack.getItem());
        }
        return  optionsList;
    }

    @Override
    public boolean takeEnergy(int energy) {
        return true;
    }

    @Override
    public IProjectorModule getTypeModule() {
        if(module.getStackInSlot(0).isEmpty())
            return null;
        return (IProjectorModule) module.getStackInSlot(0).getItem();
    }

    public List<BlockPos> getInteriorPoints() {
        return generator.getInteriorPoints();
    }

    public void burnout() {
        this.setBurnedOut(true);
        this.dropPlugins();
    }

    public void calculateField(boolean b) {
        generator.calculateField(b);
    }

    @Override
    public void remove() {
        Grid.getWorldGrid(world).getProjectors().remove(getDeviceID());
        generator.destroyField();
        super.remove();
    }
    @Override
    public List<ModeEnum> getAllowedModes() {
        ModeEnum[] enums=new ModeEnum[]{
                ModeEnum.Off,
                ModeEnum.Redstone,
                ModeEnum.Switch,
                ModeEnum.Computer
        };
        return Arrays.asList(enums);
    }
}
