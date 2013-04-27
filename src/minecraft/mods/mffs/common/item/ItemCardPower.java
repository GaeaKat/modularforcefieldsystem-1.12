package mods.mffs.common.item;

import java.util.List;

import mods.mffs.api.IForceEnergyItems;
import mods.mffs.api.IPowerLinkItem;
import mods.mffs.common.tileentity.TileEntityMachines;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCardPower extends ItemMFFSBase implements IPowerLinkItem,
		IForceEnergyItems {

	public ItemCardPower(int id) {
		super(id);
		setMaxStackSize(1);
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("mffs:PowerCard");
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public int getPercentageCapacity(ItemStack itemStack,
			TileEntityMachines tem, World world) {
		return 100;
	}

	@Override
	public int getAvailablePower(ItemStack itemStack, TileEntityMachines tem,
			World world) {
		return 10000000;
	}

	@Override
	public int getMaximumPower(ItemStack itemStack, TileEntityMachines tem,
			World world) {
		return 10000000;
	}

	@Override
	public boolean consumePower(ItemStack itemStack, int powerAmount,
			boolean simulation, TileEntityMachines tem, World world) {
		return true;
	}

	@Override
	public boolean insertPower(ItemStack itemStack, int powerAmount,
			boolean simulation, TileEntityMachines tem, World world) {
		return false;
	}

	@Override
	public int getPowersourceID(ItemStack itemStack, TileEntityMachines tem,
			World world) {
		return 0;
	}

	@Override
	public int getfreeStorageAmount(ItemStack itemStack,
			TileEntityMachines tem, World world) {
		return 0;
	}

	@Override
	public boolean isPowersourceItem() {
		return true;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,
			List info, boolean b) {

		info.add("Admin Card to Power Maschines");
		info.add("or use to infinit charge Capactior");
	}

	// ForceEnergyItems -> PowerLinkItem compatibility

	@Override
	public int getAvailablePower(ItemStack itemStack) {
		return getAvailablePower(itemStack, null, null);
	}

	@Override
	public int getMaximumPower(ItemStack itemStack) {
		return getMaximumPower(itemStack, null, null);
	}

	@Override
	public boolean consumePower(ItemStack itemStack, int powerAmount,
			boolean simulation) {
		return true;
	}

	@Override
	public void setAvailablePower(ItemStack itemStack, int amount) {
	}

	@Override
	public int getPowerTransferrate() {
		return 1000000;
	}

	@Override
	public int getItemDamage(ItemStack stackInSlot) {
		return 0;
	}

}
