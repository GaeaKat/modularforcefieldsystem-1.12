package com.nekokittygames.mffs.common;

import com.nekokittygames.mffs.common.blocks.MFFSBlocks;
import com.nekokittygames.mffs.common.common.MFFSCommonProxy;
import com.nekokittygames.mffs.common.libs.LibMisc;
import com.nekokittygames.mffs.common.world.MFFSOreGen;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

/**
 * Created by katsw on 03/04/2016.
 */
@Mod(modid = LibMisc.MOD_ID , version = LibMisc.MOD_VERSION)
public class MFFS {


    @SidedProxy(serverSide = LibMisc.COMMON_PROXY,clientSide = LibMisc.CLIENT_PROXY)
    public static MFFSCommonProxy proxy;
    public static Logger log;
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        log=event.getModLog();
        proxy.setupBlocks();
    }


    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        GameRegistry.registerWorldGenerator(new MFFSOreGen(),0);
    }

}
