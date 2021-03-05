package net.newgaea.mffs.client.models;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import net.newgaea.mffs.common.blocks.BlockForceField;
import net.newgaea.mffs.common.misc.EnumFieldType;
import net.newgaea.mffs.common.tiles.TileForcefield;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ForcefieldModel implements IDynamicBakedModel {


    private void putVertex(BakedQuadBuilder builder, Vector3d normal,
                           double x, double y, double z, float u, float v, TextureAtlasSprite sprite, float r, float g, float b) {

        ImmutableList<VertexFormatElement> elements = builder.getVertexFormat().getElements().asList();
        for (int j = 0 ; j < elements.size() ; j++) {
            VertexFormatElement e = elements.get(j);
            switch (e.getUsage()) {
                case POSITION:
                    builder.put(j, (float) x, (float) y, (float) z, 1.0f);
                    break;
                case COLOR:
                    builder.put(j, r, g, b, 1.0f);
                    break;
                case UV:
                    switch (e.getIndex()) {
                        case 0:
                            float iu = sprite.getInterpolatedU(u);
                            float iv = sprite.getInterpolatedV(v);
                            builder.put(j, iu, iv);
                            break;
                        case 2:
                            builder.put(j, (short) 0, (short) 0);
                            break;
                        default:
                            builder.put(j);
                            break;
                    }
                    break;
                case NORMAL:
                    builder.put(j, (float) normal.x, (float) normal.y, (float) normal.z);
                    break;
                default:
                    builder.put(j);
                    break;
            }
        }
    }

    private BakedQuad createQuad(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite) {
        Vector3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();
        int tw = sprite.getWidth()/2;
        int th = sprite.getHeight()/2;

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
        putVertex(builder, normal, v1.x, v1.y, v1.z, 0, 0, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.x, v2.y, v2.z, 0, th, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.x, v3.y, v3.z, tw, th, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.x, v4.y, v4.z, tw, 0, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    private static Vector3d v(double x, double y, double z) {
        return new Vector3d(x, y, z);
    }


    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side,Random rand,IModelData extraData) {
        RenderType layer = MinecraftForgeClient.getRenderLayer();
        BlockState mimic = extraData.getData(TileForcefield.MIMIC);
        EnumFieldType type = extraData.getData(TileForcefield.FIELD);
        if(mimic!=null && !(mimic.getBlock() instanceof BlockForceField)) {
            if (layer == null || RenderTypeLookup.canRenderInLayer(mimic,layer)) {
                IBakedModel model=Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(mimic);
                try {
                    return model.getQuads(mimic,side,rand, EmptyModelData.INSTANCE);

                }
                catch (Exception e) {
                    return Collections.emptyList();
                }
            }
            return Collections.emptyList();
        }
        //
        //
        //if  (layer != null && !layer.equals(RenderType.getSolid())) {
        //    return Collections.emptyList();
        //}
        if(layer!=null && !layer.equals(RenderType.getCutout()))
            return Collections.emptyList();

        TextureAtlasSprite texture = getTexture(type);
        TextureAtlasSprite texure2 = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft","block/acacia_log_top"));
        List<BakedQuad> quads = new ArrayList<>();
        double l = .0;
        double r = 1-.0;
        if(side!=null) {
            switch (side) {
                case UP:
                    quads.add(createQuad(v(l, r, l), v(l, r, r), v(r, r, r), v(r, r, l), texture));
                    break;
                case DOWN:
                    quads.add(createQuad(v(l, l, l), v(r, l, l), v(r, l, r), v(l, l, r), texture));
                    break;
                case EAST:
                    quads.add(createQuad(v(r, r, r), v(r, l, r), v(r, l, l), v(r, r, l), texture));
                    break;
                case WEST:
                    quads.add(createQuad(v(l, r, l), v(l, l, l), v(l, l, r), v(l, r, r), texture));
                    break;
                case NORTH:
                    quads.add(createQuad(v(r, r, l), v(r, l, l), v(l, l, l), v(l, r, l), texture));
                    break;
                case SOUTH:
                    quads.add(createQuad(v(l, r, r), v(l, l, r), v(r, l, r), v(r, r, r), texture));
                    break;
            }
        }

        return quads;
    }

    private TextureAtlasSprite getTexture(EnumFieldType fieldType) {
        if(fieldType!=null)
            return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(fieldType.getTexture());
        else
            return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(EnumFieldType.Default.getTexture());
    }
    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return getTexture(EnumFieldType.Default);
    }

    @Override
    public TextureAtlasSprite getParticleTexture(IModelData data) {
        EnumFieldType type = data.getData(TileForcefield.FIELD);
        if(type==null)
            type=EnumFieldType.Default;
        return getTexture(type);
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.EMPTY;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }
}
