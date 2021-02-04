package net.newgaea.mffs.common.network.messages;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.newgaea.mffs.common.tiles.TileCapacitor;
import net.newgaea.mffs.common.tiles.TileNetwork;

import java.util.function.Supplier;

public class TogglePowerLinkModeMessage {
    private BlockPos pos;

    public TogglePowerLinkModeMessage(BlockPos pos) {
        this.pos = pos;
    }

    public static TogglePowerLinkModeMessage decode(PacketBuffer packetBuffer) {
        BlockPos pos=packetBuffer.readBlockPos();
        return new TogglePowerLinkModeMessage(pos);
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
                if(te instanceof TileCapacitor) {
                    TileCapacitor tileCapacitor= (TileCapacitor) te;
                    tileCapacitor.togglePowerMode();

                }
            }
        });
        return true;
    }
}
