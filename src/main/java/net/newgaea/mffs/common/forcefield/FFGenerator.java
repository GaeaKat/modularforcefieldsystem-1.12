package net.newgaea.mffs.common.forcefield;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.newgaea.mffs.api.IModularProjector;
import net.newgaea.mffs.common.blocks.BlockForceField;
import net.newgaea.mffs.common.blocks.ModBlock;
import net.newgaea.mffs.common.config.MFFSConfig;
import net.newgaea.mffs.common.init.MFFSBlocks;
import net.newgaea.mffs.common.init.MFFSItems;
import net.newgaea.mffs.common.misc.EnumFieldType;
import net.newgaea.mffs.common.tiles.TileForcefield;
import net.newgaea.mffs.common.tiles.TileProjector;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class FFGenerator implements INBTSerializable<CompoundNBT> {
    private IModularProjector projector;
    private List<BlockPos> forcefieldBlocks;
    private int seed;
    public FFGenerator(IModularProjector projector, List<BlockPos> forcefieldBlocks, int seed) {
        this.projector = projector;
        this.forcefieldBlocks = forcefieldBlocks;
        this.seed = seed;
        Random rnd=new Random(seed);

        Collections.shuffle(forcefieldBlocks,rnd);
    }

    public int GenerateBlocks() {
        int generated = 0;
        for (BlockPos forcefieldBlock : forcefieldBlocks) {
            BlockPos curPos = forcefieldBlock.add(projector.getPos());
            if (projector.getWorld().getBlockState(curPos).getBlock() != MFFSBlocks.FORCEFIELD.get()) {
                if (projector.getWorld().isAirBlock(curPos))
                {
                    if(projector.takeEnergy(MFFSConfig.BASE_FORCEFIELD_COST.get()))
                    {
                    projector.getWorld().setBlockState(curPos, MFFSBlocks.FORCEFIELD.getDefaultState());
                    generated++;
                    TileEntity ent = projector.getWorld().getTileEntity(curPos);
                    if (ent instanceof TileForcefield) {
                        TileForcefield tileForcefield = (TileForcefield) ent;
                        tileForcefield.setFieldType(EnumFieldType.Default);
                    }}
                }
                else if(projector.hasOption(MFFSItems.BLOCK_BREAKER_OPTION.get())) {
                    if(projector.takeEnergy(MFFSConfig.BASE_FORCEFIELD_COST.get())) {
                        projector.getWorld().removeBlock(curPos, false);
                    }
                }
            }

            if (generated >= MFFSConfig.FORCEFIELD_PER_TICK.get())
                break;
        }
        return generated;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT cmp=new CompoundNBT();
        ListNBT list = new ListNBT();
        for(BlockPos blockPos:forcefieldBlocks) {
            list.add(NBTUtil.writeBlockPos(blockPos));
        }
        cmp.put("forcefields",list);
        cmp.putInt("seed",seed);
        return cmp;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ListNBT listNBT = nbt.getList("forcefields", Constants.NBT.TAG_COMPOUND);
        forcefieldBlocks.clear();
        for(int i=0;i<listNBT.size();i++) {
            forcefieldBlocks.add(NBTUtil.readBlockPos(listNBT.getCompound(i)));

        }
        this.seed = nbt.getInt("seed");
    }
}
