package com.nekokittygames.mffs.common.tiles;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import com.nekokittygames.mffs.api.IPowerStorageLink;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.Optional;

import java.util.UUID;

/**
 * Created by katsw on 03/04/2016.
 */
//@Optional.Interface(iface = "cofh.api.energy.IEnergyReceiver",modid = "CoFHCore")
public class TileCapacitor extends MFFSTile implements IEnergyReceiver,IPowerStorageLink {

    public EnergyStorage energy;


    @Override
    public int getUpgradeSlots() {
        return 3;
    }

    public TileCapacitor()
    {

        energy=new EnergyStorage(10000);
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound)
    {
        super.readCustomNBT(compound);
        energy.readFromNBT(compound);
    }


    @Override
    public void validate() {
        super.validate();
        //this.markDirty();

    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound)
    {
        super.writeCustomNBT(compound);
        energy.writeToNBT(compound);
    }

//    @Optional.Method(modid = "CoFHCore")
    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        int output=energy.receiveEnergy(maxReceive,simulate);
        if(!simulate) {
            IBlockState bs=worldObj.getBlockState(pos);
            worldObj.notifyBlockUpdate(pos,bs,bs,3);
        }
        return output;
    }

//    @Optional.Method(modid = "CoFHCore")
    @Override
    public int getEnergyStored(EnumFacing from) {
        return energy.getEnergyStored();
    }

//    @Optional.Method(modid = "CoFHCore")
    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return energy.getMaxEnergyStored();
    }

//    @Optional.Method(modid = "CoFHCore")
    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return true;
    }

    @Override
    public int getEnergyStored() {
        return energy.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return energy.getMaxEnergyStored();
    }

    @Override
    public UUID GetNetworkId() {
        // TODO: work on the network
        return UUID.randomUUID();
    }

    @Override
    public void LinkToNetwork(UUID network) {

    }

    @Override
    public void DelinkFromNetwork(UUID network) {

    }
}
