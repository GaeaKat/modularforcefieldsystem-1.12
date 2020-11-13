package net.newgaea.mffs.common.storage;

import net.minecraft.command.arguments.NBTCompoundTagArgument;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class MFFSEnergyStorage extends EnergyStorage implements INBTSerializable<CompoundNBT> {
    public MFFSEnergyStorage(int capacity,int maxTransfer) {
        super(capacity,maxTransfer);
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void addEnergy(int energy) {
        this.energy += energy;
        if (this.energy > getMaxEnergyStored()) {
            this.energy = getMaxEnergyStored();
        }
    }

    public void consumeEnergy(int energy)
    {
        this.energy-=energy;
        if(this.energy<0)
            this.energy=0;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("energy", getEnergyStored());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        setEnergy(((CompoundNBT)nbt).getInt("energy"));
    }

}
