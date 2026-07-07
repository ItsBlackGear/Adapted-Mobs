package com.cf28.adaptedmobs.client.integrations;

import com.cf28.adaptedmobs.client.level.model.block_entity.FestiveCreeperSkullModel;
import com.cf28.adaptedmobs.client.level.model.block_entity.PeeperCreeperSkullModel;
import com.cf28.adaptedmobs.client.level.model.block_entity.RocketCreeperSkullModel;
import com.cf28.adaptedmobs.client.level.model.mob.*;
import com.cf28.adaptedmobs.client.level.renderer.mob.*;
import com.cf28.adaptedmobs.client.registries.AMModelLayers;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

import static com.blackgear.platform.client.GameRendering.*;

public class EntityRenderer {
    public static void setupEntityRenderers(EntityRendererEvent event) {
        event.register(AMEntityTypes.FESTIVE_CREEPER.get(), FestiveCreeperRenderer::new);
        event.register(AMEntityTypes.SUPPORT_CREEPER.get(), SupportCreeperRenderer::new);
        event.register(AMEntityTypes.ROCKET_CREEPER.get(), RocketCreeperRenderer::new);
        event.register(AMEntityTypes.CREEPER.get(), SimpleCreeperRenderer::new);
        
        event.register(AMEntityTypes.FESTIVE_TNT.get(), FestiveTntRenderer::new);
        event.register(AMEntityTypes.MYSTERY_EGG.get(), ThrownItemRenderer::new);
    }
    
    public static void setupLayerDefinitions(ModelLayerEvent event) {
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
}