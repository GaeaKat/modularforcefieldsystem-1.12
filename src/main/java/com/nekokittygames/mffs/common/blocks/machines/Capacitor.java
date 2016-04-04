package com.nekokittygames.mffs.common.blocks.machines;

import com.nekokittygames.mffs.common.blocks.MFFSTileBlock;
import com.nekokittygames.mffs.common.tiles.TileCapacitor;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by katsw on 03/04/2016.
 */
public class Capacitor extends MFFSMachineFaced {
    public Capacitor() {
        super(Material.anvil);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileCapacitor();
    }
}
