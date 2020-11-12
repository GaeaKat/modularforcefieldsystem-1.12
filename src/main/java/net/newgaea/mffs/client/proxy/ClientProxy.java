package net.newgaea.mffs.client.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.newgaea.mffs.client.gui.screens.CapacitorScreen;
import net.newgaea.mffs.client.gui.screens.GeneratorScreen;
import net.newgaea.mffs.common.init.MFFSContainer;
import net.newgaea.mffs.common.proxy.IProxy;

public class ClientProxy implements IProxy {
    @Override
    public void init() {

        ScreenManager.registerFactory(MFFSContainer.GENERATOR.get(),GeneratorScreen::new );
        ScreenManager.registerFactory(MFFSContainer.CAPACITOR.get(), CapacitorScreen::new);
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
