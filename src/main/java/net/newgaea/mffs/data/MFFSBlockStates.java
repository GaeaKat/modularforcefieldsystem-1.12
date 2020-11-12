package net.newgaea.mffs.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.newgaea.mffs.common.blocks.BlockSimpleNetwork;
import net.newgaea.mffs.common.init.MFFSBlocks;
import net.newgaea.mffs.common.libs.LibBlocks;
import net.newgaea.mffs.common.libs.LibMisc;

public class MFFSBlockStates extends BlockStateProvider {


    public MFFSBlockStates(DataGenerator gen,  ExistingFileHelper exFileHelper) {
        super(gen, LibMisc.MOD_ID, exFileHelper);
    }


    @Override
    protected void registerStatesAndModels() {
        registerOres();
        registerBasicMachines();


    }

    private void registerBasicMachines() {
        registerBasicMachine(MFFSBlocks.GENERATOR.get(),LibBlocks.GENERATOR,mcLoc("block/furnace_front"),mcLoc("block/furnace_side"),mcLoc("block/furnace_top"));
        registerSimpleActivatableMachine(MFFSBlocks.CAPACITOR.get(),LibBlocks.CAPACITOR);
    }

    protected void registerBasicMachine(Block block,String blockname,ResourceLocation facePic,ResourceLocation otherPic,ResourceLocation topFace)
    {
        ModelFile model=models().orientable(blockname,otherPic,facePic,topFace);
        this.horizontalBlock(block,model);
        this.simpleBlockItem(block,model);
    }

    protected void registerSimpleActivatableMachine(Block block, String blockname) {
        ModelFile off = models().orientable(blockname+"_off",modLoc("block/"+blockname+"/side_inactive"),modLoc("block/"+blockname+"/face_inactive"),modLoc("block/"+blockname+"/side_inactive"));
        ModelFile on = models().orientable(blockname+"_on",modLoc("block/"+blockname+"/side_active"),modLoc("block/"+blockname+"/face_active"),modLoc("block/"+blockname+"/side_active"));
        this.horizontalBlock(block,blockState -> blockState.get(BlockSimpleNetwork.ACTIVE) ?on:off);
        this.simpleBlockItem(block,off);

    }

    private void registerOres() {
        registerFullBlock(MFFSBlocks.MONAZIT_ORE.get(), LibBlocks.MONAZIT_ORE,modLoc("block/monazit_ore"));
    }

    protected void registerFullBlock(Block block, String blockName, ResourceLocation texture)
    {
        BlockModelBuilder model=models().cubeAll(blockName, texture);
        simpleBlock(block,model);
        simpleBlockItem(block,model);
    }
}
