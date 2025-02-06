package com.cf28.adaptedmobs.client.renderer;

import com.cf28.adaptedmobs.client.AMModelLayers;
import com.cf28.adaptedmobs.client.renderer.layer.CreeperClothLayer;
import com.cf28.adaptedmobs.client.renderer.layer.EntityPowerLayer;
import com.cf28.adaptedmobs.client.renderer.model.RocketCreeperModel;
import com.cf28.adaptedmobs.common.entity.creeper.RocketCreeper;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class RocketCreeperRenderer extends SwellableCreeperRenderer<RocketCreeper, RocketCreeperModel<RocketCreeper>> {
    public RocketCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, new RocketCreeperModel<>(context.bakeLayer(AMModelLayers.ROCKET_CREEPER)), 0.5F);
        this.addLayer(new EntityPowerLayer<>(this, new RocketCreeperModel<>(context.bakeLayer(AMModelLayers.ROCKET_CREEPER_ARMOR))));
        this.addLayer(new CreeperClothLayer<>(this, new RocketCreeperModel<>(context.bakeLayer(AMModelLayers.ROCKET_CREEPER_CLOTH))));
    }

    @Override
    public ResourceLocation getTextureLocation(RocketCreeper entity) {
        return AdaptedMobs.resource("textures/entity/creeper/rocket_creeper.png");
    }
}