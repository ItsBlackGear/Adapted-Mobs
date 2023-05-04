package com.cf28.adaptedmobs.core.platform.common.fabric;

import com.cf28.adaptedmobs.core.platform.common.LootRegistry;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.world.level.storage.loot.LootPool;

public class LootRegistryImpl {
    public static void modify(LootRegistry.LootTableModifier modifier) {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> modifier.modify(lootManager, id, new LootRegistry.LootTableContext() {
            @Override
            public void addPool(LootPool pool) {
                tableBuilder.pool(pool);
            }

            @Override
            public void addPool(LootPool.Builder pool) {
                tableBuilder.withPool(pool);
            }
        }, source.isBuiltin()));
    }
}