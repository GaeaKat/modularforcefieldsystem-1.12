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

package mods.mffs.common.tileentity;

import cpw.mods.fml.common.registry.LanguageRegistry;
import dan200.computer.api.IComputerAccess;
import mods.mffs.api.IPowerLinkItem;
import mods.mffs.api.PointXYZ;
import mods.mffs.common.*;
import mods.mffs.common.container.ContainerAreaDefenseStation;
import mods.mffs.common.item.ItemCardSecurityLink;
import mods.mffs.common.item.ItemProjectorFieldModulatorDistance;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileEntityAreaDefenseStation extends TileEntityFEPoweredMachine
		implements ISidedInventory {
	private ItemStack Inventory[];
	private int capacity;
	private int distance;
	private int contratyp;
	private int actionmode;
	private int scanmode;

	protected List<EntityPlayer> warnlist = new ArrayList<EntityPlayer>();
	protected List<EntityPlayer> actionlist = new ArrayList<EntityPlayer>();
	protected List<EntityLiving> NPClist = new ArrayList<EntityLiving>();
	private final ArrayList<Item> ContraList = new ArrayList();

	public TileEntityAreaDefenseStation() {
		Random random = new Random();

		Inventory = new ItemStack[36];
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
		if (getStackInSlot(3) != null) {
			if (getStackInSlot(3).getItem() == ModularForceFieldSystem.MFFSProjectorFFDistance) {
				return (getStackInSlot(3).stackSize);
			}
		}
		return 0;

	}

	public int getInfoDistance() {
		if (getStackInSlot(2) != null) {
			if (getStackInSlot(2).getItem() == ModularForceFieldSystem.MFFSProjectorFFDistance) {
				return getActionDistance() + (getStackInSlot(2).stackSize + 3);
			}
		}
		return getActionDistance() + 3;
	}

	public boolean hasSecurityCard() {
		if (getStackInSlot(1) != null) {
			if (getStackInSlot(1).getItem() == ModularForceFieldSystem.MFFSItemSecLinkCard) {
				return true;
			}
		}
		return false;
	}

	@Override
	public TileEntityAdvSecurityStation getLinkedSecurityStation() {
		return ItemCardSecurityLink.getLinkedSecurityStation(this, 1, worldObj);
	}

	@Override
	public void invalidate() {
		Linkgrid.getWorldMap(worldObj).getDefStation().remove(getDeviceID());
		super.invalidate();
	}

	// Start NBT Read/ Save
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		contratyp = nbttagcompound.getInteger("contratyp");
		actionmode = nbttagcompound.getInteger("actionmode");
		scanmode = nbttagcompound.getInteger("scanmode");
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
		Inventory = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist
					.tagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if (byte0 >= 0 && byte0 < Inventory.length) {
				Inventory[byte0] = ItemStack
						.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("contratyp", contratyp);
		nbttagcompound.setInteger("actionmode", actionmode);
		nbttagcompound.setInteger("scanmode", scanmode);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < Inventory.length; i++) {
			if (Inventory[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				Inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("Items", nbttaglist);
	}

	@Override
	public void dropPlugins() {
		for (int a = 0; a < this.Inventory.length; a++) {
			dropplugins(a, this);
		}
	}

	public void scanner() {
		try {

			TileEntityAdvSecurityStation sec = getLinkedSecurityStation();

			if (sec != null && sec.isSecurityEstablished()) {
				int xmininfo = xCoord - getInfoDistance();
				int xmaxinfo = xCoord + getInfoDistance() + 1;
				int ymininfo = yCoord - getInfoDistance();
				int ymaxinfo = yCoord + getInfoDistance() + 1;
				int zmininfo = zCoord - getInfoDistance();
				int zmaxinfo = zCoord + getInfoDistance() + 1;

				int xminaction = xCoord - getActionDistance();
				int xmaxaction = xCoord + getActionDistance() + 1;
				int yminaction = yCoord - getActionDistance();
				int ymaxaction = yCoord + +getActionDistance() + 1;
				int zminaction = zCoord - getActionDistance();
				int zmaxaction = zCoord + getActionDistance() + 1;

				List<EntityLiving> infoLivinglist = worldObj
						.getEntitiesWithinAABB(EntityLiving.class,
								AxisAlignedBB.getBoundingBox(xmininfo,
										ymininfo, zmininfo, xmaxinfo, ymaxinfo,
										zmaxinfo));
				List<EntityLiving> actionLivinglist = worldObj
						.getEntitiesWithinAABB(EntityLiving.class,
								AxisAlignedBB.getBoundingBox(xminaction,
										yminaction, zminaction, xmaxaction,
										ymaxaction, zmaxaction));

				for (EntityLiving Living : infoLivinglist) {
					if (Living instanceof EntityPlayer) {
						EntityPlayer player = (EntityPlayer) Living;
						int distance = (int) PointXYZ.distance(
								getMaschinePoint(), new PointXYZ(
										(int) Living.posX, (int) Living.posY,
										(int) Living.posZ, worldObj));

						if (distance > getInfoDistance()
								&& this.getScanmode() == 1) {
							continue;
						}

						if (!warnlist.contains(player)) {
							warnlist.add(player);
							if (!sec.isAccessGranted(player.username,
									SecurityRight.SR)) {
								if (!(ModularForceFieldSystem.DefenceStationNPCScannsuppressnotification &&
										getActionmode() < 3)) {
									player.addChatMessage(String.format(LanguageRegistry.instance()
											.getStringLocalization("securityStation.scanningRange"),
											sec.getDeviceName()));

									if(getActionmode() == 1)
										player.attackEntityFrom(MFFSDamageSource.areaDefense, 1);
								}
							}
						}
					}
				}

				for (EntityLiving Living : actionLivinglist) {
					if (Living instanceof EntityPlayer) {
						EntityPlayer player = (EntityPlayer) Living;

						int distance = (int) Math.round(PointXYZ.distance(
								getMaschinePoint(), new PointXYZ(
										(int) Living.posX, (int) Living.posY,
										(int) Living.posZ, worldObj)));
						if (distance > getActionDistance()
								&& this.getScanmode() == 1)
							continue;

						if (!actionlist.contains(player)) {
							actionlist.add(player);
							DefenceAction(player);
						}

					} else {

						int distance = (int) Math.round(PointXYZ.distance(
								getMaschinePoint(), new PointXYZ(
										(int) Living.posX, (int) Living.posY,
										(int) Living.posZ, worldObj)));
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
			if (inventory.getStackInSlot(a) == null) {
				inventory.setInventorySlotContents(a, itemstacks);
				return true;
			} else {
				if (inventory.getStackInSlot(a).getItem() == itemstacks
						.getItem()
						&& inventory.getStackInSlot(a).getItemDamage() == itemstacks
								.getItemDamage()
						&& inventory.getStackInSlot(a).stackSize < inventory
								.getStackInSlot(a).getMaxStackSize()) {
					int free = inventory.getStackInSlot(a).getMaxStackSize()
							- inventory.getStackInSlot(a).stackSize;

					if (free > itemstacks.stackSize) {
						inventory.getStackInSlot(a).stackSize += itemstacks.stackSize;
						return true;
					} else {
						inventory.getStackInSlot(a).stackSize = inventory
								.getStackInSlot(a).getMaxStackSize();
						itemstacks.stackSize = itemstacks.stackSize - free;
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
		IInventory inv = InventoryHelper.findAttachedInventory(worldObj,
				xCoord, yCoord, zCoord);
		if (inv != null) {
			StacksToInventory(inv, itemstacks, false);
		}
	}

	public void DefenceAction(EntityLiving Living) {
		if (Living instanceof EntityPlayer)
			return;

		TileEntityAdvSecurityStation sec = getLinkedSecurityStation();

		if (hasPowerSource()) {

			if (sec != null && sec.isSecurityEstablished()) {
				if(this.consumePower(ModularForceFieldSystem.DefenceStationKillForceEnergy, true))
				{
					switch (getActionmode()) {
					case 3: // all
						consumePower(
								ModularForceFieldSystem.DefenceStationKillForceEnergy,
								false);
						Living.setEntityHealth(0);
						NPClist.remove(Living);
						break;

					case 4: // Hostile
						if (Living instanceof IMob ||
								Living instanceof IAnimals) {
							Living.setEntityHealth(0);
							NPClist.remove(Living);
							consumePower(
									ModularForceFieldSystem.DefenceStationKillForceEnergy,
									false);
						}

						break;
					case 5:// no Hostie

						if (!(Living instanceof IMob)) {
							Living.setEntityHealth(0);
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
					if (!sec.isAccessGranted(player.username, SecurityRight.SR)) {
						player.addChatMessage(LanguageRegistry.instance().getStringLocalization("areaDefense.getOut"));
					}

					break;
				case 1: // kill

					if (consumePower(
							ModularForceFieldSystem.DefenceStationKillForceEnergy,
							true)) {
						if (!sec.isAccessGranted(player.username,
								SecurityRight.SR)) {
							player.addChatMessage(LanguageRegistry.instance().getStringLocalization("areaDefense" +
									".beenWarned"));

							for (int i = 0; i < 4; i++) {
								if (player.inventory.armorInventory[i] != null) {
									StacksToInventory(this,
											player.inventory.armorInventory[i],
											true);
									player.inventory.armorInventory[i] = null;
								}
							}

							for (int i = 0; i < 36; i++) {

								if (player.inventory.mainInventory[i] != null) {
									StacksToInventory(this,
											player.inventory.mainInventory[i],
											true);
									player.inventory.mainInventory[i] = null;
								}
							}

							actionlist.remove(player);
							player.attackEntityFrom(MFFSDamageSource.areaDefense, 20);
							player.setEntityHealth(0);
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
						if (!sec.isAccessGranted(player.username,
								SecurityRight.AAI)) {
							ContraList.clear();

							for (int place = 5; place < 15; place++) {
								if (getStackInSlot(place) != null) {
									ContraList.add(getStackInSlot(place)
											.getItem());
								}
							}

							switch (this.getcontratyp()) {
							case 0:

								for (int i = 0; i < 4; i++) {
									if (player.inventory.armorInventory[i] != null) {

										if (!ContraList
												.contains(player.inventory.armorInventory[i]
														.getItem())) {
											player.addChatMessage(String.format(LanguageRegistry.instance()
													.getStringLocalization("areaDefense.illegalItems"),
													player.inventory.armorInventory[i].getItem().getItemDisplayName
															(player.inventory.armorInventory[i])));
											StacksToInventory(
													this,
													player.inventory.armorInventory[i],
													true);
											player.inventory.armorInventory[i] = null;
											consumePower(
													ModularForceFieldSystem.DefenceStationSearchForceEnergy,
													false);
										}
									}
								}

								for (int i = 0; i < 36; i++) {

									if (player.inventory.mainInventory[i] != null) {

										if (!ContraList
												.contains(player.inventory.mainInventory[i]
														.getItem())) {
											player.addChatMessage(String.format(LanguageRegistry.instance()
													.getStringLocalization("areaDefense.illegalItems"),
													player.inventory.mainInventory[i]
															.getItem()
															.getItemDisplayName(
																	player.inventory.mainInventory[i])));
											StacksToInventory(
													this,
													player.inventory.mainInventory[i],
													true);
											player.inventory.mainInventory[i] = null;
											consumePower(
													ModularForceFieldSystem.DefenceStationSearchForceEnergy,
													false);
										}
									}
								}

								break;
							case 1:

								for (int i = 0; i < 4; i++) {
									if (player.inventory.armorInventory[i] != null) {

										if (ContraList
												.contains(player.inventory.armorInventory[i]
														.getItem())) {
											player.addChatMessage(String.format(LanguageRegistry.instance()
													.getStringLocalization("areaDefense.illegalItems"),
													player.inventory.armorInventory[i]
															.getItem()
															.getItemDisplayName(
																	player.inventory.armorInventory[i])));
											StacksToInventory(
													this,
													player.inventory.armorInventory[i],
													true);
											player.inventory.armorInventory[i] = null;
											consumePower(
													ModularForceFieldSystem.DefenceStationSearchForceEnergy,
													false);
										}
									}
								}

								for (int i = 0; i < 36; i++) {
									if (player.inventory.mainInventory[i] != null) {

										if (ContraList
												.contains(player.inventory.mainInventory[i]
														.getItem())) {
											player.addChatMessage(String.format(LanguageRegistry.instance()
													.getStringLocalization("areaDefense.illegalItems"),
													player.inventory.mainInventory[i]
															.getItem()
															.getItemDisplayName(
																	player.inventory.mainInventory[i])));
											StacksToInventory(
													this,
													player.inventory.mainInventory[i],
													true);
											player.inventory.mainInventory[i] = null;
											consumePower(
													ModularForceFieldSystem.DefenceStationSearchForceEnergy,
													false);
										}
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
	public void updateEntity() {
		if (worldObj.isRemote == false) {

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
		super.updateEntity();
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (Inventory[i] != null) {
			if (Inventory[i].stackSize <= j) {
				ItemStack itemstack = Inventory[i];
				Inventory[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = Inventory[i].splitStack(j);
			if (Inventory[i].stackSize == 0) {
				Inventory[i] = null;
			}
			return itemstack1;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		Inventory[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();

		}
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return Inventory[i];
	}

	@Override
	public String getInvName() {
		return "Defstation";
	}

	@Override
	public int getSizeInventory() {
		return Inventory.length;
	}

	@Override
	public Container getContainer(InventoryPlayer inventoryplayer) {
		return new ContainerAreaDefenseStation(inventoryplayer.player, this);
	}

	@Override
	public int getStartInventorySide(ForgeDirection side) {
		return 15;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		return 20;
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

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

	@Override
	public String getType() { return "MFFSAreaDefenseStation"; }

	@Override
	public String[] getMethodNames() {
		return new String[] {
				"isActive", "setActive",

				"getWarningPerimeter", "getActionPerimeter", "getScanType", "setScanType", "getActionType",
				"setActionType", "hasItems", "hasSpace"
		};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch(method) {

			case 2: // getWarningPerimeter
				return new Object[] { this.getInfoDistance() };

			case 3: // getActionPerimeter
				return new Object[] { this.getActionDistance() };

			case 4: // getScanType
				return new Object[] { this.getScanmode() };

			case 5: // setScanType
				if(arguments.length < 1 || isActive())
					return new Object[] { false };

				int mode = (Integer)arguments[0];

				if(mode < 0 || mode > 1)
					return new Object[] { false };

				setScanmode(mode);

				return new Object[] { true };

			case 6: // getActionType
				return new Object[] { this.getActionmode() };

			case 7: // setActionMode
				if(arguments.length < 1 || isActive())
					return new Object[] { false };

				int actionMode = (Integer)arguments[0];

				if(actionMode < 0 || actionMode > 6)
					return new Object[] { false };

				setActionmode(actionMode);

				return new Object[] { true };

			case 8: // hasItems
				return new Object[] { false };

			case 9: // hasSpace
				return new Object[] { false };

			default:
				return super.callMethod(computer, method, arguments);
		}
	}
}
