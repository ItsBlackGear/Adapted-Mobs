package com.cf28.adaptedmobs.common.integrations;

import com.blackgear.platform.core.Environment;

public final class TolerableCreepersCompat {
    public static final String MOD_ID = "tolerable_creepers";

    public static boolean isLoaded() {
        return Environment.hasModLoaded(MOD_ID);
    }
}
