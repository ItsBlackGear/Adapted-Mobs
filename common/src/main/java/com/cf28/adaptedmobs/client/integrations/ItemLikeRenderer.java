package com.cf28.adaptedmobs.client.integrations;

import com.cf28.adaptedmobs.client.level.model.block_entity.FestiveCreeperSkullModel;
import com.cf28.adaptedmobs.client.level.model.block_entity.PeeperCreeperSkullModel;
import com.cf28.adaptedmobs.client.level.model.block_entity.RocketCreeperSkullModel;
import com.cf28.adaptedmobs.client.registries.AMModelLayers;
import com.cf28.adaptedmobs.common.level.block.SkullTypes;
import com.cf28.adaptedmobs.common.registries.AMBlockEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMBlocks;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;

import static com.blackgear.platform.client.GameRendering.*;

public class ItemLikeRenderer {
    public static void setupBlockEntityRenderers(BlockEntityRendererEvent event) {
        event.register(AMBlockEntityTypes.SKULL.get(), SkullBlockRenderer::new);
    }
    
    public static void setupBlockRenderers(BlockRendererEvent event) {
        event.register(RenderType.cutout(), AMBlocks.FESTIVE_TNT.get());
    }
    
    public static void setupSkullRenderers(SkullRendererEvent event) {
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