package com.cf28.adaptedmobs.client;

import com.cf28.adaptedmobs.client.renderer.FestiveCreeperRenderer;
import com.cf28.adaptedmobs.client.renderer.FestiveTntRenderer;
import com.cf28.adaptedmobs.client.renderer.RocketCreeperRenderer;
import com.cf28.adaptedmobs.client.renderer.SimpleCreeperRenderer;
import com.cf28.adaptedmobs.client.renderer.SupportCreeperRenderer;
import com.cf28.adaptedmobs.client.renderer.model.FestiveCreeperModel;
import com.cf28.adaptedmobs.client.renderer.model.RocketCreeperModel;
import com.cf28.adaptedmobs.client.renderer.model.SimpleCreeperModel;
import com.cf28.adaptedmobs.client.renderer.model.SupportCreeperModel;
import com.cf28.adaptedmobs.common.registry.AMEntityTypes;
import com.cf28.adaptedmobs.core.platform.client.RenderRegistry;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class ClientSetup {
    public static void onInstance() {
        RenderRegistry.entity(AMEntityTypes.FESTIVE_CREEPER, FestiveCreeperRenderer::new, AMModelLayers.FESTIVE_CREEPER, () -> FestiveCreeperModel.createBodyLayer(CubeDeformation.NONE));
        RenderRegistry.layerDefinition(AMModelLayers.FESTIVE_CREEPER_ARMOR, () -> FestiveCreeperModel.createBodyLayer(new CubeDeformation(2.0F)));
        RenderRegistry.entity(AMEntityTypes.SUPPORT_CREEPER, SupportCreeperRenderer::new, AMModelLayers.SUPPORT_CREEPER, () -> SupportCreeperModel.createBodyLayer(CubeDeformation.NONE));
        RenderRegistry.layerDefinition(AMModelLayers.SUPPORT_CREEPER_ARMOR, () -> SupportCreeperModel.createBodyLayer(new CubeDeformation(2.0F)));
        RenderRegistry.entity(AMEntityTypes.ROCKET_CREEPER, RocketCreeperRenderer::new, AMModelLayers.ROCKET_CREEPER, () -> RocketCreeperModel.createBodyLayer(CubeDeformation.NONE));
        RenderRegistry.layerDefinition(AMModelLayers.ROCKET_CREEPER_ARMOR, () -> RocketCreeperModel.createBodyLayer(new CubeDeformation(2.0F)));
        RenderRegistry.entity(AMEntityTypes.CREEPER, SimpleCreeperRenderer::new, AMModelLayers.CREEPER, () -> SimpleCreeperModel.createBodyLayer(CubeDeformation.NONE));
        RenderRegistry.layerDefinition(AMModelLayers.CREEPER_ARMOR, () -> SimpleCreeperModel.createBodyLayer(new CubeDeformation(2.0F)));

        RenderRegistry.renderer(AMEntityTypes.FESTIVE_TNT, FestiveTntRenderer::new);
        RenderRegistry.renderer(AMEntityTypes.MYSTERY_EGG, ThrownItemRenderer::new);
    }

    public static void postInstance() {

    }
}