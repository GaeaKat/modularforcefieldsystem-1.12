package com.nekokittygames.mffs.network;

import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.tileentity.TileEntityMachines;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by katsw on 11/09/2016.
 */
public class PacketTileHandler extends PacketTile<TileEntityMachines> implements IMessageHandler<PacketTileHandler,IMessage>{
    int eventType;
    String eventData;

    public PacketTileHandler() {
    }

    public PacketTileHandler(TileEntityMachines tile, int eventType, String eventData) {
        super(tile);
        this.eventType = eventType;
        this.eventData = eventData;
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        super.fromBytes(byteBuf);
        eventType=byteBuf.readInt();
        eventData= ByteBufUtils.readUTF8String(byteBuf);
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        super.toBytes(byteBuf);
        byteBuf.writeInt(eventType);
        ByteBufUtils.writeUTF8String(byteBuf,eventData);
    }


    @Override
    public IMessage onMessage(final PacketTileHandler message, final MessageContext ctx) {
        super.onMessage(message, ctx);
        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(new Runnable() {
            @Override
            public void run() {
                handle(message, ctx);
            }
        });
        return null;
    }

    private void handle(PacketTileHandler message, MessageContext ctx) {
        World world;
        if(ctx.side== Side.CLIENT) {
            world = ModularForceFieldSystem.proxy.getClientWorld();
        }
        else {
            world = ctx.getServerHandler().player.getEntityWorld();
        }

        if (world == null) {
        }

        TileEntity tile = world.getTileEntity(new BlockPos(message.x, message.y, message.z));
        if (tile != null) {
            message.tile = (TileEntityMachines) tile;
        }
        ((TileEntityMachines)tile).onNetworkHandlerEvent(message.eventType,message.eventData);
    }
}
