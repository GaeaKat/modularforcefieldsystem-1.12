package net.newgaea.mffs.common.tiles;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;
import net.newgaea.mffs.common.misc.EnumFieldType;

import java.util.Objects;

public class TileForcefield extends TileMFFS{
    public static final ModelProperty<BlockState> MIMIC=new ModelProperty<>();
    public static final ModelProperty<EnumFieldType> FIELD=new ModelProperty<>();

    public BlockState getMimicState() {
        return mimicState;
    }

    public TileForcefield setMimicState(BlockState mimicState) {
        this.mimicState = mimicState;
        markDirty();
        world.notifyBlockUpdate(pos,getBlockState(),getBlockState(), Constants.BlockFlags.BLOCK_UPDATE+Constants.BlockFlags.NOTIFY_NEIGHBORS);
        return this;
    }

    private BlockState mimicState;

    public EnumFieldType getFieldType() {
        return fieldType;
    }

    public TileForcefield setFieldType(EnumFieldType fieldType) {
        this.fieldType = fieldType;
        return this;
    }

    private EnumFieldType fieldType=EnumFieldType.Containment;
    public TileForcefield(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void readExtra(CompoundNBT compound) {
        if(compound.contains("mimicStack"))
            mimicState= NBTUtil.readBlockState(compound.getCompound("mimicStack"));
        if(compound.contains("fieldType"))
            fieldType=EnumFieldType.valueOf(compound.getString("fieldType"));
    }

    @Override
    public CompoundNBT writeExtra(CompoundNBT compound) {
        if(mimicState!=null)
            compound.put("mimicStack",NBTUtil.writeBlockState(mimicState));
        compound.putString("fieldType",fieldType.toString());
        return compound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        BlockState oldMimic=mimicState;
        super.onDataPacket(net, pkt);
        if(!Objects.equals(oldMimic,mimicState)){
            ModelDataManager.requestModelDataRefresh(this);
            world.notifyBlockUpdate(pos,getBlockState(),getBlockState(), Constants.BlockFlags.BLOCK_UPDATE+Constants.BlockFlags.NOTIFY_NEIGHBORS);
        }
    }

    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder().withInitial(MIMIC,mimicState)
                .withInitial(FIELD,fieldType)
                .build();
    }
}
