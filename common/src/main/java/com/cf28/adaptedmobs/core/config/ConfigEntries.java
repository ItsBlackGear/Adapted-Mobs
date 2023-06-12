package com.cf28.adaptedmobs.core.config;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class ConfigEntries {
    public static final int FESTIVE_CREEPER_SPAWN_WEIGHT = getFestiveCreeperSpawnWeight();
    public static final int SUPPORT_CREEPER_SPAWN_WEIGHT = getSupportCreeperSpawnWeight();
    public static final int ROCKET_CREEPER_SPAWN_WEIGHT = getRocketCreeperSpawnWeight();

    @ExpectPlatform
    public static int getFestiveCreeperSpawnWeight() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getSupportCreeperSpawnWeight() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getRocketCreeperSpawnWeight() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean spawnFestiveCreepers() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean spawnSupportCreepers() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean spawnRocketCreepers() {
        throw new AssertionError();
    }
}