package com.cf28.adaptedmobs.fabric.data.client;

import com.cf28.adaptedmobs.common.registries.AMBlocks;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMItems;
import com.cf28.adaptedmobs.common.registries.AMMobEffects;
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
        builder.add(AMEntityTypes.FESTIVE_CREEPER.get(), "Reaper");
        builder.add(AMEntityTypes.SUPPORT_CREEPER.get(), "Seeper");
        builder.add(AMEntityTypes.ROCKET_CREEPER.get(), "Leaper");
        builder.add(AMEntityTypes.CREEPER.get(), "Creeper");
        builder.add(AMEntityTypes.HARPY.get(), "Harpy");

        builder.add(AMItems.FESTIVE_CREEPER_SPAWN_EGG.get(), "Reaper Spawn Egg");
        builder.add(AMItems.SUPPORT_CREEPER_SPAWN_EGG.get(), "Seeper Spawn Egg");
        builder.add(AMItems.ROCKET_CREEPER_SPAWN_EGG.get(), "Leaper Spawn Egg");
        builder.add(AMItems.HARPY_SPAWN_EGG.get(), "Harpy Spawn Egg");
        builder.add(AMBlocks.HARPY_EGG.get(), "Harpy Egg");

        builder.add(AMItems.RED_MYSTERY_EGG.get(), "Red Mystery Egg");
        builder.add(AMItems.YELLOW_MYSTERY_EGG.get(), "Yellow Mystery Egg");
        builder.add(AMItems.BLUE_MYSTERY_EGG.get(), "Blue Mystery Egg");
        builder.add(AMItems.GREEN_MYSTERY_EGG.get(), "Green Mystery Egg");

        builder.add(AMBlocks.FESTIVE_CREEPER_HEAD.getFirst().get(), "Reaper Head");
        builder.add(AMBlocks.SUPPORT_CREEPER_HEAD.getFirst().get(), "Seeper Head");
        builder.add(AMBlocks.ROCKET_CREEPER_HEAD.getFirst().get(), "Leaper Head");
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

        builder.add(AMEntityTypes.SUPPORT_CREEPIE.get(), "Seepie");
        builder.add(AMEntityTypes.ROCKET_CREEPIE.get(), "Leapie");
        builder.add(AMEntityTypes.FESTIVE_CREEPIE.get(), "Reapie");

        builder.add(AMItems.SUPPORT_CREEPIE_SPAWN_EGG.get(), "Seepie Spawn Egg");
        builder.add(AMItems.ROCKET_CREEPIE_SPAWN_EGG.get(), "Leapie Spawn Egg");
        builder.add(AMItems.FESTIVE_CREEPIE_SPAWN_EGG.get(), "Reapie Spawn Egg");

        builder.add(AMMobEffects.SUPPORT_SPEED.value(), "Speed");
        builder.add(AMMobEffects.SUPPORT_STRENGTH.value(), "Strength");
        builder.add(AMMobEffects.SUPPORT_SLOWNESS.value(), "Slowness");
        builder.add(AMMobEffects.SUPPORT_WEAKNESS.value(), "Weakness");

        builder.add("block.tolerable_creepers.spore_barrel", "Creeper Spore Barrel");
        builder.add("entity.tolerable_creepers.spore_barrel", "Creeper Spore Barrel");

        builder.add("config.adaptedmobs.title", "Adapted Mobs");
        builder.add("config.adaptedmobs.category.general", "General");
        builder.add("config.adaptedmobs.category.festive_creeper", "Reaper");
        builder.add("config.adaptedmobs.category.support_creeper", "Seeper");
        builder.add("config.adaptedmobs.category.rocket_creeper", "Leaper");

        builder.add("config.adaptedmobs.option.enable_mystery_eggs", "Enable Mystery Eggs");

        builder.add("config.adaptedmobs.option.spawn_festive_creepers", "Spawn Reapers");
        builder.add("config.adaptedmobs.option.festive_creeper_spawn_weight", "Reaper Spawn Weight");
        builder.add("config.adaptedmobs.option.festive_creeper_extra_spawn_weight", "Reaper Extra Spawn Weight");
        builder.add("config.adaptedmobs.option.prevent_festive_creeper_block_damage", "Prevent Reaper Block Damage");
        builder.add("config.adaptedmobs.option.festive_spore_count_day_base", "Reaper Spore Count Day Base");
        builder.add("config.adaptedmobs.option.festive_spore_count_day_random", "Reaper Spore Count Day Random");
        builder.add("config.adaptedmobs.option.festive_spore_count_night_base", "Reaper Spore Count Night Base");
        builder.add("config.adaptedmobs.option.festive_spore_count_night_random", "Reaper Spore Count Night Random");

        builder.add("config.adaptedmobs.option.spawn_support_creepers", "Spawn Seepers");
        builder.add("config.adaptedmobs.option.support_creeper_spawn_weight", "Seeper Spawn Weight");
        builder.add("config.adaptedmobs.option.support_creeper_extra_spawn_weight", "Seeper Extra Spawn Weight");
        builder.add("config.adaptedmobs.option.prevent_support_creeper_block_damage", "Prevent Seeper Block Damage");
        builder.add("config.adaptedmobs.option.support_spore_count_day_base", "Seeper Spore Count Day Base");
        builder.add("config.adaptedmobs.option.support_spore_count_day_random", "Seeper Spore Count Day Random");
        builder.add("config.adaptedmobs.option.support_spore_count_night_base", "Seeper Spore Count Night Base");
        builder.add("config.adaptedmobs.option.support_spore_count_night_random", "Seeper Spore Count Night Random");

        builder.add("config.adaptedmobs.option.spawn_rocket_creepers", "Spawn Leapers");
        builder.add("config.adaptedmobs.option.rocket_creeper_spawn_weight", "Leaper Spawn Weight");
        builder.add("config.adaptedmobs.option.rocket_creeper_extra_spawn_weight", "Leaper Extra Spawn Weight");
        builder.add("config.adaptedmobs.option.prevent_rocket_creeper_block_damage", "Prevent Leaper Block Damage");
        builder.add("config.adaptedmobs.option.rocket_spore_count_day_base", "Leaper Spore Count Day Base");
        builder.add("config.adaptedmobs.option.rocket_spore_count_day_random", "Leaper Spore Count Day Random");
        builder.add("config.adaptedmobs.option.rocket_spore_count_night_base", "Leaper Spore Count Night Base");
        builder.add("config.adaptedmobs.option.rocket_spore_count_night_random", "Leaper Spore Count Night Random");

        builder.add("tag.item.adaptedmobs.creeper_food", "Creeper Food");
    }
}