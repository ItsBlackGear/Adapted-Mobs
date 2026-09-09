package com.cf28.adaptedmobs.client.level.renderer.mob;

import com.cf28.adaptedmobs.client.level.model.mob.MindmouldModel;
import com.cf28.adaptedmobs.client.registries.AMModelLayers;
import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import com.cf28.adaptedmobs.common.level.entity.mob.Mindmould;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.MagmaCube;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MindmouldRenderer extends MobRenderer<Mindmould, EntityModel<Mindmould>> {

    static final ResourceLocation MINDMOULD_TEXTURE = AdaptedMobs.resource("textures/entity/mindmould/mindmould.png");
    public MindmouldRenderer(EntityRendererProvider.Context context) {
        super(context, new MindmouldModel<Mindmould>(context.bakeLayer(AMModelLayers.MINDMOULD)), 1f);
    }

    @Override
    protected @Nullable RenderType getRenderType(@NotNull Mindmould livingEntity, boolean bodyVisible, boolean translucent, boolean glowing) {
        if (bodyVisible) return RenderType.entityTranslucent(MINDMOULD_TEXTURE);
        return super.getRenderType(livingEntity, false, translucent, glowing);
    }

    protected void scale(MagmaCube livingEntity, PoseStack poseStack, float partialTick) {
        float f = 0.999F;
        poseStack.scale(f, f, f);
        poseStack.translate(0.0F, 0.001F, 0.0F);
        float f1 = (float) livingEntity.getSize();
        float f2 = Mth.lerp(partialTick, livingEntity.oSquish, livingEntity.squish) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        poseStack.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
    }

    @Override public @NotNull ResourceLocation getTextureLocation(@NotNull Mindmould mindmould) { return MINDMOULD_TEXTURE; }
}
