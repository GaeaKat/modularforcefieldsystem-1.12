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

package com.nekokittygames.mffs.client.renderer;

import com.nekokittygames.mffs.common.ForceFieldTyps;
import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.block.BlockForceField;
import com.nekokittygames.mffs.common.block.ModBlocks;
import com.nekokittygames.mffs.common.tileentity.TileEntityForceField;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class MFFSBlockRenderer extends TileEntitySpecialRenderer<TileEntityForceField> {


	@Override
	public void render(TileEntityForceField te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(getWorld().getBlockState(new BlockPos(x,y,z)).getBlock()== ModBlocks.FORCE_FIELD) {
			if (getWorld().getBlockState(new BlockPos(x, y, z)).getValue(BlockForceField.FORCEFIELD_TYPE) == ForceFieldTyps.Camouflage) {
				IBlockState state = te.getForcefieldCamoblock().getStateFromMeta(te.getForcefieldCamoblockmeta());
				BufferBuilder buffer = Tessellator.getInstance().getBuffer();
				buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(state, new BlockPos(x, y, z), getWorld(), buffer);
				Tessellator.getInstance().draw();
			}
		}
	}
}
