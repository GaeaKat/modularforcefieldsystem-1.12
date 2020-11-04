package net.newgaea.mffs.common.blocks;

import net.minecraft.block.Block;

public class ModBlock extends Block {
    public ModBlock(Properties properties) {
        super(properties);
    }

    public  boolean activated(){
        return true;
    }
}
