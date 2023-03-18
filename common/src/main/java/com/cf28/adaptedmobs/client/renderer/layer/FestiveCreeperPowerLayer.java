package com.cf28.adaptedmobs.client.renderer.layer;

import com.cf28.adaptedmobs.client.AMModelLayers;
import com.cf28.adaptedmobs.client.renderer.model.FestiveCreeperModel;
import com.cf28.adaptedmobs.common.entity.creeper.FestiveCreeper;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;

public class FestiveCreeperPowerLayer extends EnergySwirlLayer<FestiveCreeper, FestiveCreeperModel<FestiveCreeper>> {
    private final FestiveCreeperModel<FestiveCreeper> model;

    public FestiveCreeperPowerLayer(RenderLayerParent<FestiveCreeper, FestiveCreeperModel<FestiveCreeper>> parent, EntityModelSet modelSet) {
        super(parent);
        this.model = new FestiveCreeperModel<>(modelSet.bakeLayer(AMModelLayers.FESTIVE_CREEPER_ARMOR));
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
    protected EntityModel<FestiveCreeper> model() {
        return this.model;
    }
}
