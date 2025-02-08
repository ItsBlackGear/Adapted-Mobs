package com.cf28.adaptedmobs.client;

import com.blackgear.platform.client.RendererRegistry;
import com.blackgear.platform.core.ParallelDispatch;
import com.cf28.adaptedmobs.client.renderer.FestiveCreeperRenderer;
import com.cf28.adaptedmobs.client.renderer.FestiveTntRenderer;
import com.cf28.adaptedmobs.client.renderer.RocketCreeperRenderer;
import com.cf28.adaptedmobs.client.renderer.SimpleCreeperRenderer;
import com.cf28.adaptedmobs.client.renderer.SupportCreeperRenderer;
import com.cf28.adaptedmobs.client.renderer.model.*;
import com.cf28.adaptedmobs.client.renderer.model.blockentity.FestiveCreeperSkullModel;
import com.cf28.adaptedmobs.client.renderer.model.blockentity.PeeperCreeperSkullModel;
import com.cf28.adaptedmobs.client.renderer.model.blockentity.RocketCreeperSkullModel;
import com.cf28.adaptedmobs.common.blockentity.Skulls;
import com.cf28.adaptedmobs.common.registry.AMBlockEntities;
import com.cf28.adaptedmobs.common.registry.AMEntityTypes;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class ClientSetup {
    public static void onInstance() {
        setupEntityRenderers();
        setupBlockEntityRenderers();
        setupLayerDefinitions();
    }

    public static void postInstance(ParallelDispatch dispatch) {
        RendererRegistry.registerSkullModel(Skulls.FESTIVE_CREEPER, FestiveCreeperSkullModel::new, AMModelLayers.FESTIVE_CREEPER_SKULL);
        RendererRegistry.registerSkullTexture(Skulls.FESTIVE_CREEPER, AdaptedMobs.resource("textures/entity/creeper/festive_creeper.png"));
        RendererRegistry.registerSkullModel(Skulls.SUPPORT_CREEPER, SkullModel::new, AMModelLayers.SUPPORT_CREEPER_SKULL);
        RendererRegistry.registerSkullTexture(Skulls.SUPPORT_CREEPER, AdaptedMobs.resource("textures/entity/creeper/support_creeper.png"));
        RendererRegistry.registerSkullModel(Skulls.ROCKET_CREEPER, RocketCreeperSkullModel::new, AMModelLayers.ROCKET_CREEPER_SKULL);
        RendererRegistry.registerSkullTexture(Skulls.ROCKET_CREEPER, AdaptedMobs.resource("textures/entity/creeper/rocket_creeper.png"));
        RendererRegistry.registerSkullModel(Skulls.PEEPER_CREEPER, PeeperCreeperSkullModel::new, AMModelLayers.PEEPER_CREEPER_SKULL);
        RendererRegistry.registerSkullTexture(Skulls.PEEPER_CREEPER, AdaptedMobs.resource("textures/entity/creeper/peeper_creeper.png"));
    }

    private static void setupBlockEntityRenderers() {
        RendererRegistry.addBlockEntityRenderer(AMBlockEntities.SKULL, SkullBlockRenderer::new);
    }

    private static void setupEntityRenderers() {
        RendererRegistry.addEntityRenderer(AMEntityTypes.FESTIVE_CREEPER, FestiveCreeperRenderer::new);
        RendererRegistry.addEntityRenderer(AMEntityTypes.SUPPORT_CREEPER, SupportCreeperRenderer::new);
        RendererRegistry.addEntityRenderer(AMEntityTypes.ROCKET_CREEPER, RocketCreeperRenderer::new);
        RendererRegistry.addEntityRenderer(AMEntityTypes.CREEPER, SimpleCreeperRenderer::new);

        RendererRegistry.addEntityRenderer(AMEntityTypes.FESTIVE_TNT, FestiveTntRenderer::new);
        RendererRegistry.addEntityRenderer(AMEntityTypes.MYSTERY_EGG, ThrownItemRenderer::new);
    }

    private static void setupLayerDefinitions() {
        CubeDeformation none = CubeDeformation.NONE;
        CubeDeformation armor = new CubeDeformation(2.0F);
        CubeDeformation cloth = new CubeDeformation(0.25F);

        RendererRegistry.addLayerDefinition(AMModelLayers.FESTIVE_CREEPER, () -> FestiveCreeperModel.createBodyLayer(none));
        RendererRegistry.addLayerDefinition(AMModelLayers.FESTIVE_CREEPER_SKULL, FestiveCreeperSkullModel::createMobHeadLayer);
        RendererRegistry.addLayerDefinition(AMModelLayers.FESTIVE_CREEPER_ARMOR, () -> FestiveCreeperModel.createBodyLayer(armor));
        RendererRegistry.addLayerDefinition(AMModelLayers.FESTIVE_CREEPER_CLOTH, () -> FestiveCreeperModel.createBodyLayer(cloth));

        RendererRegistry.addLayerDefinition(AMModelLayers.SUPPORT_CREEPER, () -> SupportCreeperModel.createBodyLayer(none));
        RendererRegistry.addLayerDefinition(AMModelLayers.SUPPORT_CREEPER_SKULL, SkullModel::createHumanoidHeadLayer);
        RendererRegistry.addLayerDefinition(AMModelLayers.SUPPORT_CREEPER_ARMOR, () -> SupportCreeperModel.createBodyLayer(armor));
        RendererRegistry.addLayerDefinition(AMModelLayers.SUPPORT_CREEPER_CLOTH, () -> SupportCreeperModel.createBodyLayer(cloth));

        RendererRegistry.addLayerDefinition(AMModelLayers.PEEPER_CREEPER, () -> PeeperCreeperModel.createBodyLayer(none));
        RendererRegistry.addLayerDefinition(AMModelLayers.PEEPER_CREEPER_SKULL, PeeperCreeperSkullModel::createMobHeadLayer);
        RendererRegistry.addLayerDefinition(AMModelLayers.PEEPER_CREEPER_ARMOR, () -> PeeperCreeperModel.createBodyLayer(armor));

        RendererRegistry.addLayerDefinition(AMModelLayers.ROCKET_CREEPER, () -> RocketCreeperModel.createBodyLayer(none));
        RendererRegistry.addLayerDefinition(AMModelLayers.ROCKET_CREEPER_SKULL, RocketCreeperSkullModel::createMobHeadLayer);
        RendererRegistry.addLayerDefinition(AMModelLayers.ROCKET_CREEPER_ARMOR, () -> RocketCreeperModel.createBodyLayer(armor));
        RendererRegistry.addLayerDefinition(AMModelLayers.ROCKET_CREEPER_CLOTH, () -> RocketCreeperModel.createBodyLayer(cloth));

        RendererRegistry.addLayerDefinition(AMModelLayers.CREEPER, () -> SimpleCreeperModel.createBodyLayer(none));
        RendererRegistry.addLayerDefinition(AMModelLayers.CREEPER_ARMOR, () -> SimpleCreeperModel.createBodyLayer(armor));
        RendererRegistry.addLayerDefinition(AMModelLayers.CREEPER_CLOTH, () -> SimpleCreeperModel.createBodyLayer(cloth));

    }
}