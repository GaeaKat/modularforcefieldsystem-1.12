package net.newgaea.mffs.transport.network.property;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateServerContainerPropertyMessage {
    private final short windowId;
    private final PropertyType<?> propertyType;
    private final short property;
    private final Object value;

    public UpdateServerContainerPropertyMessage(short windowId, PropertyType<?> propertyType, short property, Object value) {
        this.windowId = windowId;
        this.propertyType = propertyType;
        this.property = property;
        this.value = value;
    }

    public static UpdateServerContainerPropertyMessage decode(PacketBuffer packetBuffer) {
        short windowId = packetBuffer.readShort();
        PropertyType<?> propertyType = PropertyTypes.getByIndex(packetBuffer.readShort());
        short property = packetBuffer.readShort();
        Object value = propertyType.getReader().apply(packetBuffer);
        return new UpdateServerContainerPropertyMessage(windowId, propertyType, property, value);
    }

    public void encode(PacketBuffer packetBuffer) {
        packetBuffer.writeShort(windowId);
        packetBuffer.writeShort(PropertyTypes.getIndex(propertyType));
        packetBuffer.writeShort(property);
        propertyType.attemptWrite(packetBuffer, value);
    }

    public boolean consume(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            PlayerEntity playerEntity = contextSupplier.get().getSender();
            if (playerEntity != null) {
                Container container = playerEntity.openContainer;
                if (container.windowId == windowId) {
                    if (container instanceof IPropertyManaged) {
                        ((IPropertyManaged) container).getPropertyManager()
                                .update(propertyType, property, value);
                    }
                }
            }
        });
        return true;
    }
}
