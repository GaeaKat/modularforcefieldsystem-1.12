package com.nekokittygames.mffs.common.blocks;

import com.nekokittygames.mffs.common.blocks.machines.Capacitor;
import com.nekokittygames.mffs.common.blocks.machines.MFFSMachineFaced;
import com.nekokittygames.mffs.common.blocks.worldgen.MonazitOre;
import com.nekokittygames.mffs.common.libs.LibBlockNames;
import com.nekokittygames.mffs.common.tiles.TileCapacitor;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by katsw on 03/04/2016.
 */
public class MFFSBlocks {

    public static MonazitOre monazit_ore;
    public static Capacitor capacitor;


    public static void createBlocks()
    {
        monazit_ore= (MonazitOre) new MonazitOre().setHardness(3.0F).setResistance(5.0F).setUnlocalizedName(LibBlockNames.MONAZIT_ORE);
        capacitor=(Capacitor)new Capacitor().setHardness(3.0F).setUnlocalizedName(LibBlockNames.MFFS_CAPACITOR);


        registerBlocks();
    }

    public static void registerBlocks()
    {
        GameRegistry.registerBlock(monazit_ore,LibBlockNames.MONAZIT_ORE);
        GameRegistry.registerBlock(capacitor,LibBlockNames.MFFS_CAPACITOR);



        setupTileEntities();
    }


    public static void setupTileEntities()
    {
        GameRegistry.registerTileEntity(TileCapacitor.class,LibBlockNames.MFFS_CAPACITOR);
    }

    public static void setupClientBlocks()
    {
        setupClientBlock(monazit_ore,LibBlockNames.MONAZIT_ORE);
        setupClientBlock(capacitor,LibBlockNames.MFFS_CAPACITOR);
    }

    public static void setupClientBlock(Block block,String name)
    {
        if(block instanceof MFFSMachineFaced)
        {
            setupMachineClient((MFFSMachineFaced) block,name);
            return;
        }
        Item itemBlockSimple = GameRegistry.findItem("mffs", name);
        ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("mffs:"+name, "inventory");
        final int DEFAULT_ITEM_SUBTYPE = 0;
        ModelLoader.setCustomModelResourceLocation(itemBlockSimple, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
    }


    public static void setupMachineClient(MFFSMachineFaced block,String name)
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block),0,new ModelResourceLocation("mffs:"+name, "active=false,facing=north"));
    }
}
