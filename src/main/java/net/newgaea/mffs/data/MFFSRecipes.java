package net.newgaea.mffs.data;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.newgaea.mffs.api.MFFSTags;
import net.newgaea.mffs.common.init.MFFSBlocks;
import net.newgaea.mffs.common.init.MFFSItems;
import net.newgaea.mffs.common.libs.LibBlocks;
import net.newgaea.mffs.common.libs.LibMisc;
import net.newgaea.mffs.common.recipes.conditions.GeneratorEnabled;

import java.util.function.Consumer;

public class MFFSRecipes extends RecipeProvider {

    public MFFSRecipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        //super.registerRecipes(consumer);
        smeltingRecipes(consumer);
        craftingRecipes(consumer);
    }

    private void craftingRecipes(Consumer<IFinishedRecipe> consumer) {
        generateMachineRecipes(consumer);
        generateCraftingMatsRecipes(consumer);

    }

    private void generateMachineRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(MFFSBlocks.GENERATOR.get()).addCriterion("monazit_has",hasItem(MFFSItems.MONAZIT_CRYSTAL.get()))
                .key('I', Tags.Items.INGOTS_IRON)
                .key('F', Blocks.FURNACE)
                .key('M',MFFSItems.MONAZIT_CRYSTAL.get())
                .patternLine("III")
                .patternLine("IMI")
                .patternLine("IFI")
                .build(
                        iFinishedRecipe ->
                                ConditionalRecipe
                                        .builder()
                                        .addCondition(GeneratorEnabled.INSTANCE)
                                        .addRecipe(iFinishedRecipe)
                                        .build(consumer,new ResourceLocation(LibMisc.MOD_ID,"monazit_generator")));
    }

    private void generateCraftingMatsRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(MFFSItems.MONAZIT_CIRCUIT.get(),3).addCriterion("monazit_has",hasItem(MFFSTags.CRYSTAL_MONAZIT))
                .key('I', Tags.Items.INGOTS_IRON)
                .key('M', MFFSTags.CRYSTAL_MONAZIT)
                .patternLine("   ")
                .patternLine("IMI")
                .patternLine("   ")
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(MFFSItems.LINK_CARD.get()).addCriterion("circuit_has",hasItem(MFFSItems.MONAZIT_CIRCUIT.get()))
                .key('P',Items.PAPER)
                .key('C',MFFSItems.MONAZIT_CIRCUIT.get())
                .patternLine("PPP")
                .patternLine("PCP")
                .patternLine("PPP")
                .build(consumer);
    }

    private void smeltingRecipes(Consumer<IFinishedRecipe> consumer) {
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromStacks(new ItemStack(MFFSBlocks.MONAZIT_ORE.get())),MFFSItems.MONAZIT_CRYSTAL.get(),0.7f,200).addCriterion(LibBlocks.MONAZIT_ORE, InventoryChangeTrigger.Instance.forItems(MFFSItems.MONAZIT_ORE.get())).build(consumer);
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromStacks(new ItemStack(MFFSBlocks.MONAZIT_ORE.get())),MFFSItems.MONAZIT_CRYSTAL.get(),0.7f,200).addCriterion(LibBlocks.MONAZIT_ORE, InventoryChangeTrigger.Instance.forItems(MFFSItems.MONAZIT_ORE.get())).build(consumer,new ResourceLocation(LibMisc.MOD_ID,MFFSItems.MONAZIT_CRYSTAL.getId().getPath()+"_blasting"));
    }
}
