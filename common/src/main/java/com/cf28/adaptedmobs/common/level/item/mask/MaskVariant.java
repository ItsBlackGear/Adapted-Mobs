package com.cf28.adaptedmobs.common.level.item.mask;

import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

public enum MaskVariant implements StringRepresentable {
    BLANK(0, "blank"),
    GRINNING(1, "grinning"),
    WEEPING(2, "weeping"),
    WRATH(3, "wrath"),
    ANCIENT(4, "ancient"),
    SCREAMING(5, "screaming");

    private final int id;
    private final String name;
    private final ResourceLocation texture;

    MaskVariant(int id, String name) {
        this.id = id;
        this.name = name;
        this.texture = ResourceLocation.fromNamespaceAndPath(AdaptedMobs.MOD_ID, "textures/entity/mask/deepslate_mask_" + name + ".png");
    }

    public static MaskVariant byId(int id) {
        MaskVariant[] values = values();
        if (id < 0 || id >= values.length) {
            return BLANK;
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
