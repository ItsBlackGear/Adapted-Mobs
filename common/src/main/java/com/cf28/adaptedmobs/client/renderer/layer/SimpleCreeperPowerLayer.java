package com.cf28.adaptedmobs.client.renderer.layer;

import com.cf28.adaptedmobs.client.AMModelLayers;
import com.cf28.adaptedmobs.client.renderer.model.SimpleCreeperModel;
import com.cf28.adaptedmobs.client.renderer.model.SupportCreeperModel;
import com.cf28.adaptedmobs.common.entity.creeper.SupportCreeper;
import com.cf28.adaptedmobs.common.entity.creeper.TamableCreeper;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;

public class SimpleCreeperPowerLayer extends EnergySwirlLayer<TamableCreeper, SimpleCreeperModel<TamableCreeper>> {
    private final SimpleCreeperModel<TamableCreeper> model;

    public SimpleCreeperPowerLayer(RenderLayerParent<TamableCreeper, SimpleCreeperModel<TamableCreeper>> parent, EntityModelSet modelSet) {
        super(parent);
        this.model = new SimpleCreeperModel<>(modelSet.bakeLayer(AMModelLayers.CREEPER_ARMOR));
    }

    @Override
    protected float xOffset(float x) {
        return x * 0.01F;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    }

    @Override
    protected EntityModel<TamableCreeper> model() {
        return this.model;
    }
}
