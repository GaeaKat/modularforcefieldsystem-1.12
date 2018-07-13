package com.nekokittygames.mffs.common.options;

import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.ProjectorTyp;
import com.nekokittygames.mffs.common.item.ItemMFFSBase;
import com.nekokittygames.mffs.common.modules.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;


public abstract class ItemProjectorOptionBase extends ItemMFFSBase {
	public ItemProjectorOptionBase(final String itemName) {
		super(itemName);
		setMaxStackSize(8);
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
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> info, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
				|| Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			info.add(I18n.format("itemInfo.compatibleWith"));

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
			info.add(I18n.format("itemInfo.compatibleWithHoldShift"));
		}
	}

}
