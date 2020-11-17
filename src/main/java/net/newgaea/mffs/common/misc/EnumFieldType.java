package net.newgaea.mffs.common.misc;

import net.minecraft.util.ResourceLocation;
import net.newgaea.mffs.common.libs.LibMisc;

import static net.newgaea.mffs.common.libs.LibMisc.MOD_ID;

public enum EnumFieldType {
    Area(new ResourceLocation(MOD_ID,"block/field/area")),
    Containment(new ResourceLocation(MOD_ID,"block/field/containment")),
    Default(new ResourceLocation(MOD_ID,"block/field/default")),
    Zapper(new ResourceLocation(MOD_ID,"block/field/zapper"));


    private ResourceLocation texture;
    EnumFieldType(ResourceLocation texture) {
        this.texture=texture;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public EnumFieldType setTexture(ResourceLocation texture) {
        this.texture = texture;
        return this;
    }
}
