package com.cf28.adaptedmobs.common.resource;

import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class AMBiomeTags {
    public static void boostrap() {}

    public static final TagKey<Biome> SPAWN_EXTRA_FESTIVE_CREEPER = create("spawn_extra_festive_creeper");
    public static final TagKey<Biome> SPAWN_EXTRA_SUPPORT_CREEPER = create("spawn_extra_support_creeper");
    public static final TagKey<Biome> SPAWN_EXTRA_ROCKET_CREEPER = create("spawn_extra_rocket_creeper");

    private static TagKey<Biome> create(String key) {
        return TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(AdaptedMobs.MOD_ID, key));
    }
}