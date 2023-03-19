package com.cf28.adaptedmobs.common.registry;

import com.cf28.adaptedmobs.common.item.MysteryEggItem;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.cf28.adaptedmobs.core.platform.CoreRegistry;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class AMItems {
    public static final CoreRegistry<Item> ITEMS = CoreRegistry.create(Registry.ITEM, AdaptedMobs.MOD_ID);

    public static final Supplier<Item> RED_MYSTERY_EGG = create("red_mystery_egg", () -> new MysteryEggItem(AMEntityTypes.FESTIVE_CREEPER, new Item.Properties().stacksTo(16).tab(CreativeModeTab.TAB_MATERIALS)));

    private static <I extends Item> Supplier<I> create(String key, Supplier<I> item) {
        return ITEMS.register(key, item);
    }
}