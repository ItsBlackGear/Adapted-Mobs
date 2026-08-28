package com.cf28.adaptedmobs.client.registries;

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

    public static final ModelLayerLocation SUPPORT_CREEPIE = create("support_creepie");
    public static final ModelLayerLocation ROCKET_CREEPIE = create("rocket_creepie");
    public static final ModelLayerLocation FESTIVE_CREEPIE = create("festive_creepie");

    public static final ModelLayerLocation HARPY = create("harpy");
    public static final ModelLayerLocation HARPY_CHICK = create("harpy_chick");

    public static final ModelLayerLocation ENTOMBED = create("entombed");
    public static final ModelLayerLocation ARCHAIC_MASK = create("archaic_mask");

    private static ModelLayerLocation create(String name) {
        return create(name, "main");
    }

    private static ModelLayerLocation create(String name, String layer) {
        return new ModelLayerLocation(AdaptedMobs.resource(name), layer);
    }
}