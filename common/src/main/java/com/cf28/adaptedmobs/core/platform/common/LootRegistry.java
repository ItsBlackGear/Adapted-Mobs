package com.cf28.adaptedmobs.core.platform.common;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTables;

public class LootRegistry {
    @ExpectPlatform
    public static void modify(LootTableModifier modifier) {
        throw new AssertionError();
    }

    public interface LootTableModifier {
        void modify(LootTables tables, ResourceLocation path, LootTableContext context, boolean builtIn);
    }

    public interface LootTableContext {
        void addPool(LootPool pool);

        default void addPool(LootPool.Builder pool) {
            this.addPool(pool.build());
        }
    }
}