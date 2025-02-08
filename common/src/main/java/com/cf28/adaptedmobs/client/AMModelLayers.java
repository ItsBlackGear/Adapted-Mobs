package com.cf28.adaptedmobs.client;

import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class AMModelLayers {
    public static final ModelLayerLocation FESTIVE_CREEPER = create("festive_creeper");
    public static final ModelLayerLocation FESTIVE_CREEPER_SKULL = create("festive_creeper", "skull");
    public static final ModelLayerLocation FESTIVE_CREEPER_ARMOR = create("festive_creeper", "armor");
    public static final ModelLayerLocation FESTIVE_CREEPER_CLOTH = create("festive_creeper", "cloth");

    public static final ModelLayerLocation SUPPORT_CREEPER = create("support_creeper");
    public static final ModelLayerLocation SUPPORT_CREEPER_SKULL = create("support_creeper", "skull");
    public static final ModelLayerLocation SUPPORT_CREEPER_ARMOR = create("support_creeper", "armor");
    public static final ModelLayerLocation SUPPORT_CREEPER_CLOTH = create("support_creeper", "cloth");

    public static final ModelLayerLocation PEEPER_CREEPER = create("peeper_creeper");
    public static final ModelLayerLocation PEEPER_CREEPER_SKULL = create("peeper_creeper", "skull");
    public static final ModelLayerLocation PEEPER_CREEPER_ARMOR = create("peeper_creeper", "armor");

    public static final ModelLayerLocation ROCKET_CREEPER = create("rocket_creeper");
    public static final ModelLayerLocation ROCKET_CREEPER_SKULL = create("rocket_creeper", "skull");
    public static final ModelLayerLocation ROCKET_CREEPER_ARMOR = create("rocket_creeper", "armor");
    public static final ModelLayerLocation ROCKET_CREEPER_CLOTH = create("rocket_creeper", "cloth");

    public static final ModelLayerLocation CREEPER = create("creeper");
    public static final ModelLayerLocation CREEPER_ARMOR = create("creeper", "armor");
    public static final ModelLayerLocation CREEPER_CLOTH = create("creeper", "cloth");

    private static ModelLayerLocation create(String name) {
        return create(name, "main");
    }

    private static ModelLayerLocation create(String name, String layer) {
        return new ModelLayerLocation(AdaptedMobs.resource(name), layer);
    }
}