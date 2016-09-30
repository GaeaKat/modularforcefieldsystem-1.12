package com.nekokittygames.mffs.common.compat;

import com.nekokittygames.mffs.common.tileentity.TileEntityExtractor;
import net.darkhax.tesla.api.ITeslaConsumer;

/**
 * Created by katsw on 30/09/2016.
 */
public class TeslaCap implements ITeslaConsumer
{
    TileEntityExtractor extractor;
    public TeslaCap(TileEntityExtractor extractor)
    {
        this.extractor=extractor;
    }

    @Override
    public long givePower(long power, boolean simulated) {
        double freeSpace = (double)(extractor.getMaxWorkEnergy() - extractor.getWorkEnergy());
        if(freeSpace==0)
        {

        }
        if(freeSpace >= power) {
            if(!simulated)
                extractor.setWorkEnergy(extractor.getWorkEnergy() + (int)power);
            return power;
        }
        else {
            if(!simulated)
                extractor.setWorkEnergy(extractor.getMaxWorkEnergy());
            return (int)(freeSpace);
        }
    }
}