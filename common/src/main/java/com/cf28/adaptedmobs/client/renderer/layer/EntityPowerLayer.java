package com.cf28.adaptedmobs.client.renderer.layer;

import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PowerableMob;

public class EntityPowerLayer<T extends Entity & PowerableMob, M extends EntityModel<T>> extends EnergySwirlLayer<T, M> {
    public M model;

    public EntityPowerLayer(RenderLayerParent<T, M> renderer, M model) {
        super(renderer);
        this.model = model;
    }

    @Override
    protected float xOffset(float x) {
        return x * 0.01F;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return AdaptedMobs.resource("textures/entity/creeper/armor.png");
    }

    @Override
    protected EntityModel<T> model() {
        return this.model;
    }
}