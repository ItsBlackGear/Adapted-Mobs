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

        builder.add(AMItems.SUPPORT_CREEPIE_SPAWN_EGG.get(), "Support Creepie Spawn Egg");
        builder.add(AMItems.ROCKET_CREEPIE_SPAWN_EGG.get(), "Rocket Creepie Spawn Egg");
        builder.add(AMItems.FESTIVE_CREEPIE_SPAWN_EGG.get(), "Festive Creepie Spawn Egg");

        builder.add("block.tolerable_creepers.spore_barrel", "Creeper Spore Barrel");
        builder.add("entity.tolerable_creepers.spore_barrel", "Creeper Spore Barrel");

        builder.add("config.adaptedmobs.title", "Adapted Mobs");
        builder.add("config.adaptedmobs.category.general", "General");
        builder.add("config.adaptedmobs.category.festive_creeper", "Festive Creeper");
        builder.add("config.adaptedmobs.category.support_creeper", "Support Creeper");
        builder.add("config.adaptedmobs.category.rocket_creeper", "Rocket Creeper");

        builder.add("config.adaptedmobs.option.enable_mystery_eggs", "Enable Mystery Eggs");

        builder.add("config.adaptedmobs.option.spawn_festive_creepers", "Spawn Festive Creepers");
        builder.add("config.adaptedmobs.option.festive_creeper_spawn_weight", "Festive Creeper Spawn Weight");
        builder.add("config.adaptedmobs.option.festive_creeper_extra_spawn_weight", "Festive Creeper Extra Spawn Weight");
        builder.add("config.adaptedmobs.option.festive_spore_count_day_base", "Festive Spore Count Day Base");
        builder.add("config.adaptedmobs.option.festive_spore_count_day_random", "Festive Spore Count Day Random");
        builder.add("config.adaptedmobs.option.festive_spore_count_night_base", "Festive Spore Count Night Base");
        builder.add("config.adaptedmobs.option.festive_spore_count_night_random", "Festive Spore Count Night Random");

        builder.add("config.adaptedmobs.option.spawn_support_creepers", "Spawn Support Creepers");
        builder.add("config.adaptedmobs.option.support_creeper_spawn_weight", "Support Creeper Spawn Weight");
        builder.add("config.adaptedmobs.option.support_creeper_extra_spawn_weight", "Support Creeper Extra Spawn Weight");
        builder.add("config.adaptedmobs.option.support_spore_count_day_base", "Support Spore Count Day Base");
        builder.add("config.adaptedmobs.option.support_spore_count_day_random", "Support Spore Count Day Random");
        builder.add("config.adaptedmobs.option.support_spore_count_night_base", "Support Spore Count Night Base");
        builder.add("config.adaptedmobs.option.support_spore_count_night_random", "Support Spore Count Night Random");

        builder.add("config.adaptedmobs.option.spawn_rocket_creepers", "Spawn Rocket Creepers");
        builder.add("config.adaptedmobs.option.rocket_creeper_spawn_weight", "Rocket Creeper Spawn Weight");
        builder.add("config.adaptedmobs.option.rocket_creeper_extra_spawn_weight", "Rocket Creeper Extra Spawn Weight");
        builder.add("config.adaptedmobs.option.prevent_rocket_creeper_block_damage", "Prevent Rocket Creeper Block Damage");
        builder.add("config.adaptedmobs.option.rocket_spore_count_day_base", "Rocket Spore Count Day Base");
        builder.add("config.adaptedmobs.option.rocket_spore_count_day_random", "Rocket Spore Count Day Random");
        builder.add("config.adaptedmobs.option.rocket_spore_count_night_base", "Rocket Spore Count Night Base");
        builder.add("config.adaptedmobs.option.rocket_spore_count_night_random", "Rocket Spore Count Night Random");
    }
}