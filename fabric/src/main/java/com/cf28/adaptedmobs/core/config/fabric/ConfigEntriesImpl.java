package com.cf28.adaptedmobs.core.config.fabric;

import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.cf28.adaptedmobs.core.fabric.AdaptedMobsFabric;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

@Config(name = AdaptedMobs.MOD_ID)
public class ConfigEntriesImpl implements ConfigData {
    public static int getFestiveCreeperSpawnWeight() {
        return AdaptedMobsFabric.getConfig().spawns.festive_creeper_spawn_weight;
    }

    public static int getSupportCreeperSpawnWeight() {
        return AdaptedMobsFabric.getConfig().spawns.support_creeper_spawn_weight;
    }

    public static int getRocketCreeperSpawnWeight() {
        return AdaptedMobsFabric.getConfig().spawns.rocket_creeper_spawn_weight;
    }

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public Spawns spawns = new Spawns();

    public static class Spawns {
        @ConfigEntry.Gui.Tooltip
        public int festive_creeper_spawn_weight = 100;

        @ConfigEntry.Gui.Tooltip
        public int support_creeper_spawn_weight = 100;

        @ConfigEntry.Gui.Tooltip
        public int rocket_creeper_spawn_weight = 100;
    }
}