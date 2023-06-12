package com.cf28.adaptedmobs.client.renderer.layer;

import com.cf28.adaptedmobs.common.entity.creeper.TamableCreeper;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class CreeperClothLayer<T extends TamableCreeper, M extends EntityModel<T>> extends RenderLayer<T, M> {
    public CreeperClothLayer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T creeper, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (creeper.isTame() && !creeper.isInvisible()) {
            float[] rgb = creeper.getClothColor().getTextureDiffuseColors();
            renderColoredCutoutModel(this.getParentModel(), this.getClothLocation(creeper), poseStack, buffer, packedLight, creeper, rgb[0], rgb[1], rgb[2]);
        }
    }

    private ResourceLocation getClothLocation(T creeper) {
        return new ResourceLocation(AdaptedMobs.MOD_ID, "textures/entity/creeper/" + creeper.getClothType().getId() + "_cloth.png");
    }
}