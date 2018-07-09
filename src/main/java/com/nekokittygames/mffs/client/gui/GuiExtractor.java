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

package com.nekokittygames.mffs.client.gui;

import com.nekokittygames.mffs.client.GraphicButton;
import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.container.ContainerForceEnergyExtractor;
import com.nekokittygames.mffs.common.tileentity.TileEntityExtractor;
import com.nekokittygames.mffs.network.client.NetworkHandlerClient;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;


public class GuiExtractor extends GuiContainer {
	private TileEntityExtractor Extractor;
	private boolean editMode = false;

	public GuiExtractor(EntityPlayer player, TileEntityExtractor tileentity) {
		super(new ContainerForceEnergyExtractor(player, tileentity));
		this.Extractor = tileentity;
		this.xSize = 176;
		this.ySize = 186;
	}

	@Override
	protected void keyTyped(char c, int i) throws IOException {

		if (i != 1 && editMode) {
			if (c == 13) {
				editMode = false;
				return;
			}

			if (i == 14)
				NetworkHandlerClient.fireTileEntityEvent(Extractor, 12, "");

			if (i != 54 && i != 42 && i != 58 && i != 14)
				NetworkHandlerClient.fireTileEntityEvent(Extractor, 11,
						String.valueOf(c));

		} else {
			super.keyTyped(c, i);
		}
	}

	@Override
	protected void mouseClicked(int i, int j, int k) throws IOException{
		super.mouseClicked(i, j, k);

		int xMin = (width - xSize) / 2;
		int yMin = (height - ySize) / 2;

		int x = i - xMin;
		int y = j - yMin;

		if (editMode) {
			editMode = false;
		} else if (x >= 10 && y >= 5 && x <= 141 && y <= 19) {
			NetworkHandlerClient.fireTileEntityEvent(Extractor, 10, "null");
			editMode = true;
		}
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		NetworkHandlerClient.fireTileEntityEvent(Extractor, guibutton.id, "");
	}

	@Override
	public void initGui() {
		buttonList.add(new GraphicButton(0, (width / 2) + 60,
				(height / 2) - 88, Extractor, 0));
		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		mc.renderEngine.bindTexture(new ResourceLocation("modularforcefieldsystem:textures/gui/GuiExtractor.png"));
		int w = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		drawTexturedModalRect(w, k, 0, 0, xSize, ySize);

		int Workpowerslider = (79 * Extractor.getWorkdone() / 100);
		drawTexturedModalRect(w + 49, k + 89, 176, 0, Workpowerslider, 6);

		int WorkCylce = (32 * Extractor.getWorkCylce())
				/ ModularForceFieldSystem.ForciciumWorkCycle;

		drawTexturedModalRect(w + 73, k + 50, 179, 81, WorkCylce, 32);

		int ForceEnergy = (24 * Extractor.getForceEnergybuffer() / Extractor
				.getMaxForceEnergyBuffer());
		drawTexturedModalRect(w + 137, k + 60, 219, 80, 32, ForceEnergy);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRenderer.drawString("Force Energy", 5, 25, 0x404040);
		fontRenderer.drawString("Upgrades", 10, 50, 0x404040);
		fontRenderer.drawString(Extractor.getDeviceName(), 8, 9, 0x404040);
		fontRenderer.drawString("Extractor", 5, 35, 0x404040);
		fontRenderer.drawString(
				String.valueOf(Extractor.getForceEnergybuffer() / 1000).concat(
						"k"), 140, 89, 0x404040);

		fontRenderer.drawString(
				String.valueOf(Extractor.getWorkdone()).concat("%"), 23, 89,
				0x404040);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
}
