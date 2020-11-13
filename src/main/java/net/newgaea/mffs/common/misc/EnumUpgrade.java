package net.newgaea.mffs.common.misc;

import net.newgaea.mffs.api.EnumPowerLink;
import net.newgaea.mffs.common.blocks.ModBlock;
import net.newgaea.mffs.common.init.MFFSBlocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum EnumUpgrade {
    Capacity(MFFSBlocks.CAPACITOR.get()),
    Range(MFFSBlocks.CAPACITOR.get()),
    Speed(MFFSBlocks.GENERATOR.get());
    private List<ModBlock> validBlocks=new ArrayList<>();
    EnumUpgrade(ModBlock... blocks) {
        validBlocks.addAll(Arrays.asList(blocks));
    }
    public List<ModBlock> getValidBlocks() {
        return validBlocks;
    }
}
