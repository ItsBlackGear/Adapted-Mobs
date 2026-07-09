package com.cf28.adaptedmobs.fabric.data.server.loot;

import com.cf28.adaptedmobs.common.registries.AMBlocks;
import com.cf28.adaptedmobs.common.registries.AMItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;

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

        this.dropSelf(AMBlocks.FESTIVE_SPORE_BARREL.get());
        this.dropSelf(AMBlocks.ROCKET_SPORE_BARREL.get());
        this.dropSelf(AMBlocks.SUPPORT_SPORE_BARREL.get());

        this.add(AMBlocks.POTTED_FESTIVE_CREEPER_SPORES_PLANT.get(), this.createPotFlowerItemTable(AMItems.FESTIVE_CREEPER_SPORES.get()));
        this.add(AMBlocks.POTTED_ROCKET_CREEPER_SPORES_PLANT.get(), this.createPotFlowerItemTable(AMItems.ROCKET_CREEPER_SPORES.get()));
        this.add(AMBlocks.POTTED_SUPPORT_CREEPER_SPORES_PLANT.get(), this.createPotFlowerItemTable(AMItems.SUPPORT_CREEPER_SPORES.get()));
    }
}