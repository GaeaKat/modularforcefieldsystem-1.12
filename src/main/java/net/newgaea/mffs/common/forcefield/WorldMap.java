package net.newgaea.mffs.common.forcefield;

import com.google.common.collect.MapMaker;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.newgaea.mffs.common.misc.EnumFieldType;
import org.jetbrains.annotations.Contract;

import java.util.Hashtable;
import java.util.Map;

public class WorldMap {
    private static Map ForceFieldWorlds = new MapMaker().weakKeys().makeMap();
    public static class ForceFieldWorld {
        private static Map<Integer,ForceFieldBlockStack> ForceFieldStackMap = new Hashtable<>();

        public ForceFieldBlockStack getOrCreateForceFieldStack(BlockPos pos, World world) {
            if(ForceFieldStackMap.get(pos.hashCode()) ==null) {
                ForceFieldStackMap.put(pos.hashCode(),new ForceFieldBlockStack(pos));
            }
            return ForceFieldStackMap.get(pos.hashCode());
        }
        public ForceFieldBlockStack getForceFieldBlockStack(Integer hash) {
            return ForceFieldStackMap.get(hash);
        }

        public ForceFieldBlockStack getForceFieldBlockStack(BlockPos pos) {
            return ForceFieldStackMap.get(pos.hashCode());
        }

        public int isExistingForceFieldStack(BlockPos pos, int counter, Direction dir, World world) {
            BlockPos newPos;
            switch(dir) {
                case UP:
                    newPos=new BlockPos(pos.getX(),pos.getY()+counter,pos.getZ());
                    break;
                case DOWN:
                    newPos=new BlockPos(pos.getX(),pos.getY()-counter,pos.getZ());
                    break;
                case NORTH:
                    newPos=new BlockPos(pos.getX(),pos.getY(),pos.getZ()+counter);
                    break;
                case SOUTH:
                    newPos=new BlockPos(pos.getX(),pos.getY(),pos.getZ()-counter);
                    break;
                case WEST:
                    newPos=new BlockPos(pos.getX()+counter,pos.getY(),pos.getZ());
                    break;
                case EAST:
                    newPos=new BlockPos(pos.getX()-counter,pos.getY(),pos.getZ());
                default:
                    newPos=new BlockPos(pos);
            }
            ForceFieldBlockStack Map=ForceFieldStackMap.get(newPos.hashCode());
            if(Map == null) {
                return 0;
            }
            else {
                if(Map.isEmpty()) {
                    return 0;
                }
                return Map.getCapacitorID();
            }
        }
    }




    @Contract("null -> null")
    public static ForceFieldWorld getForceFieldWorld(World world) {
        ForceFieldWorld result = null;
        if (world != null) {
            if (!ForceFieldWorlds.containsKey(world)) {
                ForceFieldWorlds.put(world, new ForceFieldWorld());
            }
            result = (ForceFieldWorld) ForceFieldWorlds.get(world);
        }
        return result;
    }
}
