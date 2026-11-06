package com.cf28.adaptedmobs.client.level.renderer.mob.mindmould;

import com.cf28.adaptedmobs.client.level.layer.MindmouldVesselLayer;
import com.cf28.adaptedmobs.client.level.model.mob.MindmouldModel;
import com.cf28.adaptedmobs.client.registries.AMModelLayers;
import com.cf28.adaptedmobs.common.level.entity.mob.Mindmould;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class MindmouldRenderer extends MobRenderer<Mindmould, MindmouldModel<Mindmould>> {
    static final ResourceLocation MINDMOULD_TEXTURE = AdaptedMobs.resource("textures/entity/slime/mindmould.png");
    static final ResourceLocation MINDMOULD_SMALL_TEXTURE = AdaptedMobs.resource("textures/entity/slime/mindmould_small.png");
    static final ResourceLocation MINDMOULD_OVERLAY_TEXTURE = AdaptedMobs.resource("textures/misc/brain_slimed.png");

    public static ResourceLocation getOverlayTexture() { return MINDMOULD_OVERLAY_TEXTURE; }

    public static void renderScreenEffect(Mindmould mindmould, PoseStack poseStack) {
        ResourceLocation texture = getVesselTexture(mindmould);

        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);

        // :p
        Vector4f uvMapping = new Vector4f(
            16f / 64f, 16f / 64f,
            32f / 64f, 32f / 64f
        );
        if (mindmould.isBaby()) {
            uvMapping.x = 8f / 64f;
            uvMapping.z = 16f / 64f;
        }

        float size = .75f;
        float opacity = .7f;
        Matrix4f matrix4f = poseStack.last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferbuilder.addVertex(matrix4f, -size, -size, -0.5F).setUv(uvMapping.z, uvMapping.w).setColor(1f, 1F, 1F, opacity);
        bufferbuilder.addVertex(matrix4f, size, -size, -0.5F).setUv(uvMapping.x, uvMapping.w).setColor(1f, 1F, 1F, opacity);
        bufferbuilder.addVertex(matrix4f, size, size, -0.5F).setUv(uvMapping.x, uvMapping.y).setColor(1f, 1F, 1F, opacity);
        bufferbuilder.addVertex(matrix4f, -size, size, -0.5F).setUv(uvMapping.z, uvMapping.y).setColor(1f, 1F, 1F, opacity);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
        RenderSystem.disableBlend();
    }

    private final MindmouldModel<Mindmould> smallModel;
    private final MindmouldModel<Mindmould> defaultModel;

    public MindmouldRenderer(EntityRendererProvider.Context context) {
        super(context, new MindmouldModel<Mindmould>(context.bakeLayer(AMModelLayers.MINDMOULD)), .5f);
        this.smallModel = new MindmouldModel<Mindmould>(context.bakeLayer(AMModelLayers.MINDMOULD_SMALL));
        this.defaultModel = this.model;
        this.addLayer(new MindmouldVesselLayer(this, context.getBlockRenderDispatcher()));
    }

    @Override
    public void render(Mindmould mindmould, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (mindmould.isBaby()) this.model = smallModel;
        else this.model = defaultModel;

        super.render(mindmould, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override protected void scale(@NotNull Mindmould mindmould, @NotNull PoseStack poseStack, float partialTickTime) {
        float size = mindmould.isBaby() ? 1.0F : 2.0F;
        float squish = Mth.lerp(partialTickTime, mindmould.oSquish, mindmould.squish) / (size * 0.5F + 1.0F);
        float scale = 1.0F / (squish + 1.0F);

        Player player = mindmould.getAttachedPlayer();
        if (player != null) {
            float attachedScale = player.getScale();
            if (mindmould.isBaby()) attachedScale *= 1.33f;
            poseStack.scale(attachedScale, attachedScale, attachedScale);
        }
        poseStack.scale(scale, 1.0F / scale, scale);
    }

    @Override
    protected @Nullable RenderType getRenderType(@NotNull Mindmould mindmould, boolean bodyVisible, boolean translucent, boolean glowing) {
        // rendertype nullchecks within the render method, isBodyVisible just makes it invisible
        // this still renders the stem and it causes issues because its invisible. anyways thanks ryan for noticing this
        if (mindmould.getAttachedPlayer() != null) return null;

        if (bodyVisible) return RenderType.entityTranslucent(this.getTextureLocation(mindmould));
        return super.getRenderType(mindmould, false, translucent, glowing);
    }

    @Override protected boolean isBodyVisible(@NotNull Mindmould mindmould) { return true; }

    @Override public @NotNull ResourceLocation getTextureLocation(@NotNull Mindmould mindmould) {
        return getVesselTexture(mindmould);
    }

    public static ResourceLocation getVesselTexture(Mindmould mindmould) {
        if (mindmould.isBaby()) return MINDMOULD_SMALL_TEXTURE;
        return MINDMOULD_TEXTURE;
    }
}
