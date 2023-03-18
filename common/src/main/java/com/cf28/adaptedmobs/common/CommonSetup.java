package com.cf28.adaptedmobs.common;

import com.cf28.adaptedmobs.common.entity.creeper.FestiveCreeper;
import com.cf28.adaptedmobs.common.registry.AMEntityTypes;
import com.cf28.adaptedmobs.core.platform.common.BiomeManager;
import com.cf28.adaptedmobs.core.platform.common.EntityRegistry;

public class CommonSetup {
    public static void onInstance() {
        // Attributes
        EntityRegistry.attributes(AMEntityTypes.FESTIVE_CREEPER, FestiveCreeper::createAttributes);
    }

    public static void postInstance() {
        BiomeManager.bootstrap();
    }
}