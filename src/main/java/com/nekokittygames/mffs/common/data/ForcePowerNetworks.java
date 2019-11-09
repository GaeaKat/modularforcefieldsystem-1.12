package com.nekokittygames.mffs.common.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class ForcePowerNetworks implements INBTSerializable<CompoundNBT> {


    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt=new CompoundNBT();


        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }
}
