package net.newgaea.mffs.common.items.linkcards;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.newgaea.mffs.api.IForceEnergyStorageBlock;
import net.newgaea.mffs.api.IPowerLinkItem;
import net.newgaea.mffs.common.data.Grid;
import net.newgaea.mffs.common.tiles.TileCapacitor;
import net.newgaea.mffs.common.tiles.TileNetwork;

public class ItemPowerLinkCard extends ItemCard  implements IPowerLinkItem {


    public ItemPowerLinkCard(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entity, itemSlot, isSelected);
        if(this.Tick > 600) {
            int cap_id=this.getValueFromKey("capacitorID",stack);
            if(cap_id!=0) {
                TileCapacitor cap=Grid.getWorldGrid(entity.getEntityWorld()).getCapacitors().get(cap_id);
                if(cap!=null) {
                    if(!cap.getDeviceName().equals(this.getAreaName(stack)))
                        this.setAreaName(stack,cap.getDeviceName());
                }
            }
            Tick=0;
        }
        Tick++;
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {

        return super.onEntityItemUpdate(stack, entity);
    }


    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {

        if(!context.getWorld().isRemote) {
            TileEntity tileEntity = context.getWorld().getTileEntity(context.getPos());
            // todo: auto insert
        }
        return ActionResultType.PASS;
    }

    public IForceEnergyStorageBlock getForceEnergyStorageBlock(ItemStack stack, TileNetwork tem, World world) {
        if(!stack.isEmpty() && stack.getItem() instanceof ItemCard) {
            if(((ItemCard)stack.getItem()).isValid(stack)) {
                BlockPos pos=this.getCardTargetPoint(stack);
                if(pos!=null) {
                    if(world.getTileEntity(pos) instanceof TileCapacitor) {
                        TileCapacitor cap= (TileCapacitor) world.getTileEntity(pos);
                        int cap_id=this.getValueFromKey("capacitorID",stack);
                        if(cap.getPowerStorageID() == cap_id && cap_id!=0) {
                            if(!cap.getDeviceName().equals(ItemCard.getAreaName(stack))) {
                                ItemCard.setAreaName(stack,cap.getDeviceName());
                            }
                            if(Math.pow(cap.getTransmitRange(),2) >= tem.getPos().distanceSq(cap.getPos()))
                                return cap;
                            return null;
                        }
                    }
                    else {
                        int cap_id=this.getValueFromKey("capacitorID",stack);
                        if(cap_id!=0) {
                            TileCapacitor cap=Grid.getWorldGrid(world).getCapacitors().get(cap_id);
                            if(cap!=null) {
                                ((ItemCard)this).setInformation(stack,cap.getPos(),"capacitorID",cap_id,world);
                                if(Math.pow(cap.getTransmitRange(),2) >= tem.getPos().distanceSq(cap.getPos()))
                                    return cap;
                                return null;
                            }
                        }
                    }
                    if(world.getChunkProvider().isChunkLoaded(new ChunkPos(pos)))
                        ((ItemCard)stack.getItem()).setInvalid(stack);
                }
            }
        }
        return null;
    }

    @Override
    public int getAvailablePower(ItemStack stack, TileNetwork machine, World world) {
        IForceEnergyStorageBlock storage=getForceEnergyStorageBlock(stack,machine,world);
        if(storage!=null)
            return storage.getStorageAvailablePower();
        return 0;
    }

    @Override
    public int getMaximumPower(ItemStack stack, TileNetwork machine, World world) {
        IForceEnergyStorageBlock storage=getForceEnergyStorageBlock(stack,machine,world);
        if(storage!=null)
            return storage.getStorageMaxPower();
        return 0;
    }
    @Override
    public int getPowersourceID(ItemStack stack, TileNetwork machine, World world) {
        IForceEnergyStorageBlock storage=getForceEnergyStorageBlock(stack,machine,world);
        if(storage!=null)
            return storage.getPowerStorageID();
        return 0;
    }

    @Override
    public int getPercentageCapacity(ItemStack stack, TileNetwork machine, World world) {
        IForceEnergyStorageBlock storage=getForceEnergyStorageBlock(stack,machine,world);
        if(storage!=null)
            return storage.getPercentageStorageCapacity();
        return 0;
    }




    @Override
    public boolean consumePower(ItemStack stack, int powerAmount, boolean simulation, TileNetwork machine, World world) {
        IForceEnergyStorageBlock storage=getForceEnergyStorageBlock(stack,machine,world);
        if(storage!=null)
            return storage.consumePowerfromStorage(powerAmount, simulation);
        return false;
    }

    @Override
    public boolean insertPower(ItemStack stack, int powerAmount, boolean simulation, TileNetwork machine, World world) {
        IForceEnergyStorageBlock storage=getForceEnergyStorageBlock(stack,machine,world);
        if(storage!=null)
            return storage.insertPowertoStorage(powerAmount, simulation);
        return false;
    }


    @Override
    public int getFreeStorageAmount(ItemStack stack, TileNetwork machine, World world) {
        IForceEnergyStorageBlock storage=getForceEnergyStorageBlock(stack,machine,world);
        if(storage!=null)
            return storage.getfreeStorageAmount();
        return 0;
    }

    @Override
    public boolean isPowerSourceItem() {
        return false;
    }


}
