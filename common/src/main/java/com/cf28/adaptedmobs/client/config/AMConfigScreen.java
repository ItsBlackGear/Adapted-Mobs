package com.cf28.adaptedmobs.client.config;

import com.blackgear.platform.core.util.config.ConfigBuilder;
import com.cf28.adaptedmobs.common.CommonConfig;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public final class AMConfigScreen {
    private AMConfigScreen() {}

    public static Screen create(Screen parent) {
        CommonConfig config = AdaptedMobs.CONFIG;
        List<ConfigBuilder.ConfigValue<?>> values = new ArrayList<>();

        YetAnotherConfigLib.Builder builder = YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("config.adaptedmobs.title"))
                .save(() -> values.forEach(ConfigBuilder.ConfigValue::save));

        ConfigCategory general = ConfigCategory.createBuilder()
                .name(Component.translatable("config.adaptedmobs.category.general"))
                .option(boolOption(values, "enable_mystery_eggs", false, config.enableMysteryEggs))
                .build();

        ConfigCategory festiveCreeper = ConfigCategory.createBuilder()
                .name(Component.translatable("config.adaptedmobs.category.festive_creeper"))
                .option(boolOption(values, "spawn_festive_creepers", true, config.spawnFestiveCreepers))
                .option(intOption(values, "festive_creeper_spawn_weight", 18, 0, 100, config.festiveCreeperSpawnWeight))
                .option(intOption(values, "festive_creeper_extra_spawn_weight", 40, 0, 100, config.festiveCreeperExtraSpawnWeight))
                .option(intOption(values, "festive_spore_count_day_base", 1, 0, 100, config.festiveSporeCountDayBase))
                .option(intOption(values, "festive_spore_count_day_random", 2, 0, 100, config.festiveSporeCountDayRandom))
                .option(intOption(values, "festive_spore_count_night_base", 3, 0, 100, config.festiveSporeCountNightBase))
                .option(intOption(values, "festive_spore_count_night_random", 4, 0, 100, config.festiveSporeCountNightRandom))
                .build();

        ConfigCategory supportCreeper = ConfigCategory.createBuilder()
                .name(Component.translatable("config.adaptedmobs.category.support_creeper"))
                .option(boolOption(values, "spawn_support_creepers", true, config.spawnSupportCreepers))
                .option(intOption(values, "support_creeper_spawn_weight", 20, 0, 100, config.supportCreeperSpawnWeight))
                .option(intOption(values, "support_creeper_extra_spawn_weight", 50, 0, 100, config.supportCreeperExtraSpawnWeight))
                .option(intOption(values, "support_spore_count_day_base", 1, 0, 100, config.supportSporeCountDayBase))
                .option(intOption(values, "support_spore_count_day_random", 2, 0, 100, config.supportSporeCountDayRandom))
                .option(intOption(values, "support_spore_count_night_base", 3, 0, 100, config.supportSporeCountNightBase))
                .option(intOption(values, "support_spore_count_night_random", 4, 0, 100, config.supportSporeCountNightRandom))
                .build();

        ConfigCategory rocketCreeper = ConfigCategory.createBuilder()
                .name(Component.translatable("config.adaptedmobs.category.rocket_creeper"))
                .option(boolOption(values, "spawn_rocket_creepers", true, config.spawnRocketCreepers))
                .option(intOption(values, "rocket_creeper_spawn_weight", 25, 0, 100, config.rocketCreeperSpawnWeight))
                .option(intOption(values, "rocket_creeper_extra_spawn_weight", 50, 0, 100, config.rocketCreeperExtraSpawnWeight))
                .option(boolOption(values, "prevent_rocket_creeper_block_damage", true, config.preventRocketCreeperBlockDamage))
                .option(intOption(values, "rocket_spore_count_day_base", 1, 0, 100, config.rocketSporeCountDayBase))
                .option(intOption(values, "rocket_spore_count_day_random", 2, 0, 100, config.rocketSporeCountDayRandom))
                .option(intOption(values, "rocket_spore_count_night_base", 3, 0, 100, config.rocketSporeCountNightBase))
                .option(intOption(values, "rocket_spore_count_night_random", 4, 0, 100, config.rocketSporeCountNightRandom))
                .build();

        return builder.category(general)
                .category(festiveCreeper)
                .category(supportCreeper)
                .category(rocketCreeper)
                .build()
                .generateScreen(parent);
    }

    private static Option<Boolean> boolOption(List<ConfigBuilder.ConfigValue<?>> values, String name, boolean defaultValue, ConfigBuilder.ConfigValue<Boolean> value) {
        values.add(value);
        return Option.<Boolean>createBuilder()
                .name(Component.translatable("config.adaptedmobs.option." + name))
                .binding(defaultValue, value::get, value::set)
                .controller(TickBoxControllerBuilder::create)
                .build();
    }

    private static Option<Integer> intOption(List<ConfigBuilder.ConfigValue<?>> values, String name, int defaultValue, int min, int max, ConfigBuilder.ConfigValue<Integer> value) {
        values.add(value);
        return Option.<Integer>createBuilder()
                .name(Component.translatable("config.adaptedmobs.option." + name))
                .binding(defaultValue, value::get, value::set)
                .controller(option -> IntegerSliderControllerBuilder.create(option).range(min, max).step(1))
                .build();
    }
}
