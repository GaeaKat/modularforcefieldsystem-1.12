package com.nekokittygames.mffs.common.common;

import com.nekokittygames.mffs.client.gui.GuiMFFSCapacitor;
import com.nekokittygames.mffs.common.blocks.machines.MFFSCapacitor;
import com.nekokittygames.mffs.common.inventory.MFFSCapacitorContainer;
import com.nekokittygames.mffs.common.tiles.MFFSTile;
import com.nekokittygames.mffs.common.tiles.TileCapacitor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * Created by Katrina on 19/04/2016.
 */
public class GUIHandler implements IGuiHandler{



    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity entity=world.getTileEntity(new BlockPos(x,y,z));
        if(entity instanceof TileCapacitor)
        {
            return new MFFSCapacitorContainer(player.inventory, (MFFSTile) entity);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity entity=world.getTileEntity(new BlockPos(x,y,z));
        if(entity instanceof TileCapacitor)
        {
            return new GuiMFFSCapacitor(player.inventory, (TileCapacitor)entity,175,165,"Capacitor");
        }
        return null;
    }
}
