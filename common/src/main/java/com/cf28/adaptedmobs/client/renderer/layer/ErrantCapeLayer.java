package com.cf28.adaptedmobs.client.renderer.layer;

import com.cf28.adaptedmobs.client.renderer.model.ErrantModel;
import com.cf28.adaptedmobs.common.entity.errant.Errant;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class ErrantCapeLayer extends RenderLayer<Errant, ErrantModel> {
    public static final ResourceLocation GREEN_CAPE_LOCATION = new ResourceLocation(AdaptedMobs.MOD_ID, "textures/entity/errant/green_cape.png");
    public static final ResourceLocation CUBIC_CAPE_LOCATION = new ResourceLocation(AdaptedMobs.MOD_ID, "textures/entity/errant/cubic_cape.png");
    public static final ResourceLocation HEROBRINE_CAPE_LOCATION = new ResourceLocation(AdaptedMobs.MOD_ID, "textures/entity/errant/herobrine_cape.png");

    public ErrantCapeLayer(RenderLayerParent<Errant, ErrantModel> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Errant errant, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        poseStack.pushPose();
        poseStack.translate(0.0, 0.0, 0.125);

        double d = Mth.lerp(partialTick, errant.xCloakO, errant.xCloak)
            - Mth.lerp(partialTick, errant.xo, errant.getX());
        double e = Mth.lerp(partialTick, errant.yCloakO, errant.yCloak)
            - Mth.lerp(partialTick, errant.yo, errant.getY());
        double f = Mth.lerp(partialTick, errant.zCloakO, errant.zCloak)
            - Mth.lerp(partialTick, errant.zo, errant.getZ());
        float g = errant.yBodyRotO + (errant.yBodyRot - errant.yBodyRotO);
        double h = Mth.sin(g * (float) (Math.PI / 180.0));
        double i = -Mth.cos(g * (float) (Math.PI / 180.0));
        float j = (float)e * 10.0F;
        j = Mth.clamp(j, -6.0F, 32.0F);
        float k = (float)(d * h + f * i) * 100.0F;
        k = Mth.clamp(k, 0.0F, 150.0F);
        float l = (float)(d * i - f * h) * 100.0F;
        l = Mth.clamp(l, -20.0F, 20.0F);
        if (k < 0.0F) {
            k = 0.0F;
        }

        float m = Mth.lerp(partialTick, errant.oBob, errant.bob);
        j += Mth.sin(Mth.lerp(partialTick, errant.walkDistO, errant.walkDist) * 6.0F) * 32.0F * m;
        if (errant.isCrouching()) {
            j += 25.0F;
        }

        poseStack.mulPose(Vector3f.XP.rotationDegrees(6.0F + k / 2.0F + j));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(l / 2.0F));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - l / 2.0F));
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entitySolid(errant.getCloakTextureLocation()));
        this.getParentModel().renderCloak(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }
}