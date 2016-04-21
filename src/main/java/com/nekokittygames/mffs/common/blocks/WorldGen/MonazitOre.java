package com.nekokittygames.mffs.common.blocks.worldgen;

import com.nekokittygames.mffs.common.blocks.ModBlock;
import com.nekokittygames.mffs.common.libs.LibBlockNames;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by katsw on 03/04/2016.
 */
public class MonazitOre extends ModBlock {
    public MonazitOre() {
        super(Material.rock);
        setRegistryName(LibBlockNames.MONAZIT_ORE);
        setUnlocalizedName(LibBlockNames.MONAZIT_ORE);
        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
    }


    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.SOLID;
    }
}
