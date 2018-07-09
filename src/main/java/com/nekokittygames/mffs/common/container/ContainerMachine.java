package com.nekokittygames.mffs.common.container;

import java.util.function.Predicate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.nekokittygames.mffs.common.tileentity.TileEntityMachines;

public class ContainerMachine extends Container {
	private final Predicate<EntityPlayer> interactionCheck;
	private final EntityPlayer player;
	
	public ContainerMachine(EntityPlayer player, TileEntityMachines machine) {
		interactionCheck = machine::isUsableByPlayer;
		this.player = player;
	}

	public EntityPlayer getPlayer() {
		return player;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return interactionCheck.test(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack movedStack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack slotStack = slot.getStack();
			movedStack = slotStack.copy();
			
			if (slotStack.getCount() <= 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			
			if (slotStack.getCount() != movedStack.getCount()) {
				slot.onSlotChanged();
			} else {
				return ItemStack.EMPTY;
			}
		}
		
		return movedStack;
	}
}