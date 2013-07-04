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
import mods.mffs.common.ModularForceFieldSystem;
import mods.mffs.common.container.ContainerConverter;
import mods.mffs.common.tileentity.TileEntityConverter;
import mods.mffs.network.client.NetworkHandlerClient;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.ResourceLocation;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;


public class GuiConverter extends GuiContainer {

	private TileEntityConverter Converter;
	private boolean NameeditMode = false;

	public GuiConverter(EntityPlayer player, TileEntityConverter tileentity) {
		super(new ContainerConverter(player, tileentity));
		Converter = tileentity;
		xSize = 256;
		ySize = 216;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		//mc.renderEngine.bindTexture("/mods/mffs/textures/gui/GuiConvertor.png");
		mc.renderEngine.func_110577_a(new ResourceLocation("mffs:textures/gui/GuiConvertor.png"));
		int w = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		drawTexturedModalRect(w, k, 0, 0, xSize, ySize);
		int i1 = (76 * Converter.getCapacity()) / 100;
		drawTexturedModalRect(w + 14, k + 65, 0, 233, i1 + 1, 23);
		if (!ModularForceFieldSystem.ic2Found)
			drawTexturedModalRect(w + 99, k + 45, 0, 217, 70, 13);
	}

	@Override
	protected void keyTyped(char c, int i) {

		if (i != 1 && NameeditMode) {
			if (c == 13) {
				NameeditMode = false;
				return;
			}

			if (i == 14)
				NetworkHandlerClient.fireTileEntityEvent(Converter, 12, "");

			if (i != 54 && i != 42 && i != 58 && i != 14)
				NetworkHandlerClient.fireTileEntityEvent(Converter, 11,
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

		if (NameeditMode) {
			NameeditMode = false;
		} else if (x >= 97 && y >= 4 && x <= 227 && y <= 18) {
			NetworkHandlerClient.fireTileEntityEvent(Converter, 10, "null");
			NameeditMode = true;
		}
		if (ModularForceFieldSystem.ic2Found) {
			if (x >= 100 && y >= 46 && x <= 114 && y <= 57)
				NetworkHandlerClient.fireTileEntityEvent(Converter, 200, "+");
			if (x >= 115 && y >= 46 && x <= 128 && y <= 57)
				NetworkHandlerClient.fireTileEntityEvent(Converter, 200, "-");
			if (x >= 140 && y >= 46 && x <= 154 && y <= 57)
				NetworkHandlerClient.fireTileEntityEvent(Converter, 201, "+");
			if (x >= 155 && y >= 46 && x <= 168 && y <= 57)
				NetworkHandlerClient.fireTileEntityEvent(Converter, 201, "-");
		}
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		NetworkHandlerClient.fireTileEntityEvent(Converter, guibutton.id,
				"null");
	}

	@Override
	public void initGui() {
		buttonList.add(new GraphicButton(0, (width / 2) + 107,
				(height / 2) - 104, Converter, 0));

		if (ModularForceFieldSystem.ic2Found)
			buttonList.add(new GraphicButton(100, (width / 2) - 25,
					(height / 2) - 80, Converter, 1));

		super.initGui();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRenderer.drawString(Converter.getDeviceName(), 100, 8, 0x404040);
		fontRenderer.drawString("MFFS Converter", 8, 8, 0x404040);

		if (ModularForceFieldSystem.ic2Found) {
			fontRenderer.drawString("IC 2", 125, 33, 0x000000);
			fontRenderer.drawString("EU", 110, 62, 0x000000);
			fontRenderer.drawString("packets", 130, 62, 0x000000);

			fontRenderer.drawString("" + Converter.getIC_Outputpacketsize(),
					103, 48, 0xFFFFFF);
			fontRenderer.drawString("x", 132, 48, 0xFFFFFF);
			fontRenderer.drawString("" + Converter.getIC_Outputpacketamount(),
					150, 48, 0xFFFFFF);
		}

		if (Converter.getPowerSourceID() != 0) {
			fontRenderer.drawString("FE: " + Converter.getLinkPower(), 17, 54,
					0x404040);
		} else {
			fontRenderer.drawString("FE: No Link/OOR", 17, 54, 0x404040);
		}

		if (!Converter.isActive()) {
			fontRenderer.drawString("OFFLINE", 32, 99, 0xFF0000);
		} else {
			fontRenderer.drawString("ONLINE", 34, 99, 0x008B00);
		}

	}
}
