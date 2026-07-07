package com.cf28.adaptedmobs.core.tags;

import com.blackgear.platform.common.data.TagRegistry;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class AMItemTags {
    public static final TagRegistry<Item> REGISTRY = TagRegistry.create(Registries.ITEM, AdaptedMobs.MOD_ID);

    public static final TagKey<Item> CREEPER_FOOD = REGISTRY.register("creeper_food");
}