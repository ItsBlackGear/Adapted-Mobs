package com.cf28.adaptedmobs.client.renderer.layer;

import com.cf28.adaptedmobs.client.AMModelLayers;
import com.cf28.adaptedmobs.client.renderer.model.RocketCreeperModel;
import com.cf28.adaptedmobs.client.renderer.model.SupportCreeperModel;
import com.cf28.adaptedmobs.common.entity.creeper.RocketCreeper;
import com.cf28.adaptedmobs.common.entity.creeper.SupportCreeper;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;

public class RocketCreeperPowerLayer extends EnergySwirlLayer<RocketCreeper, RocketCreeperModel<RocketCreeper>> {
    private final RocketCreeperModel<RocketCreeper> model;

    public RocketCreeperPowerLayer(RenderLayerParent<RocketCreeper, RocketCreeperModel<RocketCreeper>> parent, EntityModelSet modelSet) {
        super(parent);
        this.model = new RocketCreeperModel<>(modelSet.bakeLayer(AMModelLayers.SUPPORT_CREEPER_ARMOR));
    }

    @Override
    protected float xOffset(float x) {
        return x * 0.01F;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return new ResourceLocation(AdaptedMobs.MOD_ID, "textures/entity/creeper/armor.png");
    }

    @Override
    protected EntityModel<RocketCreeper> model() {
        return this.model;
    }
}
