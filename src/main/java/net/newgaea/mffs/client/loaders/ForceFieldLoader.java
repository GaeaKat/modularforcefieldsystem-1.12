package net.newgaea.mffs.client.loaders;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class ForceFieldLoader implements IModelLoader<ForceFieldGeometry> {
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public ForceFieldGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return new ForceFieldGeometry();
    }
}
