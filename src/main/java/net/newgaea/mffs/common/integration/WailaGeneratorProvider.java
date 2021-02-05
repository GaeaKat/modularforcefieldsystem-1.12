package net.newgaea.mffs.common.integration;

import mcp.mobius.waila.api.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.newgaea.mffs.common.tiles.TileGenerator;

import java.util.List;

public class WailaGeneratorProvider implements IComponentProvider, IServerDataProvider<TileEntity> {
    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        TileEntity entity=accessor.getTileEntity();
        if(entity instanceof TileGenerator) {
            if(accessor.getServerData().contains("energy", Constants.NBT.TAG_INT_ARRAY)) {
                int[] data = accessor.getServerData().getIntArray("energy");
                tooltip.add(new StringTextComponent("Force Energy: " + data[0] + "/" + data[1]));
            }
        }
    }

    @Override
    public void appendServerData(CompoundNBT compoundNBT, ServerPlayerEntity serverPlayerEntity, World world, TileEntity tileEntity) {
        if(tileEntity instanceof TileGenerator) {
            TileGenerator tileGenerator = (TileGenerator) tileEntity;
            IntArrayNBT list=new IntArrayNBT(new int[] {tileGenerator.getEnergy(),tileGenerator.getTotalEnergy()});
            compoundNBT.put("energy",list);

        }

    }
}
