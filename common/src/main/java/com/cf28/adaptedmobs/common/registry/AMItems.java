package com.cf28.adaptedmobs.common.registry;

import com.cf28.adaptedmobs.common.item.MysteryEggItem;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.cf28.adaptedmobs.core.platform.CoreRegistry;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

import java.util.function.Supplier;

public class AMItems {
    public static final CoreRegistry<Item> ITEMS = CoreRegistry.create(Registry.ITEM, AdaptedMobs.MOD_ID);

    public static final Supplier<Item> RED_MYSTERY_EGG = create("red_mystery_egg", () -> new MysteryEggItem(AMEntityTypes.FESTIVE_CREEPER, new Item.Properties().stacksTo(16).tab(CreativeModeTab.TAB_MATERIALS)));
    public static final Supplier<Item> YELLOW_MYSTERY_EGG = create("yellow_mystery_egg", () -> new MysteryEggItem(AMEntityTypes.SUPPORT_CREEPER, new Item.Properties().stacksTo(16).tab(CreativeModeTab.TAB_MATERIALS)));
    public static final Supplier<Item> BLUE_MYSTERY_EGG = create("blue_mystery_egg", () -> new MysteryEggItem(AMEntityTypes.SUPPORT_CREEPER, new Item.Properties().stacksTo(16).tab(CreativeModeTab.TAB_MATERIALS)));
    public static final Supplier<Item> GREEN_MYSTERY_EGG = create("green_mystery_egg", () -> new MysteryEggItem(AMEntityTypes.CREEPER, new Item.Properties().stacksTo(16).tab(CreativeModeTab.TAB_MATERIALS)));

    public static final Supplier<Item> FESTIVE_CREEPER_SPAWN_EGG = create("festive_creeper_spawn_egg", () -> new SpawnEggItem(AMEntityTypes.FESTIVE_CREEPER.get(), 14625830, 9114901, new Item.Properties().stacksTo(16).tab(CreativeModeTab.TAB_MATERIALS)));
    public static final Supplier<Item> SUPPORT_CREEPER_SPAWN_EGG = create("support_creeper_spawn_egg", () -> new SpawnEggItem(AMEntityTypes.SUPPORT_CREEPER.get(), 15197997, 8487234, new Item.Properties().stacksTo(16).tab(CreativeModeTab.TAB_MATERIALS)));
    public static final Supplier<Item> ROCKET_CREEPER_SPAWN_EGG = create("rocket_creeper_spawn_egg", () -> new SpawnEggItem(AMEntityTypes.CREEPER.get(), 6406895, 4352641, new Item.Properties().stacksTo(16).tab(CreativeModeTab.TAB_MATERIALS)));

    private static <I extends Item> Supplier<I> create(String key, Supplier<I> item) {
        return ITEMS.register(key, item);
    }
}