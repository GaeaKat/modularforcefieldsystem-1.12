package com.nekokittygames.mffs.client;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

/**
 * Created by katsw on 11/09/2016.
 */
public class MeshDefinition implements ItemMeshDefinition {
    public ModelResourceLocation[] rl;
    public MeshDefinition(ModelResourceLocation mrl)
    {
        rl = new ModelResourceLocation[1];
        rl[0] = mrl;
    }

    public MeshDefinition(ModelResourceLocation[] mrl)
    {
        rl = mrl;
    }

    public MeshDefinition()
    {
        Setup();
    }

    public void Setup()
    {

    }

    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack)
    {
        if(rl.length > 1 && stack.getItemDamage() < rl.length)
            return rl[stack.getItemDamage()];

        return rl[0];
    }
}
