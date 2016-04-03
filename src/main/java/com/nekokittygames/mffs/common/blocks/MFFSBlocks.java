package com.nekokittygames.mffs.common.blocks;

import com.nekokittygames.mffs.common.blocks.WorldGen.MonazitOre;
import com.nekokittygames.mffs.common.libs.LibBlockNames;
import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by katsw on 03/04/2016.
 */
public class MFFSBlocks {

    public static MonazitOre monazit_ore;


    public static void createBlocks()
    {
        monazit_ore= (MonazitOre) new MonazitOre().setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName(LibBlockNames.MONAZIT_ORE);



        registerBlocks();
    }

    public static void registerBlocks()
    {
        GameRegistry.registerBlock(monazit_ore,LibBlockNames.MONAZIT_ORE);
    }

    public static void setupClientBlocks()
    {
        setupClientBlock(monazit_ore,LibBlockNames.MONAZIT_ORE);
    }

    public static void setupClientBlock(Block block,String name)
    {
        Item itemBlockSimple = GameRegistry.findItem("mffs", name);
        ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("mffs:"+name, "inventory");
        final int DEFAULT_ITEM_SUBTYPE = 0;
        ModelLoader.setCustomModelResourceLocation(itemBlockSimple, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
    }
}
