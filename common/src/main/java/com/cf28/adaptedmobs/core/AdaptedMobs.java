package com.cf28.adaptedmobs.core;

import com.cf28.adaptedmobs.client.ClientSetup;
import com.cf28.adaptedmobs.common.CommonSetup;
import com.cf28.adaptedmobs.core.platform.ModInstance;

/**
 * EXTRA FEATURES:
 * - vanilla creepers now drop their own green mystery egg
 * - all mystery eggs are as rare s a wither skull dropping from a wither skeleton and are equally effected by looting
 * - all baby versions of creepers cannot attack, and simply follow around, you can make them sit and have compat with Domestication Innovation
 * - tamed creepers do their usual explosion attacks, however they do not hurt themselves, you, or other tamed animals in the process
 * - tamed creepers can sit, and you cannot charge them with lightning, they'll only target entities that you attack or that attack you
 */
public class AdaptedMobs {
    public static final String MOD_ID = "adaptedmobs";
    public static final ModInstance INSTANCE = ModInstance.create(MOD_ID).common(CommonSetup::onInstance).postCommon(CommonSetup::postInstance).client(ClientSetup::onInstance).postClient(ClientSetup::postInstance).build();

    public static void bootstrap() {
        INSTANCE.bootstrap();
    }
}