package com.nekokittygames.mffs.network;

import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by katsw on 08/09/2016.
 */
public class PacketTile<T extends TileEntity> implements IMessage {

    private static final long serialVersionUID = -1447633008013055477L;
    protected int dim, x, y, z;
    protected transient T tile;
    protected transient EntityPlayer player;

    public PacketTile() {

    }

    public PacketTile(T tile) {
        this.tile = tile;

        this.x = tile.getPos().getX();
        this.y = tile.getPos().getY();
        this.z = tile.getPos().getZ();
        this.dim = tile.getWorld().provider.getDimension();
    }
    @Override
    public void fromBytes(ByteBuf byteBuf) {
        x = byteBuf.readInt();
        y = byteBuf.readInt();
        z = byteBuf.readInt();
        dim = byteBuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(x);
        byteBuf.writeInt(y);
        byteBuf.writeInt(z);
        byteBuf.writeInt(dim);
    }

    public IMessage onMessage(PacketTile message, MessageContext ctx) {

        return null;
    }
}
