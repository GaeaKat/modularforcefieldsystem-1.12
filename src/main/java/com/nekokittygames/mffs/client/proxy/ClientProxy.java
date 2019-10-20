package com.nekokittygames.mffs.client.proxy;

import com.nekokittygames.mffs.client.gui.screens.GeneratorScreen;
import com.nekokittygames.mffs.common.init.MFFSContainers;
import com.nekokittygames.mffs.common.proxy.IProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ClientProxy implements IProxy {

    @Override
    public void init() {
        ScreenManager.registerFactory(MFFSContainers.GENERATOR, GeneratorScreen::new);
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }
}
