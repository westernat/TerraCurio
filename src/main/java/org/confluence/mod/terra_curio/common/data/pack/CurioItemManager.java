package org.confluence.mod.terra_curio.common.data.pack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class CurioItemManager extends SimpleJsonResourceReloadListener {
    public static final CurioItemManager INSTANCE = new CurioItemManager();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public CurioItemManager() {
        super(new Gson(), "curio_items");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        jsonElementMap.forEach((resourceLocation, jsonElement) -> {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();

        });
    }
}
