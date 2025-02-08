package com.cf28.adaptedmobs.common.registry;

import com.blackgear.platform.core.helper.ItemRegistry;
import com.cf28.adaptedmobs.common.item.MysteryEggItem;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class AMItems {
    public static final ItemRegistry ITEMS = ItemRegistry.create(AdaptedMobs.MOD_ID);

    public static final Supplier<Item> RED_MYSTERY_EGG = ITEMS.register(
        "red_mystery_egg",
        () -> new MysteryEggItem(
            AMEntityTypes.FESTIVE_CREEPER,
            new Item.Properties().stacksTo(16).tab(CreativeModeTab.TAB_MATERIALS)
        )
    );
    public static final Supplier<Item> YELLOW_MYSTERY_EGG = ITEMS.register(
        "yellow_mystery_egg",
        () -> new MysteryEggItem(
            AMEntityTypes.SUPPORT_CREEPER,
            new Item.Properties().stacksTo(16).tab(CreativeModeTab.TAB_MATERIALS)
        )
    );
    public static final Supplier<Item> BLUE_MYSTERY_EGG = ITEMS.register(
        "blue_mystery_egg",
        () -> new MysteryEggItem(
            AMEntityTypes.ROCKET_CREEPER,
            new Item.Properties().stacksTo(16).tab(CreativeModeTab.TAB_MATERIALS)
        )
    );
    public static final Supplier<Item> GREEN_MYSTERY_EGG = ITEMS.register(
        "green_mystery_egg",
        () -> new MysteryEggItem(
            AMEntityTypes.CREEPER,
            new Item.Properties().stacksTo(16).tab(CreativeModeTab.TAB_MATERIALS)
        )
    );

    public static final Supplier<Item> FESTIVE_CREEPER_SPAWN_EGG = ITEMS.spawnEgg(
        "festive_creeper_spawn_egg",
        AMEntityTypes.FESTIVE_CREEPER,
        14625830,
        0,
        new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)
    );
    public static final Supplier<Item> SUPPORT_CREEPER_SPAWN_EGG = ITEMS.spawnEgg(
        "support_creeper_spawn_egg",
        AMEntityTypes.SUPPORT_CREEPER,
        15197997,
        0,
        new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)
    );
    public static final Supplier<Item> ROCKET_CREEPER_SPAWN_EGG = ITEMS.spawnEgg(
        "rocket_creeper_spawn_egg",
        AMEntityTypes.ROCKET_CREEPER,
        6406895,
        0,
        new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)
    );
}