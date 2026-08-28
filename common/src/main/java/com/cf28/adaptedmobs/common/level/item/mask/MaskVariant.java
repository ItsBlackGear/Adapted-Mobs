package com.cf28.adaptedmobs.common.level.item.mask;

import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

public enum MaskVariant implements StringRepresentable {
    ALCHEMIST(0, "alchemist"),
    ARCHITECT(1, "architect"),
    BUILDER(2, "builder"),
    CLERIC(3, "cleric"),
    CRANIAL(4, "cranial"),
    ODDITY(5, "oddity"),
    SPIRAL(6, "spiral"),
    TRAVELER(7, "traveler"),
    WARRIOR(8, "warrior"),
    WEAVER(9, "weaver");

    private final int id;
    private final String name;
    private final ResourceLocation texture;

    MaskVariant(int id, String name) {
        this.id = id;
        this.name = name;
        this.texture = ResourceLocation.fromNamespaceAndPath(AdaptedMobs.MOD_ID, "textures/models/armor/archaic_mask_" + name + ".png");
    }

    public static MaskVariant byId(int id) {
        MaskVariant[] values = values();
        if (id < 0 || id >= values.length) {
            return ALCHEMIST;
        }
        return values[id];
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
