package com.nekokittygames.mffs.common.inventory;

import com.nekokittygames.mffs.common.tiles.MFFSTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Created by Katrina on 19/04/2016.
 */
public abstract class MFFSMachineContainer extends Container {

    protected MFFSTile inventory;


    public MFFSMachineContainer(InventoryPlayer player,MFFSTile inv)
    {
        inventory=inv;
        for(int i=0;i<inv.upgradeInventorySlots.getSlots();i++) {
            addSlotToContainer(new SlotItemHandler(inv.upgradeInventorySlots,i,0,0 ));
        }

        bindPlayerInventory(player);
    }


    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
                        8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
    }
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
