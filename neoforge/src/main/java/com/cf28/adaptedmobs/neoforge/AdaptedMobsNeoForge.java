package com.cf28.adaptedmobs.neoforge;

import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.cf28.adaptedmobs.neoforge.client.ClientConfigSetup;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(AdaptedMobs.MOD_ID)
public final class AdaptedMobsNeoForge {
    public AdaptedMobsNeoForge(ModContainer modContainer) {
        AdaptedMobs.bootstrap();

        if (FMLEnvironment.dist.isClient()) {
            ClientConfigSetup.register(modContainer);
        }
    }
}
