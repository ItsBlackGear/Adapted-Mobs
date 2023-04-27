package com.cf28.adaptedmobs.common;

import com.cf28.adaptedmobs.common.entity.creeper.FestiveCreeper;
import com.cf28.adaptedmobs.common.entity.creeper.RocketCreeper;
import com.cf28.adaptedmobs.common.entity.creeper.SupportCreeper;
import com.cf28.adaptedmobs.common.entity.creeper.TamableCreeper;
import com.cf28.adaptedmobs.common.level.WorldGeneration;
import com.cf28.adaptedmobs.common.registry.AMEntityTypes;
import com.cf28.adaptedmobs.core.platform.common.BiomeManager;
import com.cf28.adaptedmobs.core.platform.common.EntityRegistry;

public class CommonSetup {
    public static void onInstance() {
        // Attributes
        EntityRegistry.attributes(AMEntityTypes.FESTIVE_CREEPER, FestiveCreeper::createAttributes);
        EntityRegistry.attributes(AMEntityTypes.SUPPORT_CREEPER, SupportCreeper::createAttributes);
        EntityRegistry.attributes(AMEntityTypes.ROCKET_CREEPER, RocketCreeper::createAttributes);
        EntityRegistry.attributes(AMEntityTypes.CREEPER, TamableCreeper::createAttributes);
    }

    public static void postInstance() {
        BiomeManager.bootstrap();
        WorldGeneration.bootstrap();
    }
}