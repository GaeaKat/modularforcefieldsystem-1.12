package net.newgaea.mffs.common.forcefield;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.newgaea.mffs.api.IProjectorOption;
import net.newgaea.mffs.common.blocks.BlockForceField;
import net.newgaea.mffs.common.config.MFFSConfig;
import net.newgaea.mffs.common.data.Grid;
import net.newgaea.mffs.common.init.MFFSBlocks;
import net.newgaea.mffs.common.init.MFFSItems;
import net.newgaea.mffs.common.items.modules.*;
import net.newgaea.mffs.common.items.options.ItemFieldFusionOption;
import net.newgaea.mffs.common.items.options.ItemFieldJammerOption;
import net.newgaea.mffs.common.misc.EnumFieldType;
import net.newgaea.mffs.common.misc.InventoryFunctions;
import net.newgaea.mffs.common.tiles.TileForcefield;
import net.newgaea.mffs.common.tiles.TileProjector;

import java.util.*;

public class FFGenerator implements INBTSerializable<CompoundNBT> {

    private EnumFieldType fieldType;
    private TileProjector projector;
    private List<BlockPos> forcefieldBlocks=new ArrayList<>();
    private List<BlockPos> interiorBlocks=new ArrayList<>();
    private Stack<Integer> field_queue=new Stack<>();
    private int blockCounter;
    private int seed;
    public FFGenerator(TileProjector projector) {
        this.projector = projector;
    }

    public boolean calculateField(boolean add) {
        long time = System.currentTimeMillis();
        forcefieldBlocks.clear();
        interiorBlocks.clear();
        if(projector.getTypeModule()==null)
            return false;
        Set<BlockPos> tField=new HashSet<>();
        Set<BlockPos> tFieldInterior=new HashSet<>();
        if(projector.getTypeModule().is3D()) {
            projector.getTypeModule().calculateField(projector,tField,tFieldInterior);
        } else {
            projector.getTypeModule().calculateField(projector,tField);
        }
        for(BlockPos pnt:tField) {
            if(pnt.getY()+projector.getPos().getY() < 255) {
                BlockPos tp=projector.getPos().add(pnt);
                if(ForceFieldDefine(tp,add)) {
                    forcefieldBlocks.add(tp);
                }
                else
                    return false;
                // todo: Interiors

            }

        }
        return true;
    }

    public boolean calculateBlock(BlockPos pos) {
        return true;
    }

    private boolean ForceFieldDefine(BlockPos pos, boolean addToMap) {
        for(IProjectorOption opt:projector.options()) {
            if(opt instanceof ItemFieldJammerOption) {
                if(((ItemFieldJammerOption)opt).CheckJammerInfluence(pos,projector.getWorld(),projector))
                    return false;
            }
            if(opt instanceof ItemFieldFusionOption) {
                if(((ItemFieldFusionOption)opt).checkFieldFusionInfluence(pos,projector.getWorld(),projector))
                    return true;
            }

        }
        ForceFieldBlockStack map= WorldMap.getForceFieldWorld(projector.getWorld()).getOrCreateForceFieldStack(pos,projector.getWorld());
        if(!map.isEmpty()) {
            if(map.getProjectorID()!= projector.getDeviceID()) {
                map.removeByProjector(projector.getDeviceID());
                map.add(projector.getPowerSourceID(), projector.getDeviceID(), fieldType);
            }
        } else {
            map.add(projector.getPowerSourceID(), projector.getDeviceID(), fieldType);
            map.setSync(false);
        }
        field_queue.push(pos.hashCode());
        return true;
    }

