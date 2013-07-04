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
    Matchlighter
 */

package mods.mffs.client.gui;

import mods.mffs.common.SecurityRight;
import mods.mffs.common.container.ContainerAdvSecurityStation;
import mods.mffs.common.item.ItemCardPersonalID;
import mods.mffs.common.tileentity.TileEntityAdvSecurityStation;
import mods.mffs.network.client.NetworkHandlerClient;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.ResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.List;


public class GuiAdvSecurityStation extends GuiContainer {
	private TileEntityAdvSecurityStation tileEntity;
	private SecurityRight hoverSR;
	private boolean editMode = false;

	public GuiAdvSecurityStation(EntityPlayer player,
			TileEntityAdvSecurityStation tileentity) {
		super(new ContainerAdvSecurityStation(player, tileentity));
		this.tileEntity = tileentity;
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
				NetworkHandlerClient.fireTileEntityEvent(tileEntity, 12, "");

			if (i != 54 && i != 42 && i != 58 && i != 14)
				NetworkHandlerClient.fireTileEntityEvent(tileEntity, 11,
						String.valueOf(c));

		} else {
			super.keyTyped(c, i);
		}

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX,
			int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.func_110577_a(new ResourceLocation("mffs:textures/gui/GuiAdvSecstation.png"));
		int w = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		drawTexturedModalRect(w, k, 0, 0, xSize, ySize);

		hoverSR = null;
		int scale = 18;
		int ct = 0;

