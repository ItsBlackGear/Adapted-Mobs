package com.cf28.adaptedmobs.client.renderer.layer;

import com.cf28.adaptedmobs.client.AMModelLayers;
import com.cf28.adaptedmobs.client.renderer.SupportCreeperRenderer;
import com.cf28.adaptedmobs.client.renderer.model.FestiveCreeperModel;
import com.cf28.adaptedmobs.client.renderer.model.SupportCreeperModel;
import com.cf28.adaptedmobs.common.entity.creeper.FestiveCreeper;
import com.cf28.adaptedmobs.common.entity.creeper.SupportCreeper;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;

public class SupportCreeperPowerLayer extends EnergySwirlLayer<SupportCreeper, SupportCreeperModel<SupportCreeper>> {
    private final SupportCreeperModel<SupportCreeper> model;

    public SupportCreeperPowerLayer(RenderLayerParent<SupportCreeper, SupportCreeperModel<SupportCreeper>> parent, EntityModelSet modelSet) {
        super(parent);
        this.model = new SupportCreeperModel<>(modelSet.bakeLayer(AMModelLayers.SUPPORT_CREEPER_ARMOR));
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
    protected EntityModel<SupportCreeper> model() {
        return this.model;
    }
}
