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
import com.nekokittygames.mffs.common.ProjectorTyp;
import com.nekokittygames.mffs.common.container.ContainerProjector;
import com.nekokittygames.mffs.common.item.ModItems;
import com.nekokittygames.mffs.common.tileentity.TileEntityProjector;
import com.nekokittygames.mffs.network.client.NetworkHandlerClient;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;


public class GuiProjector extends GuiContainer {
	private TileEntityProjector projector;
	private boolean editMode = false;

	public GuiProjector(EntityPlayer player, TileEntityProjector tileentity) {
		super(new ContainerProjector(player, tileentity));
		projector = tileentity;
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
				NetworkHandlerClient.fireTileEntityEvent(projector, 12, "");

			if (i != 54 && i != 42 && i != 58 && i != 14)
				NetworkHandlerClient.fireTileEntityEvent(projector, 11,
						String.valueOf(c));

		} else {
			super.keyTyped(c, i);
		}
	}

	@Override
	protected void mouseClicked(int i, int j, int k) throws IOException {
		super.mouseClicked(i, j, k);

		int xMin = (width - xSize) / 2;
		int yMin = (height - ySize) / 2;

		int x = i - xMin;
		int y = j - yMin;

		if (editMode) {
			editMode = false;
		} else if (x >= 10 && y >= 5 && x <= 141 && y <= 19) {
			NetworkHandlerClient.fireTileEntityEvent(projector, 10, "null");
			editMode = true;
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		mc.renderEngine.bindTexture(new ResourceLocation("modularforcefieldsystem:textures/gui/GuiProjector.png"));

		int w = (width - xSize) / 2;
		int k = (height - ySize) / 2;

		drawTexturedModalRect(w, k, 0, 0, xSize, ySize);
		int i1 = (79 * projector.getCapacity() / 100);
		drawTexturedModalRect(w + 8, k + 91, 176, 0, i1 + 1, 79);

		if (projector.hasValidTypeMod()) {

			if (ProjectorTyp.TypfromItem(projector.get_type()).ProTyp != 7) {
				drawTexturedModalRect(w + 119, k + 63, 177, 143, 16, 16);
			}

			if (ProjectorTyp.TypfromItem(projector.get_type()).ProTyp != 4
					&& ProjectorTyp.TypfromItem(projector.get_type()).ProTyp != 2) {
				drawTexturedModalRect(w + 155, k + 63, 177, 143, 16, 16);
			}

			if (ProjectorTyp.TypfromItem(projector.get_type()).ProTyp == 1
					|| ProjectorTyp.TypfromItem(projector.get_type()).ProTyp == 2
					|| ProjectorTyp.TypfromItem(projector.get_type()).ProTyp == 6
					|| ProjectorTyp.TypfromItem(projector.get_type()).ProTyp == 7
					|| ProjectorTyp.TypfromItem(projector.get_type()).ProTyp == 8) {
				drawTexturedModalRect(w + 137, k + 28, 177, 143, 16, 16);

				drawTexturedModalRect(w + 137, k + 62, 177, 143, 16, 16);

				drawTexturedModalRect(w + 154, k + 45, 177, 143, 16, 16);

				drawTexturedModalRect(w + 120, k + 45, 177, 143, 16, 16);
			}

			if (projector
					.hasOption(
							ModItems.OPTION_CAMOFLAGE,
							true)) {
				drawTexturedModalRect(w + 137, k + 45, 177, 143, 16, 16); // center
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		NetworkHandlerClient.fireTileEntityEvent(projector, guibutton.id, "");
	}

	@Override
	public void initGui() {
		buttonList.add(new GraphicButton(1, (width / 2) + 4, (height / 2) - 37,
				projector, 1));
		buttonList.add(new GraphicButton(0, (width / 2) + 67,
				(height / 2) - 88, projector, 0));

		super.initGui();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRenderer.drawString(projector.getDeviceName(), 12, 9, 0x404040);
		fontRenderer.drawString("MFFS Projector", 12, 24, 0x404040);
		fontRenderer.drawString("Typ-Mode", 34, 44, 0x404040);
		fontRenderer.drawString("PowerLink", 34, 66, 0x404040);
		if (projector.hasPowerSource()) {
			fontRenderer.drawString(String.valueOf(projector.getLinkPower()),
					30, 80, 0x404040);
		} else {
			fontRenderer.drawString("No Link/OOR", 30, 80, 0x404040);
		}
	}
}
