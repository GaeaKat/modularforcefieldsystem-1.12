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

import mods.mffs.common.tileentity.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.ResourceLocation;
import net.minecraft.tileentity.TileEntity;

public class GraphicButton extends GuiButton {
	private final TileEntity tileEntity;
	private final int typ;

	private final int SWITCH_MODE_TEXTURE_SIZE = 96;
	private final int CONVERTER_OUTPUT_TEXTURE_SIZE = 32;
	private final int CONTROL_SYSTEM_TEXTURE_SIZE = 32;
	private final int DEFENSE_STATION_TEXTURE_SIZE = 96;
	private final int CAPACITOR_TEXTURE_SIZE = 48;
	private final int PROJECTOR_TEXTURE_SIZE = 64;

	public GraphicButton(int par1, int par2, int par3, TileEntity tileEntity,
			int typ) {
		super(par1, par2, par3, 16, 16, "");
		this.tileEntity = tileEntity;
		this.typ = typ;
	}

	private void drawButtonRect(int x, int y, int frameX, int frameY, int width, int height, int textureSize)
	{
		double uvMult = 1.0 / (double)textureSize;

		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();

		tessellator.addVertexWithUV(x, y + height, this.zLevel, frameX * uvMult, (frameY + height) * uvMult);
		tessellator.addVertexWithUV(x + width, y + height, this.zLevel, (frameX + width) * uvMult,
				(frameY + height) * uvMult);
		tessellator.addVertexWithUV(x + width, y, this.zLevel, (frameX + width) * uvMult, frameY * uvMult);
		tessellator.addVertexWithUV(x, y, this.zLevel, frameX * uvMult, frameY * uvMult);

		tessellator.draw();
	}

	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
		if (this.drawButton) {
			if ((tileEntity instanceof TileEntityMachines) && typ == 0) {
				//par1Minecraft.renderEngine.bindTexture("/mods/mffs/textures/gui/SwitchModes.png");
				par1Minecraft.renderEngine.func_110577_a(new ResourceLocation("mffs:textures/gui/SwitchModes.png"));

				drawButtonRect(this.xPosition, this.yPosition, (((TileEntityMachines) tileEntity).getSwitchModi() *
						16), 0, this.width, this.height, SWITCH_MODE_TEXTURE_SIZE);
			}

			if (tileEntity instanceof TileEntityConverter) {
				if (typ == 1) {
					//par1Minecraft.renderEngine.bindTexture("/mods/mffs/textures/gui/ConverterOutput.png");
					par1Minecraft.renderEngine.func_110577_a(new ResourceLocation("mffs:textures/gui/ConverterOutput.png"));

					drawButtonRect(xPosition, yPosition, (((TileEntityConverter) tileEntity).getIC_Output() * 16), 0,
							width, height, CONVERTER_OUTPUT_TEXTURE_SIZE);
				}
			}

			if (tileEntity instanceof TileEntityControlSystem) {
				if (((TileEntityControlSystem) tileEntity).getStackInSlot(1) != null) {
					if (typ == 1) {
						boolean active = ((TileEntityControlSystem)tileEntity).getRemoteActive();

						//par1Minecraft.renderEngine.bindTexture("/mods/mffs/textures/gui/ControlSystemButtons.png");
						par1Minecraft.renderEngine.func_110577_a(new ResourceLocation
								("mffs:textures/gui/ControlSystemButtons.png"));

						drawButtonRect(xPosition, yPosition,
								(active ? 0 : 16), 0, width, height, CONTROL_SYSTEM_TEXTURE_SIZE);
					}

					if (typ == 2)
						if (((TileEntityControlSystem) tileEntity)
								.getRemoteSwitchModi() > 0) {
							//par1Minecraft.renderEngine.bindTexture("/mods/mffs/textures/gui/SwitchModes.png");
							par1Minecraft.renderEngine.func_110577_a(new ResourceLocation
									("mffs:textures/gui/Switchmodes.png"));

							drawButtonRect(this.xPosition, this.yPosition, (((TileEntityControlSystem)tileEntity)
									.getRemoteSwitchModi() *
									16), 0, this.width, this.height, SWITCH_MODE_TEXTURE_SIZE);
						}

					if (typ == 3)
						if (((TileEntityControlSystem) tileEntity)
								.getRemoteSwitchModi() == 3)
						{
							boolean switchValue = ((TileEntityControlSystem)tileEntity).getRemoteSwitchValue();

							//par1Minecraft.renderEngine.bindTexture("/mods/mffs/textures/gui/ControlSystemButtons" +
							// ".png");
							par1Minecraft.renderEngine.func_110577_a(new ResourceLocation
									("mffs:textures/gui/ControlSystemButtons.png"));

							drawButtonRect(xPosition, yPosition,
									(switchValue ? 0 : 16), 16, width, height, CONTROL_SYSTEM_TEXTURE_SIZE);
						}
				}
			}

			if ((tileEntity instanceof TileEntityAreaDefenseStation)) {
				if (typ == 1) {
					//par1Minecraft.renderEngine.bindTexture("/mods/mffs/textures/gui/ControlSystemButtons.png");
					par1Minecraft.renderEngine.func_110577_a(new ResourceLocation
							("mffs:textures/gui/ControlSystemButtons.png"));

					drawButtonRect(xPosition, yPosition, (((TileEntityAreaDefenseStation)tileEntity).getcontratyp())
							* 16,
							0, width, height, CONTROL_SYSTEM_TEXTURE_SIZE);
				}
				if (typ == 2) {
					//par1Minecraft.renderEngine.bindTexture("/mods/mffs/textures/gui/DefenseStationButtons.png");
					par1Minecraft.renderEngine.func_110577_a(new ResourceLocation
							("mffs:textures/gui/DefenseStationButtons.png"));

					drawButtonRect(xPosition, yPosition, (((TileEntityAreaDefenseStation)tileEntity).getActionmode()
							* 16), 0, width, height, DEFENSE_STATION_TEXTURE_SIZE);
				}
				if (typ == 3) {
					//par1Minecraft.renderEngine.bindTexture("/mods/mffs/textures/gui/DefenseStationButtons.png");
					par1Minecraft.renderEngine.func_110577_a(new ResourceLocation
							("mffs:textures/gui/DefenseStationButtons.png"));

					drawButtonRect(xPosition, yPosition, (((TileEntityAreaDefenseStation)tileEntity).getScanmode() *
							16), 16, width, height, DEFENSE_STATION_TEXTURE_SIZE);
				}
			}

			if (tileEntity instanceof TileEntityCapacitor) {
				if (typ == 1)
				{
					//par1Minecraft.renderEngine.bindTexture("/mods/mffs/textures/gui/CapacitorButtons.png");
					par1Minecraft.renderEngine.func_110577_a(new ResourceLocation
							("mffs:textures/gui/CapacitorButtons.png"));

					int powerLinkMode = ((TileEntityCapacitor)tileEntity).getPowerlinkmode();

					drawButtonRect(xPosition, yPosition,
							(powerLinkMode < 3 ? powerLinkMode : (powerLinkMode - 3)) * 16,
							(powerLinkMode < 3 ? 0 : 16), width, height,
							CAPACITOR_TEXTURE_SIZE);
				}
			}

			if (tileEntity instanceof TileEntityProjector) {
				if (typ == 1)
				{
					//par1Minecraft.renderEngine.bindTexture("/mods/mffs/textures/gui/ProjectorButtons.png");
					par1Minecraft.renderEngine.func_110577_a(new ResourceLocation
							("mffs:textures/gui/ProjectorButtons.png"));

					drawButtonRect(xPosition, yPosition,
							((TileEntityProjector)tileEntity).getaccesstyp() * 16,
							0,
							width, height,
							PROJECTOR_TEXTURE_SIZE);
				}
			}

		}
	}
}
