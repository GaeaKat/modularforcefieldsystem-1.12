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

package com.nekokittygames.mffs.common.multitool;

import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.tileentity.TileEntityForceField;
import com.nekokittygames.mffs.common.tileentity.TileEntityMachines;
import com.nekokittygames.mffs.libs.LibItemNames;
import com.nekokittygames.mffs.libs.LibMisc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDebugger extends ItemMultitool {
	protected StringBuffer info = new StringBuffer();

	public ItemDebugger() {
		super(3, false,LibItemNames.MULTITOOL_DEBUGGER);
	}



	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		TileEntity tileEntity = world.getTileEntity(pos);

		if (world.isRemote) {

			if (tileEntity instanceof TileEntityMachines) {
				System.out.println("client"
						+ ((TileEntityMachines) tileEntity).isActive());
			}

			if(tileEntity instanceof TileEntityForceField)
			{
				TileEntityForceField tef=(TileEntityForceField)tileEntity;
				ModularForceFieldSystem.log.info("Client Block: "+tef.getForcefieldCamoblock());
				ModularForceFieldSystem.log.info("Client Block Meta: "+tef.getForcefieldCamoblockmeta());
			}

		} else {

			if (tileEntity instanceof TileEntityMachines) {
				System.out.println("server"
						+ ((TileEntityMachines) tileEntity).isActive());
			}
			if(tileEntity instanceof TileEntityForceField)
			{
				TileEntityForceField tef=(TileEntityForceField)tileEntity;
				ModularForceFieldSystem.log.info("Server Block: "+tef.getForcefieldCamoblock());
				ModularForceFieldSystem.log.info("Server sBlock Meta: "+tef.getForcefieldCamoblockmeta());
				tef.markDirty();
			}
			ModularForceFieldSystem.log.info("Tile Entity: "+((tileEntity!=null)?tileEntity.toString():"null"));
		}

		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		return ActionResult.newResult(EnumActionResult.FAIL,playerIn.getHeldItem(handIn));
	}


}
