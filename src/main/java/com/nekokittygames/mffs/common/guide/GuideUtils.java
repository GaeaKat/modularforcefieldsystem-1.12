package com.nekokittygames.mffs.common.guide;

import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.page.PageIRecipe;
import com.nekokittygames.mffs.common.ModularForceFieldSystem;
import com.nekokittygames.mffs.common.RecipesFactory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;

import java.util.ArrayList;

/**
 * Created by katsw on 27/12/2016.
 */
public class GuideUtils {
    public static void addRecipes(ArrayList<IPage> blockBreakerPages, Object obj) {
        if(ModularForceFieldSystem.enableEIRecipes && ModularForceFieldSystem.enderIoFound)
        {
            IRecipe eiRecipe= RecipesFactory.GetRecipe(obj,3);
            blockBreakerPages.add(new PageIRecipe(eiRecipe,new EnderIoShapedRecipeRenderer(((ShapedRecipes)eiRecipe))));
        }

        if(ModularForceFieldSystem.enableIC2Recipes && ModularForceFieldSystem.ic2Found)
        {
            IRecipe ic2Recipe=RecipesFactory.GetRecipe(obj,1);
            blockBreakerPages.add(new PageIRecipe(ic2Recipe,new IC2ShapedRecipeRenderer((ShapedRecipes)ic2Recipe)));
        }
    }
}
