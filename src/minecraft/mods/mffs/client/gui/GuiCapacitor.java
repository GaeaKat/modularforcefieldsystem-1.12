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
import mods.mffs.common.container.ContainerCapacitor;
import mods.mffs.common.tileentity.TileEntityCapacitor;
import mods.mffs.network.client.NetworkHandlerClient;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;


public class GuiCapacitor extends GuiContainer {
	private TileEntityCapacitor Core;
	private boolean editMode = false;

	public GuiCapacitor(EntityPlayer player, TileEntityCapacitor tileentity) {
		super(new ContainerCapacitor(player, tileentity));
		Core = tileentity;
		this.xSize = 176;
		this.ySize = 207;
	}

	@Override
	protected void keyTyped(char c, int i) {

		if (i != 1 && editMode) {
			if (c == 13) {
				editMode = false;
				return;
			}

			if (i == 14)
				NetworkHandlerClient.fireTileEntityEvent(Core, 12, "");

			if (i != 54 && i != 42 && i != 58 && i != 14)
				NetworkHandlerClient.fireTileEntityEvent(Core, 11,
						String.valueOf(c));

		} else {
			super.keyTyped(c, i);
		}
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
		} else if (x >= 5 && y >= 4 && x <= 135 && y <= 18) {
			NetworkHandlerClient.fireTileEntityEvent(Core, 10, "null");
			editMode = true;
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture("/mods/mffs/textures/gui/GuiCapacitor.png");
		int w = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		drawTexturedModalRect(w, k, 0, 0, xSize, ySize);
		int i1 = (79 * Core.getPercentageStorageCapacity() / 100);
		drawTexturedModalRect(w + 8, k + 112, 176, 0, i1 + 1, 79);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRenderer.drawString("Force Energy Capacitor", 8, 25, 0x404040);
		fontRenderer.drawString(Core.getDeviceName(), 8, 8, 0x404040);
		fontRenderer.drawString(
				"FE: " + String.valueOf(Core.getStorageAvailablePower()), 8,
				100, 0x404040);
		fontRenderer.drawString("Power Uplink: ", 8, 80, 0x404040);

		fontRenderer.drawString("transmit range:", 8, 60, 0x404040);
		fontRenderer.drawString(
				(new StringBuilder()).append(" ")
						.append(Core.getTransmitRange()).toString(), 90, 60,
				0x404040);
		fontRenderer.drawString("linked device:", 8, 43, 0x404040);
		fontRenderer.drawString(
				(new StringBuilder()).append(" ")
						.append(Core.getLinketProjektor()).toString(), 90, 45,
				0x404040);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		NetworkHandlerClient.fireTileEntityEvent(Core, guibutton.id, "");
	}

	@Override
	public void initGui() {
		buttonList.add(new GraphicButton(0, (width / 2) + 65,
				(height / 2) - 100, Core, 0));
		buttonList.add(new GraphicButton(1, (width / 2) + 20,
				(height / 2) - 28, Core, 1));

		super.initGui();
	}
}
