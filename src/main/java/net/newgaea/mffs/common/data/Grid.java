package net.newgaea.mffs.common.data;

import com.google.common.collect.MapMaker;
import net.minecraft.world.World;
import net.newgaea.mffs.common.blocks.BlockCapacitor;
import net.newgaea.mffs.common.blocks.BlockExtractor;
import net.newgaea.mffs.common.blocks.BlockProjector;
import net.newgaea.mffs.common.libs.LibMachines;
import net.newgaea.mffs.common.tiles.TileCapacitor;
import net.newgaea.mffs.common.tiles.TileExtractor;
import net.newgaea.mffs.common.tiles.TileNetwork;
import net.newgaea.mffs.common.tiles.TileProjector;

import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

public final class Grid {
    private static Map WorldMap =new MapMaker().weakKeys().makeMap();

    public static class WorldGrid {
        private Map<Integer, TileProjector> projectors=new Hashtable<>();
        private Map<Integer, TileCapacitor> capacitors=new Hashtable<>();
        private Map<Integer, TileExtractor> extractors = new Hashtable<>();

        // todo: fill in here as machines made

        // Options
        private Map<Integer, TileProjector> jammers=new Hashtable<>();
        private Map<Integer, TileProjector> fusions=new Hashtable<>();


        public Map<Integer, TileProjector> getProjectors() {
            return projectors;
        }

        public Map<Integer, TileCapacitor> getCapacitors() {
            return capacitors;
        }

        public Map<Integer,TileExtractor> getExtractors() { return extractors;}

        public Map<Integer, TileProjector> getJammers() {
            return jammers;
        }

        public Map<Integer, TileProjector> getFusions() {
            return fusions;
        }

        public int refreshID(TileNetwork tileEntity,int remDeviceID) {
            Random rand=new Random();
            int DeviceId= rand.nextInt();

            if(tileEntity instanceof TileCapacitor) {
                if(remDeviceID==0) {
                    while (capacitors.get(DeviceId)!=null) {
                        DeviceId= rand.nextInt();
                    }
                } else {
                    DeviceId = remDeviceID;
                }
                capacitors.put(DeviceId, (TileCapacitor) tileEntity);
                return DeviceId;
            }
            if(tileEntity instanceof TileProjector) {
                if(remDeviceID==0) {
                    while (projectors.get(DeviceId)!=null) {
                        DeviceId= rand.nextInt();
                    }
                } else {
                    DeviceId = remDeviceID;
                }
                projectors.put(DeviceId, (TileProjector) tileEntity);
                return DeviceId;
            }
            if(tileEntity instanceof TileExtractor) {
                if(remDeviceID==0) {
                    while (extractors.get(DeviceId)!=null) {
                        DeviceId= rand.nextInt();
                    }
                } else {
                    DeviceId = remDeviceID;
                }
                extractors.put(DeviceId, (TileExtractor) tileEntity);
                return DeviceId;
            }
            // todo Add here as more machines ported
            return 0;
        }

        public int connectedToCapacitor(TileCapacitor cap,float range) {
            int counter = 0;
            for(TileCapacitor tileEntity : capacitors.values()) {
                if(tileEntity.getPowerSourceID() == cap.getPowerStorageID()) {
                    if(range>=tileEntity.getPos().distanceSq(cap.getPos()))
                        counter++;
                }
            }
            for(TileProjector tileEntity: projectors.values()) {
                if(tileEntity.getPowerSourceID() == cap.getPowerStorageID()) {
                    if(range >= tileEntity.getPos().distanceSq(cap.getPos()))
                        counter++;
                }
            }
            for(TileExtractor tileEntity: extractors.values()) {
                if(tileEntity.getPowerSourceID() == cap.getPowerStorageID()) {
                    if(range>= tileEntity.getPos().distanceSq(cap.getPos()))
                        counter++;
                }
            }
            // todo other machines
            return counter;
        }

        public TileNetwork getTileMachines(String displayName,int key) {
            Class tem= LibMachines.getMachines().get(displayName);
            if(tem!=null) {
                if(tem.equals(BlockProjector.class)) {
                    return getProjectors().get(key);
                }
                else if(tem.equals(BlockCapacitor.class)) {
                    return getCapacitors().get(key);
                }
                else if(tem.equals(BlockExtractor.class)) {
                    return getExtractors().get(key);
                }
            }
            return null;
        }
    }
    public static WorldGrid getWorldGrid(World world) {
        if(world!=null) {
            if(!WorldMap.containsKey(world))
                WorldMap.put(world,new WorldGrid());
            return (WorldGrid) WorldMap.get(world);
        }
        return null;
    }
}
