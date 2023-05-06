package com.cf28.adaptedmobs.core.fabric;

import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.cf28.adaptedmobs.core.config.fabric.ConfigEntriesImpl;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class AdaptedMobsFabric implements ModInitializer {
    private static final ConfigEntriesImpl CONFIG = AutoConfig.register(ConfigEntriesImpl.class, GsonConfigSerializer::new).getConfig();

    @Override
    public void onInitialize() {
        AdaptedMobs.bootstrap();
    }

    public static ConfigEntriesImpl getConfig() {
        return CONFIG;
    }
}