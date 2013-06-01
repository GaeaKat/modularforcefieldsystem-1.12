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

package mods.mffs.common;

import java.lang.reflect.Constructor;

import mods.mffs.client.gui.GuiManualScreen;
import mods.mffs.common.container.ContainerDummy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {
	public void registerTileEntitySpecialRenderer() {
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {

		if (ID != 0) {
			return new GuiManualScreen(new ContainerDummy());
		}

		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te == null) {
			return null;
		}

		MFFSMaschines machType = MFFSMaschines.fromTE(te);

		try {
			Constructor mkGui = Class.forName(
					"mods.mffs.client.gui." + machType.Gui).getConstructor(
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

		TileEntity te = world.getBlockTileEntity(x, y, z);
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

}
