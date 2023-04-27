package com.cf28.adaptedmobs.core.platform.common;

import com.google.common.collect.ImmutableSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;

public interface BiomeContext {
    ResourceKey<Biome> getBiomeKey();

    Biome getBiome();

    boolean is(TagKey<Biome> tag);

    boolean is(ResourceKey<Biome> biome);

    default boolean hasEntity(EntityType<?>... entities) {
        return hasEntity(ImmutableSet.copyOf(entities));
    }

    default boolean hasEntity(Set<EntityType<?>> entities) {
        MobSpawnSettings settings = this.getBiome().getMobSettings();

        return Arrays.stream(MobCategory.values())
                .flatMap(category -> settings.getMobs(category).unwrap().stream())
                .anyMatch(spawner -> entities.contains(spawner.type));
    }
}