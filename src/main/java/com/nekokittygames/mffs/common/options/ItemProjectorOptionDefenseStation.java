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

package com.nekokittygames.mffs.common.options;

import com.nekokittygames.mffs.api.PointXYZ;
import com.nekokittygames.mffs.common.IModularProjector.Slots;
import com.nekokittygames.mffs.common.Linkgrid;
import com.nekokittygames.mffs.common.MFFSDamageSource;
import com.nekokittygames.mffs.common.SecurityRight;
import com.nekokittygames.mffs.common.modules.ItemProjectorModuleSphere;
import com.nekokittygames.mffs.common.tileentity.TileEntityAdvSecurityStation;
import com.nekokittygames.mffs.common.tileentity.TileEntityCapacitor;
import com.nekokittygames.mffs.common.tileentity.TileEntityProjector;
import com.nekokittygames.mffs.libs.LibItemNames;
import com.nekokittygames.mffs.libs.LibMisc;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.List;

public class ItemProjectorOptionDefenseStation extends ItemProjectorOptionBase {

	public ItemProjectorOptionDefenseStation() {
		super(LibItemNames.OPTION_DEFENSE_STATION);
	}



	public static void ProjectorPlayerDefence(TileEntityProjector projector,
			World world) {

		if (projector.isActive()) {
			int fieldxmin = projector.getPos().getX();
			int fieldxmax = projector.getPos().getX();
			int fieldymin = projector.getPos().getY();
			int fieldymax = projector.getPos().getY();
			int fieldzmin = projector.getPos().getZ();
			int fieldzmax = projector.getPos().getZ();

			for (PointXYZ png : projector.getfield_queue()) {
				fieldxmax = Math.max(fieldxmax, png.pos.getX());
				fieldxmin = Math.min(fieldxmin, png.pos.getX());
				fieldymax = Math.max(fieldymax, png.pos.getY());
				fieldymin = Math.min(fieldymin, png.pos.getY());
				fieldzmax = Math.max(fieldzmax, png.pos.getZ());
				fieldzmin = Math.min(fieldzmin, png.pos.getZ());
			}

			List<EntityLivingBase> LivingEntitylist = world.getEntitiesWithinAABB(
					EntityLivingBase.class, new AxisAlignedBB(fieldxmin,
							fieldymin, fieldzmin, fieldxmax, fieldymax,
							fieldzmax));

			for (int i = 0; i < LivingEntitylist.size(); i++) {
				EntityLivingBase EntityLivingBase = LivingEntitylist.get(i);

				if (!(EntityLivingBase instanceof EntityPlayer)) {
					continue;
				}

				if (projector.get_type() instanceof ItemProjectorModuleSphere)
					if (PointXYZ.distance(new PointXYZ(EntityLivingBase.getPosition(),
							world), projector.getMaschinePoint()) > projector
							.countItemsInSlot(Slots.Distance) + 4)
						continue;

				if (projector.getLinkPower() < 10000) {
					break;
				}

				if (projector.getLinkPower() > 10000) {

					boolean killswitch = false;

					if (projector.getaccesstyp() == 2) {
						TileEntityCapacitor cap = Linkgrid.getWorldMap(world)
								.getCapacitor()
								.get(projector.getPowerSourceID());
						if (cap != null) {
							TileEntityAdvSecurityStation SecurityStation = cap
									.getLinkedSecurityStation();

							if (SecurityStation != null) {
								killswitch = !SecurityStation.isAccessGranted(
										((EntityPlayer) EntityLivingBase).getUniqueID().toString(),
										SecurityRight.SR);
							}
						}
					}
					if (projector.getaccesstyp() == 3) {
						TileEntityAdvSecurityStation SecurityStation = projector
								.getLinkedSecurityStation();
						if (SecurityStation != null) {
							killswitch = !SecurityStation.isAccessGranted(
									((EntityPlayer) EntityLivingBase).getUniqueID().toString(),
									SecurityRight.SR);
						}
					}

					if (killswitch) {
						if (projector.consumePower(10000, true)) {
							((EntityPlayer) EntityLivingBase).sendMessage(new TextComponentTranslation("warning" +
											".areaDefense"));
							((EntityPlayer) EntityLivingBase).attackEntityFrom(
									MFFSDamageSource.fieldDefense, 10);
							projector.consumePower(10000, false);

						}

						continue;
					}

				}
			}
		}
	}

}