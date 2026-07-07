package com.cf28.adaptedmobs.common.registries;

import com.blackgear.platform.core.helper.ItemRegistry;
import com.cf28.adaptedmobs.common.level.item.MysteryEggItem;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

import java.util.function.Supplier;

import static com.blackgear.platform.core.helper.ItemRegistry.*;

public class AMItems {
    public static final ItemRegistry REGISTRIES = create(AdaptedMobs.MOD_ID);

    public static final Supplier<Item> GREEN_MYSTERY_EGG = REGISTRIES.register("green_mystery_egg",
        properties -> new MysteryEggItem(AMEntityTypes.CREEPER, properties),
        new Properties().stacksTo(16));
    public static final Supplier<Item> RED_MYSTERY_EGG = REGISTRIES.register("red_mystery_egg",
        properties -> new MysteryEggItem(AMEntityTypes.FESTIVE_CREEPER, properties),
        new Properties().stacksTo(16));
    public static final Supplier<Item> YELLOW_MYSTERY_EGG = REGISTRIES.register("yellow_mystery_egg",
        properties -> new MysteryEggItem(AMEntityTypes.SUPPORT_CREEPER, properties),
        new Properties().stacksTo(16));
    public static final Supplier<Item> BLUE_MYSTERY_EGG = REGISTRIES.register("blue_mystery_egg",
        properties -> new MysteryEggItem(AMEntityTypes.ROCKET_CREEPER, properties),
        new Properties().stacksTo(16));
    
    public static final Supplier<Item> FESTIVE_CREEPER_SPAWN_EGG = REGISTRIES.register("festive_creeper_spawn_egg",
        properties -> createSpawnEgg(AMEntityTypes.FESTIVE_CREEPER, 14625830, 0, properties));
    public static final Supplier<Item> SUPPORT_CREEPER_SPAWN_EGG = REGISTRIES.register("support_creeper_spawn_egg",
        properties -> createSpawnEgg(AMEntityTypes.SUPPORT_CREEPER, 15197997, 0, properties));
    public static final Supplier<Item> ROCKET_CREEPER_SPAWN_EGG = REGISTRIES.register("rocket_creeper_spawn_egg",
        properties -> createSpawnEgg(AMEntityTypes.ROCKET_CREEPER, 6406895, 0, properties));
}