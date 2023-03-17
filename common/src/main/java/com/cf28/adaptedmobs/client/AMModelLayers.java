package com.cf28.adaptedmobs.client;

import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class AMModelLayers {
    public static final ModelLayerLocation FESTIVE_CREEPER = create("festive_creeper");

    private static ModelLayerLocation create(String key) {
        return create(key, "main");
    }

    private static ModelLayerLocation create(String key, String layer) {
        return new ModelLayerLocation(new ResourceLocation(AdaptedMobs.MOD_ID, key), layer);
    }
}