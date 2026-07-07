package com.cf28.adaptedmobs.common.integrations;

import com.blackgear.platform.common.data.LootModifier;
import com.blackgear.platform.core.events.ServerLifecycleEvents;
import com.cf28.adaptedmobs.common.registries.AMItems;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public final class LootIntegrations implements LootModifier.LootTableModifier {
    public static final LootIntegrations INSTANCE = new LootIntegrations();
    
    @Override
    public void modify(ResourceKey<LootTable> loot, LootModifier.LootTableContext context, boolean builtin) {
        if (loot.equals(EntityType.CREEPER.getDefaultLootTable())) {
            ServerLifecycleEvents.STARTING.register(server -> {
                context.addPool(
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(AMItems.GREEN_MYSTERY_EGG.get())
                            .when(LootItemKilledByPlayerCondition.killedByPlayer())
                            .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(server.registryAccess(), 0.025F, 0.01F)))
                );
            });
        }
    }
}