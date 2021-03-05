package net.newgaea.mffs.common.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class CommonProxy implements IProxy {

    @Override
    public void init() {

    }

    @Override
    public void register() {

    }

    @Override
    public World getClientWorld() {
        throw new IllegalStateException("Client side code running on server");
    }

    @Override
    public PlayerEntity getClientPlayer() {
        throw new IllegalStateException("Client side code running on server");
    }
}
