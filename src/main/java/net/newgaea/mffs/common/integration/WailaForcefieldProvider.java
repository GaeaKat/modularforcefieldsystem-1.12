package net.newgaea.mffs.common.integration;

import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.newgaea.mffs.common.tiles.TileForcefield;

import java.util.List;

public class WailaForcefieldProvider  implements IComponentProvider, IServerDataProvider<TileEntity> {

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if(accessor.getTileEntity() instanceof TileForcefield) {
            TileForcefield forcefield= (TileForcefield) accessor.getTileEntity();
            tooltip.add(new StringTextComponent("Client: "+forcefield.getFieldType()));
            if(accessor.getServerData().contains("serv"))
                tooltip.add(new StringTextComponent("Server: "+accessor.getServerData().getString("serv")));
        }
    }


    @Override
    public void appendServerData(CompoundNBT compoundNBT, ServerPlayerEntity serverPlayerEntity, World world, TileEntity tileEntity) {
        if(tileEntity instanceof TileForcefield) {
            TileForcefield forcefield= (TileForcefield) tileEntity;
            compoundNBT.putString("serv",forcefield.getFieldType().toString());
        }
    }
}
