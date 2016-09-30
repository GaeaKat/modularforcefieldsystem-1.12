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

package com.nekokittygames.mffs.common;

import java.util.Set;

import com.nekokittygames.mffs.api.PointXYZ;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IModularProjector extends IInventory {

	public static enum Slots {
		Linkcard(0), TypeMod(1), Option1(2), Option2(3), Option3(4), Distance(5), Strength(
				6), FocusUp(7), FocusDown(8), FocusRight(9), FocusLeft(10), Centerslot(
				11), SecCard(12);
		public int slot;

		private Slots(int num) {
			slot = num;
		}
	}

	public World getWorldObj();

	public int countItemsInSlot(Slots slt);

	public int getDeviceID();

	public Set<PointXYZ> getInteriorPoints();

	public void setBurnedOut(boolean burnOut);

	public boolean isActive();

	// true / false is Projector Active

	public EnumFacing getSide();

}
