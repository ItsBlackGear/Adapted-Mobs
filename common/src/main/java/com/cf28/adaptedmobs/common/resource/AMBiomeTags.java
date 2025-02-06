package com.cf28.adaptedmobs.common.resource;

import com.blackgear.platform.common.data.TagRegistry;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class AMBiomeTags {
    public static final TagRegistry<Biome> TAGS = TagRegistry.create(Registry.BIOME_REGISTRY, AdaptedMobs.MOD_ID);

    public static final TagKey<Biome> SPAWN_EXTRA_FESTIVE_CREEPER = TAGS.register("spawn_extra_festive_creeper");
    public static final TagKey<Biome> SPAWN_EXTRA_SUPPORT_CREEPER = TAGS.register("spawn_extra_support_creeper");
    public static final TagKey<Biome> SPAWN_EXTRA_ROCKET_CREEPER = TAGS.register("spawn_extra_rocket_creeper");
}