package com.cf28.adaptedmobs.fabric.data.server.tags;

import com.cf28.adaptedmobs.core.tags.AMBiomeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.concurrent.CompletableFuture;

public class BiomeTagGenerator extends FabricTagProvider<Biome> {
    public BiomeTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, Registries.BIOME, lookup);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        this.getOrCreateTagBuilder(AMBiomeTags.EXTRA_FESTIVE_CREEPER_SPAWNS)
                .forceAddTag(BiomeTags.IS_JUNGLE);

        this.getOrCreateTagBuilder(AMBiomeTags.EXTRA_SUPPORT_CREEPER_SPAWNS)
                .add(Biomes.LUSH_CAVES)
                .forceAddTag(BiomeTags.IS_JUNGLE);

        this.getOrCreateTagBuilder(AMBiomeTags.EXTRA_ROCKET_CREEPER_SPAWNS)
                .add(Biomes.SNOWY_PLAINS)
                .add(Biomes.ICE_SPIKES)
                .add(Biomes.FROZEN_PEAKS)
                .add(Biomes.JAGGED_PEAKS)
                .add(Biomes.SNOWY_SLOPES)
                .add(Biomes.FROZEN_OCEAN)
                .add(Biomes.DEEP_FROZEN_OCEAN)
                .add(Biomes.GROVE)
                .add(Biomes.FROZEN_RIVER)
                .add(Biomes.SNOWY_TAIGA)
                .add(Biomes.SNOWY_BEACH);
    }
}