package com.nekokittygames.mffs.common.blocks.worldgen;

import com.nekokittygames.mffs.common.blocks.MFFSBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by katsw on 03/04/2016.
 */
public class MonazitOre extends MFFSBlock {
    public MonazitOre() {
        super(Material.rock);
    }


    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.SOLID;
    }
}
