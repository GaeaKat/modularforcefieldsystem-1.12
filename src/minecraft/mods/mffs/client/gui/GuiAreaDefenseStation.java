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
import mods.mffs.common.container.ContainerAreaDefenseStation;
import mods.mffs.common.tileentity.TileEntityAreaDefenseStation;
import mods.mffs.network.client.NetworkHandlerClient;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.ResourceLocation;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;


public class GuiAreaDefenseStation extends GuiContainer {
	private TileEntityAreaDefenseStation DefenceStation;
	private boolean editMode = false;

	public GuiAreaDefenseStation(EntityPlayer player,
			TileEntityAreaDefenseStation tileentity) {
		super(new ContainerAreaDefenseStation(player, tileentity));
		DefenceStation = tileentity;
		xSize = 256;
		ySize = 216;
	}

	@Override
	protected void keyTyped(char c, int i) {

		if (i != 1 && editMode) {
			if (c == 13) {
				editMode = false;
				return;
			}

			if (i == 14)
				NetworkHandlerClient
						.fireTileEntityEvent(DefenceStation, 12, "");

			if (i != 54 && i != 42 && i != 58 && i != 14)
				NetworkHandlerClient.fireTileEntityEvent(DefenceStation, 11,
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
		} else if (x >= 115 && y >= 5 && x <= 233 && y <= 19) {
			NetworkHandlerClient
					.fireTileEntityEvent(DefenceStation, 10, "null");
			editMode = true;
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		int xSize = 256;
		int ySize = 216;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		//mc.renderEngine.bindTexture("/mods/mffs/textures/gui/GuiDefStation.png");
		mc.renderEngine.func_110577_a(new ResourceLocation("mffs:textures/gui/GuiDefStation.png"));
		int w = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		drawTexturedModalRect(w, k, 0, 0, xSize, ySize);

	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		NetworkHandlerClient.fireTileEntityEvent(DefenceStation, guibutton.id,
				"");
	}

	@Override
	public void initGui() {
		buttonList.add(new GraphicButton(0, (width / 2) + 107,
				(height / 2) - 104, DefenceStation, 0));
		buttonList.add(new GraphicButton(100, (width / 2) + 47,
				(height / 2) - 38, DefenceStation, 1));
		buttonList.add(new GraphicButton(101, (width / 2) - 36,
				(height / 2) - 58, DefenceStation, 2));
		buttonList.add(new GraphicButton(102, (width / 2) + 20,
				(height / 2) - 58, DefenceStation, 3));
		super.initGui();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRenderer.drawString("MFFS Defence Station", 7, 9, 0x404040);
		fontRenderer.drawString(DefenceStation.getDeviceName(), 120, 9,
				0x404040);

		switch (DefenceStation.getActionmode()) {
		case 0:
			fontRenderer.drawString("inform", 110, 55, 0x404040);

			fontRenderer.drawString(" send Info", 95, 85, 0x404040);
			fontRenderer.drawString(" to player ", 95, 95, 0x404040);
			fontRenderer.drawString(" without (SR)", 95, 105, 0x404040);
			fontRenderer.drawString(" Stay Right", 95, 115, 0x404040);

			break;
		case 1:
			fontRenderer.drawString("kill", 110, 55, 0x404040);

			fontRenderer.drawString(" kill player", 95, 85, 0x404040);
			fontRenderer.drawString(" without (SR)", 95, 95, 0x404040);
			fontRenderer.drawString(" gathers his", 95, 105, 0x404040);
			fontRenderer.drawString(" equipment", 95, 115, 0x404040);

			break;
		case 2:
			fontRenderer.drawString("search", 110, 55, 0x404040);

			fontRenderer.drawString("scans player", 95, 85, 0x404040);
			fontRenderer.drawString("without (AAI)", 95, 95, 0x404040);
			fontRenderer.drawString("and remove", 95, 105, 0x404040);
			fontRenderer.drawString("banned items", 95, 115, 0x404040);
			break;

		case 3:
			fontRenderer.drawString("NPC kill", 110, 55, 0x404040);

			fontRenderer.drawString("kill any NPC", 95, 85, 0x404040);
			fontRenderer.drawString("friendly or", 95, 95, 0x404040);
			fontRenderer.drawString("hostile", 95, 105, 0x404040);
			break;

		case 4:
			fontRenderer.drawString("NPC kill", 110, 55, 0x404040);

			fontRenderer.drawString("kill only", 95, 85, 0x404040);
			fontRenderer.drawString("hostile NPCs", 95, 95, 0x404040);

			break;

		case 5:
			fontRenderer.drawString("NPC kill", 110, 55, 0x404040);

			fontRenderer.drawString("kill only", 95, 85, 0x404040);
			fontRenderer.drawString("friendly NPCs", 95, 95, 0x404040);

			break;

		}

		fontRenderer.drawString("Action desc:", 95, 73, 0x00008B);

		fontRenderer.drawString("items", 205, 68, 0x228B22);

		if (DefenceStation.getcontratyp() == 0) {
			fontRenderer.drawString("allowed", 200, 82, 0x228B22);
		}

		if (DefenceStation.getcontratyp() == 1) {
			fontRenderer.drawString("banned", 200, 82, 0xFF0000);
		}

		if (DefenceStation.getPowerSourceID() != 0) {
			fontRenderer.drawString((new StringBuilder()).append("FE: ")
					.append(DefenceStation.getCapacity()).append(" %")
					.toString(), 35, 31, 0x404040);
		} else {
			fontRenderer.drawString("No Link/OOR", 35, 31, 0xFF0000);
		}

		if (DefenceStation.hasSecurityCard()) {
			fontRenderer.drawString("linked", 120, 31, 0x228B22);
		}

		fontRenderer.drawString("warning", 35, 55, 0x00008B);
		fontRenderer.drawString(
				"perimeter: " + DefenceStation.getInfoDistance(), 12, 73,
				0x404040);

		fontRenderer.drawString("action", 35, 91, 0xEE3B3B);
		fontRenderer.drawString(
				"perimeter: " + DefenceStation.getActionDistance(), 12, 111,
				0x404040);

		fontRenderer.drawString("inventory ", 180, 195, 0x404040);

	}
}
