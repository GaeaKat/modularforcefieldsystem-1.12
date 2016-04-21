package com.nekokittygames.mffs.api;

import net.minecraft.util.EnumFacing;

/**
 * Created by Katrina on 21/04/2016.
 */
public interface IPowerStorageLink extends ILinkable {

    /**
     * Gets the energy stored in this block
     * @return
     */
    int getEnergyStored();

    /**
     * Gets the maximum amount of energy able to be stored in this block
     * @return
     */
    int getMaxEnergyStored();

    /**
     * Extracts energy from this block,
     * @param maxExtract maximum amount of energy to extract
     * @param simulate simulate the extraction?
     * @return amount of energy extracted
     */
    //int extractEnergy(int maxExtract, boolean simulate);

}
