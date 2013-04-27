package mods.mffs.common.block;

import mods.mffs.common.ModularForceFieldSystem;
import mods.mffs.common.tileentity.TileEntityControlSystem;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockControlSystem extends BlockMFFSBase {

	public BlockControlSystem(int i) {
		super(i);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityControlSystem();
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		icons[0] = icons[1] = iconRegister
				.registerIcon("mffs:ControlSystem/Inactive"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));
		icons[2] = icons[3] = iconRegister
				.registerIcon("mffs:ControlSystem/Active"
						+ (ModularForceFieldSystem.graphicsStyle == 1 ? "_32"
								: ""));

		blockIcon = icons[0];
	}
}
