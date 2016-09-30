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
    
    Thunderdark 
    Matchlighter
 
 */

package com.nekokittygames.mffs.api;


import com.nekokittygames.mffs.common.tileentity.TileEntityMachines;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IPowerLinkItem {

	public int getPercentageCapacity(ItemStack itemStack,
									 TileEntityMachines tem, World world);

	public int getAvailablePower(ItemStack itemStack, TileEntityMachines tem,
			World world);

	public int getMaximumPower(ItemStack itemStack, TileEntityMachines tem,
			World world);

	public boolean consumePower(ItemStack itemStack, int powerAmount,
			boolean simulation, TileEntityMachines tem, World world);

	public boolean insertPower(ItemStack itemStack, int powerAmount,
			boolean simulation, TileEntityMachines tem, World world);

	public int getPowersourceID(ItemStack itemStack, TileEntityMachines tem,
			World world);

	public int getfreeStorageAmount(ItemStack itemStack,
			TileEntityMachines tem, World world);

	public boolean isPowersourceItem();

}
