package net.newgaea.mffs.common.recipes.conditions;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import net.newgaea.mffs.common.config.MFFSConfig;
import net.newgaea.mffs.common.libs.LibMisc;

public class GeneratorEnabled implements ICondition {
    public static final GeneratorEnabled INSTANCE = new GeneratorEnabled();
    private static final ResourceLocation NAME = new ResourceLocation(LibMisc.MOD_ID, "generator_enabled");


    private GeneratorEnabled(){}
    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test() {
        return MFFSConfig.GENERATOR_ENABLED.get();
    }

    @Override
    public String toString() {
        return "generatorEnabled";
    }

    public static class Serializer implements IConditionSerializer<GeneratorEnabled> {
        public static final GeneratorEnabled.Serializer INSTANCE = new GeneratorEnabled.Serializer();

        @Override
        public void write(JsonObject json, GeneratorEnabled value) { }

        @Override
        public GeneratorEnabled read(JsonObject json)
        {
            return GeneratorEnabled.INSTANCE;
        }

        @Override
        public ResourceLocation getID()
        {
            return GeneratorEnabled.NAME;
        }
    }
}
