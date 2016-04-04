package com.nekokittygames.mffs.common.tiles;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.Optional;

/**
 * Created by katsw on 03/04/2016.
 */
@Optional.Interface(iface = "cofh.api.energy.IEnergyReceiver",modid = "CoFHCore")
public class TileCapacitor extends MFFSTile implements IEnergyReceiver {

    public EnergyStorage energy;



    public TileCapacitor()
    {
        energy=new EnergyStorage(10000);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        readCustomNBT(compound);
    }

    public void readCustomNBT(NBTTagCompound compound)
    {
        energy.readFromNBT(compound);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        writeCustomNBT(compound);
    }

    public void writeCustomNBT(NBTTagCompound compound)
    {
        energy.writeToNBT(compound);
    }

    @Optional.Method(modid = "CoFHCore")
    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        return energy.receiveEnergy(maxReceive,simulate);
    }

    @Optional.Method(modid = "CoFHCore")
    @Override
    public int getEnergyStored(EnumFacing from) {
        return energy.getEnergyStored();
    }

    @Optional.Method(modid = "CoFHCore")
    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return energy.getMaxEnergyStored();
    }

    @Optional.Method(modid = "CoFHCore")
    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return true;
    }
}
