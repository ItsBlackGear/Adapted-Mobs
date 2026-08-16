package com.cf28.adaptedmobs.client.integrations;

import com.cf28.adaptedmobs.client.level.model.block_entity.FestiveCreeperSkullModel;
import com.cf28.adaptedmobs.client.level.model.block_entity.PeeperCreeperSkullModel;
import com.cf28.adaptedmobs.client.level.model.block_entity.RocketCreeperSkullModel;
import com.cf28.adaptedmobs.client.level.model.block_entity.SupportCreeperSkullModel;
import com.cf28.adaptedmobs.client.level.model.mob.*;
import com.cf28.adaptedmobs.client.level.renderer.mob.*;
import com.cf28.adaptedmobs.client.registries.AMModelLayers;
import com.cf28.adaptedmobs.common.integrations.TolerableCreepersCompat;
import com.cf28.adaptedmobs.common.integrations.tolerablecreepers.SupportCreepieEntity;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.evandev.tolerable_creepers.common.entity.Creepie;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.function.Function;

import static com.blackgear.platform.client.GameRendering.EntityRendererEvent;
import static com.blackgear.platform.client.GameRendering.ModelLayerEvent;

public class EntityRenderer {
    public static void setupEntityRenderers(EntityRendererEvent event) {
        event.register(AMEntityTypes.FESTIVE_CREEPER.get(), FestiveCreeperRenderer::new);
        event.register(AMEntityTypes.SUPPORT_CREEPER.get(), SupportCreeperRenderer::new);
        event.register(AMEntityTypes.ROCKET_CREEPER.get(), RocketCreeperRenderer::new);
        event.register(AMEntityTypes.CREEPER.get(), SimpleCreeperRenderer::new);
        event.register(AMEntityTypes.HARPY.get(), HarpyRenderer::new);
        event.register(AMEntityTypes.ENTOMBED.get(), EntombedRenderer::new);

        event.register(AMEntityTypes.FESTIVE_TNT.get(), FestiveTntRenderer::new);
        event.register(AMEntityTypes.MYSTERY_EGG.get(), ThrownItemRenderer::new);

        if (TolerableCreepersCompat.isLoaded()) {
            event.register(AMEntityTypes.SUPPORT_SPORES.get(), NoopEntityRenderer::new);
            event.register(AMEntityTypes.ROCKET_SPORES.get(), NoopEntityRenderer::new);
            event.register(AMEntityTypes.FESTIVE_SPORES.get(), NoopEntityRenderer::new);

            registerCreepieRenderer(event, AMEntityTypes.SUPPORT_CREEPIE.get(),
                    context -> new SupportCreepieModel<>(context.bakeLayer(AMModelLayers.SUPPORT_CREEPIE)),
                    SupportCreepieEntity.Variant.SPEED.getTexture());
            registerCreepieRenderer(event, AMEntityTypes.ROCKET_CREEPIE.get(),
                    context -> new RocketCreepieModel<>(context.bakeLayer(AMModelLayers.ROCKET_CREEPIE)),
                    ResourceLocation.fromNamespaceAndPath(AdaptedMobs.MOD_ID, "textures/entity/tolerable_creepers/rocket_creepie.png"));
            registerCreepieRenderer(event, AMEntityTypes.FESTIVE_CREEPIE.get(),
                    context -> new FestiveCreepieModel<>(context.bakeLayer(AMModelLayers.FESTIVE_CREEPIE)),
                    ResourceLocation.fromNamespaceAndPath(AdaptedMobs.MOD_ID, "textures/entity/tolerable_creepers/festive_creepie.png"));
        }
        event.register(AMEntityTypes.PRIMED_SPORE_BARREL.get(), AMPrimedSporeBarrelRenderer::new);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void registerCreepieRenderer(EntityRendererEvent event,
                                                EntityType<?> entityType,
                                                Function<EntityRendererProvider.Context, EntityModel<? extends Creepie>> modelFactory,
                                                ResourceLocation texture) {
        event.register((EntityType) entityType, context ->
                new AMCreepieRenderer<>(context, (EntityModel) modelFactory.apply(context), texture));
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
        event.register(AMModelLayers.SUPPORT_CREEPER_SKULL, SupportCreeperSkullModel::createMobHeadLayer);
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

        if (TolerableCreepersCompat.isLoaded()) {
            event.register(AMModelLayers.SUPPORT_CREEPIE, SupportCreepieModel::createBodyLayer);
            event.register(AMModelLayers.ROCKET_CREEPIE, RocketCreepieModel::createBodyLayer);
            event.register(AMModelLayers.FESTIVE_CREEPIE, FestiveCreepieModel::createBodyLayer);
        }

        event.register(AMModelLayers.HARPY, HarpyModel::createBodyLayer);
        event.register(AMModelLayers.HARPY_CHICK, HarpyChickModel::createBodyLayer);
        event.register(AMModelLayers.ENTOMBED, EntombedModel::createBodyLayer);
    }
}