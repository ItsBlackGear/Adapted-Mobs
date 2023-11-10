package com.cf28.adaptedmobs.client.renderer.layer;

import com.cf28.adaptedmobs.common.entity.creeper.TamableCreeper;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class CreeperClothLayer<T extends TamableCreeper, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final EntityModel<T> model;

    public CreeperClothLayer(RenderLayerParent<T, M> renderLayerParent, EntityModel<T> model) {
        super(renderLayerParent);
        this.model = model;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T creeper, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (creeper.isTame() && !creeper.isInvisible()) {
            float[] rgb = creeper.getClothColor().getTextureDiffuseColors();
            EntityModel<T> model = this.model;
            model.prepareMobModel(creeper, limbSwing, limbSwingAmount, partialTick);
            this.getParentModel().copyPropertiesTo(model);
            model.setupAnim(creeper, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
//            model.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
            renderColoredCutoutModel(model, this.getClothLocation(creeper), poseStack, buffer, packedLight, creeper, rgb[0], rgb[1], rgb[2]);
        }
    }

    private ResourceLocation getClothLocation(T creeper) {
        return new ResourceLocation(AdaptedMobs.MOD_ID, "textures/entity/creeper/" + creeper.getClothType().getId() + "_cloth.png");
    }
}