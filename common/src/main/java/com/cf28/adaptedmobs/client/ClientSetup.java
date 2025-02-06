package com.cf28.adaptedmobs.client;

import com.blackgear.platform.client.RendererRegistry;
import com.blackgear.platform.core.ParallelDispatch;
import com.cf28.adaptedmobs.client.renderer.FestiveCreeperRenderer;
import com.cf28.adaptedmobs.client.renderer.FestiveTntRenderer;
import com.cf28.adaptedmobs.client.renderer.RocketCreeperRenderer;
import com.cf28.adaptedmobs.client.renderer.SimpleCreeperRenderer;
import com.cf28.adaptedmobs.client.renderer.SupportCreeperRenderer;
import com.cf28.adaptedmobs.client.renderer.model.*;
import com.cf28.adaptedmobs.common.registry.AMEntityTypes;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class ClientSetup {
    public static void onInstance() {
        RendererRegistry.addEntityRenderer(AMEntityTypes.FESTIVE_CREEPER, FestiveCreeperRenderer::new);
        RendererRegistry.addLayerDefinition(AMModelLayers.FESTIVE_CREEPER, () -> FestiveCreeperModel.createBodyLayer(CubeDeformation.NONE));
        RendererRegistry.addLayerDefinition(AMModelLayers.FESTIVE_CREEPER_ARMOR, () -> FestiveCreeperModel.createBodyLayer(new CubeDeformation(2.0F)));
        RendererRegistry.addLayerDefinition(AMModelLayers.FESTIVE_CREEPER_CLOTH, () -> FestiveCreeperModel.createBodyLayer(new CubeDeformation(0.25F)));

        RendererRegistry.addEntityRenderer(AMEntityTypes.SUPPORT_CREEPER, SupportCreeperRenderer::new);
        RendererRegistry.addLayerDefinition(AMModelLayers.SUPPORT_CREEPER, () -> SupportCreeperModel.createBodyLayer(CubeDeformation.NONE));
        RendererRegistry.addLayerDefinition(AMModelLayers.SUPPORT_CREEPER_CLOTH, () -> SupportCreeperModel.createBodyLayer(new CubeDeformation(0.25F)));
        RendererRegistry.addLayerDefinition(AMModelLayers.SUPPORT_CREEPER_ARMOR, () -> SupportCreeperModel.createBodyLayer(new CubeDeformation(2.0F)));

        RendererRegistry.addLayerDefinition(AMModelLayers.PEEPER_CREEPER, () -> PeeperCreeperModel.createBodyLayer(CubeDeformation.NONE));
        RendererRegistry.addLayerDefinition(AMModelLayers.PEEPER_CREEPER_ARMOR, () -> PeeperCreeperModel.createBodyLayer(new CubeDeformation(2.0F)));

        RendererRegistry.addEntityRenderer(AMEntityTypes.ROCKET_CREEPER, RocketCreeperRenderer::new);
        RendererRegistry.addLayerDefinition(AMModelLayers.ROCKET_CREEPER, () -> RocketCreeperModel.createBodyLayer(CubeDeformation.NONE));
        RendererRegistry.addLayerDefinition(AMModelLayers.ROCKET_CREEPER_ARMOR, () -> RocketCreeperModel.createBodyLayer(new CubeDeformation(2.0F)));
        RendererRegistry.addLayerDefinition(AMModelLayers.ROCKET_CREEPER_CLOTH, () -> RocketCreeperModel.createBodyLayer(new CubeDeformation(0.25F)));

        RendererRegistry.addEntityRenderer(AMEntityTypes.CREEPER, SimpleCreeperRenderer::new);
        RendererRegistry.addLayerDefinition(AMModelLayers.CREEPER, () -> SimpleCreeperModel.createBodyLayer(CubeDeformation.NONE));
        RendererRegistry.addLayerDefinition(AMModelLayers.CREEPER_ARMOR, () -> SimpleCreeperModel.createBodyLayer(new CubeDeformation(2.0F)));
        RendererRegistry.addLayerDefinition(AMModelLayers.CREEPER_CLOTH, () -> SimpleCreeperModel.createBodyLayer(new CubeDeformation(0.25F)));

        RendererRegistry.addEntityRenderer(AMEntityTypes.FESTIVE_TNT, FestiveTntRenderer::new);
        RendererRegistry.addEntityRenderer(AMEntityTypes.MYSTERY_EGG, ThrownItemRenderer::new);
    }

    public static void postInstance(ParallelDispatch dispatch) {

    }
}