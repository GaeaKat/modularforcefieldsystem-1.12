package com.nekokittygames.mffs.datagen;

import com.nekokittygames.mffs.common.config.MFFSConfig;
import com.nekokittygames.mffs.common.init.MFFSBlocks;
import com.nekokittygames.mffs.common.init.MFFSItems;
import com.nekokittygames.mffs.common.init.MFFSRegistration;
import com.nekokittygames.mffs.common.libs.LibMisc;
import com.nekokittygames.mffs.datagen.conditions.GeneratorEnabled;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.util.function.Consumer;

public class MFFSRecipes extends RecipeProvider {
    public MFFSRecipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        SmeltingRecipes(consumer);
        CraftingRecipes(consumer);
    }

    private void CraftingRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(MFFSRegistration.Blocks.GENERATOR.get())
                .addCriterion("monazit_has",hasItem(MFFSRegistration.Items.MONAZIT_CRYSTAL.get()))
                .key('I', Tags.Items.INGOTS_IRON).key('F', Blocks.FURNACE).key('M',MFFSRegistration.Items.MONAZIT_CRYSTAL.get())
                .patternLine("III").patternLine("IMI").patternLine("IFI")
                .build(iFinishedRecipe -> ConditionalRecipe.builder().addCondition(GeneratorEnabled.INSTANCE).addRecipe(iFinishedRecipe)
                        .build(consumer,new ResourceLocation(LibMisc.MOD_ID,"monazit_generator")));

    }

    private void SmeltingRecipes(Consumer<IFinishedRecipe> consumer) {
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromStacks(new ItemStack(MFFSRegistration.Blocks.MONAZIT_ORE.get())), MFFSRegistration.Items.MONAZIT_CRYSTAL.get(),0.7f,200).addCriterion("monazit_ore", InventoryChangeTrigger.Instance.forItems(MFFSRegistration.Blocks.MONAZIT_ORE.get())).build(consumer);
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromStacks(new ItemStack(MFFSRegistration.Blocks.MONAZIT_ORE.get())),MFFSRegistration.Items.MONAZIT_CRYSTAL.get(),0.7f,100).addCriterion("monazit_ore", InventoryChangeTrigger.Instance.forItems(MFFSRegistration.Blocks.MONAZIT_ORE.get())).build(consumer,new ResourceLocation(LibMisc.MOD_ID,"monazit_crystal_blasting"));
    }
}
