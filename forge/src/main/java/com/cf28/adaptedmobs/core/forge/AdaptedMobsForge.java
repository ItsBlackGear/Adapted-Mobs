package com.cf28.adaptedmobs.core.forge;

import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.cf28.adaptedmobs.core.config.forge.ConfigEntriesImpl;
import com.cf28.adaptedmobs.core.platform.common.BiomeManager;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(AdaptedMobs.MOD_ID)
public class AdaptedMobsForge {
    public AdaptedMobsForge() {
        AdaptedMobs.bootstrap();
        BiomeManager.bootstrap();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigEntriesImpl.COMMON_SPEC);
    }
}