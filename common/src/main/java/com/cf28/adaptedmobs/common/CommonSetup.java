package com.cf28.adaptedmobs.common;

import com.cf28.adaptedmobs.core.platform.common.BiomeManager;

public class CommonSetup {
    public static void onInstance() {

    }

    public static void postInstance() {
        BiomeManager.bootstrap();
    }
}