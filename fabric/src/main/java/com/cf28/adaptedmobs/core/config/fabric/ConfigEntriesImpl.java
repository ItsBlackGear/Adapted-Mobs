package com.cf28.adaptedmobs.core.config.fabric;

import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.cf28.adaptedmobs.core.fabric.AdaptedMobsFabric;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = AdaptedMobs.MOD_ID)
public class ConfigEntriesImpl implements ConfigData {
    public static int getFestiveCreeperSpawnWeight() {
        return AdaptedMobsFabric.CONFIG.spawns.festive_creeper_spawn_weight;
    }

    public static int getFestiveCreeperExtraSpawnWeight() {
        return AdaptedMobsFabric.CONFIG.spawns.festive_creeper_extra_spawn_weight;
    }

    public static int getSupportCreeperSpawnWeight() {
        return AdaptedMobsFabric.CONFIG.spawns.support_creeper_spawn_weight;
    }

    public static int getSupportCreeperExtraSpawnWeight() {
        return AdaptedMobsFabric.CONFIG.spawns.support_creeper_extra_spawn_weight;
    }

    public static int getRocketCreeperSpawnWeight() {
        return AdaptedMobsFabric.CONFIG.spawns.rocket_creeper_spawn_weight;
    }

    public static int getRocketCreeperExtraSpawnWeight() {
        return AdaptedMobsFabric.CONFIG.spawns.rocket_creeper_extra_spawn_weight;
    }

    public static boolean spawnFestiveCreepers() {
        return AdaptedMobsFabric.CONFIG.spawns.spawn_festive_creeper;
    }

    public static boolean spawnSupportCreepers() {
        return AdaptedMobsFabric.CONFIG.spawns.spawn_support_creeper;
    }

    public static boolean spawnRocketCreepers() {
        return AdaptedMobsFabric.CONFIG.spawns.spawn_rocket_creeper;
    }

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public Spawns spawns = new Spawns();

    public static class Spawns {
        @ConfigEntry.Gui.Tooltip
        public boolean spawn_festive_creeper = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int festive_creeper_spawn_weight = 25;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int festive_creeper_extra_spawn_weight = 50;

        @ConfigEntry.Gui.Tooltip
        public boolean spawn_support_creeper = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int support_creeper_spawn_weight = 25;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int support_creeper_extra_spawn_weight = 50;

        @ConfigEntry.Gui.Tooltip
        public boolean spawn_rocket_creeper = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int rocket_creeper_spawn_weight = 25;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int rocket_creeper_extra_spawn_weight = 50;
    }
}