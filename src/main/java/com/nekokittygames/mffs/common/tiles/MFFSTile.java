package com.nekokittygames.mffs.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Created by katsw on 03/04/2016.
 */
public abstract class MFFSTile extends TileEntity  {

    @CapabilityInject(IItemHandler.class)
    static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;

    public IItemHandler upgradeInventorySlots;
    public static final String UPGRADE_SLOTS_KEY="upgradeSlots";

    public abstract int getUpgradeSlots();

    public MFFSTile()
    {

        upgradeInventorySlots=new ItemStackHandler(getUpgradeSlots());
    }


    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if(capability==ITEM_HANDLER_CAPABILITY)
            return true;
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability==ITEM_HANDLER_CAPABILITY)
            return(T)upgradeInventorySlots;
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        readCustomNBT(compound);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        writeCustomNBT(compound);
    }

    public void readCustomNBT(NBTTagCompound compound)
    {
        ITEM_HANDLER_CAPABILITY.readNBT(upgradeInventorySlots,null,compound.getTag(UPGRADE_SLOTS_KEY));
    }

    public void writeCustomNBT(NBTTagCompound compound)
    {
        compound.setTag(UPGRADE_SLOTS_KEY,ITEM_HANDLER_CAPABILITY.writeNBT(upgradeInventorySlots,null));
    }

    @Override
    public void markDirty() {
        super.markDirty();
    }


    public String getDescription()
    {
        return getBlockType().getUnlocalizedName()+".description";
    }

    @Override
    public Packet<?> getDescriptionPacket() {
        NBTTagCompound cmp=new NBTTagCompound();
        writeCustomNBT(cmp);
        return new SPacketUpdateTileEntity(pos,1,cmp);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readCustomNBT(pkt.getNbtCompound());
    }
}
