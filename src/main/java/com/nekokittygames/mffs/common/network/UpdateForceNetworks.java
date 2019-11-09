package com.nekokittygames.mffs.common.network;

import com.nekokittygames.mffs.client.network.ClientNetworkHandler;
import com.nekokittygames.mffs.common.data.ForcePowerNetworks;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateForceNetworks {

    public CompoundNBT networksNBT;

    public UpdateForceNetworks(ForcePowerNetworks networks)
    {
        this.networksNBT = networks.serializeNBT();
    }

    public UpdateForceNetworks(CompoundNBT networksNBT){
        this.networksNBT=networksNBT;
    }

    public UpdateForceNetworks(PacketBuffer buf)
    {
        this.networksNBT=buf.readCompoundTag();
    }

    public void encode(PacketBuffer buf)
    {
        buf.writeCompoundTag(networksNBT);
    }

    public boolean handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> ClientNetworkHandler.HandleNetworks(this));
        context.get().setPacketHandled(true);
        return true;

    }
}
