package com.cf28.adaptedmobs.core.tags;

import com.blackgear.platform.common.data.TagRegistry;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class AMBlockTags {
    public static final TagRegistry<Block> REGISTRY = TagRegistry.create(Registries.BLOCK, AdaptedMobs.MOD_ID);

    public static final TagKey<Block> HARPY_EGG_HATCH_BOOST = REGISTRY.register("harpy_egg_hatch_boost");
}
