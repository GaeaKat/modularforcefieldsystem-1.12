package net.newgaea.mffs.common.forcefield;

import net.minecraft.util.math.BlockPos;
import net.newgaea.mffs.common.misc.EnumFieldType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ForceFieldBlockStack {
    private BlockPos pos;
    private boolean sync;
    public Queue<ForceFieldBlockInfo> blocks=new LinkedList<>();

    public ForceFieldBlockStack(BlockPos pos) {
        this.pos = pos;
        sync=false;
    }

    public int getSize() { return blocks.size();}
    public void removeBlock() {
        blocks.poll();
    }

    public synchronized void removeByProjector(int projector_id) {
        ArrayList<ForceFieldBlockInfo> tempblock=new ArrayList<>();
        for(ForceFieldBlockInfo ffBlock : blocks) {
            if(ffBlock.getProjector_id() == projector_id)
                tempblock.add(ffBlock);
        }
        if(!tempblock.isEmpty())
            blocks.removeAll(tempblock);
    }

    public int getCapacitorID() {
        ForceFieldBlockInfo ffBlock = blocks.peek();
        if(ffBlock!=null)
            return ffBlock.getCapacitor_id();
        return 0;
    }

    public int getProjectorID() {
        ForceFieldBlockInfo ffBlock = blocks.peek();
        if(ffBlock!=null)
            return ffBlock.getProjector_id();
        return 0;
    }
    public EnumFieldType getType() {
        ForceFieldBlockInfo ffBlock = blocks.peek();
        if(ffBlock!=null)
            return ffBlock.getType();
        return EnumFieldType.Default;
    }
    public boolean isSync() {
        return  sync;
    }
    public void setSync(boolean sync) {
        this.sync=sync;
    }
    public boolean isEmpty() { return blocks.isEmpty();}
    public ForceFieldBlockInfo get() { return blocks.peek();}
    public void add(int capacitor_id,int projector_id,EnumFieldType type) {
        blocks.offer(new ForceFieldBlockInfo(type, projector_id, capacitor_id));
    }

    public BlockPos getPos() {
        return pos;
    }
}
