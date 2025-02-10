package com.cf28.adaptedmobs.core.data.common.loot;

import com.cf28.adaptedmobs.common.registry.AMBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class BlockLootGenerator extends FabricBlockLootTableProvider {
    public BlockLootGenerator(FabricDataGenerator generator) {
        super(generator);
    }

    @Override
    protected void generateBlockLootTables() {
        this.dropSelf(AMBlocks.FESTIVE_CREEPER_HEAD.get());
        this.dropSelf(AMBlocks.SUPPORT_CREEPER_HEAD.get());
        this.dropSelf(AMBlocks.ROCKET_CREEPER_HEAD.get());
        this.dropSelf(AMBlocks.PEEPER_CREEPER_HEAD.get());
    }
}