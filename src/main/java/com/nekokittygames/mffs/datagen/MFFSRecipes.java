package com.nekokittygames.mffs.datagen;

import com.nekokittygames.mffs.common.init.MFFSBlocks;
import com.nekokittygames.mffs.common.init.MFFSItems;
import com.nekokittygames.mffs.common.libs.LibMisc;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

public class MFFSRecipes extends RecipeProvider {
    public MFFSRecipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        SmeltingRecipes(consumer);
    }

    private void SmeltingRecipes(Consumer<IFinishedRecipe> consumer) {
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromStacks(new ItemStack(MFFSBlocks.MONAZIT_ORE)), MFFSItems.MONAZIT_CRYSTAL,0.7f,200).addCriterion("monazit_ore", InventoryChangeTrigger.Instance.forItems(MFFSBlocks.MONAZIT_ORE)).build(consumer);
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromStacks(new ItemStack(MFFSBlocks.MONAZIT_ORE)),MFFSItems.MONAZIT_CRYSTAL,0.7f,100).addCriterion("monazit_ore", InventoryChangeTrigger.Instance.forItems(MFFSBlocks.MONAZIT_ORE)).build(consumer,new ResourceLocation(LibMisc.MOD_ID,"monazit_crystal_blasting"));
    }
}
