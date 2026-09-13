package com.cf28.adaptedmobs.client.level.renderer.mob;

import com.cf28.adaptedmobs.client.level.model.mob.HarpyModel;
import com.cf28.adaptedmobs.client.level.model.mob.MindmouldModel;
import com.cf28.adaptedmobs.client.registries.AMModelLayers;
import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import com.cf28.adaptedmobs.common.level.entity.mob.Mindmould;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.MagmaCube;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MindmouldRenderer extends MobRenderer<Mindmould, EntityModel<Mindmould>> {
    static final ResourceLocation MINDMOULD_TEXTURE = AdaptedMobs.resource("textures/entity/slime/mindmould.png");
    static final ResourceLocation MINDMOULD_SMALL_TEXTURE = AdaptedMobs.resource("textures/entity/slime/mindmould_small.png");

    private final MindmouldModel<Mindmould> smallModel;
    private final MindmouldModel<Mindmould> defaultModel;

    public MindmouldRenderer(EntityRendererProvider.Context context) {
        super(context, new MindmouldModel<Mindmould>(context.bakeLayer(AMModelLayers.MINDMOULD)), .5f);
        this.smallModel = new MindmouldModel<Mindmould>(context.bakeLayer(AMModelLayers.MINDMOULD_SMALL));
        this.defaultModel = (MindmouldModel<Mindmould>) this.model;
    }

    @Override
    public void render(Mindmould entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (entity.isBaby()) this.model = smallModel;
        else this.model = defaultModel;

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    protected @Nullable RenderType getRenderType(@NotNull Mindmould livingEntity, boolean bodyVisible, boolean translucent, boolean glowing) {
        if (bodyVisible) return RenderType.entityTranslucent(this.getTextureLocation(livingEntity));
        return super.getRenderType(livingEntity, false, translucent, glowing);
    }

    @Override public @NotNull ResourceLocation getTextureLocation(@NotNull Mindmould mindmould) {
        if (mindmould.isBaby()) return MINDMOULD_SMALL_TEXTURE;
        return MINDMOULD_TEXTURE;
    }
}
