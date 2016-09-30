package com.nekokittygames.mffs.common.block;

import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.tileentity.TileEntityControlSystem;
import com.nekokittygames.mffs.libs.LibBlockNames;
import com.nekokittygames.mffs.libs.LibMisc;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockControlSystem extends BlockMFFSBase {

	public BlockControlSystem() {
		super();
		setUnlocalizedName( LibMisc.UNLOCALIZED_PREFIX+ LibBlockNames.CONTROL_SYSTEM);
		setRegistryName(LibBlockNames.CONTROL_SYSTEM);
	}

	@Override
	public TileEntity createNewTileEntity(World world,int meta) {
		return new TileEntityControlSystem();
	}


}
