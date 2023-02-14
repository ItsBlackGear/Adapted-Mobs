package com.cf28.adaptedmobs.core.forge;

import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.cf28.adaptedmobs.core.platform.common.BiomeManager;
import net.minecraftforge.fml.common.Mod;

@Mod(AdaptedMobs.MOD_ID)
public class AdaptedMobsForge {
    public AdaptedMobsForge() {
        AdaptedMobs.bootstrap();
        BiomeManager.bootstrap();
    }
}