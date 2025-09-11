package com.cf28.adaptedmobs.client;

import com.blackgear.platform.client.GameRendering;
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
import com.cf28.adaptedmobs.common.registry.AMBlockEntityTypes;
import com.cf28.adaptedmobs.common.registry.AMBlocks;
import com.cf28.adaptedmobs.common.resource.SkullTypes;
import com.cf28.adaptedmobs.common.registry.AMEntityTypes;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class ClientSetup {
    public static void onInstance() {
        GameRendering.registerEntityRenderers(ClientSetup::setupEntityRenderers);
        GameRendering.registerModelLayers(ClientSetup::setupLayerDefinitions);
        GameRendering.registerBlockEntityRenderers(event -> event.register(AMBlockEntityTypes.SKULL.get(), SkullBlockRenderer::new));
    }

    public static void postInstance(ParallelDispatch dispatch) {
        GameRendering.registerSkullRenderers(ClientSetup::setupSkullRenderers);
        GameRendering.registerBlockRenderers(event -> event.register(RenderType.cutout(), AMBlocks.FESTIVE_TNT.get()));
    }

    private static void setupEntityRenderers(GameRendering.EntityRendererEvent event) {
        event.register(AMEntityTypes.FESTIVE_CREEPER.get(), FestiveCreeperRenderer::new);
        event.register(AMEntityTypes.SUPPORT_CREEPER.get(), SupportCreeperRenderer::new);
        event.register(AMEntityTypes.ROCKET_CREEPER.get(), RocketCreeperRenderer::new);
        event.register(AMEntityTypes.CREEPER.get(), SimpleCreeperRenderer::new);

        event.register(AMEntityTypes.FESTIVE_TNT.get(), FestiveTntRenderer::new);
        event.register(AMEntityTypes.MYSTERY_EGG.get(), ThrownItemRenderer::new);
    }

    private static void setupLayerDefinitions(GameRendering.ModelLayerEvent event) {
        CubeDeformation none = CubeDeformation.NONE;
        CubeDeformation armor = new CubeDeformation(2.0F);
        CubeDeformation cloth = new CubeDeformation(0.25F);

        event.register(AMModelLayers.FESTIVE_CREEPER, () -> FestiveCreeperModel.createBodyLayer(none));
        event.register(AMModelLayers.FESTIVE_CREEPER_SKULL, FestiveCreeperSkullModel::createMobHeadLayer);
        event.register(AMModelLayers.FESTIVE_CREEPER_ARMOR, () -> FestiveCreeperModel.createBodyLayer(armor));
        event.register(AMModelLayers.FESTIVE_CREEPER_CLOTH, () -> FestiveCreeperModel.createBodyLayer(cloth));

        event.register(AMModelLayers.SUPPORT_CREEPER, () -> SupportCreeperModel.createBodyLayer(none));
        event.register(AMModelLayers.SUPPORT_CREEPER_SKULL, SkullModel::createHumanoidHeadLayer);
        event.register(AMModelLayers.SUPPORT_CREEPER_ARMOR, () -> SupportCreeperModel.createBodyLayer(armor));
        event.register(AMModelLayers.SUPPORT_CREEPER_CLOTH, () -> SupportCreeperModel.createBodyLayer(cloth));

        event.register(AMModelLayers.PEEPER_CREEPER, () -> PeeperCreeperModel.createBodyLayer(none));
        event.register(AMModelLayers.PEEPER_CREEPER_SKULL, PeeperCreeperSkullModel::createMobHeadLayer);
        event.register(AMModelLayers.PEEPER_CREEPER_ARMOR, () -> PeeperCreeperModel.createBodyLayer(armor));

        event.register(AMModelLayers.ROCKET_CREEPER, () -> RocketCreeperModel.createBodyLayer(none));
        event.register(AMModelLayers.ROCKET_CREEPER_SKULL, RocketCreeperSkullModel::createMobHeadLayer);
        event.register(AMModelLayers.ROCKET_CREEPER_ARMOR, () -> RocketCreeperModel.createBodyLayer(armor));
        event.register(AMModelLayers.ROCKET_CREEPER_CLOTH, () -> RocketCreeperModel.createBodyLayer(cloth));

        event.register(AMModelLayers.CREEPER, () -> SimpleCreeperModel.createBodyLayer(none));
        event.register(AMModelLayers.CREEPER_ARMOR, () -> SimpleCreeperModel.createBodyLayer(armor));
        event.register(AMModelLayers.CREEPER_CLOTH, () -> SimpleCreeperModel.createBodyLayer(cloth));
    }

    private static void setupSkullRenderers(GameRendering.SkullRendererEvent event) {
        event.registerSkullModel(SkullTypes.FESTIVE_CREEPER, FestiveCreeperSkullModel::new, AMModelLayers.FESTIVE_CREEPER_SKULL);
        event.registerSkullTexture(SkullTypes.FESTIVE_CREEPER, AdaptedMobs.resource("textures/entity/creeper/festive_creeper.png"));
        event.registerSkullModel(SkullTypes.SUPPORT_CREEPER, SkullModel::new, AMModelLayers.SUPPORT_CREEPER_SKULL);
        event.registerSkullTexture(SkullTypes.SUPPORT_CREEPER, AdaptedMobs.resource("textures/entity/creeper/support_creeper.png"));
        event.registerSkullModel(SkullTypes.ROCKET_CREEPER, RocketCreeperSkullModel::new, AMModelLayers.ROCKET_CREEPER_SKULL);
        event.registerSkullTexture(SkullTypes.ROCKET_CREEPER, AdaptedMobs.resource("textures/entity/creeper/rocket_creeper.png"));
        event.registerSkullModel(SkullTypes.PEEPER_CREEPER, PeeperCreeperSkullModel::new, AMModelLayers.PEEPER_CREEPER_SKULL);
        event.registerSkullTexture(SkullTypes.PEEPER_CREEPER, AdaptedMobs.resource("textures/entity/creeper/peeper_creeper.png"));
    }
}