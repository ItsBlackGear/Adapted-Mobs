package com.cf28.adaptedmobs.common;

import com.cf28.adaptedmobs.common.entity.creeper.FestiveCreeper;
import com.cf28.adaptedmobs.common.entity.creeper.RocketCreeper;
import com.cf28.adaptedmobs.common.entity.creeper.SupportCreeper;
import com.cf28.adaptedmobs.common.entity.creeper.TamableCreeper;
import com.cf28.adaptedmobs.common.level.WorldGeneration;
import com.cf28.adaptedmobs.common.registry.AMEntityTypes;
import com.cf28.adaptedmobs.common.registry.AMItems;
import com.cf28.adaptedmobs.core.platform.common.BiomeManager;
import com.cf28.adaptedmobs.core.platform.common.EntityRegistry;
import com.cf28.adaptedmobs.core.platform.common.LootRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class CommonSetup {
    public static void onInstance() {
        // Attributes
        EntityRegistry.attributes(AMEntityTypes.FESTIVE_CREEPER, FestiveCreeper::createAttributes);
        EntityRegistry.attributes(AMEntityTypes.SUPPORT_CREEPER, SupportCreeper::createAttributes);
        EntityRegistry.attributes(AMEntityTypes.ROCKET_CREEPER, RocketCreeper::createAttributes);
        EntityRegistry.attributes(AMEntityTypes.CREEPER, TamableCreeper::createAttributes);
    }

    public static void postInstance() {
        BiomeManager.bootstrap();
        WorldGeneration.bootstrap();

        LootRegistry.modify((tables, path, context, builtIn) -> {
            if (path.equals(EntityType.CREEPER.getDefaultLootTable())) {
                context.addPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(AMItems.GREEN_MYSTERY_EGG.get()))
                                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.01F))
                );
            }
        });
    }
}