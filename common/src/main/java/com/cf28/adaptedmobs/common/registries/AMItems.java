package com.cf28.adaptedmobs.common.registries;

import com.blackgear.platform.core.helper.ItemRegistry;
import com.cf28.adaptedmobs.common.integrations.TolerableCreepersCompat;
import com.cf28.adaptedmobs.common.integrations.TolerableCreepersIntegration;
import com.cf28.adaptedmobs.common.level.item.MysteryEggItem;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

import java.util.function.Supplier;

import static com.blackgear.platform.core.helper.ItemRegistry.create;
import static com.blackgear.platform.core.helper.ItemRegistry.createSpawnEgg;

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
            properties -> createSpawnEgg(AMEntityTypes.FESTIVE_CREEPER, 10571065, 0, properties));
    public static final Supplier<Item> SUPPORT_CREEPER_SPAWN_EGG = REGISTRIES.register("support_creeper_spawn_egg",
            properties -> createSpawnEgg(AMEntityTypes.SUPPORT_CREEPER, 7110705, 0, properties));
    public static final Supplier<Item> ROCKET_CREEPER_SPAWN_EGG = REGISTRIES.register("rocket_creeper_spawn_egg",
            properties -> createSpawnEgg(AMEntityTypes.ROCKET_CREEPER, 5999444, 0, properties));

    public static final Supplier<Item> HARPY_SPAWN_EGG = REGISTRIES.register("harpy_spawn_egg",
            properties -> createSpawnEgg(AMEntityTypes.HARPY, 0x333149, 0x9790a4, properties));

    public static final Supplier<Item> HARPY_EGG = REGISTRIES.register("harpy_egg",
            () -> new BlockItem(AMBlocks.HARPY_EGG.get(), new Properties()));

    public static final Supplier<Item> FESTIVE_CREEPIE_SPAWN_EGG = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.register("festive_creepie_spawn_egg",
                    properties -> createSpawnEgg(AMEntityTypes.FESTIVE_CREEPIE, 10571065, 8754737, properties))
            : null;
    public static final Supplier<Item> SUPPORT_CREEPIE_SPAWN_EGG = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.register("support_creepie_spawn_egg",
                    properties -> createSpawnEgg(AMEntityTypes.SUPPORT_CREEPIE, 7110705, 10895394, properties))
            : null;
    public static final Supplier<Item> ROCKET_CREEPIE_SPAWN_EGG = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.register("rocket_creepie_spawn_egg",
                    properties -> createSpawnEgg(AMEntityTypes.ROCKET_CREEPIE, 5999444, 10800234, properties))
            : null;

    public static final Supplier<Item> FESTIVE_CREEPER_SPORES = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.register("festive_creeper_spores", TolerableCreepersIntegration::createFestiveSporesItem, new Properties())
            : null;

    public static final Supplier<Item> ROCKET_CREEPER_SPORES = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.register("rocket_creeper_spores", TolerableCreepersIntegration::createRocketSporesItem, new Properties())
            : null;

    public static final Supplier<Item> SUPPORT_CREEPER_SPORES = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.register("support_creeper_spores", TolerableCreepersIntegration::createSupportSporesItem, new Properties())
            : null;
}