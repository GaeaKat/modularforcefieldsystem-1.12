package net.newgaea.mffs.common.tiles;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
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
import net.newgaea.mffs.common.config.MFFSConfig;
import net.newgaea.mffs.common.data.Grid;
import net.newgaea.mffs.common.forcefield.ForceFieldBlockStack;
import net.newgaea.mffs.common.forcefield.WorldMap;
import net.newgaea.mffs.common.init.MFFSBlocks;
import net.newgaea.mffs.common.init.MFFSItems;
import net.newgaea.mffs.common.inventory.ProjectorContainer;
import net.newgaea.mffs.common.items.modules.ItemProjectorModule;
import net.newgaea.mffs.common.items.options.ItemFieldFusionOption;
import net.newgaea.mffs.common.items.options.ItemFieldJammerOption;
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


    private EnumFieldType field_type;

    public EnumFieldType getField_type() {
        return field_type;
    }

    public TileProjector setField_type(EnumFieldType field_type) {
        this.field_type = field_type;
        return this;
    }

    private boolean burnout;
    private int accessType;
    private int capacity;
    private int switchDelay=0;
    private int linkPower;
    protected Stack<Integer> field_queue = new Stack<Integer>();
    protected Set<BlockPos> field_interior = new HashSet<BlockPos>();
    protected Set<BlockPos> field_def = new HashSet<BlockPos>();
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
        if(!world.isRemote) {
            if(!initialized) {
                checkSlots();
                if(this.isActive())
                {
                    calculateField(true);
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
                        if(calculateField(true))
                            generateBlocks(true);
                    } catch(ArrayIndexOutOfBoundsException ex) {

                    }

                }
            }
            if((!getSwitchValue() && switchDelay >=40) || !hasValidTypeMod()
                || !hasPowerSource() || burnout || this.getLinkPower() <= neededForcePower(1)) {
                if(isActive()!=false) {
                    setActive(false);
                    switchDelay=0;
                    destroyField();
                }
            }

            if(this.getTicker() == 20) {
                if(isActive()) {
                    generateBlocks(false);
                    // todo: Handle Mob defence and player defence options
                }
                this.setTicker((short) 0);

            }
            this.setTicker((short) (this.getTicker()+1));
        }
        switchDelay++;
        super.tick();
    }
    private int blockcounter;
    private void generateBlocks(boolean init) {
        int cost = 0;
        if(init) {
            cost = MFFSConfig.BASE_FORCEFIELD_COST.get() * MFFSConfig.FIELD_CREATE_MODIFIER.get();
        } else {
            cost = MFFSConfig.BASE_FORCEFIELD_COST.get();
        }
        if(getField_type()==EnumFieldType.Zapper) {
            cost *=MFFSConfig.ZAPPER_MODIFIER.get();
        }
        consumePower(cost * field_def.size(),false);
        blockcounter=0;

        for(BlockPos pnt:field_def) {
            if(blockcounter >= MFFSConfig.FORCEFIELD_PER_TICK.get()) {
                break;
            }
            ForceFieldBlockStack ffb = WorldMap.getForceFieldWorld(world).getForceFieldBlockStack(pnt.hashCode());
            if(ffb!=null) {
                if(ffb.isSync())
                    continue;
                BlockPos png=ffb.getPos();
                if(world.getChunkProvider().isChunkLoaded(new ChunkPos(png))) {
                    if(!ffb.isEmpty()) {
                        if(ffb.getProjectorID() == getDeviceID()) {
                            if(hasOption(MFFSItems.BLOCK_BREAKER_OPTION.get())) {
                                // todo: make items drop or into inv
                                world.setBlockState(png, Blocks.AIR.getDefaultState(),Constants.BlockFlags.DEFAULT_AND_RERENDER);
                            }
                            if(world.getBlockState(png).getMaterial().isLiquid()
                            || world.isAirBlock(png)
                            || world.getBlockState(png).getBlock() == MFFSBlocks.FORCEFIELD.get()) {
                                if(world.getBlockState(png).getBlock() != MFFSBlocks.FORCEFIELD.get()) {
                                    world.setBlockState(png,MFFSBlocks.FORCEFIELD.getDefaultState(),Constants.BlockFlags.DEFAULT_AND_RERENDER);
                                    if(world.getTileEntity(png) instanceof TileForcefield) {
                                        TileForcefield force= (TileForcefield) world.getTileEntity(png);
                                        force.setFieldType(getField_type());

                                    }
                                    else {
                                        MFFS.getLog().info("ERRORING!");
                                    }
                                    blockcounter++;
                                }
                                ffb.setSync(true);
                            }
                        }
                    }
                }
            }
        }
    }

    public void destroyField() {
        while(!field_queue.isEmpty()) {
            ForceFieldBlockStack ffWorldMap = WorldMap.getForceFieldWorld(world).getForceFieldBlockStack(field_queue.pop());

            if(!ffWorldMap.isEmpty()) {
                if(ffWorldMap.getProjectorID() == getDeviceID()) {
                    ffWorldMap.removeByProjector(getDeviceID());
                    if(ffWorldMap.isSync()) {
                        BlockPos png = ffWorldMap.getPos();
                        world.removeTileEntity(png);
                        world.setBlockState(png,Blocks.AIR.getDefaultState(),Constants.BlockFlags.DEFAULT_AND_RERENDER);
                    }
                    ffWorldMap.setSync(false);
                }
            }
        }
        Map<Integer, TileProjector> FieldFusion = Grid.getWorldGrid(
                world).getFusions();
        for (TileProjector tileentity : FieldFusion.values()) {

            if (tileentity.getPowerSourceID() == this.getPowerSourceID()) {
                if (tileentity.isActive()) {
                    tileentity.calculateField(false);
                }
            }
        }

    }
    private boolean hasValidTypeMod() {
        return getTypeModule()!=null;
    }

    private int neededForcePower(int factor) {

        if(!field_def.isEmpty())
            return field_def.size() * MFFSConfig.BASE_FORCEFIELD_COST.get();
        int forcepower =0 ;
        int blocks = 0;
        int tmplength=1;
        if(this.strengthItems()!=0)
            tmplength = this.strengthItems();

        switch(this.getTypeModule().getModuleType()) {

            case Empty:
                break;
            case Advanced_Cube:
                break;
            case Containment:
                break;
            case Cube:
                break;
            case Deflector:
                break;
            case Diagonal_Wall:
                break;
            case Sphere:
                break;
            case Tube:
                break;
            case Wall:
                break;
            case Custom:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + this.getTypeModule().getModuleType());
        }
        return 0;
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
        linkPower = 0;
        field_type=EnumFieldType.Default;

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
        return new ProjectorContainer(windowId,playerEntity,this);
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
        EnumProjectorModule newModule;
        if(module.getStackInSlot(0).isEmpty()) {
               newModule=EnumProjectorModule.Empty;
        }
        else
            newModule=((ItemProjectorModule)module.getStackInSlot(0).getItem()).getModuleType();

        if(this.getBlockState().get(BlockProjector.TYPE)!=newModule) {

            this.getWorld().setBlockState(pos,this.getBlockState().with(BlockProjector.TYPE,newModule), Constants.BlockFlags.BLOCK_UPDATE+Constants.BlockFlags.UPDATE_NEIGHBORS);
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
            setField_type(EnumFieldType.Default);
        }
        else if(hasOption(MFFSItems.ZAPPER_OPTION.get())) {
            setField_type(EnumFieldType.Zapper);
        }
        else
            setField_type(EnumFieldType.Default);

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
        return new ArrayList<BlockPos>(field_interior);
    }

    public void burnout() {
        this.setBurnedOut(true);
        this.dropPlugins();
    }

    public boolean calculateField(boolean addToMap) {
        long time = System.currentTimeMillis();
        field_def.clear();
        field_interior.clear();
        if(hasValidTypeMod()) {
            Set<BlockPos> tField=new HashSet<>();
            Set<BlockPos> tFieldInt=new HashSet<>();
            if(getTypeModule().is3D()) {
                getTypeModule().calculateField(this,tField,tFieldInt);
            }
            else {
                getTypeModule().calculateField(this,tField);
            }
            for(BlockPos pnt:tField) {
                if(pnt.getY()+this.pos.getY() < world.getChunk(pnt).getHeight()) {
                    BlockPos tp= this.pos.add(pnt);
                    if(defineForceField(tp,addToMap)) {
                        field_def.add(tp);
                    }
                    else {
                        return false;
                    }

                }
            }
            return true;
        }
        return false;
    }

    public boolean defineForceField(BlockPos png, boolean addToMap) {
        for(IProjectorOption opt:options()) {
            if(opt instanceof ItemFieldJammerOption) {
                if( ((ItemFieldJammerOption)opt).CheckJammerInfluence(png,world,this) )
                    return false;
            }
            if(opt instanceof ItemFieldFusionOption) {
                if (((ItemFieldFusionOption)opt).checkFieldFusionInfluence(png,world,this))
                    return true;
            }
        }
        ForceFieldBlockStack ffWorldMap = WorldMap.getForceFieldWorld(world).getOrCreateForceFieldStack(png,world);
        if(!ffWorldMap.isEmpty()) {
            if(ffWorldMap.getProjectorID()!=getDeviceID()) {
                ffWorldMap.removeByProjector(getDeviceID());
                ffWorldMap.add(getPowerSourceID(),getDeviceID(),getField_type());
            }
        } else {
            ffWorldMap.add(getPowerSourceID(),getDeviceID(),getField_type());
            ffWorldMap.setSync(false);
        }
        field_queue.push(png.hashCode());
        return true;
    }

    @Override
    public void remove() {
        Grid.getWorldGrid(world).getProjectors().remove(getDeviceID());
        destroyField();
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

    public IItemHandler getModule() {
        return module;
    }

    public IItemHandler getFoci() {
        return foci;
    }

    public IItemHandler getDistanceModifiers() {
        return distanceModifier;
    }

    public IItemHandler getStrengthModifiers() {
        return strengthModifier;
    }

    public IItemHandler getOptions() {
        return options;
    }
}
