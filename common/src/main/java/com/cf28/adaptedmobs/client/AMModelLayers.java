package com.cf28.adaptedmobs.client;

import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class AMModelLayers {
    public static final ModelLayerLocation FESTIVE_CREEPER = create("festive_creeper");
    public static final ModelLayerLocation FESTIVE_CREEPER_ARMOR = create("festive_creeper", "armor");
    public static final ModelLayerLocation SUPPORT_CREEPER = create("support_creeper");
    public static final ModelLayerLocation SUPPORT_CREEPER_ARMOR = create("support_creeper", "armor");

    private static ModelLayerLocation create(String key) {
        return create(key, "main");
    }

    private static ModelLayerLocation create(String key, String layer) {
        return new ModelLayerLocation(new ResourceLocation(AdaptedMobs.MOD_ID, key), layer);
    }
}