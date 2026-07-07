package com.cf28.adaptedmobs.neoforge;

import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.neoforged.fml.common.Mod;

@Mod(AdaptedMobs.MOD_ID)
public final class AdaptedMobsNeoForge {
    public AdaptedMobsNeoForge() {
        // Run our common setup.
        AdaptedMobs.bootstrap();
    }
}
