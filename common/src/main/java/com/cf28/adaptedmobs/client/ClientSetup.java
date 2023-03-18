package com.cf28.adaptedmobs.client;

import com.cf28.adaptedmobs.client.renderer.FestiveCreeperRenderer;
import com.cf28.adaptedmobs.client.renderer.FestiveTntRenderer;
import com.cf28.adaptedmobs.client.renderer.model.FestiveCreeperModel;
import com.cf28.adaptedmobs.common.registry.AMEntityTypes;
import com.cf28.adaptedmobs.core.platform.client.RenderRegistry;
import net.minecraft.client.model.geom.builders.CubeDeformation;

public class ClientSetup {
    public static void onInstance() {
        RenderRegistry.entity(AMEntityTypes.FESTIVE_CREEPER, FestiveCreeperRenderer::new, AMModelLayers.FESTIVE_CREEPER, () -> FestiveCreeperModel.createBodyLayer(CubeDeformation.NONE));
        RenderRegistry.layerDefinition(AMModelLayers.FESTIVE_CREEPER_ARMOR, () -> FestiveCreeperModel.createBodyLayer(new CubeDeformation(2.0F)));
        RenderRegistry.renderer(AMEntityTypes.FESTIVE_TNT, FestiveTntRenderer::new);
    }

    public static void postInstance() {

    }
}