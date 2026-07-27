package com.cf28.adaptedmobs.fabric.data.server.loot;

import com.cf28.adaptedmobs.common.registries.AMBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.storage.loot.entries.LootItem;

import java.util.concurrent.CompletableFuture;

public class BlockLootGenerator extends FabricBlockLootTableProvider {
    public BlockLootGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup);
    }

    @Override
    public void generate() {
        this.dropSelf(AMBlocks.FESTIVE_CREEPER_HEAD.getFirst().get());
        this.dropSelf(AMBlocks.SUPPORT_CREEPER_HEAD.getFirst().get());
        this.dropSelf(AMBlocks.ROCKET_CREEPER_HEAD.getFirst().get());
        this.dropSelf(AMBlocks.PEEPER_CREEPER_HEAD.getFirst().get());
        
        this.add(AMBlocks.HARPY_EGG.get(), this.createSilkTouchDispatchTable(AMBlocks.HARPY_EGG.get(), this.applyExplosionDecay(AMBlocks.HARPY_EGG.get(), LootItem.lootTableItem(AMBlocks.HARPY_EGG.get()))));
    }
}