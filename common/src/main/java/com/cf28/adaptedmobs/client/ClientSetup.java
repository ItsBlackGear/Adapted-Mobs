package com.cf28.adaptedmobs.client;

import com.cf28.adaptedmobs.client.renderer.FestiveCreeperRenderer;
import com.cf28.adaptedmobs.client.renderer.model.FestiveCreeperModel;
import com.cf28.adaptedmobs.common.registry.AMEntityTypes;
import com.cf28.adaptedmobs.core.platform.client.RenderRegistry;
import net.minecraft.client.renderer.entity.CreeperRenderer;

public class ClientSetup {
    public static void onInstance() {
//        RenderRegistry.renderer(AMEntityTypes.FESTIVE_CREEPER, CreeperRenderer::new);
        RenderRegistry.entity(AMEntityTypes.FESTIVE_CREEPER, FestiveCreeperRenderer::new, AMModelLayers.FESTIVE_CREEPER, FestiveCreeperModel::createBodyLayer);
    }

    public static void postInstance() {

    }
}