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

package mods.mffs.common.options;

import java.util.List;

import mods.mffs.api.PointXYZ;
import mods.mffs.common.MFFSDamageSource;
import mods.mffs.common.IModularProjector.Slots;
import mods.mffs.common.modules.ItemProjectorModuleSphere;
import mods.mffs.common.tileentity.TileEntityProjector;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class ItemProjectorOptionMobDefence extends ItemProjectorOptionBase {

	public ItemProjectorOptionMobDefence(int i) {
		super(i);
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("mffs:options/MobDefence");
	}

	public static void ProjectorNPCDefence(TileEntityProjector projector,
			World world) {

		if (projector.isActive()) {

			int fieldxmin = projector.xCoord;
			int fieldxmax = projector.xCoord;
			int fieldymin = projector.yCoord;
			int fieldymax = projector.yCoord;
			int fieldzmin = projector.zCoord;
			int fieldzmax = projector.zCoord;

			for (PointXYZ png : projector.getfield_queue()) {
				fieldxmax = Math.max(fieldxmax, png.X);
				fieldxmin = Math.min(fieldxmin, png.X);
				fieldymax = Math.max(fieldymax, png.Y);
				fieldymin = Math.min(fieldymin, png.Y);
				fieldzmax = Math.max(fieldzmax, png.Z);
				fieldzmin = Math.min(fieldzmin, png.Z);
			}

			List<EntityLiving> LivingEntitylist = world.getEntitiesWithinAABB(
					EntityLiving.class, AxisAlignedBB.getBoundingBox(fieldxmin,
							fieldymin, fieldzmin, fieldxmax, fieldymax,
							fieldzmax));

			for (int i = 0; i < LivingEntitylist.size(); i++) {
				EntityLiving entityLiving = LivingEntitylist.get(i);

				if (!(entityLiving instanceof EntityMob)
						&& !(entityLiving instanceof EntitySlime)
						&& !(entityLiving instanceof EntityGhast)) {
					continue;
				}

				if (projector.get_type() instanceof ItemProjectorModuleSphere)
					if (PointXYZ.distance(new PointXYZ((int) entityLiving.posX,
							(int) entityLiving.posY, (int) entityLiving.posZ,
							world), projector.getMaschinePoint()) > projector
							.countItemsInSlot(Slots.Distance) + 4)
						continue;

				if (projector.getLinkPower() < 10000) {
					break;
				}

				if (projector.consumePower(10000, true)) {
					entityLiving.attackEntityFrom(
							MFFSDamageSource.fieldDefense, 10);
					projector.consumePower(10000, false);

				}

			}
		}
	}
}