package com.nekokittygames.mffs.network.client;

import com.nekokittygames.mffs.common.tileentity.TileEntityForceField;
import com.nekokittygames.mffs.common.tileentity.TileEntityProjector;
import com.nekokittygames.mffs.network.PacketTile;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by katsw on 07/09/2016.
 */
public class PacketClientForceField extends PacketTile<TileEntityForceField> implements IMessageHandler<PacketClientForceField,IMessage> {



    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    public IMessage onMessage(PacketClientForceField message, MessageContext ctx) {
        super.onMessage(message,ctx);

        return null;
    }
}
