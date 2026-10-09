package com.cf28.adaptedmobs.core.tags;

import com.blackgear.platform.common.data.TagRegistry;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

public class AMEntityTags {
    public static final TagRegistry<EntityType<?>> REGISTRY = TagRegistry.create(Registries.ENTITY_TYPE, AdaptedMobs.MOD_ID);

    public static final TagKey<EntityType<?>> MINDMOULD_TARGETS = REGISTRY.register("mindmould_targets");
}
