package net.newgaea.mffs.common.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.newgaea.mffs.client.proxy.ClientProxy;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface IProxy {
    void init();
    void register();
    World getClientWorld();
    PlayerEntity getClientPlayer();

    @Contract(value = " -> new", pure = true)
    public static @NotNull IProxy createClientProxy() {
        return new ClientProxy();
    }
    @Contract(value = " -> new", pure = true)
    public static @NotNull IProxy createCommonProxy() {
        return new CommonProxy();
    }
}
