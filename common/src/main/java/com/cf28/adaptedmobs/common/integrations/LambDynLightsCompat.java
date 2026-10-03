package com.cf28.adaptedmobs.common.integrations;

import com.blackgear.platform.core.Environment;
import net.minecraft.world.entity.LivingEntity;

public final class LambDynLightsCompat {
    public static final String MOD_ID = "lambdynlights";

    public static boolean isLoaded() {
        return Environment.hasModLoaded(MOD_ID);
    }

    public static int getLivingEntityLuminance(LivingEntity entity) {
        if (!isLoaded() || entity == null) {
            return 0;
        }
        if (entity.isOnFire()) {
            return 15;
        }
        return Helper.getLuminance(entity);
    }

    private static class Helper {
        private static int getLuminance(LivingEntity entity) {
            return LambDynamicLightsIntegration.getLivingEntityLuminance(entity);
        }
    }
}
