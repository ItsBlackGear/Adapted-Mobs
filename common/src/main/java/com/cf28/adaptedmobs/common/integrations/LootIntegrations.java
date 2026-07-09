package com.cf28.adaptedmobs.common.integrations;

import com.blackgear.platform.common.data.LootModifier;
import com.blackgear.platform.core.events.ServerLifecycleEvents;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMItems;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.Supplier;

public final class LootIntegrations implements LootModifier.LootTableModifier {
    public static final LootIntegrations INSTANCE = new LootIntegrations();

    private static NbtPredicate isTame(boolean tamed) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Tamed", tamed);
        return new NbtPredicate(tag);
    }

    @Override
    public void modify(ResourceKey<LootTable> loot, LootModifier.LootTableContext context, boolean builtin) {
        if (loot.equals(EntityType.CREEPER.getDefaultLootTable())) {
            this.addMysteryEggPool(context, AMItems.GREEN_MYSTERY_EGG);
        } else if (loot.equals(AMEntityTypes.CREEPER.get().getDefaultLootTable())) {
            this.addMysteryEggPool(context, AMItems.GREEN_MYSTERY_EGG);
        } else if (loot.equals(AMEntityTypes.FESTIVE_CREEPER.get().getDefaultLootTable())) {
            this.addMysteryEggPool(context, AMItems.RED_MYSTERY_EGG);
        } else if (loot.equals(AMEntityTypes.SUPPORT_CREEPER.get().getDefaultLootTable())) {
            this.addMysteryEggPool(context, AMItems.YELLOW_MYSTERY_EGG);
        } else if (loot.equals(AMEntityTypes.ROCKET_CREEPER.get().getDefaultLootTable())) {
            this.addMysteryEggPool(context, AMItems.BLUE_MYSTERY_EGG);
        }
    }

    private void addMysteryEggPool(LootModifier.LootTableContext context, Supplier<Item> egg) {
        ServerLifecycleEvents.STARTING.register(server -> {
            if (!AdaptedMobs.CONFIG.enableMysteryEggs.get()) {
                return;
            }

            context.addPool(
                    LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(
                                    AlternativesEntry.alternatives(
                                            LootItem.lootTableItem(egg.get())
                                                    .when(LootItemEntityPropertyCondition.hasProperties(
                                                            LootContext.EntityTarget.THIS,
                                                            EntityPredicate.Builder.entity().nbt(isTame(false))
                                                    ))
                                                    .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(server.registryAccess(), 0.025F, 0.01F)),
                                            LootItem.lootTableItem(egg.get())
                                                    .when(LootItemEntityPropertyCondition.hasProperties(
                                                            LootContext.EntityTarget.THIS,
                                                            EntityPredicate.Builder.entity().nbt(isTame(true))
                                                    ))
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(server.registryAccess(), 0.2F, 0.01F))
                                    )
                            )
            );
        });
    }
}
