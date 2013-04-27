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

package mods.mffs.client.gui;

import mods.mffs.client.GraphicButton;
import mods.mffs.common.container.ContainerSecStorage;
import mods.mffs.common.tileentity.TileEntitySecStorage;
import mods.mffs.network.client.NetworkHandlerClient;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;


public class GuiSecStorage extends GuiContainer {

	private TileEntitySecStorage SecStorage;
	private boolean editMode = false;

	public GuiSecStorage(EntityPlayer player, TileEntitySecStorage tileentity) {
		super(new ContainerSecStorage(player, tileentity));
		SecStorage = tileentity;
		this.xSize = 185;
		this.ySize = 238;
	}

	@Override
	protected void keyTyped(char c, int i) {

		if (i != 1 && editMode) {
			if (c == 13) {
				editMode = false;
				return;
			}

			if (i == 14)
				NetworkHandlerClient.fireTileEntityEvent(SecStorage, 12, "");

			if (i != 54 && i != 42 && i != 58 && i != 14)
				NetworkHandlerClient.fireTileEntityEvent(SecStorage, 11,
						String.valueOf(c));

		} else {
			super.keyTyped(c, i);
		}
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		NetworkHandlerClient.fireTileEntityEvent(SecStorage, guibutton.id, "");
	}

	@Override
	public void initGui() {
		buttonList.add(new GraphicButton(0, (width / 2) + 65,
				(height / 2) - 113, SecStorage, 0));
		super.initGui();
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);

		int xMin = (width - xSize) / 2;
		int yMin = (height - ySize) / 2;

		int x = i - xMin;
		int y = j - yMin;

		if (editMode) {
			editMode = false;
		} else if (x >= 10 && y >= 5 && x <= 141 && y <= 19) {
			NetworkHandlerClient.fireTileEntityEvent(SecStorage, 10, "null");
			editMode = true;
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine
				.bindTexture("/mods/mffs/textures/gui/GuiSecStorage.png");
		int w = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		drawTexturedModalRect(w, k, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRenderer.drawString(SecStorage.getDeviceName(), 12, 9, 0x404040);
		fontRenderer.drawString("MFFS Security Storage", 38, 28, 0x404040);

	}
}
