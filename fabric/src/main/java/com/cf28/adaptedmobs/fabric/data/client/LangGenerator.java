package com.cf28.adaptedmobs.fabric.data.client;

import com.cf28.adaptedmobs.common.registries.AMBlocks;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class LangGenerator extends FabricLanguageProvider {
    public LangGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider lookup, TranslationBuilder builder) {
        builder.add(AMEntityTypes.FESTIVE_CREEPER.get(), "Festive Creeper");
        builder.add(AMEntityTypes.SUPPORT_CREEPER.get(), "Support Creeper");
        builder.add(AMEntityTypes.ROCKET_CREEPER.get(), "Rocket Creeper");
        builder.add(AMEntityTypes.CREEPER.get(), "Creeper");

        builder.add(AMItems.FESTIVE_CREEPER_SPAWN_EGG.get(), "Festive Creeper Spawn Egg");
        builder.add(AMItems.SUPPORT_CREEPER_SPAWN_EGG.get(), "Support Creeper Spawn Egg");
        builder.add(AMItems.ROCKET_CREEPER_SPAWN_EGG.get(), "Rocket Creeper Spawn Egg");

        builder.add(AMItems.RED_MYSTERY_EGG.get(), "Red Mystery Egg");
        builder.add(AMItems.YELLOW_MYSTERY_EGG.get(), "Yellow Mystery Egg");
        builder.add(AMItems.BLUE_MYSTERY_EGG.get(), "Blue Mystery Egg");
        builder.add(AMItems.GREEN_MYSTERY_EGG.get(), "Green Mystery Egg");

        builder.add(AMBlocks.FESTIVE_CREEPER_HEAD.getFirst().get(), "Festive Creeper Head");
        builder.add(AMBlocks.SUPPORT_CREEPER_HEAD.getFirst().get(), "Support Creeper Head");
        builder.add(AMBlocks.ROCKET_CREEPER_HEAD.getFirst().get(), "Rocket Creeper Head");
        builder.add(AMBlocks.PEEPER_CREEPER_HEAD.getFirst().get(), "Peeper Head");

        builder.add(AMItems.SUPPORT_CREEPER_SPORES.get(), "Seeper Spores");
        builder.add(AMItems.ROCKET_CREEPER_SPORES.get(), "Leaper Spores");
        builder.add(AMItems.FESTIVE_CREEPER_SPORES.get(), "Reaper Spores");

        builder.add(AMBlocks.SUPPORT_SPORE_BARREL.get(), "Seeper Spore Barrel");
        builder.add(AMBlocks.ROCKET_SPORE_BARREL.get(), "Leaper Spore Barrel");
        builder.add(AMBlocks.FESTIVE_SPORE_BARREL.get(), "Reaper Spore Barrel");

        builder.add(AMBlocks.POTTED_SUPPORT_CREEPER_SPORES_PLANT.get(), "Potted Seeper Spore Flower");
        builder.add(AMBlocks.POTTED_ROCKET_CREEPER_SPORES_PLANT.get(), "Potted Leaper Spore Flower");
        builder.add(AMBlocks.POTTED_FESTIVE_CREEPER_SPORES_PLANT.get(), "Potted Reaper Spore Flower");

        builder.add(AMEntityTypes.SUPPORT_CREEPIE.get(), "Support Creepie");
        builder.add(AMEntityTypes.ROCKET_CREEPIE.get(), "Rocket Creepie");
        builder.add(AMEntityTypes.FESTIVE_CREEPIE.get(), "Festive Creepie");
    }
}