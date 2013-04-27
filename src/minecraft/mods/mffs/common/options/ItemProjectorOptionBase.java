package mods.mffs.common.options;

import java.util.ArrayList;
import java.util.List;

import mods.mffs.common.ModularForceFieldSystem;
import mods.mffs.common.ProjectorTyp;
import mods.mffs.common.modules.ItemProjectorModuleAdvCube;
import mods.mffs.common.modules.ItemProjectorModuleContainment;
import mods.mffs.common.modules.ItemProjectorModuleCube;
import mods.mffs.common.modules.ItemProjectorModuleDeflector;
import mods.mffs.common.modules.ItemProjectorModuleSphere;
import mods.mffs.common.modules.ItemProjectorModuleTube;
import mods.mffs.common.modules.ItemProjectorModuleWall;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;


public abstract class ItemProjectorOptionBase extends Item {
	public ItemProjectorOptionBase(int i) {
		super(i);
		setMaxStackSize(8);
		setCreativeTab(ModularForceFieldSystem.MFFSTab);
		instances.add(this);
	}

	private static List<ItemProjectorOptionBase> instances = new ArrayList<ItemProjectorOptionBase>();

	public static List<ItemProjectorOptionBase> get_instances() {
		return instances;
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,
			List info, boolean b) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
				|| Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			info.add("compatible with:");

			if (ItemProjectorModuleWall.supportsOption(this))
				info.add(ProjectorTyp.getDisplayName(ProjectorTyp.wall));
			if (ItemProjectorModuleWall.supportsOption(this))
				info.add(ProjectorTyp
						.getDisplayName(ProjectorTyp.diagonallywall));
			if (ItemProjectorModuleDeflector.supportsOption(this))
				info.add(ProjectorTyp.getDisplayName(ProjectorTyp.deflector));
			if (ItemProjectorModuleTube.supportsOption(this))
				info.add(ProjectorTyp.getDisplayName(ProjectorTyp.tube));
			if (ItemProjectorModuleSphere.supportsOption(this))
				info.add(ProjectorTyp.getDisplayName(ProjectorTyp.sphere));
			if (ItemProjectorModuleCube.supportsOption(this))
				info.add(ProjectorTyp.getDisplayName(ProjectorTyp.cube));
			if (ItemProjectorModuleAdvCube.supportsOption(this))
				info.add(ProjectorTyp.getDisplayName(ProjectorTyp.AdvCube));
			if (ItemProjectorModuleContainment.supportsOption(this))
				info.add(ProjectorTyp.getDisplayName(ProjectorTyp.containment));

		} else {
			info.add("compatible with: (Hold Shift)");
		}
	}

}
