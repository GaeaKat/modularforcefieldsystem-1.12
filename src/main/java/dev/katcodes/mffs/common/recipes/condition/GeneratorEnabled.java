package dev.katcodes.mffs.common.recipes.condition;

import com.google.gson.JsonObject;
import dev.katcodes.mffs.MFFSMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class GeneratorEnabled implements ICondition {

    private static final ResourceLocation NAME = new ResourceLocation(MFFSMod.MODID,"generator_enabled");
    public static final GeneratorEnabled INSTANCE = new GeneratorEnabled();
    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test() {
        return true;
    }

    public static class Serializer implements IConditionSerializer<GeneratorEnabled> {

        public static final GeneratorEnabled.Serializer INSTANCE = new GeneratorEnabled.Serializer();
        @Override
        public void write(JsonObject json, GeneratorEnabled value) {

        }

        @Override
        public GeneratorEnabled read(JsonObject json) {
            return GeneratorEnabled.INSTANCE;
        }

        @Override
        public ResourceLocation getID() {
            return NAME;
        }
    }
}
