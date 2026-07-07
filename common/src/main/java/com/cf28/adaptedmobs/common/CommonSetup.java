package com.cf28.adaptedmobs.common;

import com.blackgear.platform.common.data.LootModifier;
import com.blackgear.platform.common.integration.MobIntegration;
import com.blackgear.platform.common.worldgen.modifier.BiomeManager;
import com.blackgear.platform.core.ParallelDispatch;
import com.cf28.adaptedmobs.common.integrations.BiomeIntegrations;
import com.cf28.adaptedmobs.common.integrations.LootIntegrations;
import com.cf28.adaptedmobs.common.integrations.MobIntegrations;

public class CommonSetup {
    public static void setup() {
        MobIntegration.registerIntegrations(MobIntegrations::setupMobAttributes);
        MobIntegration.registerIntegrations(MobIntegrations::setupSpawnPlacements);
    }
    
    public static void asyncSetup(ParallelDispatch dispatch) {
        BiomeManager.add(BiomeIntegrations::create);
        LootModifier.modify(LootIntegrations.INSTANCE);
    }
}