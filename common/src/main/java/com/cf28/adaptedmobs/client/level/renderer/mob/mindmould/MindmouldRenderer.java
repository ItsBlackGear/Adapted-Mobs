package com.cf28.adaptedmobs.client.level.renderer.mob.mindmould;

import com.cf28.adaptedmobs.client.level.layer.MindmouldVesselLayer;
import com.cf28.adaptedmobs.client.level.model.mob.MindmouldModel;
import com.cf28.adaptedmobs.client.registries.AMModelLayers;
import com.cf28.adaptedmobs.common.level.entity.mob.Mindmould;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MindmouldRenderer extends MobRenderer<Mindmould, MindmouldModel<Mindmould>> {
    static final ResourceLocation MINDMOULD_TEXTURE = AdaptedMobs.resource("textures/entity/slime/mindmould.png");
    static final ResourceLocation MINDMOULD_SMALL_TEXTURE = AdaptedMobs.resource("textures/entity/slime/mindmould_small.png");

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

    @Override
    protected @Nullable RenderType getRenderType(@NotNull Mindmould livingEntity, boolean bodyVisible, boolean translucent, boolean glowing) {
        if (bodyVisible) return RenderType.entityTranslucent(this.getTextureLocation(livingEntity));
        return super.getRenderType(livingEntity, false, translucent, glowing);
    }

    @Override public @NotNull ResourceLocation getTextureLocation(@NotNull Mindmould mindmould) {
        return getVesselTexture(mindmould);
    }

    public static ResourceLocation getVesselTexture(Mindmould mindmould) {
        if (mindmould.isBaby()) return MINDMOULD_SMALL_TEXTURE;
        return MINDMOULD_TEXTURE;
    }
}