		ItemStack modCard = tileEntity.getModCardStack();
		if (modCard != null) {
			if (modCard.getItem() instanceof ItemCardPersonalID) {
				List<String> srKeys = new ArrayList<String>();
				srKeys.addAll(SecurityRight.rights.keySet());
				java.util.Collections.sort(srKeys);
				for (String srKey : srKeys) {
					SecurityRight sr = SecurityRight.rights.get(srKey);

					int x = ct % 7 * (scale + 2) + 18;
					int y = ct / 7 * (scale + 2) + 54;

					if (ItemCardPersonalID.hasRight(modCard, sr)) {
						drawSprite(this.guiLeft + x, this.guiTop + y, 0, 0, sr);
					} else {
						drawSprite(this.guiLeft + x, this.guiTop + y, 0, scale,
								sr);
					}
					if ((mouseX >= x + guiLeft && mouseX <= x + guiLeft + scale)
							&& (mouseY >= guiTop + y && mouseY <= guiTop + y
									+ scale)) {
						hoverSR = sr;
					}

					ct++;
				}
			}
		}
	}

	private void drawSprite(int par1, int par2, int par3, int par4,
			SecurityRight sr) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		//this.mc.renderEngine.bindTexture(sr.texture);
		mc.renderEngine.func_110577_a(new ResourceLocation(sr.texture));

		if (sr.texIndex <= 6) {
			par3 += sr.texIndex * 18;
		} else {
			par3 += (sr.texIndex - 7) * 18;
		}

		if (sr.texIndex > 6)
			par4 += 36;

		Tessellator var10 = Tessellator.instance;
		var10.startDrawingQuads();
		var10.addVertexWithUV(par1 + 0, par2 + 18,
				this.zLevel,
				(par3 + 0) * 0.0078125F,
				(par4 + 18) * 0.0078125F);
		var10.addVertexWithUV(par1 + 18, par2 + 18,
				this.zLevel,
				(par3 + 18) * 0.0078125F,
				(par4 + 18) * 0.0078125F);
		var10.addVertexWithUV(par1 + 18, par2 + 0,
				this.zLevel,
				(par3 + 18) * 0.0078125F,
				(par4 + 0) * 0.0078125F);
		var10.addVertexWithUV(par1 + 0, par2 + 0,
				this.zLevel,
				(par3 + 0) * 0.0078125F,
				(par4 + 0) * 0.0078125F);
		var10.draw();
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);

		int xMin = (width - xSize) / 2;
		int yMin = (height - ySize) / 2;

		int x = i - xMin;
		int y = j - yMin;

		if (x >= 12 && y >= 103 && x <= 27 && y <= 118) {
			NetworkHandlerClient.fireTileEntityEvent(tileEntity, 101, "null");
		}

		if (x >= 68 && y >= 103 && x <= 83 && y <= 118) {
			NetworkHandlerClient.fireTileEntityEvent(tileEntity, 102, "null");
		}

		if (editMode) {
			editMode = false;
		} else if (x >= 120 && y >= 4 && x <= 250 && y <= 18) {
			NetworkHandlerClient.fireTileEntityEvent(tileEntity, 10, "null");
			editMode = true;
		}
		if (hoverSR != null) {
			NetworkHandlerClient.fireTileEntityEvent(tileEntity, 100,
					hoverSR.rightKey);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString("MFFS Security Station:", 8, 8, 0x404040);
		fontRenderer.drawString(tileEntity.getDeviceName(), 125, 8, 0x404040);
		fontRenderer.drawString("Master", 200, 38, 0x404040);
		fontRenderer.drawString("Rights Allocation", 52, 35, 0x404040);
		fontRenderer.drawString("Copy->", 109, 106, 0x404040);
		fontRenderer.drawString("validity", 31, 106, 0x404040);

		if (hoverSR != null) {
			List list = new ArrayList();
			list.add(hoverSR.name);
			if (list.size() > 0) {
				GL11.glDisable(GL12.GL_RESCALE_NORMAL);
				RenderHelper.disableStandardItemLighting();
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);

				int j2 = 0;
				for (int k2 = 0; k2 < list.size(); k2++) {
					int i3 = fontRenderer.getStringWidth((String) list.get(k2));
					if (i3 > j2) {
						j2 = i3;
					}
				}

				int l2 = (mouseX - guiLeft) + 12;
				int j3 = mouseY - guiTop - 12;
				int k3 = j2;
				int l3 = 8;
				if (list.size() > 1) {
					l3 += 2 + (list.size() - 1) * 10;
				}

				zLevel = 300F;
				itemRenderer.zLevel = 300F;
				int i4 = 0xf0100010;
				drawGradientRect(l2 - 3, j3 - 4, l2 + k3 + 3, j3 - 3, i4, i4);
				drawGradientRect(l2 - 3, j3 + l3 + 3, l2 + k3 + 3, j3 + l3 + 4,
						i4, i4);
				drawGradientRect(l2 - 3, j3 - 3, l2 + k3 + 3, j3 + l3 + 3, i4,
						i4);
				drawGradientRect(l2 - 4, j3 - 3, l2 - 3, j3 + l3 + 3, i4, i4);
				drawGradientRect(l2 + k3 + 3, j3 - 3, l2 + k3 + 4, j3 + l3 + 3,
						i4, i4);
				int j4 = 0x505000ff;
				int k4 = (j4 & 0xfefefe) >> 1 | j4 & 0xff000000;
				drawGradientRect(l2 - 3, (j3 - 3) + 1, (l2 - 3) + 1,
						(j3 + l3 + 3) - 1, j4, k4);
				drawGradientRect(l2 + k3 + 2, (j3 - 3) + 1, l2 + k3 + 3, (j3
						+ l3 + 3) - 1, j4, k4);
				drawGradientRect(l2 - 3, j3 - 3, l2 + k3 + 3, (j3 - 3) + 1, j4,
						j4);
				drawGradientRect(l2 - 3, j3 + l3 + 2, l2 + k3 + 3, j3 + l3 + 3,
						k4, k4);
				for (int l4 = 0; l4 < list.size(); l4++) {
					String s = (String) list.get(l4);
					if (l4 == 0) {
						s = (new StringBuilder()).append("\247F").append(s)
								.toString();
					} else {
						s = (new StringBuilder()).append("\2477").append(s)
								.toString();
					}
					fontRenderer.drawStringWithShadow(s, l2, j3, -1);
					if (l4 == 0) {
						j3 += 2;
					}
					j3 += 10;
				}

				zLevel = 0.0F;
				itemRenderer.zLevel = 0.0F;
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
		}

	}
}
