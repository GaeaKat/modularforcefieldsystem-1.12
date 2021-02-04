package net.newgaea.mffs.common.network;


import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.newgaea.mffs.common.libs.LibMisc;
import net.newgaea.mffs.common.network.messages.TogglePowerLinkModeMessage;
import net.newgaea.mffs.common.network.messages.ToggleSwitchModeMessage;
import net.newgaea.mffs.transport.network.property.UpdateClientContainerPropertiesMessage;
import net.newgaea.mffs.transport.network.property.UpdateServerContainerPropertyMessage;

public class NetworkHandler {
    private static final String VERSION = "2";
    private final SimpleChannel channel;

    public NetworkHandler() {
        this.channel = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(LibMisc.MOD_ID, "network_handler"),
                () -> VERSION,
                VERSION::equals,
                VERSION::equals
        );
        this.channel.messageBuilder(UpdateClientContainerPropertiesMessage.class, 2)
                .decoder(UpdateClientContainerPropertiesMessage::decode)
                .encoder(UpdateClientContainerPropertiesMessage::encode)
                .consumer(UpdateClientContainerPropertiesMessage::consume)
                .add();

        this.channel.messageBuilder(UpdateServerContainerPropertyMessage.class, 3)
                .decoder(UpdateServerContainerPropertyMessage::decode)
                .encoder(UpdateServerContainerPropertyMessage::encode)
                .consumer(UpdateServerContainerPropertyMessage::consume)
                .add();
        this.channel.messageBuilder(ToggleSwitchModeMessage.class,4)
                .decoder(ToggleSwitchModeMessage::decode)
                .encoder(ToggleSwitchModeMessage::encode)
                .consumer(ToggleSwitchModeMessage::consume)
                .add();
        this.channel.messageBuilder(TogglePowerLinkModeMessage.class,5)
                .decoder(TogglePowerLinkModeMessage::decode)
                .encoder(TogglePowerLinkModeMessage::encode)
                .consumer(TogglePowerLinkModeMessage::consume)
                .add();
    }

    public void sendUpdateClientContainerProperties(ServerPlayerEntity playerEntity, UpdateClientContainerPropertiesMessage message) {
        this.channel.send(PacketDistributor.PLAYER.with(() -> playerEntity), message);
    }

    public void sendUpdateServerContainerProperties(UpdateServerContainerPropertyMessage message) {
        this.channel.send(PacketDistributor.SERVER.noArg(), message);
    }

    public void sendToggleSwitchMode(ToggleSwitchModeMessage message) {
        this.channel.send(PacketDistributor.SERVER.noArg(), message);
    }
    public void sendTogglePowerLinkMode(TogglePowerLinkModeMessage message) {
        this.channel.send(PacketDistributor.SERVER.noArg(), message);
    }
}
