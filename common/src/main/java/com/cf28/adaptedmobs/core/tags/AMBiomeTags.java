package com.cf28.adaptedmobs.core.tags;

import com.blackgear.platform.common.data.TagRegistry;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class AMBiomeTags {
    public static final TagRegistry<Biome> REGISTRY = TagRegistry.create(Registries.BIOME, AdaptedMobs.MOD_ID);
    
    public static final TagKey<Biome> EXTRA_FESTIVE_CREEPER_SPAWNS = REGISTRY.register("extra_festive_creeper_spawns");
    public static final TagKey<Biome> EXTRA_SUPPORT_CREEPER_SPAWNS = REGISTRY.register("extra_support_creeper_spawns");
    public static final TagKey<Biome> EXTRA_ROCKET_CREEPER_SPAWNS = REGISTRY.register("extra_rocket_creeper_spawns");
}