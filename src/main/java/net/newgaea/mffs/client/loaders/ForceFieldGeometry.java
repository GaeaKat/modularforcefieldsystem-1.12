package net.newgaea.mffs.client.loaders;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import net.newgaea.mffs.client.models.ForcefieldModel;
import net.newgaea.mffs.common.misc.EnumFieldType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class ForceFieldGeometry implements IModelGeometry<ForceFieldGeometry> {
    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation) {
        return new ForcefieldModel();
    }

    @Override
    public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        List<RenderMaterial> textures=new ArrayList<>();
        for(EnumFieldType types:EnumFieldType.values()) {
            textures.add(new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE,types.getTexture()));
        }
        return textures;
    }
}
