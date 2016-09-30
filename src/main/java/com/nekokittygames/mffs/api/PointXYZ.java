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

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class PointXYZ {

	public BlockPos pos;
	public int dimensionId;

	public PointXYZ(BlockPos pos) {
		this(pos, Integer.MAX_VALUE);
	}

	public PointXYZ(BlockPos pos, World worldObj) {
		this(pos, worldObj.provider.getDimension());
	}
	public PointXYZ(double x,double y,double z,World world)
	{
		this(new BlockPos(x,y,z),world);
	}
	public PointXYZ(double x,double y,double z,int dimensionId)
	{
		this(new BlockPos(x,y,z),dimensionId);
	}
	public PointXYZ(double x,double y,double z)
	{
		this(new BlockPos(x,y,z));
	}
	public PointXYZ(BlockPos pos, int dimensionId) {
		this.pos=pos;
		this.dimensionId = dimensionId;
	}

	public PointXYZ(NBTTagCompound nbt) {
		this(new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z")), nbt
				.getInteger("dim"));
	}

	public NBTTagCompound asNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("x", pos.getX());
		nbt.setInteger("y", pos.getY());
		nbt.setInteger("z", pos.getZ());
		nbt.setInteger("dim", dimensionId);
		return nbt;
	}

	public World getPointWorld() {
		if (dimensionId != Integer.MAX_VALUE)
			return DimensionManager.getWorld(dimensionId);
		return null;
	}

	public static double distance(PointXYZ png1, PointXYZ png2) {
		if (png1.dimensionId == png2.dimensionId) {
			int dx = png1.pos.getX() - png2.pos.getX();
			int dy = png1.pos.getY() - png2.pos.getY();
			int dz = png1.pos.getZ() - png2.pos.getZ();
			return Math.sqrt(dx * dx + dy * dy + dz * dz);
		}
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean equals(Object pnt2) {
		if (pnt2 instanceof PointXYZ) {
			PointXYZ p = (PointXYZ) pnt2;
			return (this.pos.equals(p.pos) && this.dimensionId == p.dimensionId);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return ("X: " + pos.getX() + " Y: " + pos.getY() + " Z: " + pos.getZ() + "D: " + dimensionId)
				.hashCode();
	}

	@Override
	public String toString() {
		return "X: " + pos.getX() + " Y: " + pos.getY() + " Z: " + pos.getZ();
	}

}
