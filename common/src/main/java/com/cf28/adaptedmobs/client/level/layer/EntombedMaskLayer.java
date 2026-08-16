package com.cf28.adaptedmobs.client.level.layer;

import com.cf28.adaptedmobs.client.level.model.mob.EntombedModel;
import com.cf28.adaptedmobs.common.level.entity.mob.Entombed;
import com.cf28.adaptedmobs.common.level.item.mask.DeepslateMaskItem;
import com.cf28.adaptedmobs.common.level.item.mask.MaskVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class EntombedMaskLayer extends RenderLayer<Entombed, EntombedModel<Entombed>> {
    public EntombedMaskLayer(RenderLayerParent<Entombed, EntombedModel<Entombed>> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Entombed entombed, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entombed.isInvisible()) {
            return;
        }

        ItemStack headItem = entombed.getItemBySlot(EquipmentSlot.HEAD);
        MaskVariant variant = entombed.getVariant();
        if (headItem.getItem() instanceof DeepslateMaskItem maskItem) {
            variant = maskItem.getVariant();
        } else if (headItem.isEmpty()) {
            return;
        }

        ResourceLocation maskTexture = variant.getTexture();
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(maskTexture));
        this.getParentModel().renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
    }
}
