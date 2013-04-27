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

package mods.mffs.client.renderer;

import mods.mffs.common.ForceFieldTyps;
import mods.mffs.common.ModularForceFieldSystem;
import mods.mffs.common.tileentity.TileEntityForceField;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class MFFSBlockRenderer implements ISimpleBlockRenderingHandler {
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		if (block == ModularForceFieldSystem.MFFSFieldblock) {
			if (world.getBlockMetadata(x, y, z) == ForceFieldTyps.Camouflage
					.ordinal()) {
				TileEntity te = world.getBlockTileEntity(x, y, z);
				if (te instanceof TileEntityForceField) {
					if (((TileEntityForceField) te).getForcefieldCamoblockid() != -1) {
						if (ForceFieldTyps.Camouflage.ordinal() == ((TileEntityForceField) te)
								.getForcefieldCamoblockmeta()
								&& ((TileEntityForceField) te)
										.getForcefieldCamoblockid() != 327
								&& ((TileEntityForceField) te)
										.getForcefieldCamoblockid() != 326) {
							Block customblock = Block.blocksList[((TileEntityForceField) te)
									.getForcefieldCamoblockid()];
							if (customblock != null) {
								renderer.renderBlockByRenderType(customblock,
										x, y, z);
								return true;
							}
						}
					}

					renderer.renderStandardBlock(block, x, y, z);
					return true;
				}

				renderer.renderStandardBlock(block, x, y, z);
			} else {
				renderer.renderStandardBlock(block, x, y, z);
			}

			return true;
		}

		return true;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return false;
	}

	@Override
	public int getRenderId() {
		return ModularForceFieldSystem.MFFSRENDER_ID;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {

	}
}
