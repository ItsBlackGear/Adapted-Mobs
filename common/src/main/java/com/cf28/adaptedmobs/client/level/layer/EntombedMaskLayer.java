package com.cf28.adaptedmobs.client.level.layer;

import com.cf28.adaptedmobs.client.level.model.mob.ArchaicMaskModel;
import com.cf28.adaptedmobs.client.level.model.mob.EntombedModel;
import com.cf28.adaptedmobs.client.registries.AMModelLayers;
import com.cf28.adaptedmobs.common.level.entity.mob.Entombed;
import com.cf28.adaptedmobs.common.level.item.mask.ArchaicMaskItem;
import com.cf28.adaptedmobs.common.level.item.mask.MaskVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class EntombedMaskLayer extends RenderLayer<Entombed, EntombedModel<Entombed>> {
    private final ArchaicMaskModel maskModel;

    public EntombedMaskLayer(RenderLayerParent<Entombed, EntombedModel<Entombed>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.maskModel = new ArchaicMaskModel(modelSet.bakeLayer(AMModelLayers.ARCHAIC_MASK));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Entombed entombed, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entombed.isInvisible()) {
            return;
        }

        ItemStack headItem = entombed.getItemBySlot(EquipmentSlot.HEAD);
        MaskVariant variant = entombed.getVariant();
        if (headItem.getItem() instanceof ArchaicMaskItem maskItem) {
            variant = maskItem.getVariant();
        } else if (headItem.isEmpty()) {
            return;
        }

        ResourceLocation maskTexture = variant.getTexture();
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(maskTexture));

        poseStack.pushPose();
        this.getParentModel().chest.translateAndRotate(poseStack);
        this.getParentModel().head.translateAndRotate(poseStack);
        this.maskModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
        poseStack.popPose();
    }
}
