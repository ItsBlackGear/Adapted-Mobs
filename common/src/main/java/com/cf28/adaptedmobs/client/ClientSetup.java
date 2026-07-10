package com.cf28.adaptedmobs.client;

import com.blackgear.platform.client.GameRendering;
import com.blackgear.platform.core.ParallelDispatch;
import com.blackgear.platform.core.events.ResourcePackManager;
import com.cf28.adaptedmobs.client.integrations.CreativeTabIntegrations;
import com.cf28.adaptedmobs.client.integrations.EntityRenderer;
import com.cf28.adaptedmobs.client.integrations.ItemLikeRenderer;
import com.cf28.adaptedmobs.client.particle.AMFlowerParticle;
import com.cf28.adaptedmobs.client.particle.AMSporeParticle;
import com.cf28.adaptedmobs.common.registries.AMParticles;
import com.cf28.adaptedmobs.core.AdaptedMobs;

public class ClientSetup {
    public static void setup() {
        ResourcePackManager.registerBuiltResourcePack(AdaptedMobs.resource("aux_textures"), AdaptedMobs.MOD_ID, "Aux's Textures");

        GameRendering.registerEntityRenderers(EntityRenderer::setupEntityRenderers);
        GameRendering.registerModelLayers(EntityRenderer::setupLayerDefinitions);
        GameRendering.registerBlockEntityRenderers(ItemLikeRenderer::setupBlockEntityRenderers);
        GameRendering.registerParticleFactories(ClientSetup::setupParticles);
    }

    public static void setupParticles(GameRendering.ParticleFactoryEvent event) {
        event.register(AMParticles.FESTIVE_TNT_PARTICLETRAIL, AMSporeParticle.Provider::new);
        event.register(AMParticles.FESTIVE_SPORES, AMSporeParticle.Provider::new);
        event.register(AMParticles.ROCKET_SPORES, AMSporeParticle.Provider::new);
        event.register(AMParticles.SUPPORTED_RED, AMFlowerParticle.Provider::new);
        event.register(AMParticles.SUPPORTED_BLUE, AMFlowerParticle.Provider::new);
        event.register(AMParticles.SUPPORTED_YELLOW, AMFlowerParticle.Provider::new);
        event.register(AMParticles.SUPPORTED_GREY, AMFlowerParticle.Provider::new);
        event.register(AMParticles.CREEPER_HEAL, AMFlowerParticle.Provider::new);
    }

    public static void asyncSetup(ParallelDispatch dispatch) {
        GameRendering.registerSkullRenderers(ItemLikeRenderer::setupSkullRenderers);
        GameRendering.registerBlockRenderers(ItemLikeRenderer::setupBlockRenderers);
        CreativeTabIntegrations.bootstrap();
    }
}