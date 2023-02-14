package com.cf28.adaptedmobs.core.fabric;

import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.fabricmc.api.ModInitializer;

public class AdaptedMobsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        AdaptedMobs.bootstrap();
    }
}