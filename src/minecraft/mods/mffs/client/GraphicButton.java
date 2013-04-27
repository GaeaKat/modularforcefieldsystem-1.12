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

package mods.mffs.client;

import mods.mffs.common.tileentity.TileEntityAreaDefenseStation;
import mods.mffs.common.tileentity.TileEntityCapacitor;
import mods.mffs.common.tileentity.TileEntityControlSystem;
import mods.mffs.common.tileentity.TileEntityConverter;
import mods.mffs.common.tileentity.TileEntityMachines;
import mods.mffs.common.tileentity.TileEntityProjector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

public class GraphicButton extends GuiButton {
	private final TileEntity tileEntity;
	private final int typ;

	public GraphicButton(int par1, int par2, int par3, TileEntity tileEntity,
			int typ) {
		super(par1, par2, par3, 16, 16, "");
		this.tileEntity = tileEntity;
		this.typ = typ;
	}

	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
		if (this.drawButton) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1Minecraft.renderEngine
					.getTexture("/mods/mffs/textures/gui/items.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			if ((tileEntity instanceof TileEntityMachines) && typ == 0) {
				this.drawTexturedModalRect(
						this.xPosition,
						this.yPosition,
						80 + (((TileEntityMachines) tileEntity).getSwitchModi() * 16),
						112, this.width, this.height);
			}

			if (tileEntity instanceof TileEntityConverter) {
				if (typ == 1) {
					this.drawTexturedModalRect(this.xPosition, this.yPosition,
							80 + (((TileEntityConverter) tileEntity)
									.getIC_Output() * 16), 128, this.width,
							this.height);
				}
				if (typ == 2) {
					this.drawTexturedModalRect(this.xPosition, this.yPosition,
							80 + (((TileEntityConverter) tileEntity)
									.getUE_Output() * 16), 128, this.width,
							this.height);
				}
			}

			if (tileEntity instanceof TileEntityControlSystem) {
				if (((TileEntityControlSystem) tileEntity).getStackInSlot(1) != null) {
					if (typ == 1) {
						if (((TileEntityControlSystem) tileEntity)
								.getRemoteActive())
							this.drawTexturedModalRect(this.xPosition,
									this.yPosition, 176, 80, this.width,
									this.height);

						if (!((TileEntityControlSystem) tileEntity)
								.getRemoteActive())
							this.drawTexturedModalRect(this.xPosition,
									this.yPosition, 192, 80, this.width,
									this.height);
					}
					if (typ == 2)
						if (((TileEntityControlSystem) tileEntity)
								.getRemoteSwitchModi() > 0)
							this.drawTexturedModalRect(
									this.xPosition,
									this.yPosition,
									80 + (((TileEntityControlSystem) tileEntity)
											.getRemoteSwitchModi() * 16), 112,
									this.width, this.height);

					if (typ == 3)
						if (((TileEntityControlSystem) tileEntity)
								.getRemoteSwitchModi() == 3)
							if (((TileEntityControlSystem) tileEntity)
									.getRemoteSwitchValue()) {
								this.drawTexturedModalRect(this.xPosition,
										this.yPosition, 208, 80, this.width,
										this.height);
							} else {
								this.drawTexturedModalRect(this.xPosition,
										this.yPosition, 224, 80, this.width,
										this.height);
							}

				}
			}

			if ((tileEntity instanceof TileEntityAreaDefenseStation)) {
				if (typ == 1)
					this.drawTexturedModalRect(this.xPosition, this.yPosition,
							176 + (((TileEntityAreaDefenseStation) tileEntity)
									.getcontratyp() * 16), 80, this.width,
							this.height);
				if (typ == 2)
					this.drawTexturedModalRect(this.xPosition, this.yPosition,
							64 + (((TileEntityAreaDefenseStation) tileEntity)
									.getActionmode() * 16), 96, this.width,
							this.height);
				if (typ == 3)
					this.drawTexturedModalRect(this.xPosition, this.yPosition,
							160 + (((TileEntityAreaDefenseStation) tileEntity)
									.getScanmode() * 16), 96, this.width,
							this.height);
			}

			if (tileEntity instanceof TileEntityCapacitor) {
				if (typ == 1)
					this.drawTexturedModalRect(this.xPosition, this.yPosition,
							96 + (((TileEntityCapacitor) tileEntity)
									.getPowerlinkmode() * 16), 80, this.width,
							this.height);
			}

			if (tileEntity instanceof TileEntityProjector) {
				if (typ == 1)
					this.drawTexturedModalRect(this.xPosition, this.yPosition,
							0 + (((TileEntityProjector) tileEntity)
									.getaccesstyp() * 16), 80, this.width,
							this.height);
			}

		}
	}
}
