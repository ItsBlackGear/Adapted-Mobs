package com.cf28.adaptedmobs.client;

import com.blackgear.platform.client.GameRendering;
import com.blackgear.platform.core.ParallelDispatch;
import com.blackgear.platform.core.events.ResourcePackManager;
import com.cf28.adaptedmobs.client.integrations.CreativeTabIntegrations;
import com.cf28.adaptedmobs.client.integrations.EntityRenderer;
import com.cf28.adaptedmobs.client.integrations.ItemLikeRenderer;
import com.cf28.adaptedmobs.core.AdaptedMobs;

public class ClientSetup {
    public static void setup() {
        ResourcePackManager.registerBuiltResourcePack(AdaptedMobs.resource("aux_textures"), AdaptedMobs.MOD_ID, "Aux's Textures");
        
        GameRendering.registerEntityRenderers(EntityRenderer::setupEntityRenderers);
        GameRendering.registerModelLayers(EntityRenderer::setupLayerDefinitions);
        GameRendering.registerBlockEntityRenderers(ItemLikeRenderer::setupBlockEntityRenderers);
    }
    
    public static void asyncSetup(ParallelDispatch dispatch) {
        GameRendering.registerSkullRenderers(ItemLikeRenderer::setupSkullRenderers);
        GameRendering.registerBlockRenderers(ItemLikeRenderer::setupBlockRenderers);
        CreativeTabIntegrations.bootstrap();
    }
}