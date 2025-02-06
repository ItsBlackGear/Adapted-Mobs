package com.cf28.adaptedmobs.client.renderer.layer;

import com.cf28.adaptedmobs.client.AMModelLayers;
import com.cf28.adaptedmobs.client.renderer.model.PeeperCreeperModel;
import com.cf28.adaptedmobs.client.renderer.model.SupportCreeperModel;
import com.cf28.adaptedmobs.common.entity.creeper.SupportCreeper;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;

import java.util.Map;

public class SupportCreeperPowerLayer<M extends EntityModel<SupportCreeper>> extends EntityPowerLayer<SupportCreeper, M> {
    private final Map<SupportCreeper.Variant, SupportCreeperModel<SupportCreeper>> modelByVariant;

    public SupportCreeperPowerLayer(RenderLayerParent<SupportCreeper, M> renderer, EntityModelSet models) {
        super(renderer, null);
        this.modelByVariant = Util.make(Maps.newHashMap(), map -> {
            map.put(SupportCreeper.Variant.NORMAL, new SupportCreeperModel<>(models.bakeLayer(AMModelLayers.SUPPORT_CREEPER_ARMOR)));
            map.put(SupportCreeper.Variant.PEEPER, new PeeperCreeperModel<>(models.bakeLayer(AMModelLayers.PEEPER_CREEPER_ARMOR)));
        });
    }

    @Override @SuppressWarnings("unchecked")
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, SupportCreeper creeper, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model = (M) this.modelByVariant.get(creeper.getVariant());
        super.render(poseStack, buffer, packedLight, creeper, limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch);
    }
}