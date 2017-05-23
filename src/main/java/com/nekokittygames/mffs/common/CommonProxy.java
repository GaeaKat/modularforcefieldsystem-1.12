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

import com.nekokittygames.mffs.client.gui.GuiManualScreen;
import com.nekokittygames.mffs.common.block.BlockMFFSBase;
import com.nekokittygames.mffs.common.container.ContainerDummy;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import java.lang.reflect.Constructor;

public class CommonProxy implements IGuiHandler {
	public void registerTileEntitySpecialRenderer() {
	}


	public  void setupClientBlock(Block block, String name)
	{

	}

	public void setupClientFieldBlock(Block block,String name)
	{

	}

	public void setupClientMachine(BlockMFFSBase block, String name) {

	}

	public void setupClientItem(Item item, String name) {

	}
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		if (ID != 0) {
			return new GuiManualScreen(new ContainerDummy());
		}

		TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
		if (te == null) {
			return null;
		}

		MFFSMaschines machType = MFFSMaschines.fromTE(te);

		try {
			Constructor mkGui = Class.forName(
					"com.nekokittygames.mffs.client.gui." + machType.Gui).getConstructor(
					new Class[] { EntityPlayer.class, machType.clazz });
			return mkGui.newInstance(player, (machType.clazz.cast(te)));

		} catch (Exception ex) {
			System.out.println(ex.getLocalizedMessage());
		}

		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {

		TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
		if (te == null) {
			return null;
		}

		MFFSMaschines machType = MFFSMaschines.fromTE(te);

		try {
			Constructor mkGui = machType.Container.getConstructor(new Class[] {
					EntityPlayer.class, machType.clazz });
			return mkGui.newInstance(player, (machType.clazz.cast(te)));
		} catch (Exception ex) {
			System.out.println(ex.getLocalizedMessage());
		}

		return null;

	}

	public World getClientWorld() {
		return null;
	}

	public boolean isClient() {
		return false;
	}

	public void addBookPages() {

	}
}
