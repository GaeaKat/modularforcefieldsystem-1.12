package com.nekokittygames.mffs.common.blocks.WorldGen;

import com.nekokittygames.mffs.common.blocks.MFFSBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumWorldBlockLayer;
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
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.SOLID;
    }
}
