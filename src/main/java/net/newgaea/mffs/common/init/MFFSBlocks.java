package net.newgaea.mffs.common.init;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.newgaea.mffs.common.blocks.BlockGenerator;
import net.newgaea.mffs.common.libs.LibBlocks;
import net.newgaea.mffs.common.libs.LibMisc;
import net.newgaea.mffs.common.misc.ItemGroupMFFS;

public class MFFSBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, LibMisc.MOD_ID);

    public static void init()
    {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Block> MONAZIT_ORE = BLOCKS.register(LibBlocks.MONAZIT_ORE,() -> new Block(
            AbstractBlock.Properties.create(Material.ROCK)
            .hardnessAndResistance(5.0f,6.0f)
            .sound(SoundType.STONE)
            .harvestLevel(1)
            .harvestTool(ToolType.PICKAXE)
            )
    );

    public static final RegistryObject<Block> GENERATOR = BLOCKS.register(LibBlocks.GENERATOR, () -> new BlockGenerator(
            AbstractBlock.Properties.create(Material.IRON)
            .hardnessAndResistance(5.0f,6.0f)
            .sound(SoundType.METAL)
            .harvestLevel(1)
            .harvestTool(ToolType.PICKAXE)
    ));
}
