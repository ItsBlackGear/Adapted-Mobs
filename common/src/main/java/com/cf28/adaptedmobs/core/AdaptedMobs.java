package com.cf28.adaptedmobs.core;

import com.blackgear.platform.core.Environment;
import com.blackgear.platform.core.ModInstance;
import com.blackgear.platform.core.util.config.ConfigLoader;
import com.blackgear.platform.core.util.config.ModConfig;
import com.cf28.adaptedmobs.client.ClientSetup;
import com.cf28.adaptedmobs.common.CommonConfig;
import com.cf28.adaptedmobs.common.CommonSetup;
import com.cf28.adaptedmobs.common.registries.*;
import com.cf28.adaptedmobs.core.tags.AMBiomeTags;
import com.cf28.adaptedmobs.core.tags.AMItemTags;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public final class AdaptedMobs {
    public static final String MOD_ID = "adaptedmobs";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final CommonConfig CONFIG = Environment.registerConfig(MOD_ID, ModConfig.Type.COMMON, CommonConfig::new);
    public static final ModInstance INSTANCE = ModInstance.create(MOD_ID)
            .client(() -> ClientSetup.setup())
            .postClient(dispatch -> ClientSetup.asyncSetup(dispatch))
            .common(CommonSetup::setup)
            .postCommon(CommonSetup::asyncSetup)
            .build();

    public static void bootstrap() {
        INSTANCE.bootstrap();
        ConfigLoader.bootstrap();

        AMItems.REGISTRIES.register();
        AMBlocks.REGISTRIES.register();
        AMEntityTypes.REGISTRIES.register();
        AMBlockEntityTypes.REGISTRIES.register();
        AMEntityDataSerializers.REGISTRIES.register();
        AMParticles.REGISTRIES.register();
        AMMobEffects.REGISTRIES.register();
        AMSoundEvents.REGISTRIES.register();
        AMStructureTypes.REGISTRIES.register();
        AMStructurePieceTypes.REGISTRIES.register();

        AMBiomeTags.REGISTRY.register();
        AMItemTags.REGISTRY.register();
    }

    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}