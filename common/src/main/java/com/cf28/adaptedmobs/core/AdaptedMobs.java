package com.cf28.adaptedmobs.core;

import com.cf28.adaptedmobs.client.ClientSetup;
import com.cf28.adaptedmobs.common.CommonSetup;
import com.cf28.adaptedmobs.core.platform.ModInstance;

public class AdaptedMobs {
    public static final String MOD_ID = "adaptedmobs";
    public static final ModInstance INSTANCE = ModInstance.create(MOD_ID).common(CommonSetup::onInstance).postCommon(CommonSetup::postInstance).client(ClientSetup::onInstance).postClient(ClientSetup::postInstance).build();

    public static void bootstrap() {
        INSTANCE.bootstrap();
    }
}