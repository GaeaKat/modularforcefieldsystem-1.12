package com.nekokittygames.mffs.common.guide;

import amerifrance.guideapi.api.util.TextHelper;
import amerifrance.guideapi.page.reciperenderer.ShapedRecipesRenderer;
import net.minecraft.item.crafting.ShapedRecipes;

/**
 * Created by katsw on 27/12/2016.
 */
public class EnderIoShapedRecipeRenderer extends ShapedRecipesRenderer
{

    public EnderIoShapedRecipeRenderer(ShapedRecipes recipe) {
        super(recipe);
    }

    @Override
    protected String getRecipeName() {
        return TextHelper.localizeEffect("mffs.guide.recipes.enderio");
    }
}