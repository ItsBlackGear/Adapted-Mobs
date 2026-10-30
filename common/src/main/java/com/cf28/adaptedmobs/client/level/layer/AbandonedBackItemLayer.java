package com.cf28.adaptedmobs.client.level.layer;

import com.cf28.adaptedmobs.client.level.model.mob.AbandonedModel;
import com.cf28.adaptedmobs.common.level.entity.mob.Abandoned;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class AbandonedBackItemLayer extends RenderLayer<Abandoned, AbandonedModel<Abandoned>> {
    private final ItemRenderer itemRenderer;

    public AbandonedBackItemLayer(RenderLayerParent<Abandoned, AbandonedModel<Abandoned>> parent, ItemRenderer itemRenderer) {
        super(parent);
        this.itemRenderer = itemRenderer;
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, Abandoned entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stowed = entity.getStowedItem();

        if (!stowed.isEmpty()) {
            pose.pushPose();
            this.getParentModel().body.translateAndRotate(pose);

            pose.translate(0.0F, -0.4F, 0.17F);
            pose.mulPose(Axis.ZP.rotationDegrees(-90.0F));
            pose.scale(0.85F, 0.85F, 0.85F);

            this.itemRenderer.renderStatic(stowed, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, pose, buffer, entity.level(), entity.getId());
            pose.popPose();
        }
    }
}