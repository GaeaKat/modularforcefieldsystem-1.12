/*
    Copyright (C) 2012 Thunderdark

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

    Contributors:
    Thunderdark - initial implementation
 */

package com.nekokittygames.mffs.common.tileentity;


import com.nekokittygames.mffs.api.IPowerLinkItem;
import com.nekokittygames.mffs.api.PointXYZ;
import com.nekokittygames.mffs.common.*;
import com.nekokittygames.mffs.common.container.ContainerAreaDefenseStation;
import com.nekokittygames.mffs.common.item.ItemCardSecurityLink;
import com.nekokittygames.mffs.common.item.ItemProjectorFieldModulatorDistance;
import com.nekokittygames.mffs.common.item.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;

public class TileEntityAreaDefenseStation extends TileEntityFEPoweredMachine
		implements ISidedInventory {
	private int capacity;
	private int distance;
	private int contratyp;
	private int actionmode;
	private int scanmode;

	protected List<EntityPlayer> warnlist = new ArrayList<EntityPlayer>();
	protected List<EntityPlayer> actionlist = new ArrayList<EntityPlayer>();
	protected List<EntityLivingBase> NPClist = new ArrayList<EntityLivingBase>();
	private final ArrayList<Item> ContraList = new ArrayList<Item>();

	public TileEntityAreaDefenseStation() {
		super(36);

		capacity = 0;
		contratyp = 1;
		actionmode = 0;
		scanmode = 1;
	}

	// Start Getter AND Setter

	public int getScanmode() {
		return scanmode;
	}

	public void setScanmode(int scanmode) {
		this.scanmode = scanmode;
	}

	public int getActionmode() {
		return actionmode;
	}

	public void setActionmode(int actionmode) {
		this.actionmode = actionmode;
	}

	public int getcontratyp() {
		return contratyp;
	}

	public void setcontratyp(int a) {
		contratyp = a;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int Capacity) {
		this.capacity = Capacity;
	}

	public int getActionDistance() {
		if (!getStackInSlot(3).isEmpty()) {
			if (getStackInSlot(3).getItem() == ModItems.PROJECTOR_DISTANCE) {
				return (getStackInSlot(3).getCount());
			}
		}
		return 0;

	}

	public int getInfoDistance() {
		if (!getStackInSlot(2).isEmpty()) {
			if (getStackInSlot(2).getItem() == ModItems.PROJECTOR_DISTANCE) {
				return getActionDistance() + (getStackInSlot(2).getCount()+ 3);
			}
		}
		return getActionDistance() + 3;
	}

	public boolean hasSecurityCard() {
		if (!getStackInSlot(1).isEmpty()) {
			if (getStackInSlot(1).getItem() == ModItems.SECURITYLINK_CARD) {
				return true;
			}
		}
		return false;
	}

	@Override
	public TileEntityAdvSecurityStation getLinkedSecurityStation() {
		return ItemCardSecurityLink.getLinkedSecurityStation(this, 1, world);
	}

	@Override
	public void invalidate() {
		Linkgrid.getWorldMap(world).getDefStation().remove(getDeviceID());
		super.invalidate();
	}

	// Start NBT Read/ Save
    @Override
    public void readExtraNBT(NBTTagCompound nbttagcompound) {
        super.readExtraNBT(nbttagcompound);
        contratyp = nbttagcompound.getInteger("contratyp");
        actionmode = nbttagcompound.getInteger("actionmode");
        scanmode = nbttagcompound.getInteger("scanmode");
    }

    @Override
    public void writeExtraNBT(NBTTagCompound nbttagcompound) {
        super.writeExtraNBT(nbttagcompound);
        nbttagcompound.setInteger("contratyp", contratyp);
        nbttagcompound.setInteger("actionmode", actionmode);
        nbttagcompound.setInteger("scanmode", scanmode);
    }

    @Override
	public void dropPlugins() {
		for (int a = 0; a < this.inventory.size(); a++) {
			dropPlugins(a);
		}
	}

	public void scanner() {
		try {

			TileEntityAdvSecurityStation sec = getLinkedSecurityStation();

			if (sec != null && sec.isSecurityEstablished()) {
				int xmininfo = pos.getX() - getInfoDistance();
				int xmaxinfo = pos.getX() + getInfoDistance() + 1;
				int ymininfo = pos.getY() - getInfoDistance();
				int ymaxinfo = pos.getY() + getInfoDistance() + 1;
				int zmininfo = pos.getZ() - getInfoDistance();
				int zmaxinfo = pos.getZ() + getInfoDistance() + 1;

				int xminaction = pos.getX() - getActionDistance();
				int xmaxaction = pos.getX() + getActionDistance() + 1;
				int yminaction = pos.getY() - getActionDistance();
				int ymaxaction = pos.getY() + +getActionDistance() + 1;
				int zminaction = pos.getZ() - getActionDistance();
				int zmaxaction = pos.getZ() + getActionDistance() + 1;

				List<EntityLivingBase> infoLivinglist = world
						.getEntitiesWithinAABB(EntityLivingBase.class,
								new AxisAlignedBB(xmininfo,
										ymininfo, zmininfo, xmaxinfo, ymaxinfo,
										zmaxinfo));
				List<EntityLivingBase> actionLivinglist = world
						.getEntitiesWithinAABB(EntityLivingBase.class,
								new AxisAlignedBB(xminaction,
										yminaction, zminaction, xmaxaction,
										ymaxaction, zmaxaction));

				for (EntityLivingBase Living : infoLivinglist) {
					if (Living instanceof EntityPlayer) {
						EntityPlayer player = (EntityPlayer) Living;
						int distance = (int) PointXYZ.distance(
								getMaschinePoint(), new PointXYZ(Living.getPosition(),world));

						if (distance > getInfoDistance()
								&& this.getScanmode() == 1) {
							continue;
						}

						if (!warnlist.contains(player)) {
							warnlist.add(player);
							if (!sec.isAccessGranted(player.getUniqueID().toString(),
									SecurityRight.SR)) {
								if (!(ModularForceFieldSystem.DefenceStationNPCScannsuppressnotification &&
										getActionmode() < 3)) {

									player.sendMessage(new TextComponentTranslation("securityStation.scanningRange",
											sec.getDeviceName()));

									if(getActionmode() == 1)
										player.attackEntityFrom(MFFSDamageSource.areaDefense, 1);
								}
							}
						}
					}
				}

				for (EntityLivingBase Living : actionLivinglist) {
					if (Living instanceof EntityPlayer) {
						EntityPlayer player = (EntityPlayer) Living;

						int distance = (int) Math.round(PointXYZ.distance(
								getMaschinePoint(), new PointXYZ(Living.getPosition(), world)));
						if (distance > getActionDistance()
								&& this.getScanmode() == 1)
							continue;

						if (!actionlist.contains(player)) {
							actionlist.add(player);
							DefenceAction(player);
						}

					} else {

						int distance = (int) Math.round(PointXYZ.distance(
								getMaschinePoint(), new PointXYZ(Living.getPosition(), world)));
						if (distance > getActionDistance()
								&& this.getScanmode() == 1)
							continue;

						if (!NPClist.contains(Living)) {
							NPClist.add(Living);
							DefenceAction(Living);
						}

					}
				}

				for (int i = 0; i < actionlist.size(); i++) {
					if (!actionLivinglist.contains(actionlist.get(i)))
						actionlist.remove(actionlist.get(i));
				}

				for (int i = 0; i < warnlist.size(); i++) {

					if (!infoLivinglist.contains(warnlist.get(i)))
						warnlist.remove(warnlist.get(i));
				}

			}
		} catch (Exception ex) {
			System.err
					.println("[ModularForceFieldSystem] catch  Crash <TileEntityAreaDefenseStation:scanner> ");
		}

	}

	public void DefenceAction() {

		for (int i = 0; i < actionlist.size(); i++) {
			DefenceAction(actionlist.get(i));
		}

	}

	public boolean StacksToInventory(IInventory inventory,
			ItemStack itemstacks, boolean loop) {

		int count = 0;

		if (inventory instanceof TileEntitySecStorage)
			count = 1;

		if (inventory instanceof TileEntityAreaDefenseStation)
			count = 15;

		for (int a = count; a <= inventory.getSizeInventory() - 1; a++) {
			if (inventory.getStackInSlot(a).isEmpty()) {
				inventory.setInventorySlotContents(a, itemstacks);
				return true;
			} else {
				if (inventory.getStackInSlot(a).getItem() == itemstacks
						.getItem()
						&& inventory.getStackInSlot(a).getItemDamage() == itemstacks
								.getItemDamage()
						&& inventory.getStackInSlot(a).getCount() < inventory
								.getStackInSlot(a).getMaxStackSize()) {
					int free = inventory.getStackInSlot(a).getMaxStackSize()
							- inventory.getStackInSlot(a).getCount();

					if (free > itemstacks.getCount()) {
						inventory.getStackInSlot(a).setCount(inventory.getStackInSlot(a).getCount()+ itemstacks.getCount());;
						return true;
					} else {
						inventory.getStackInSlot(a).setCount(inventory
								.getStackInSlot(a).getMaxStackSize());
						itemstacks.setCount(itemstacks.getCount()- free);
						continue;
					}

				}
			}

		}
		if (loop)
			addremoteInventory(itemstacks);

		return false;

	}

	public void addremoteInventory(ItemStack itemstacks) {
		IInventory inv = InventoryHelper.findAttachedInventory(world,
				pos);
		if (inv != null) {
			StacksToInventory(inv, itemstacks, false);
		}
	}

	public void DefenceAction(EntityLivingBase Living) {
		if (Living instanceof EntityPlayer)
			return;

		TileEntityAdvSecurityStation sec = getLinkedSecurityStation();

		if (hasPowerSource()) {

			if (sec != null && sec.isSecurityEstablished()) {
				if(this.consumePower(ModularForceFieldSystem.DefenceStationKillForceEnergy, true))
				{
					switch (getActionmode()) {
					case 3: // NPC Kill - All
						consumePower(
								ModularForceFieldSystem.DefenceStationKillForceEnergy,
								false);
						Living.setHealth(0);
						NPClist.remove(Living);
						break;

					case 4: // NPC Kill - Hostile
						if (Living instanceof IMob) {
							Living.setHealth(0);
							NPClist.remove(Living);
							consumePower(
									ModularForceFieldSystem.DefenceStationKillForceEnergy,
									false);
						}

						break;
					case 5: // NPC Kill - Friendly
						if (Living instanceof IAnimals) {
							Living.setHealth(0);
							NPClist.remove(Living);
							consumePower(
									ModularForceFieldSystem.DefenceStationKillForceEnergy,
									false);
						}
						break;
					}
				}
			}
		}
	}

	public void DefenceAction(EntityPlayer player) {

		TileEntityAdvSecurityStation sec = getLinkedSecurityStation();

		if (hasPowerSource()) {
			if (sec != null && sec.isSecurityEstablished()) {

				switch (getActionmode()) {
				case 0: // Inform
					if (!sec.isAccessGranted(player.getUniqueID().toString(), SecurityRight.SR)) {
						player.sendMessage(new TextComponentTranslation("areaDefense.getOut"));
					}

					break;
				case 1: // kill

					if (consumePower(
							ModularForceFieldSystem.DefenceStationKillForceEnergy,
							true)) {
						if (!sec.isAccessGranted(player.getUniqueID().toString(),
								SecurityRight.SR)) {
							player.sendMessage(new TextComponentTranslation("areaDefense" +
									".beenWarned"));

							for (int i = 0; i < 4; i++) {
								StacksToInventory(this,
                                        player.inventory.armorInventory.get(i),
                                        true);
								player.inventory.armorInventory.set(i,ItemStack.EMPTY);
							}

							for (int i = 0; i < 36; i++) {

								StacksToInventory(this,
                                        player.inventory.mainInventory.get(i),
                                        true);
								player.inventory.mainInventory.set(i,ItemStack.EMPTY);
							}

							actionlist.remove(player);
							player.attackEntityFrom(MFFSDamageSource.areaDefense, 20);
							player.setHealth(0);
							consumePower(
									ModularForceFieldSystem.DefenceStationKillForceEnergy,
									false);
						}

					}

					break;
				case 2: // search

					if (consumePower(
							ModularForceFieldSystem.DefenceStationSearchForceEnergy,
							true)) {
						if (!sec.isAccessGranted(player.getUniqueID().toString(),
								SecurityRight.AAI)) {
							ContraList.clear();

							for (int place = 5; place < 15; place++) {
								if (!getStackInSlot(place).isEmpty()) {
									ContraList.add(getStackInSlot(place)
											.getItem());
								}
							}

							switch (this.getcontratyp()) {
							case 0:

								for (int i = 0; i < 4; i++) {

										if (!ContraList
												.contains(player.inventory.armorInventory.get(i)
														.getItem())) {
											player.sendMessage(new TextComponentTranslation("areaDefense.illegalItems",
													player.inventory.armorInventory.get(i).getItem().getItemStackDisplayName(player.inventory.armorInventory.get(i))));
											StacksToInventory(
													this,
													player.inventory.armorInventory.get(i),
													true);
											player.inventory.armorInventory.set(i,ItemStack.EMPTY);
											consumePower(
													ModularForceFieldSystem.DefenceStationSearchForceEnergy,
													false);
									}
								}

								for (int i = 0; i < 36; i++) {

									if (!ContraList
                                            .contains(player.inventory.mainInventory.get(i)
                                                    .getItem())) {
                                        player.sendMessage(new TextComponentTranslation("areaDefense.illegalItems",
                                                player.inventory.mainInventory.get(i)
                                                        .getItem()
                                                        .getItemStackDisplayName(
                                                                player.inventory.mainInventory.get(i))));
                                        StacksToInventory(
                                                this,
                                                player.inventory.mainInventory.get(i),
                                                true);
                                        player.inventory.mainInventory.set(i,ItemStack.EMPTY);
                                        consumePower(
                                                ModularForceFieldSystem.DefenceStationSearchForceEnergy,
                                                false);
                                    }
								}

								break;
							case 1:

								for (int i = 0; i < 4; i++) {

									if (ContraList
                                            .contains(player.inventory.armorInventory.get(i)
                                                    .getItem())) {
                                        player.sendMessage(new TextComponentTranslation("areaDefense.illegalItems",
                                                player.inventory.armorInventory.get(i)
                                                        .getItem()
                                                        .getItemStackDisplayName(
                                                                player.inventory.armorInventory.get(i))));
                                        StacksToInventory(
                                                this,
                                                player.inventory.armorInventory.get(i),
                                                true);
                                        player.inventory.armorInventory.set(i,ItemStack.EMPTY);
                                        consumePower(
                                                ModularForceFieldSystem.DefenceStationSearchForceEnergy,
                                                false);
                                    }
								}

								for (int i = 0; i < 36; i++) {

									if (ContraList
                                            .contains(player.inventory.mainInventory.get(i)
                                                    .getItem())) {
                                        player.sendMessage(new TextComponentTranslation("areaDefense.illegalItems",
                                                player.inventory.mainInventory.get(i)
                                                        .getItem()
                                                        .getItemStackDisplayName(
                                                                player.inventory.mainInventory.get(i))));
                                        StacksToInventory(
                                                this,
                                                player.inventory.mainInventory.get(i),
                                                true);
                                        player.inventory.mainInventory.set(i,ItemStack.EMPTY);
                                        consumePower(
                                                ModularForceFieldSystem.DefenceStationSearchForceEnergy,
                                                false);
                                    }
								}

								break;

							}

						}
					}
					break;
				}
			}
		}
	}

	@Override
	public void update() {
		if (world.isRemote == false) {

			if (getSwitchModi() == 1)
				if (!getSwitchValue() && isRedstoneSignal())
					toggelSwitchValue();

			if (getSwitchModi() == 1)
				if (getSwitchValue() && !isRedstoneSignal())
					toggelSwitchValue();

			if (getSwitchValue() && hasPowerSource() && getAvailablePower() > 0
					&& getLinkedSecurityStation() != null && !isActive())
				setActive(true);

			if ((!getSwitchValue()
					|| !hasPowerSource()
					|| getAvailablePower() < ModularForceFieldSystem.DefenceStationScannForceEnergy
							* getInfoDistance() || getLinkedSecurityStation() == null)
					&& isActive())
				setActive(false);

			if (this.isActive()) {
				if (consumePower(
						ModularForceFieldSystem.DefenceStationScannForceEnergy
								* getInfoDistance(), true)) {
					consumePower(
							ModularForceFieldSystem.DefenceStationScannForceEnergy
									* getInfoDistance(), false);
					scanner();
				}
			}

			if (this.getTicker() == 100) {
				if (this.isActive()) {
					DefenceAction();
				}
				this.setTicker((short) 0);
			}
			this.setTicker((short) (this.getTicker() + 1));
		}
		super.update();
	}

	@Override
	public String getName() {
		return "Defstation";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public Container getContainer(InventoryPlayer inventoryplayer) {
		return new ContainerAreaDefenseStation(inventoryplayer.player, this);
	}

	@Override
	public void onNetworkHandlerEvent(int key, String value) {

		if (!this.isActive()) {
			switch (key) {

			case 100:

				if (this.getcontratyp() == 0) {
					this.setcontratyp(1);
				} else {
					this.setcontratyp(0);
				}
				break;
			case 101:
				if (getActionmode() == 5) {
					setActionmode(0);
				} else {
					setActionmode(getActionmode() + 1);
				}
				break;
			case 102:
				if (this.getScanmode() == 0) {
					this.setScanmode(1);
				} else {
					this.setScanmode(0);
				}
				break;

			}
		}
		super.onNetworkHandlerEvent(key, value);
	}

	@Override
	public short getmaxSwitchModi() {
		return 3;
	}

	@Override
	public short getminSwitchModi() {
		return 1;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack, int Slot) {

		switch (Slot) {
		case 0:
			if (par1ItemStack.getItem() instanceof IPowerLinkItem)
				return true;
			break;
		case 1:
			if (par1ItemStack.getItem() instanceof ItemCardSecurityLink)
				return true;
			break;
		case 2:
		case 3:
			if (par1ItemStack.getItem() instanceof ItemProjectorFieldModulatorDistance)
				return true;
			break;
		}

		if (Slot >= 5 && Slot <= 14)
			return true;

		return false;
	}

	@Override
	public int getSlotStackLimit(int Slot) {

		switch (Slot) {

		case 0:
		case 1:

			return 1;

		case 2: // Distance mod
		case 3:
			return 64;
		}

		if (Slot >= 5 && Slot <= 14)
			return 1;

		if (Slot >= 5 && Slot <= 14)
			return 1;

		return 64;
	}

	@Override
	public ItemStack getPowerLinkStack() {
		return this.getStackInSlot(getPowerlinkSlot());
	}

	@Override
	public int getPowerlinkSlot() {
		return 0;
	}
}
