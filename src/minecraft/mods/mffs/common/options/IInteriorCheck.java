package mods.mffs.common.options;

import mods.mffs.api.PointXYZ;
import mods.mffs.common.tileentity.TileEntityProjector;
import net.minecraft.world.World;

public interface IInteriorCheck {

	public void checkInteriorBlock(PointXYZ png, World world,
			TileEntityProjector Projector);

}