    public void GenerateBlocks(boolean init) {
        int cost = 0;
        if(init)
            cost = MFFSConfig.BASE_FORCEFIELD_COST.get() * MFFSConfig.FIELD_CREATE_MODIFIER.get();
        else
            cost = MFFSConfig.BASE_FORCEFIELD_COST.get();

        if(fieldType==EnumFieldType.Zapper)
            cost*=MFFSConfig.ZAPPER_MODIFIER.get();

        projector.consumePower(cost*forcefieldBlocks.size(),false);

        blockCounter=0;

        for(BlockPos pos:forcefieldBlocks) {
            if(blockCounter>=MFFSConfig.FORCEFIELD_PER_TICK.get())
                break;
            ForceFieldBlockStack ffb=WorldMap.getForceFieldWorld(projector.getWorld()).getForceFieldBlockStack(pos.hashCode());
            if(ffb!=null) {
                if(ffb.isSync())
                    continue;

                BlockPos png=ffb.getPos();
                if(projector.getWorld().getChunkProvider().isChunkLoaded(new ChunkPos(png))) {
                    if(!ffb.isEmpty()) {
                        if(ffb.getProjectorID() == projector.getDeviceID()) {
                            if(projector.hasOption(MFFSItems.BLOCK_BREAKER_OPTION.get())) {
                                BlockState block=projector.getWorld().getBlockState(png);
                                TileEntity tileEntity=projector.getWorld().getTileEntity(png);

                                if(block.getBlock()!=MFFSBlocks.FORCEFIELD.get()
                                        && !block.isAir()
                                        && block.getBlock()!= Blocks.BEDROCK
                                        && tileEntity!=null) {
                                    List<ItemStack> stacks= InventoryFunctions.getItemStacksFromBlock(projector.getWorld(),png);
                                    projector.getWorld().setBlockState(png,Blocks.AIR.getDefaultState());
                                    if(stacks!=null) {
                                        IItemHandler inventory = InventoryFunctions.findAttachedInventory(projector.getWorld(),projector.getPos());
                                        if(inventory!=null) {
                                            int curPlace=0;
                                            for(int i=0;i<inventory.getSlots();i++) {
                                                if(inventory.getStackInSlot(i).isEmpty()) {
                                                    inventory.insertItem(i, stacks.get(curPlace), false);
                                                    curPlace++;
                                                    if(curPlace>=stacks.size())
                                                        break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if(projector.getWorld().getBlockState(png).getMaterial().isLiquid()
                            || projector.getWorld().isAirBlock(png)
                            || projector.getWorld().getBlockState(png).getBlock()==MFFSBlocks.FORCEFIELD.get()) {
                                if(projector.getWorld().getBlockState(png).getBlock()!=MFFSBlocks.FORCEFIELD.get()) {
                                    projector.getWorld().setBlockState(png,MFFSBlocks.FORCEFIELD.getDefaultState(),Constants.BlockFlags.DEFAULT_AND_RERENDER);
                                    TileEntity ent = projector.getWorld().getTileEntity(png);
                                    if (ent instanceof TileForcefield) {
                                        TileForcefield tileForcefield = (TileForcefield) ent;
                                        tileForcefield.setFieldType(fieldType);
                                    }
                                    blockCounter++;
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
            ForceFieldBlockStack ffb=WorldMap.getForceFieldWorld(projector.getWorld()).getForceFieldBlockStack(field_queue.pop());
            if(!ffb.isEmpty()) {
                if(ffb.getProjectorID()== projector.getDeviceID()) {
                    ffb.removeByProjector(projector.getDeviceID());
                    if(ffb.isSync()) {
                        BlockPos pos=ffb.getPos();
                        projector.getWorld().removeTileEntity(pos);
                        projector.getWorld().setBlockState(pos,Blocks.AIR.getDefaultState(),Constants.BlockFlags.DEFAULT_AND_RERENDER);
                    }
                    ffb.setSync(false);
                }
                else {
                    ffb.removeByProjector(projector.getDeviceID());
                }
            }
        }

        Map<Integer,TileProjector> FieldFusion=Grid.getWorldGrid(projector.getWorld()).getFusions();
        for(TileProjector te:FieldFusion.values()) {
            if(te.getPowerSourceID() == projector.getPowerSourceID()) {
                if(te.isActive()) {
                    te.calculateField(false);
                }
            }
        }
    }
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT cmp=new CompoundNBT();
        ListNBT list = new ListNBT();
        for(BlockPos blockPos:forcefieldBlocks) {
            list.add(NBTUtil.writeBlockPos(blockPos));
        }
        cmp.put("forcefields",list);

        list = new ListNBT();
        for(BlockPos blockPos:interiorBlocks) {
            list.add(NBTUtil.writeBlockPos(blockPos));
        }
        cmp.put("interior",list);

        cmp.putInt("seed",seed);
        cmp.putInt("fieldType",fieldType.ordinal());
        return cmp;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ListNBT listNBT = nbt.getList("forcefields", Constants.NBT.TAG_COMPOUND);
        forcefieldBlocks.clear();
        for(int i=0;i<listNBT.size();i++) {
            forcefieldBlocks.add(NBTUtil.readBlockPos(listNBT.getCompound(i)));

        }

        if(nbt.contains("interior")) {
            listNBT = nbt.getList("interior", Constants.NBT.TAG_COMPOUND);
            interiorBlocks.clear();
            for (int i = 0; i < listNBT.size(); i++) {
                interiorBlocks.add(NBTUtil.readBlockPos(listNBT.getCompound(i)));

            }
        }
        this.seed = nbt.getInt("seed");
        fieldType=EnumFieldType.values()[nbt.getInt("fieldType")];
    }

    public List<BlockPos> getInteriorPoints() {
        return interiorBlocks;
    }

    public EnumFieldType getFieldType() {
        return fieldType;
    }

    public FFGenerator setFieldType(EnumFieldType fieldType) {
        this.fieldType = fieldType;
        return this;
    }

    public int neededForcePower(int factor) {
        if(!forcefieldBlocks.isEmpty()) {
            return forcefieldBlocks.size() * MFFSConfig.BASE_FORCEFIELD_COST.get();
        }
        int forcepower = 0;
        int blocks = 0;
        int tmplength = 1;
        if(projector.strengthItems()!=0)
            tmplength=projector.strengthItems();

        if(projector.getTypeModule() instanceof ItemWallModule)
            blocks = ((projector.focusItems(Direction.DOWN) + projector.focusItems(Direction.EAST) + projector.focusItems(Direction.WEST) + projector.focusItems(Direction.UP)) + 1) * tmplength;
        if(projector.getTypeModule() instanceof ItemDeflectorModule)
            blocks = (projector.focusItems(Direction.DOWN) + projector.focusItems(Direction.UP)+1)  * (projector.focusItems(Direction.EAST) + projector.focusItems(Direction.WEST)+1);
        if(projector.getTypeModule() instanceof ItemTubeModule)
            blocks = (((projector.distanceItems() + 2 + projector.distanceItems() + 2) * 4) + 4) * (projector.strengthItems()+1);
        if(projector.getTypeModule() instanceof ItemCubeModule || projector.getTypeModule() instanceof ItemSphereModule)
            blocks = (projector.distanceItems() * projector.distanceItems()) * 6;
        forcepower = blocks * MFFSConfig.BASE_FORCEFIELD_COST.get();
        if(factor!=1) {
            forcepower = (forcepower * MFFSConfig.FIELD_CREATE_MODIFIER.get()) + (forcepower * 5);
        }
        return forcepower;


    }
}
