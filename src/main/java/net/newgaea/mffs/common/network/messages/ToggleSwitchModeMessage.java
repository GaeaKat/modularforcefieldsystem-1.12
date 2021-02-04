package net.newgaea.mffs.common.network.messages;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.newgaea.mffs.common.misc.ModeEnum;
import net.newgaea.mffs.common.tiles.TileNetwork;

import java.util.function.Supplier;

public class ToggleSwitchModeMessage {
    private BlockPos pos;

    public ToggleSwitchModeMessage(BlockPos pos) {
        this.pos = pos;
    }

    public static ToggleSwitchModeMessage decode(PacketBuffer packetBuffer) {
        BlockPos pos=packetBuffer.readBlockPos();
        return new ToggleSwitchModeMessage(pos);
    }

    public void encode(PacketBuffer packetBuffer) {
        packetBuffer.writeBlockPos(pos);
    }

    public boolean consume(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(new Runnable() {
            @Override
            public void run() {
                World world=contextSupplier.get().getSender().world;
                TileEntity te=world.getTileEntity(pos);
                if(te instanceof TileNetwork) {
                    TileNetwork tileNetwork= (TileNetwork) te;
                    tileNetwork.toggleSwitchMode();

                }
            }
        });
        return true;
    }
}
