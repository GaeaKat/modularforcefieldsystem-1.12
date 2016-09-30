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
import com.nekokittygames.mffs.common.container.ContainerControlSystem;
import com.nekokittygames.mffs.common.tileentity.TileEntityControlSystem;
import com.nekokittygames.mffs.network.client.NetworkHandlerClient;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;


public class GuiControlSystem extends GuiContainer {

	private TileEntityControlSystem ControlSystem;
	private boolean editMode = false;
	private EntityPlayer player;

	public GuiControlSystem(EntityPlayer player,
			TileEntityControlSystem tileentity) {
		super(new ContainerControlSystem(player, tileentity));
		ControlSystem = tileentity;
		xSize = 256;
		ySize = 216;
		this.player = player;
	}

	@Override
	protected void keyTyped(char c, int i) throws IOException{

		if (i != 1 && editMode) {
			if (c == 13) {
				editMode = false;
				return;
			}

			if (i == 14)
				NetworkHandlerClient.fireTileEntityEvent(ControlSystem, 12, "");

			if (i != 54 && i != 42 && i != 58 && i != 14)
				NetworkHandlerClient.fireTileEntityEvent(ControlSystem, 11,
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
		} else if (x >= 115 && y >= 5 && x <= 234 && y <= 19) {
			NetworkHandlerClient.fireTileEntityEvent(ControlSystem, 10, "null");
			editMode = true;
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(new ResourceLocation("modularforcefieldsystem:textures/gui/GuiControlSystem.png"));
		int w = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		drawTexturedModalRect(w, k, 0, 0, xSize, ySize);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton.id == 103) {
			NetworkHandlerClient.fireTileEntityEvent(ControlSystem,
					guibutton.id, player.getUniqueID().toString());
		} else {
			NetworkHandlerClient.fireTileEntityEvent(ControlSystem,
					guibutton.id, "");
		}
	}

	@Override
	public void initGui() {

		buttonList.add(new GraphicButton(100, (width / 2) - 115,
				(height / 2) - 45, ControlSystem, 1));
		buttonList.add(new GraphicButton(101, (width / 2) - 115,
				(height / 2) - 25, ControlSystem, 2));
		buttonList.add(new GraphicButton(102, (width / 2) - 115,
				(height / 2) - 5, ControlSystem, 3));
		buttonList.add(new GuiButton(103, (width / 2) + -65, (height / 2) - 8,
				100, 20, "Open Remote Gui"));
		super.initGui();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRendererObj.drawString("MFFS Control System", 8, 9, 0x404040);
		fontRendererObj
				.drawString(ControlSystem.getDeviceName(), 123, 9, 0x404040);

		fontRendererObj.drawString("DataLink", 190, 54, 0x404040);
		fontRendererObj.drawString("Reader", 190, 65, 0x404040);

		fontRendererObj.drawString("Name: " + ControlSystem.getRemoteDeviceName(),
				15, 30, 0x404040);
		fontRendererObj.drawString("Type:  " + ControlSystem.getRemoteDeviceTyp(),
				15, 45, 0x404040);
		if (ControlSystem.getStackInSlot(1) != null) {

			RenderHelper.enableGUIStandardItemLighting();
			itemRender.renderItemIntoGUI(
					new ItemStack(ModularForceFieldSystem.MFFSItemSecLinkCard),
					40, 59);
			itemRender.renderItemIntoGUI(
					new ItemStack(ModularForceFieldSystem.MFFSitemfc), 100, 59);
			RenderHelper.disableStandardItemLighting();
			if (ControlSystem.getRemoteSecurityStationlink()) {
				fontRendererObj.drawString("linked", 60, 64, 0x228B22);
			} else {
				fontRendererObj.drawString("linked", 60, 64, 0x8B1A1A);
			}
			if (ControlSystem.getRemotehasPowersource()) {
				fontRendererObj.drawString("linked", 120, 64, 0x228B22);
				fontRendererObj.drawString(
						"Power left: " + ControlSystem.getRemotePowerleft()
								+ "%", 40, 80, 0x404040);
			} else {
				fontRendererObj.drawString("linked", 120, 64, 0x8B1A1A);
			}

			if (ControlSystem.getRemoteGUIinRange()) {
				fontRendererObj.drawString("OK", 40, 107, 0x228B22);
			} else {
				fontRendererObj.drawString("OOR", 40, 107, 0x8B1A1A);
			}

		}
	}
}
