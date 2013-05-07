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

package mods.mffs.common.modules;

import cpw.mods.fml.common.registry.LanguageRegistry;
import mods.mffs.api.PointXYZ;
import mods.mffs.common.*;
import mods.mffs.common.tileentity.TileEntityProjector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class ModuleBase extends Item {

	private static List<ModuleBase> instances = new ArrayList<ModuleBase>();

	public static List<ModuleBase> get_instances() {
		return instances;
	}

	public ModuleBase(int i) {
		super(i);
		setMaxStackSize(8);
		instances.add(this);
		setCreativeTab(ModularForceFieldSystem.MFFSTab);
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	public abstract boolean supportsDistance();

	public abstract boolean supportsStrength();

	public abstract boolean supportsMatrix();

	/**
	 * Assume that Y+ (up) is the direction that the projector is facing. Return
	 * points local to the projector. e.g. (0,1,0) would be right in front of
	 * the projector
	 * 
	 * @param projector
	 * @return Set of PointXYZ.
	 */
	public abstract void calculateField(IModularProjector projector,
			Set<PointXYZ> fieldPoints);

	@Override
	public boolean onItemUseFirst(ItemStack itemstack,
			EntityPlayer entityplayer, World world, int i, int j, int k,
			int side, float hitX, float hitY, float hitZ) {
		TileEntity tileEntity = world.getBlockTileEntity(i, j, k);

		if (!world.isRemote) {
			if (tileEntity instanceof IModularProjector) {
				if (!SecurityHelper.isAccessGranted(tileEntity, entityplayer,
						world, SecurityRight.EB)) {
					return false;
				}

				if (((IModularProjector) tileEntity).getStackInSlot(1) == null) {
					((IModularProjector) tileEntity).setInventorySlotContents(
							1, itemstack.splitStack(1));
					Functions.ChattoPlayer(entityplayer, String.format(LanguageRegistry.instance()
							.getStringLocalization("projectorModule.installed"), ProjectorTyp
							.TypfromItem(((IModularProjector) tileEntity)
									.getStackInSlot(1)
									.getItem()).displayName));

					((TileEntityProjector) tileEntity).checkslots();
					return true;
				} else {
					Functions.ChattoPlayer(entityplayer, LanguageRegistry.instance().getStringLocalization
							("projectorModule.notEmpty"));
					return false;
				}
			}
		}
		return false;
	}

	public ForceFieldTyps getForceFieldTyps() {
		return ForceFieldTyps.Default;
	}

	public abstract boolean supportsOption(Item item);

}