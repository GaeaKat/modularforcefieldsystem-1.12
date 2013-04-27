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
    Shedar - initial implementation
    Thunderdark - adaptable and extension
 */

package mods.mffs.client.renderer;

import mods.mffs.common.tileentity.TileEntityCapacitor;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;


public class TECapacitorRenderer extends TileEntitySpecialRenderer {
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y,
			double z, float f) {
		if (tileEntity instanceof TileEntityCapacitor) {
			TileEntityCapacitor topview = (TileEntityCapacitor) tileEntity;
			GL11.glPushMatrix();
			GL11.glPolygonOffset(-10, -10);
			GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
			int side = topview.getSide();
			float dx = 1F / 16;
			float dz = 1F / 16;
			float displayWidth = 1 - 2F / 16;
			float displayHeight = 1 - 2F / 16;
			GL11.glTranslatef((float) x, (float) y, (float) z);
			switch (side) {
			case 1:

				break;
			case 0:
				GL11.glTranslatef(1, 1, 0);
				GL11.glRotatef(180, 1, 0, 0);
				GL11.glRotatef(180, 0, 1, 0);

				break;
			case 3:
				GL11.glTranslatef(0, 1, 0);
				GL11.glRotatef(0, 0, 1, 0);
				GL11.glRotatef(90, 1, 0, 0);

				break;
			case 2:
				GL11.glTranslatef(1, 1, 1);
				GL11.glRotatef(180, 0, 1, 0);
				GL11.glRotatef(90, 1, 0, 0);

				break;
			case 5:
				GL11.glTranslatef(0, 1, 1);
				GL11.glRotatef(90, 0, 1, 0);
				GL11.glRotatef(90, 1, 0, 0);

				break;
			case 4:
				GL11.glTranslatef(1, 1, 0);
				GL11.glRotatef(-90, 0, 1, 0);
				GL11.glRotatef(90, 1, 0, 0);

				break;
			}
			GL11.glTranslatef(dx + displayWidth / 2, 1F, dz + displayHeight / 2);
			GL11.glRotatef(-90, 1, 0, 0);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			FontRenderer fontRenderer = this.getFontRenderer();
			int maxWidth = 1;
			String header = "MFFS Capacitor";
			maxWidth = Math.max(fontRenderer.getStringWidth(header), maxWidth);
			maxWidth += 4;
			int lineHeight = fontRenderer.FONT_HEIGHT + 2;
			int requiredHeight = lineHeight * 1;
			float scaleX = displayWidth / maxWidth;
			float scaleY = displayHeight / requiredHeight;
			float scale = Math.min(scaleX, scaleY);
			GL11.glScalef(scale, -scale, scale);
			GL11.glDepthMask(false);
			int offsetX;
			int offsetY;
			int realHeight = (int) Math.floor(displayHeight / scale);
			int realWidth = (int) Math.floor(displayWidth / scale);

			if (scaleX < scaleY) {
				offsetX = 2;
				offsetY = (realHeight - requiredHeight) / 2;
			} else {
				offsetX = (realWidth - maxWidth) / 2 + 2;
				offsetY = 0;
			}
			GL11.glDisable(GL11.GL_LIGHTING);
			fontRenderer.drawString(header, offsetX - realWidth / 2, 1
					+ offsetY - realHeight / 2 + -2 * lineHeight, 1);
			fontRenderer.drawString("capacity: ", offsetX - realWidth / 2, 1
					+ offsetY - realHeight / 2 + 0 * lineHeight, 1);
			fontRenderer.drawString(
					String.valueOf(topview.getPercentageStorageCapacity())
							.concat(" % "),
					offsetX
							+ realWidth
							/ 2
							- offsetX
							- fontRenderer.getStringWidth(String.valueOf(
									topview.getPercentageStorageCapacity())
									.concat(" % ")), offsetY - realHeight / 2
							- 0 * lineHeight, 1);
			fontRenderer.drawString("range: ", offsetX - realWidth / 2, 1
					+ offsetY - realHeight / 2 + 1 * lineHeight, 1);
			fontRenderer.drawString(
					String.valueOf(topview.getTransmitRange()),
					offsetX
							+ realWidth
							/ 2
							- offsetX
							- fontRenderer.getStringWidth(String
									.valueOf(topview.getTransmitRange())),
					offsetY - realHeight / 2 + 1 * lineHeight, 1);
			fontRenderer.drawString("linked device: ", offsetX - realWidth / 2,
					1 + offsetY - realHeight / 2 + 2 * lineHeight, 1);
			fontRenderer.drawString(
					String.valueOf(topview.getLinketProjektor()),
					offsetX
							+ realWidth
							/ 2
							- offsetX
							- fontRenderer.getStringWidth(String
									.valueOf(topview.getLinketProjektor())),
					offsetY - realHeight / 2 + 2 * lineHeight, 1);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDepthMask(true);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
			GL11.glPopMatrix();
		}
	}
}
