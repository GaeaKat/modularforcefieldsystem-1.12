package com.nekokittygames.mffs.common;

import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;

import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import com.nekokittygames.mffs.common.ModularForceFieldSystem;

public class CompactConditionFactory implements IConditionFactory{

	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		//ModularForceFieldSystem.log.info(json.get("modid").toString() + "COMPACT:");
		switch(json.get("modid").toString()) {
		case "\"ic2\"":
			//ModularForceFieldSystem.log.info((ModularForceFieldSystem.ic2Found && ModularForceFieldSystem.enableIC2Recipes));
			return () -> ModularForceFieldSystem.ic2Found && ModularForceFieldSystem.enableIC2Recipes;
		case "\"thermalexpansion\"":
			return () -> ModularForceFieldSystem.thermalExpansionFound && ModularForceFieldSystem.enableTERecipes;
		case "\"enderio\"":
			return () -> ModularForceFieldSystem.enderIoFound && ModularForceFieldSystem.enableEIRecipes;
		default:
			return () -> false;
		}
	}

}
